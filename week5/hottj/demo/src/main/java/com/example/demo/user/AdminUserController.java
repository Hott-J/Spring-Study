package com.example.demo.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin") // /admin/users 로 prefix가 된다.
public class AdminUserController {
    private UserDaoService service; // 인스턴스 선언 -> 스프링 컨테이너에 의해 관리되도록 해야함
    //private UserDaoService service = new (x) -> 의존성 주입이란 방법을 사용해야함
    // 스프링 컨테이너에 등록된 빈은 개발자가 프로그램 사용도중 변경이 불가능하므로 일관성 있는 인스턴스 사용 가능하다.

    public AdminUserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){ // 필터링 데이터를 반환하려면 MaapingJasksonValue 객체형으로 반환해야한다.

        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn"); //포함시킬 데이터

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    // GET /admin/users/1 -> /admin/v1/users/1
   // @GetMapping("v1/users/{id}")

    // @GetMapping(value="/users/{id}/",params="version=1")
    // 파라미터가 두개면 value 로 작성ㅌ, 맨뒤에 / 가 있어야 버젼 정보가 추가로 뒤에 들어가진다. <Request Parameter>
    //http://localhost:8088/admin/users/1/?version=1 와 같은 방식으로 요청보내면됨. 파라미터부분은 ? 로 넣어준다.

    //@GetMapping(value="/users/{id}",headers="X-API-VERSION=1")
    // headers 값은 임의로 정하면됨. <헤더를 이용한 방법> // KEY = X-API-VERSION, VALUE = 1

    @GetMapping(value="/users/{id}",produces="application/vnd.company.appv1+json")
    // <마임타입> KEY = Accept, VALUE = application/vnd.company.appv1+json
    public MappingJacksonValue retrieveUserV1(@PathVariable int id){
        User user = service.findOne(id);

        if (user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn"); //포함시킬 데이터

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    //@GetMapping("v2/users/{id}")
   // @GetMapping(value="/users/{id}/",params="version=2")
    //@GetMapping(value="/users/{id}",headers="X-API-VERSION=2")
    @GetMapping(value="/users/{id}",produces="application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id){
        User user = service.findOne(id);

        if (user==null){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        // User -> User2
        UserV2 userV2 = new UserV2(); // 디폴트 생성자 필요
        BeanUtils.copyProperties(user,userV2); // 공통 프로퍼티 복사, id,name,joinDate,password,ssn
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn","grade"); //포함시킬 데이터

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }
}
