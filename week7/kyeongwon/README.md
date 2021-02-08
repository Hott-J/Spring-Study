## :cherry_blossom: 처음 배우는 스프링 부트2 - Chapter 4 스프링 부트 웹

### 1. 커뮤니티 게시판 설계하기
* MVC 패턴 기반으로 구현

* 간단한 CRUD 기능만 제공

* 필요한 게시판 객체 : Board, User
  * 두 테이블 모두 PK(Primary Key)로 idx 설정

### 2. 커뮤니티 게시판 프로젝트 준비하기
* 프로젝트명 : Spring-Boot-Community-Web

* dependencies 추가 : DevTools, Lombok, Web, Thymeleaf, JPA, H2

### 3. 커뮤니티 게시판 구현하기
* 도메인 매핑하기
  * JPA를 사용하여 DB와 도메인 클래스(Board, User)를 연결시켜주는 작업

  * Board 클래스, User 클래스 (도메인 엔티티 관계도는 p.95 참고)
    * @Entity : 엔티티가 될 클래스 (DB에서 영속적으로 저장된 데이터를 자바 객체로 매핑하여 '인스턴스의 형태'로 존재하는 데이터)
    * @Id : 기본키(PK)를 의미한다. 모든 Entity클래스는 @Id설정이 필요하다.
    * @Column :  Entity클래스의 모든 필드는 데이터베이스의 컬럼과 매핑된다. 매핑될 Column명이 다르거나, default값이 다른경우에 사용한다.
    * @GeneratedValue(strategy = GenerationType.IDENTITY) : JPA가 기본키 생성을 하도록 한다.
    * @Enumerated(EnumType.STRING) : Enum 타입 매핑용 어노테이션이다. 실제로는 자바 Enum형이지만 DB의 String형으로 변환하여 저장한다.
    * @OneToOne(fetch = FetchType.LAZY) : 도메인을 1:1 관계로 설정. 실제로 DB에 저장될 때는 User 객체가 저장되는 것이 아니라 User의 PK인 user_idx 값이 저장된다.
    * @ManyToOne : 다른 Entity 클래스와의 외래키 다대일(N:1)관계를 의미한다.
    * @OneToMany : 다른 Entity 클래스와 일대다(1:N)관계를 의미한다.

* 도메인 테스트하기
  * @DataJpaTest를 사용하여 도메인 테스트 진행
    * 메모리 DB인 H2 DB를 사용
    * 테스트가 끝나고 이전의 데이터로 롤백하기 때문에 실제 DB에 반영되지 않는다.
    * assertThat(actual, is(expected)) : actual이 expected와 같은가?

  * JpaRepository를 상속받는 BoardRepository, UserRepository 생성
    * JpaRepository에는 **기본적인 CRUD 기능을 제공해주는 getOne, findById, findAll, save, delete 등의 함수가 정의**되어 있어 간단한 내용을 DB로부터 처리할 수 있게 해준다.

* 서비스와 컨트롤러 생성
  * BoardService 클래스
    * 게시판의 리스트와 폼을 찾아주는 핵심 비즈니스 로직
    * JPA Pageable 페이징 처리 : PageRequest에 첫번째 인자로는 가져올 페이지, 두번째 인자로는 페이지의 크기를 넘겨준다.

  * BoardController 클래스
    * 요청된 URI 경로로 필요한 서비스 호출
    * 서비스를 통해 처리된 데이터를 뷰 쪽에 바인딩 시켜주는 역할

* CommandLineRunner를 사용하여 DB에 데이터 넣기
  * CommandLineRunner : 애플리케이션 구동 후 특정 코드를 실행시키고 싶을 때 직접 구현하는 인터페이스 -> 애플리케이션 구동 시 테스트 데이터를 함께 생성하여 프로젝트를 실행하고 싶을 때 편리하게 쓸 수 있다.
  * CommandLineRunner을 Bean으로 등록한 후(@Bean), UserRepository와 BoardRepository를 주입받는다.

* 게시글 리스트 기능 및 페이징 처리
  * 템플릿 엔진 : Thymeleaf 사용
  * list.html, form.html 추가

<br>

## :cherry_blossom: 처음 배우는 스프링 부트2 - Chapter 5 스프링 부트 Security + OAuth2

### 1. 배경지식
* Spring Boot Security
  * Spring에서 인증, 권한 처리를 편리하게 관리할 수 있다.

  * 인증 : 사용자(클라이언트)가 애플리케이션의 특정 동작에 관하여 허락(인증)된 사용자인지 확인하는 절차

  * 권한 부여 : 데이터나 프로 그램 등의 특정 자원이나 서비스에 접근할 수 있는 권한

  * OAuth2
    * 토큰을 사용한 범용적인 방법의 인증을 제공하는 표준 인증 포로토콜
    * 권한 부여 코드 승인 타입 : 클라이언트가 다른 사용자 대신 특정 리소스에 접근을 요청할 때 사용된다. 리소스 접근을 위한 사용자명과 비밀번호, 권한 서버에 요청해서 받은 권한 코드를 함께 활용하여 리소스에 대한 액세스 토큰을 받으면 이를 인증에 이용하는 방식이다. 주로 페이스북, 구글, 카카오 등의 소셜 미디어들이 웹 서버 형태의 클라이언트를 지원하는 데 이 방식을 사용한다.

