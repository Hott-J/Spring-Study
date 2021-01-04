package com.example.demo.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private UserDaoService service; // 인스턴스 선언 -> 스프링 컨테이너에 의해 관리되도록 해야함
    //private UserDaoService service = new (x) -> 의존성 주입이란 방법을 사용해야함
    // 스프링 컨테이너에 등록된 빈은 개발자가 프로그램 사용도중 변경이 불가능하므로 일관성 있는 인스턴스 사용 가능하다.

    public UserController(UserDaoService service){
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    // GET /users/1 or /users/10 -> String 형태로 전달됨
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){
        return service.findOne(id);
    }

    @PostMapping("/users") // 생성시 Post
    public ResponseEntity<User> createUser(@RequestBody User user){ //object(JSON,XML...) 타입을 받기위해 @RequestBody 사용

        User savedUser = service.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()// 현재 요청된 request 값 사용
                .path("/{id}") // 반환값
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    // 폼 데이터, 제이커리, 자스 와 같은 게 필요하다. 화면단이 필요. 우리는 postman으로 간단히 한다.
}
