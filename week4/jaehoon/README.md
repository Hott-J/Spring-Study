# 🔶 SOAP vs REST
**REST** : 아키텍처 스타일이다. URI와 HTTP 프로토콜 기반이며 '단순함'이 핵심. 웹에 최적화되어 있고 JSON을 사용하기 때문에 브라우저 간 호환성이 좋다.

**SOAP** : 프로토콜이다. REST보다 더 많은 표준이 있고 복잡하고 오버헤드가 커서 일반적인 웹 서비스에는 어울리지 않지만 보안, 트랜잭션, ACID를 준수해야하는 종합적인 기능이 필요한 조직에게는 어울릴 수 있다.

![image](https://user-images.githubusercontent.com/46257667/104990005-0c519c00-5a5f-11eb-8163-7c24c5d2b557.png)


# 🔶 자주 사용되는 Lombok 어노테이션

1) **@Getter, @Setter**

2) **@AllArgsConstructor, @NoArgsConstructor, 
@RequiredArgsConstructor**

@NoArgsConstructor : 파라미터가 없는 기본 생성자 생성 <br>
@AllArgsConstructor : 모든 필드 값을 파라미터로 하는 생성자 생성<br>
@RequiredArgsConstructor : final이나 @NoNull인 필드 값만 파라미터로 받는 생성자 생성 <br>

3) **@ToString**

@ToString(exclude = "password")로 특정 필드를 toString() 결과에서 제외시킬 수도 있다.

4) **@EqualsAndHashCode**

5) **@Data**

끝판왕이다. 모든 필드를 대상으로 접근자와 설정자가 자동으로 생성되고, final 또는 @NonNull 필드 값을 파라미터로 받는 생성자가 만들어지며, toStirng, equals, hashCode 메소드가 자동으로 만들어진다.

# 🔶 @PathVariable

> URL 경로에 변수를 넣어주는 것. RESTful 서비스의 URI 형태

@PathVariable 어노테이션을 이용해서 {템플릿 변수} 와 동일한 이름을 갖는 파라미터를 추가하면 된다. 

```java
    @DeleteMapping("/users/for/{id}")
    public void deleteOneFor(@PathVariable int id)
```

# 🔶 서버측에서 response로 user id 가져오기
```java
@PostMapping("/users/new")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User newUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
```
# 🔶 예외처리
```java
if(user == null){
    throw new UserNotFoundException(String.format("ID[%s] not found", id));
}
```
```java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```
![image](https://user-images.githubusercontent.com/46257667/104906284-d8c33300-59c6-11eb-87b0-8e455e90d31f.png)

❗보안상의 문제가 발생할 수 있다.❗ <br>
Response body에 어디서 에러가 났는지 코드가 노출이 된다. 

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

```java
@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
```
![image](https://user-images.githubusercontent.com/46257667/104925343-0ff30d80-59e2-11eb-877b-d63496b51e50.png)

예외 핸들링으로 Response body에 직접 내용을 써줌으로 코드 노출을 피할 수 있다.

# 🔶 Validation check
@Valid : service 단이 아닌 객체 안에서, 들어오는 값에 대해 검증을 할 수 있다.
```java
@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid (MethodArgumentNotValidException ex,
     HttpHeaders headers,
     HttpStatus status,
     WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation Failed", ex.getBindingResult().toString());

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
```
[Valid 어노테이션 정리](https://jyami.tistory.com/55)

# 🔶 Internationalized
RestfulWebServiceApplication 클래스에 LocaleResolver을 빈으로 등록한다. 
```java
@Bean
public LocaleResolver localeResolver(){
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.KOREA);
    return localeResolver;
}
```

핸들러에서 해당 국가에 따른 greeting.message를 출력한다.
```java
@GetMapping(path="/hello-world-internationalized")
public String helloWorldInternationalized(
        @RequestHeader(name="Accept-Language", required=false) Locale locale){
    return messageSource.getMessage("greeting.message",null,locale);
    }
```
![image](https://user-images.githubusercontent.com/46257667/104991497-3bb5d800-5a62-11eb-8fcc-b52565eb461c.png)


# 🔶 Filtering
1) @JsonIgnoreProperties(value={"password"}) // Entity클래스 전체에 어노테이션 거는 방법
2) @JsonIgnore // 데이터 위에 하나씩 붙이는 방법
3) @JsonFilter("UserInfo") // SimpleBeanPropertyFilter 사용하는 방법
# 🔶 Version 관리
<br>

1) URI Versioning
```java
@GetMapping("/v1/users/{id}")
```
2) Request Parameter Versioning
```java
@GetMapping(value = "/users/{id}/", params="version=1")
```
URI 맨 뒤에 / 를 빼놓으면 안된다. 마지막에 파라미터 넣어줄때 앞에 "?" 붙이는 것 주의<br>

3) (custom) Headers Versioning 
```java
@GetMapping(value="/users/{id}", headers="X-API-VERSION=1")
```

headers key에 "X-API-VERSION"을 value에 "1"을 넣어 보낸다. <br>

4) Media Type Versioning
```java
@GetMapping(value="/users/{id}", produces = "application/vnd.company.appv1+json")
```
headers key에 Accpet를 value에 application/vnd.company.appv1+json를 넣어 보낸다.<br>
