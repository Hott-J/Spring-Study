package com.example.SpringBootCommunityWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration // 설정
@EnableSwagger2
public class SwaggerConfig {

    // 연락처 정보
    private static final Contact DEFAULT_CONTACT = new Contact("Hottj",
            "http://www.hottj.co.kr","jhj13062004@naver.com");

    // API Info
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("제목 작성")
                .version("버전 작성")
                .description("설명 작성")
                .license("라이센스 작성")
                .licenseUrl("라이센스 URL 작성")
                .build();
    }

    @Bean
    public Docket api(){ // Documentation 화
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) //기본정보
                .select()
                .apis(RequestHandlerSelectors.any())  //주소접근 옵션
                .paths(PathSelectors.any())
                .build();
    }
}