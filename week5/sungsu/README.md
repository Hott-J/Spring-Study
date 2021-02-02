# :cherries: RESTful service
# Spring Boot API

## LEVEL3 단계의 REST API 구현을 위한 HATEOAS 적용
- Hateos
  - 현재 리소스와 연관된 자원 상태 정보를 제공
- 관련 메소드를 사용하기 위해 의존성 추가
```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateoas</artifactId>

		</dependency>
```

- User code에 Hateoas 관련 코드 추가
```java
@GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        User user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s} not found",id));
        }
        EntityModel<User> model= new EntityModel<>(user);
        WebMvcLinkBuilder linkTo=linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        return model;
    }
```

- retrieveAllUsers란 함수와 all-users를 맵핑시켰다. 따라서 현재 있는 리소스에서 할 수 있는 다른 작업이 표시된다.

## Rest API Documentation을 위한 Swagger사용
- 목표ㅣ 개발자 도움말 페이지 생성하기
- swagger 메서드를 사용하기 위한 dependency 추가
```xml
<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.9.2</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.9.2</version>
		</dependency>
```
- config를 설정할 수 있는 새로운 클래스 생성
```java
package com.example.restfulwebservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2);
    }
    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }
}
```
- 사용했던 여러가지 메소드를 관리하거나 볼 수 있다.
![api](https://user-images.githubusercontent.com/51367515/105642245-f6633180-5ecb-11eb-9bda-023d42dd5bdc.PNG)
- api-docs에 있던 json파일을 토대로 만들어짐

## Swagger Documentation 구현 방법
- 세부적인 사항을 설정할 수 있음
```java
private static final Contact DEFFAULT_CONTACT=new Contact("kss",
            "httpL..www.joneconsulting.co.kr","edowon@joneconsulting.co.kr" );

    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Awesome API Title",
            "My User management REST API service","1.0","urn:tos",
            DEFFAULT_CONTACT, "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>());

    private static final Set<String> DEFAULT_PRODUCTS_AND_CONSUMES=new HashSet<>(
            Arrays.asList("application/json","application/xml")
    );

```
- @ApiModel(description="")를 설정하면 설명을 적을 수 있다.

## Monitoring을 위한 Actuator 설정
- 모니터링 기능 추가-> 어플리케이션의 기능 파악 가능
- dependency추가
```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

![actuator](https://user-images.githubusercontent.com/51367515/105643034-ce2a0180-5ed0-11eb-8d64-b14417da6de3.PNG)

- yml파일을 통해 더 많은 정보를 추가할 수 있다.
- actuator를 통해 얻어지는 정보는 서버에 모니터링 도구로 커스터마이징해서 사용할 수 있다.

## HAL Browser를 이용한 HATEOAS 구현
- HAL Browser: Hypertext를 사용해 어플리케이션에 부가적인 기능을 부여하는 것
- dependency 추가
```xml
<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-rest-hal-browser</artifactId>
		</dependency>
```
![hal](https://user-images.githubusercontent.com/51367515/105643737-fddb0880-5ed4-11eb-9158-b4ed20465570.PNG)
- Hal browser의 장점 : rest 자원을 생성하지 않더라도 hateoas 기능을 사용할 수 있다.

## Spirng security를 이용한 인증처리
- 인증 과정을 거친 사용자만 이용가능 할 경우에 사용
- dependency 추가
```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

- 이를 실행하면 spring에서는 저절로 암호 하나를 설정해서 알려준다.-> 실행할때마다 변경이 된다. IntelliJ 로그를 통해 확인할 수 있다.

