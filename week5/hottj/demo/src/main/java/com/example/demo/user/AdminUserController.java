package com.example.demo.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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

    // GET /users/1 or /users/10 -> String 형태로 전달됨
    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id){
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
}
