package com.community.havi;

import com.community.havi.controller.BookController;
import com.community.havi.domain.Book;
import com.community.havi.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class) // 특정 컨트롤러에 대한 가벼운 WebMvc Test (따라서, 모든 의존성을 로드하는 것이 아닌 BookController관련 빈만 로드한다)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    // 컨트롤러 내부의 의존성 객체인 BookService를 목 객체(가짜 객체)로 대체함. 특정 행위를 실제 객체처럼 동작하게 함 (given으로 메서드 반환갑 설정)
    @MockBean
    private BookService bookService;

    @Test
    public void Book_MVC_테스트() throws Exception {
        Book book = new Book("Spring Boot book", LocalDateTime.now());

        // given -> getBookList()메서에 대한 반환값 정의
        given(bookService.getBookList()).willReturn(Collections.singletonList(book));

        mvc.perform(get("/books"))
                .andExpect(status().isOk()) // HTTP 200 OK 가 반환되는가?
                .andExpect(view().name("book")) // 반환되는 뷰의 이름이 "book"인가?
                .andExpect(model().attributeExists("bookList")) // 모델의 프로퍼티 중 "bookList"라는 프로퍼티가 있는가?
                .andExpect(model().attribute("bookList", contains(book))); // "bookList" 프로퍼티에 "book"객체가 담겨져 있는가?
    }
}
