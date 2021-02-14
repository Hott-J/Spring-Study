# 스프링 부트 테스트
## @SpringBootTest

* @RunWith(SpringRunner.class)
@SrpingBootTest 어노테이션을 사용하려면 JUnit 실행에 필요한 SpringJUnit4ClassRunner 클래스를 상속받은 @Runwith(SpringRunner.class)를 꼭 붙여야 한다.

* value
테스트가 실행되기 전에 적용할 프로퍼티 주입시킬 수 있음. 즉 기존의 프로퍼티를 오버라이드함.

* properties
테스트가 실행되기 전에 (key=value) 형식으로 프로퍼티를 추가할 수 있음.

* classes
애플리케이션 컨텍스트에 로드할 클래스를 지정할 수 있음. 따로 지정하지 않으면 @SpringBootConfiguration을 찾아서 로드

* webEnvironment
애플리케이션이 실행될 때의