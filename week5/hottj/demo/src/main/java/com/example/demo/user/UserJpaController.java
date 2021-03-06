package com.example.demo.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/jpa") // prefix
public class UserJpaController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // http://localhost:8080/jpa/users
    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id); // 있을지 없을지 모르니 Optional로 감싸야한다.
        if (!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        // 헤테오스
        // 개별 조회지만 전체 조회 링크도 보여주게끔 한다.
        EntityModel<User> model = new EntityModel<>(user.get());
        WebMvcLinkBuilder linkTo=linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        return model; // 존재할 경우, get으로 옵셔널 객체에서 꺼낸다.
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userRepository.save(user);

        // id 값 자동으로 지정
        URI location=ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s} not found", id));
        }

        User storedUser = optionalUser.get();
        storedUser.setName(user.getName());
        storedUser.setPassword(user.getPassword());

        User updatedUser = userRepository.save(storedUser);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // /jpa/users/9991/posts -> 9991번의 게시판들을 보여주세요
    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id){
        Optional<User> user = userRepository.findById(id); // 있을지 없을지 모르니 Optional로 감싸야한다.
        if (!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }
        return user.get().getPosts();
    }

    // 게시판 생성
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post){
        Optional<User> user = userRepository.findById(id); // 있을지 없을지 모르니 Optional로 감싸야한다.
        if (!user.isPresent()){
            throw new UserNotFoundException(String.format("ID[%s] not found",id));
        }

        post.setUser(user.get()); // 게시판에는 사용자도 저장되므로, 사용자 정보도 필요하다.
        Post savedPost = postRepository.save(post);

        // id 값 자동으로 지정
        URI location=ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}") // savedPost 값이 여기에 들어간다.
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}