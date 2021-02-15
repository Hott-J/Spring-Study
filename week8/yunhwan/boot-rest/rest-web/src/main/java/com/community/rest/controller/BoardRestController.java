package com.community.rest.controller;

import com.community.rest.domain.Board;
import com.community.rest.repository.BoardRepository;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/boards") // 복수형
//@Log
public class BoardRestController {

    private BoardRepository boardRepository;

    public BoardRestController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // uri는 기본값, 반환값을 Json형태로 지정
    // Pageable객체 → page (페이지 번호), size(페이지 크기), sort 프로퍼티를 가짐
    public ResponseEntity<?> getBoards(@PageableDefault Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);

        // 전체 페이지 수, 현재 페이지 번호, 총 게시판 수의 정보를 담는 PageMetadata 객체 생성 (PagedResources의 생성자의 인자로 사용될 예정)
        PageMetadata pageMetadata = new PageMetadata(pageable.getPageSize(), boards.getNumber(), boards.getTotalElements());

        // 리스트 컬렉션 형태의 boards와 메타데이터를 생성자에 넘겨 객체를 생성한다. 이 객체는 HATEOAS가 적용되며 페이징값까지 생성된 REST형 데이터를 만들어
        PagedResources<Board> resources = new PagedResources<>(boards.getContent(), pageMetadata);

        // 추가적으로 제공할 링크를 추가
        resources.add(linkTo(methodOn(BoardRestController.class).getBoards(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    // 커뮤니티 게시판 글 추가를 위한 메서드 추가
    @PostMapping
    public ResponseEntity<?> postBoard(@RequestBody Board board) {
        //log.info("gogo post");
        board.setCreatedDateNow(); // 서버 시간으로 생성된 날짜 저장
        boardRepository.save(board);
        return new ResponseEntity<>("{}", HttpStatus.CREATED); // 바디에는 아무것도 반환하지 않고 상태값은 CREATED를 반환
    }

    // 커뮤니티 게시판 글 수정을 위한 메서드 추가
    @PutMapping("/{idx}")
    public ResponseEntity<?> putBoard(@PathVariable("idx") Long idx, @RequestBody Board board) {
        //log.info("gogo put");
        // lazy initialize exception 발생으로 코드를 getOne(idx)에서 살짝 수정
        Optional<Board> persistBoard = boardRepository.findById(idx);// getOne(idx);
        persistBoard.get().update(board); // createdTime은 건드리지 않고 모든 필드 수정.
        boardRepository.save(persistBoard.get());
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteBoard(@PathVariable("idx") Long idx) {
        //log.info("gogo delete");
        boardRepository.deleteById(idx);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    // API는 8081 포트로 커뮤니티 게시판은 8080포트로 실행된다.
    // 따라서, Ajax 통신하는 쪽에서 API사용하는 쪽의 Security 비밀번호를 헤더에 추가해줘야한다.
//    {
//        url: "http://localhost:8081/api/boards",
//                type: "POST",
//            data: jsonData,
//            contentType: "application/json",
//            headers: {
//        "Authorization": "Basic " + btoa("user" + ":" + "9fec7b06-e3c3-4e59-8682-4d0aa438afef")
//    }
}
