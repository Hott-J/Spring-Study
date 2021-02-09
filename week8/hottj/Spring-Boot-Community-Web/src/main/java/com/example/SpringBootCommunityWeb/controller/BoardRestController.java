package com.example.SpringBootCommunityWeb.controller;

import com.example.SpringBootCommunityWeb.domain.Board;
import com.example.SpringBootCommunityWeb.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/boards")
public class BoardRestController {
    private BoardRepository boardRepository;

    public BoardRestController(BoardRepository boardRepository){
        this.boardRepository=boardRepository;
    }

    // HTTP GET이고 JSON 형태 사용
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBoards(@PageableDefault Pageable pageable){
        Page<Board> boards = boardRepository.findAll(pageable);
        PagedResources.PageMetadata pageMetadata =new PagedResources.PageMetadata(pageable.getPageSize(),boards.getNumber(),boards.getTotalElements());
        PagedResources<Board> resources = new PagedResources<>(boards.getContent(), pageMetadata);
        // HATEOS BoardApiController.class의 메소드 링크 제공
        resources.add(linkTo(methodOn(BoardRestController.class).getBoards(pageable)).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    // HTTP POST이고 Board를 Body 정보를 Body로 받음
    @PostMapping
    public ResponseEntity<?> postBoard(@RequestBody Board board) {
        //valid 체크
        board.setCreatedDateNow();
        boardRepository.save(board);
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    // HTTP PUT으로 idx는 url path로 Board 정보는 Body로 받음
    @PutMapping("/{idx}")
    public ResponseEntity<?> putBoard(@PathVariable("idx")Long idx, @RequestBody Board board) {
        //valid 체크
        Board persistBoard = boardRepository.getOne(idx) ;
        persistBoard.update(board);
        boardRepository.save(persistBoard);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    // HTTP DELETE로 idx를 url path로 받음
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteBoard(@PathVariable("idx")Long idx) {
        //valid 체크
        boardRepository.deleteById(idx);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
