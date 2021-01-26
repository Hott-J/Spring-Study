# 🔶 Level3 API 구현을 위한 HATEOAS
```java
EntityModel<User> resource = new EntityModel<>(user);
WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

resource.add(linkTo.withRel("all-users"));

return resource;
```
![image](https://user-images.githubusercontent.com/46257667/105514702-27e6cc00-5d17-11eb-9446-94aee93f9028.png)

✔ 다른 API로 넘어갈 수 있도록 도와준다.

>❓ 왜 필요한건데

❗ REST API 단점
* 앤드포인트 URL 변경 시 모두 수정해야 한다.
* 자원의 상태를 고려하지 않는다.

LEVEL 3 하이퍼 미디어 링크를 사용하여 서버가 클라이언트에게 자원의 보내면서 클라이언트는 다음 작업을 할 수 있는 URL을 보내준다.

# 🔶 REST API 문서 작성 framework : ```Swagger```
```java
    private static final Contact DEFAULT_CONTACT = new Contact("jaehoon", "","zpqls321@naver.com");
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Awesome API Title", "My User management REST API service", "1.0", "urn:tos",DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());
    private static final Set<String> DEFAULT_PRODUCES_AND_OCNSUMES = new HashSet<>(Arrays.asList("application/json", "application/xml"));

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO).produces(DEFAULT_PRODUCES_AND_OCNSUMES).consumes(DEFAULT_PRODUCES_AND_OCNSUMES);
    }

```
Swagger 설정을 정의한 코드입니다.
* .consume()과 .produces()는 각각 Request Content-Type, Response Content-Type에 대한 설정입니다.(선택)
* .apiInfo()는 Swagger API 문서에 대한 설명을 표기하는 메소드입니다. (선택)
* .apis()는 Swagger API 문서로 만들기 원하는 basePackage 경로입니다. (필수)
* .path()는 URL 경로를 지정하여 해당 URL에 해당하는 요청만  Swagger API 문서로 만듭니다.(필수)

# 🔶 어플리케이션 모니터링 툴 : ```Actuator```

경로	설명
/beans	초기화된 모든 스프링 빈의 목록을 표시
/env	스프링 설정 가능한 환경 속성 목록욜 표시, OS 환경 변수 및 컨피규레이션 파일의 속성 목록
/health	애플리케이션 상태 정보 표시
/info	애플리케이션의 임의 정보 표시 properties 등
/loggers	로거 컨피규레이션 정보를 표시하고 수정
/metrics	매트릭스 정보를 표시(메모리, 실행중인 스레드 수, REST 메서드 응답 시간)
/trace	트레이스 정뵤 표시(기본적으로 마지막 100개의 HTTP Request)

|경로|설명|
|------|---|
|/beans|초기화된 모든 스프링 빈의 목록을 표시|
|/env|스프링 설정 가능한 환경 속성 목록욜 표시, OS 환경 변수 및 컨피규레이션 파일의 속성 목록|
|/health|애플리케이션 상태 정보 표시|
|/info|애플리케이션의 임의 정보 표시 properties 등|
|/loggers|로거 컨피규레이션 정보를 표시하고 수정|
|/metrics|매트릭스 정보를 표시(메모리, 실행중인 스레드 수, REST 메서드 응답 시간)|
|/trace|트레이스 정뵤 표시(기본적으로 마지막 100개의 HTTP Request)|

❗ 민감한 정보가 담겨있으므로 보안에 신경을 써야한다. 또한 정보가 메모리에 올려지므로 실제 운영에서는 영구저장소에 저장해야한다. 

# 🔶 HAL

# 🔶 JPA

@Valid @RequestBody 사용안하면 null 값이 들어간다.

@ManytoOne, @OnetoMany, fetchtype.LAZY

```java
@OneToMany(mappedBy = "user")
private List<Post> posts;
```

![image](https://user-images.githubusercontent.com/46257667/105810773-e728d480-5fee-11eb-871b-d26c11f6ec1a.png)


내부 매커니즘은 위의 그림과 같다.

* 로딩되는 시점에 Lazy 로딩 설정이 되어있는 User 엔티티는 프록시 객체로 가져온다.

* 후에 실제 객체를 사용하는 시점에(User을 사용하는 시점에) 초기화가 된다. DB에 쿼리가 나간다.

* getUser()으로 User을 조회하면 프록시 객체가 조회가 된다.

* getUser().getXXX()으로 팀의 필드에 접근 할 때, 쿼리가 나간다.

```java
@GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostByUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return user.get().getPosts();
    }
```

* 실무에서는 가급적 지연 로딩만 사용하다. 즉시 로딩 쓰지 말자.

* JPA 구현체도 한번에 가저오려고 하고, 한번에 가져와서 쓰면 좋지 않나?

* 즉시 로딩을 적용하면 예상하지 못한 SQL이 발생한다.

* @ManyToOne이 5개 있는데 전부 EAGER로 설정되어 있다고 생각해보자. 조인이 5개 일어난다. 실무에선 테이블이 더 많다.

* 즉시 로딩은 JPQL에서 N+1 문제를 일으킨다.

* 실무에서 복잡한 쿼리를 많이 풀어내기 위해서 뒤에서 학습할 JPQL을 많이 사용한다.

* em.find()는 PK를 정해놓고 DB에서 가져오기 때문에 JPA 내부에서 최적화를 할 수 있다.(한방 쿼리)

* 하지만, JPQL에선 입력 받은 query string이 그대로 SQL로 변환된다.

* "select m from Member m" 이 문장으로 당연히 Member만 SELECT 하게 된다.

* MEMBER를 쭉 다 가져와서 보니까

* 어 근데, Member 엔티티의 Team의 fetchType이 EAGER네?

* LAZY면 프록시를 넣으면 되겠지만, EAGER는 반환하는 시점에 다 조회가 되어 있어야 한다.

* 따라서, Member를 다 가져오고 나서, 그 Member와 연관된 Team을 다시 다 가져온다.
