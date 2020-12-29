# :cherry_blossom: Spring 

---

## :one: 웹 MVC 개발

### :smile: 회원 웹 기능 - 홈 화면 추가

- 홈 컨트롤러에 *@GetMapping* 으로 '/' 가 되어있으므로 home.html을 찾는다. 있으므로 기존에 static 폴더에 있던 index.html은 실행되지 않는다.
  - 스프링 컨테이너안에 있는 관련 컨트롤러를 먼저 찾고, 없으면 static 파일들을 찾는다. **우선순위**

![스프링2](https://user-images.githubusercontent.com/47052106/103139411-18c42c80-471f-11eb-81ae-9c759d3691bf.JPG)

### :smile: 회원 웹 기능 - 등록

- form 태그
  - 값을 입력할 수 있는 html 태그
  - *action 속성*
    - **이 속성은 데이터를 어디로 보낼 것인지 지정한다.** 이 값은 반드시 *유효한 URL*이어야 한다.만약 이 속성을 지정하지 않으면 데이터는 폼이 있는 페이지의 URL로 보내질 것이다.
  ```html
  <form action="./login.do">
    form 내용
  </form>
  ```
  - *method 속성*
    - **이 속성은 데이터를 어떻게 보낼 것인지 정의한다.** HTTP protocol 은 요청 방법에 대해 다양한 방법들을 제공한다.  HTML 폼 데이터는 오직 2가지 방법으로만 전송 할 수 있는데 바로 GET 방식과 POST방식이 있다.
    - **get**
      - GET은 서버로부터 정보를 조회하기 위해 설계된 메소드입니다.
GET은 요청을 전송할 때 필요한 데이터를 Body에 담지 않고, 쿼리스트링을 통해 전송합니다. URL의 끝에 ?와 함께 이름과 값으로 쌍을 이루는 요청 파라미터를 쿼리스트링이라고 부릅니다. 만약, 요청 파라미터가 여러 개이면 &로 연결합니다. 쿼리스트링을 사용하게 되면 URL에 조회 조건을 표시하기 때문에 특정 페이지를 링크하거나 북마크할 수 있습니다. 쿼리스트링을 포함한 URL의 샘플은 아래와 같습니다. 여기서 요청 파라미터명은 name1, name2이고, 각각의 파라미터는 value1, value2라는 값으로 서버에 요청을 보내게 됩니다.
`www.example-url.com/resources?name1=value1&name2=value2`
그리고 GET은 불필요한 요청을 제한하기 위해 요청이 캐시될 수 있습니다. js, css, 이미지 같은 정적 컨텐츠는 데이터양이 크고, 변경될 일이 적어서 반복해서 동일한 요청을 보낼 필요가 없습니다. 정적 컨텐츠를 요청하고 나면 브라우저에서는 요청을 캐시해두고, 동일한 요청이 발생할 때 서버로 요청을 보내지 않고 캐시된 데이터를 사용합니다. 그래서 프론트엔드 개발을 하다보면 정적 컨텐츠가 캐시돼 컨텐츠를 변경해도 내용이 바뀌지 않는 경우가 종종 발생합니다. 이 때는 브라우저의 캐시를 지워주면 다시 컨텐츠를 조회하기 위해 서버로 요청을 보내게 됩니다.
    - **post**
      - POST는 리소스를 생성/변경하기 위해 설계되었기 때문에 GET과 달리 전송해야될 데이터를 HTTP 메세지의 Body에 담아서 전송합니다. HTTP 메세지의 Body는 길이의 제한없이 데이터를 전송할 수 있습니다. 그래서 POST 요청은 GET과 달리 대용량 데이터를 전송할 수 있습니다. 이처럼 POST는 데이터가 Body로 전송되고 내용이 눈에 보이지 않아 GET보다 보안적인 면에서 안전하다고 생각할 수 있지만, POST 요청도 크롬 개발자 도구, Fiddler와 같은 툴로 요청 내용을 확인할 수 있기 때문에 민감한 데이터의 경우에는 반드시 암호화해 전송해야 합니다. 그리고 POST로 요청을 보낼 때는 요청 헤더의 Content-Type에 요청 데이터의 타입을 표시해야 합니다. 데이터 타입을 표시하지 않으면 서버는 내용이나 URL에 포함된 리소스의 확장자명 등으로 데이터 타입을 유추합니다. 만약, 알 수 없는 경우에는 `application/octet-stream`로 요청을 처리합니다.

- *PostMapping*
  - 데이터를 Form 같은 곳에 넣어서 전달하고 등록할 때 주로 사용
  
- *GetMapping*
  - 데이터를 조회하는데 주로 사용
  
![스프링](https://user-images.githubusercontent.com/47052106/103180508-85832680-48d9-11eb-82a2-fdaafe855a04.JPG) <br>
name이 서버로 넘어올 때 Key가 된다.
![스프링1](https://user-images.githubusercontent.com/47052106/103180510-86b45380-48d9-11eb-83f0-099241b960f9.JPG) <br>
name에 해당하는 value가 들어가게 된다.
![스프링3](https://user-images.githubusercontent.com/47052106/103180511-86b45380-48d9-11eb-8708-86867d8def82.JPG)

### :smile: 회원 웹 기능 - 조회

![스프링1](https://user-images.githubusercontent.com/47052106/103181156-4953c400-48e1-11eb-8cd5-b91e6d46982a.JPG) <br>
*model.addAttribute* 메소드를 통해 "members"가 key가 된다.
`model.addAttribute("변수이름","변수에 넣을 데이터값");`
![스프링2](https://user-images.githubusercontent.com/47052106/103181158-4fe23b80-48e1-11eb-8635-f6ce0cb2b9ce.JPG) <br>
${members}로 값을 전부 가져온다.
`${변수이름}`

- *model 객체*
  - **Controller 에서 생성된 데이터를 담아서 View 로 전달할 때 사용하는 객체.**
  - Servelt 의 `request.setAttribute()` 와 유사한 역할.
  - Method 에 Model 타입이 지정된 경우 Model 타입의 객체를 만들어서 메서드에 주입
  - **addAttribute("키", "값")** 메소드를 사용하여 전달할 데이터 세팅.

- *@ModelAttribute*
  - 강제로 전달받은 파라미터를 Model에 담아서 전달하도록 할 때 필요한 어노테이션
  - 스프링에서 *Java beans 규칙(Getter, Setter, 생성자 포함)에 맞는 객체는 파라미터 전달이 자동으로 가능.*
  - 하지만 **일반 변수**의 경우, 자동 전달 불가능. model 객체를 통해서 전달 필요.
  `ModelAttribute("page")int page` 와 같이 일반 변수를 감싸서 파라미터를 전송해준다.
  - [참고](https://lopicit.tistory.com/224)

## :two: 스프링 DB 접근 기술

### :smile: H2 데이터베이스 설치

- **데이터베이스 파일 생성 방법**
  - 접속이 잘 안될 경우, 도메인창에 *localhost* 로 수정한다. **(key값은 그대로 유지!)**
  - `jdbc:h2:~/test` (최초 한번)
  - `~/test.mv.db` 파일 *생성 확인*
  - 이후부터는 `jdbc:h2:tcp://localhost/~/test` 이렇게 접속한다
    - *소켓으로 접속하여 충돌을 막는다*
  - **문제가 생길 경우, `~/test.mv.db` 파일을 삭제하고 서버를 껐다 키고 다시 처음부터 한다.**
  
### :smile: 순수 JDBC

- 과거의 코딩이다.
- 인터페이스의 다형성을 활용한다.
- *개방-폐쇄 원칙(OCP,Open-Closed Principle)*
  - 확장에는 열려있고, 수정, 변경에는 닫혀있다.
- 스프링의 DI을 사용하면 **기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경**할 수 있다.
- 데이터를 DB에 저장하므로 스프링 서버를 다시 실행해도 데이터가 안전하게 저장된다.
  
 ### :smile: 스프링 통합 테스트
 
 - *@SpringBootTest*
  - 스프링 컨테이너와 테스트를 함께 실행한다. **진짜 스프링을 띄워서 테스트**
 - *@Transactional*
  - 테스트 시작 전에 트랜잭션을 실행하고, 데이터들을 DB에 넣고 테스트가 끝나면 **롤백** 해준다.
    - *번거롭게 BeforeEach, AfterEach 만들지 않아도 된다.*
    - 다음 테스트에 영향을 주지 않는다.
    - 테스트 케이스에 붙어있을때만 롤백한다.
 - Commit / AutoCommit
  - DB는 데이터를 insert call을 한 뒤에 Commit이 되어야 DB에 반영이 된다. AutoCommit의 경우에는 자동으로 Commit이 되어 반영된다.
 - 테스트는 그냥 필드주입으로 *@Autowired* 한다. 필요한거 인젝션하고 끝이므로 편한 방법을 택한다. 생성자 주입 x
 - **컨테이너 없이 순수한 단위 테스트가 좋은 테스트일 확률이 높다. 최대한 작은 단위의 테스트 설계를 하자!!!**

 ### :smile: 스프링 JdbcTemplate
 
- JdbcTemplate은 *dataSource*를 인젝션 받는다.
- Spring JDBC를 사용하려면 먼저, DB Connection을 가져오는 DataSource를 Spring IoC 컨테이너의 공유 가능한 Bean으로 등록해야 한다.
```java
private final JdbcTemplate jdbcTemplate;
public JdbcTemplateMemberRepository(DataSource dataSource){
  jdbcTemplate=new JdbcTemplate(dataSource);
}
```
- 생성자가 하나일 경우 *@Autowired* 생략 가능!
- SQL문에서의 *"?"*
  - 동적 SQL을 실행할 때. 매개변수가 있는 쿼리이다.
- **JdbcTemplate**
  - *execute*
    - DDL을 실행
    - `jdbc.execute("drop table persons if exists");`
  - *update*
    - Read를 제외한 나머지 insert, update, delete 는 모두 이 메서드를 사용하면 된다. 
    - `jdbc.update("insert into persons (name) values (?)",name);`
  - *queryForObject*
    - queryForObject는 하나의 도메인 객체를 리턴받거나 하나의 컬럼을 리턴 받을 때 사용한다. 
    ```java
    public Long findId(Long id) {
      return this.jdbcTemplate.queryForObject("SELECT id FROM persons where id = ?", Long.class, id);
     }
    
     public Person findOne(Long id) {
      return this.jdbcTemplate.queryForObject("SELECT id, name FROM persons where id = ?",
        (rs, rowNum) -> new Person(rs.getLong("id"), rs.getString("name")),
        id);
        }
    }
    ```
  - *query*
    - 아마 가장 많은 메서드를 갖고 있고 위의 설명한 것을 제외하면 모두 이 메서드를 이용하면 된다.
    - `RowMapper<T> 인터페이스를 해당 도메인에 맞게 구현하면 된다.
    ```java
    <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;
    <T> List<T> query(String sql, RowMapper<T> rowMapper, @Nullable Object... args) throws DataAccessException;
    ```
    ```java
    /* 모든 학생 객체 */
    String SQL = "select * from Student"; 
    List<Student> students = jdbcTemplateObject.query(SQL, new StudentMapper()); 
    // RowMapper interface의 구현 클래스 정의 
    public class StudentMapper implements RowMapper<Student> {
    // interface method
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException { 
      Student student = new Student(); 

      student.setID(rs.getInt("id")); 
      student.setName(rs.getString("name")); 
      student.setAge(rs.getInt("age")); 

      return student; 
      } 
    }
    ```
    ```java
      public List<Person> findAll() {
        return this.jdbcTemplate.query("SELECT id, name FROM persons",
        (rs, rowNum) -> new Person(rs.getLong("id"), rs.getString("name")));
      }
      ```
      
 ### :smile: JPA

- *@Entity*
  - 자바 JPA가 관리한다는 뜻
- 쿼리에 값을 입력해주면 ID는 DB에서 자동으로 생성해주는 것을 *Identity 전략* 이라 한다.
  - *@ID(Primary Key) @GeneratedValue(strategy=GenerationType.IDENTITY)*
- JPA를 사용하려면 *EntityManager* 를 주입받아야 한다.
- JPA를 사용할때, 모든 데이터 변경이 *@Transactional* 안에서 실행되어야 한다.

- **저장**
```java
public Member save(Member member) {
 em.persist(member);
 return member;
}
```

- **PK 조회**
```java
public Optional<Member> findById(Long id) {
 Member member = em.find(Member.class, id);
 return Optional.ofNullable(member);
}
```

- **PK 제외 조회**
  - JPQL 필요
```java
 public List<Member> findAll() {
 return em.createQuery("select m from Member m", Member.class) //Member 객체 m 에서 객체 m 전체를 select 한다.
 .getResultList();
}
 ```

### :smile: 스프링 데이터 JPA

- **인터페이스만**으로 완료
- *기본적인 CRUD 제공*
- 개발 생산성 증대
```java
import org.springframework.data.jpa.repository.JpaRepository; // 인터페이스이다.

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, // 인터페이스가 인터페이스 상속할 때, extends 사용. 
Long>, MemberRepository { // 인터페이스는 다중 상속이 가능
 Optional<Member> findByName(String name);
}
```
- 위의 코드를 보고, JPA 에서 **알아서** *SpringDataJpaMemberRepository* 구현체를 만들어서 빈에 등록해준다.
  - 이를 인젝션해서 사용
  - *Proxy*
- findByName 함수의 경우는 **비지니스마다 name 값은 다르므로,** 만들어줘야한다.

`Optional<Member> findByName(String name);`
- 위와 같이 코드를 작성할 경우, JPQL이 `select m from Member m where m.name=?` 로 SQL문으로 만들어준다. 
- *인터페이스 이름만으로도 개발이 끝남*

![캡처](https://user-images.githubusercontent.com/47052106/103316665-800a1580-4a6c-11eb-9fbd-6f25afe35cc9.JPG)

