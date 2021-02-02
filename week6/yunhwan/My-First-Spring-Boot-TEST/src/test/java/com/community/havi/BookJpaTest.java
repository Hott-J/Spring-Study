package com.community.havi;

import ch.qos.logback.classic.LoggerContext;
import com.community.havi.domain.Book;
import com.community.havi.repository.BookRepository;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookJpaTest {
    private final static String BOOT_TEST_TITLE = "Spring Boot Test Book";

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    // testEntityManager로 persist() 기능이 정상 작동 하는지 테스트 합니다.
    @Test
    public void BookList_저장하기_테스트() {
        Book book = Book.builder()
                .title(BOOT_TEST_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();

        testEntityManager.persist(book);

        assertThat(bookRepository.getOne(book.getIdx()), is(book));
    }

    // 서로 다른 Book 3개를 저장한 후 저장된 Book의 개수가 3개가 맞는지 확인하고 저장된 Book에 각 객체가 포함되어 있는지 테스트
    @Test
    public void BookList_저장하고_검색_테스트() {

        Book book1 = Book.builder()
                .title(BOOT_TEST_TITLE+"1")
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book1);

        Book book2 = Book.builder()
                .title(BOOT_TEST_TITLE+"2")
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book2);

        Book book3 = Book.builder()
                .title(BOOT_TEST_TITLE+"3")
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book3);

        List<Book> bookList = bookRepository.findAll();
        //System.out.println(bookList.get(0).getTitle() + ", " + bookList.get(1).getTitle() + ", " + bookList.get(2).getTitle());

        assertThat(bookList, hasSize(3));
        assertThat(bookList, contains(book1, book2, book3));
    }

    // 두 개 저장한 후 삭제하고 잘 삭제되었는지 테스트
    @Test
    public void BookList_저장하고_삭제_테스트() {
        Book book1 = Book.builder()
                .title(BOOT_TEST_TITLE+"1")
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book1);

        Book book2 = Book.builder()
                .title(BOOT_TEST_TITLE+"2")
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book2);

        bookRepository.deleteAll();
        assertThat(bookRepository.findAll(), IsEmptyCollection.empty());
    }
}
