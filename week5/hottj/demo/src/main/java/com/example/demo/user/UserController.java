package com.example.demo.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {
    private UserDaoService service; // 인스턴스 선언 -> 스프링 컨테이너에 의해 관리되도록 해야함
    //private UserDaoService service = new (x) -> 의존성 주입이란 방법을 사용해야함
    // 스프링 컨테이너에 등록된 빈은 개발자가 프로그램 사용도중 변경이 불가능하므로 일관성 있는 인스턴스 사용 가능하다.

    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){

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

        // HATEOAS
        EntityModel<User> model = new EntityModel<>(user);
        WebMvcLinkBuilder linkTo=linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn"); //포함시킬 데이터

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(model);
        mapping.setFilters(filters);
        return mapping;
    }

    @PostMapping("/users") // 생성시 Post
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){ //object(JSON,XML...) 타입을 받기위해 @RequestBody 사용
// @Valid 를 통한 유효성 검사
        User savedUser = service.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()// 현재 요청된 request 값 사용. 사용자 요청 uri
                .path("/{id}") // 반환값 . builAndExpand를 통해 얻은 값이 들어옴
                .buildAndExpand(savedUser.getId()) //{id}에 넣어줄 값
                .toUri(); // uri 생성

        return ResponseEntity.created(location).build();
    }
    // 폼 데이터, 제이커리, 자스 와 같은 게 필요하다. 화면단이 필요. 우리는 postman으로 간단히 한다.

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.deleteById(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

    // {id: "1", name: "new name", password: "new password"}
    @PutMapping("/users/{id}")
    public MappingJacksonValue updateUser(@PathVariable int id, @RequestBody User user) {
        User updatedUser = service.update(id, user);

        if (updatedUser == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", user.getId()));
        }

        // HATEOAS

        EntityModel<User> model = new EntityModel<>(updatedUser);
        WebMvcLinkBuilder linkTo=linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        MappingJacksonValue mapping = new MappingJacksonValue(model);
        return mapping;
    }
}