* OAuth 주체
  * Client(웹사이트) : 리소스에 접근하려고 하는 주체, Service Provider에 있는 특정 정보에 접근하여 사용하고자 한다.

  * Resoure Owner(인증이 필요한 사용자)) = Client에 제공하는 정보를 가지고 있는 사람. 여기서는 이메일과 이름을 제공해서 편리하게 로그인하고 싶어하는 웹사이트 사용자

  * Resource (사용자 정보, 이메일, 이름) = 서비스 제공자에 저장된 정보

  * Service Provider(Google, Facebook, Kakao 등..) : 리소스를 저장하고 있는 제3의 주체. Client가 여기에 저장된 정보에 접근하고자 한다.
    * 인증 및 권한 부여를 담당하는 Authoriation Server(인증 서버) + 사용자의 데이터를 관리하는 Resource Server(리소스 서버)로 이루어져 있다.

* OAuth 과정 (출처 : [https://gdtbgl93.tistory.com/180](https://gdtbgl93.tistory.com/180))   
  <img src="https://user-images.githubusercontent.com/61045469/107243524-6d71fb80-6a70-11eb-979e-37855615ac93.png" width="60%" height="40%"></img><br/>
  * Service Provider에 Client 정보 등록

    * 1. Service Provider에 Client를 등록하면 Client별로 고유하게 식별할 수 있는 Client Id와 Secret Key가 생성된다.

    * 2. Redirect URI를 등록한다. Redirect URI는 Service Provider가 Client에게 임시출입증과 같은 Access Token과 토큰을 발급받기 위해 사용하는 Authentication Code를 전해주는 경로이다.
    <br/>

  <img src="https://user-images.githubusercontent.com/61045469/107243569-7c58ae00-6a70-11eb-91fd-29843f1529b8.png" width="60%" height="40%"></img><br/>
  * Client의 인증 요청 & 권한 부여 요청

    * 3. Resource Owner가 Client에 접속하여 '구글 아이디로 가입' 클릭

    * 4. Client Page에서는 Service Provider에 권한을 부여하기 위해 Resource Owner의 승인을 요청한다.

    * 5. 권한 인증을 요청할 때는 Client Id와 사용할 리소스의 범위를 나타내는 Scope, 그리고 Resource Owner의 리소스 사용 승인 시 임시 토큰인 Authorization Code를 전달할 Redirect Url을 함께 파라미터로 넘겨준다.

    * 6. Service Provider에서는 Resource Owner가 로그인하여 리소스 사용을 승인할 수 있는 페이지로 302 코드 응답을 통해 슬쩍 Resource Owner를 이동시킨다.

    * 7. 이 페이지는 Client가 사용할 권한 목록을 Resource Owner에게 명시적으로 보여주며, Resource Owner는 이에 동의 시 "나는 이 Client가 Scope에 명시한 범위 안에 있는 내 데이터에 접근할 권한을 부여하는 것에 동의한다."라고 말하는 것과 같다.   
    <img src="https://user-images.githubusercontent.com/61045469/107242044-e96b4400-6a6e-11eb-9a6b-de6de20cdebb.png" width="50%" height="30%"></img><br/>
    <br/>

  <img src="https://user-images.githubusercontent.com/61045469/107243651-8ed2e780-6a70-11eb-81b9-d22060c97edc.png" width="60%" height="40%"></img><br/>
  * 인증 성공 시 Authentication Code 부여

    * 8. Resource Owner가 인증 및 권한 사용 승인까지 마치면, Service Provider의 인증 서버는 Access Token을 발행받기 위한 Authentication Code를 302 상태 코드를 통해 Client에게 전달한다.

      * 302 상태 코드는 URL을 Redirect시킨다는 뜻으로 Response Header에 설정된 Location필드에 있는 URL로 사용자를 이동시킬 수 있다.
      * Authentication Code(인가 코드)는 "이 Client는 Resource Owner에게 사용 허락을 받았음"이라고 적혀있는 증서라고 생각하면 된다.
    <br/>
  
  <img src="https://user-images.githubusercontent.com/61045469/107243739-aa3df280-6a70-11eb-8dd4-9c55432cb129.png" width="60%" height="40%"></img><br/>
  * Access Token 발행 요청

    * 9. Client는 서비스 제공자에게 Client Id와 Secret Key, Access Token이 발행되면, Authentication Code를 발급 받을때 사용했던 Redirect URL, Resource Owner에게 받은 Authentication Code(인가 코드)를 가지고 Authentication Server에 Access Token 발행 요청을 한다.

    * 10. Service Provider의 Authoriation Server(인증 서버)는 리소스에 접근할 수 있는 Access Token을 발행하고 Client에게 보내준다.
    <br/>

  <img src="https://user-images.githubusercontent.com/61045469/107243790-b5911e00-6a70-11eb-8b2e-ba981c6ef8cc.png" width="60%" height="40%"></img><br/>
  * 리소스에 접근 요청
  
    * 11. Access Token을 받으면 Client는 이 토큰을 통해 Resource Server에서 사용자의 정보에 접근한다.