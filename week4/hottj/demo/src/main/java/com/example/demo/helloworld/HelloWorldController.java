package com.example.demo.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorldController {

    @Autowired
    private MessageSource messageSource;

    // GET
    // /hello-world (endpoint)
    // @RequestMapping(method=RequestMethod.GET, path="/hello-world") : 과거의 방식
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    // alt + enter
    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean("Hello World"); //자바 빈형태로 반환 -> 텍스트나 object 형태가 아닌 JSON형태로 반환
    }

    // 가변 변수 name 과 매개변수 name이 다른 경우에는 @PathVariable 매개변수로 value=name(가변변수)로 매핑해줘야한다.
//    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
//    public HelloWorldBean helloWorldBean(@PathVariable(value="name") String name1){ // HelloWorldBean()과매개변수가 다르므로 오버로딩
//        return new HelloWorldBean(String.format("Hello World, %s", name1));
//    }


    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name){ // HelloWorldBean()과매개변수가 다르므로 오버로딩
        return new HelloWorldBean(String.format("Hello World, %s", name));
    }

    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized(
            @RequestHeader(name="Accept-Language",required = false) Locale locale){ // Locale : 지역
        //accept-language라는 헤더값이 없으면 디폴트값이 나온다.(한국어)
        return messageSource.getMessage("greeting.message",null,locale);
    }
}
