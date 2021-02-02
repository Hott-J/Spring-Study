package com.community.havi;

import com.community.havi.domain.Book;
import com.community.havi.service.BookRestService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;

import java.awt.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(BookRestService.class)
public class BookRestTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private BookRestService bookRestService;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    public void rest_테스트() {
        // "/rest/test" URI에 요청 보내고 그에 대한 응답을 test.json에서 json타입으로 받아옴
        // 그것을 같은지 비교하는 코드
        this.mockRestServiceServer.expect(requestTo("/rest/test"))
                .andRespond(withSuccess(new ClassPathResource("/test.json",
                        getClass()), MediaType.APPLICATION_JSON));
        Book book = this.bookRestService.getRestBook();
        assertThat(book.getTitle()).isEqualTo("테스트");
    }

    @Test
    public void rest_error_테스트() {
        // 서버 에러
        this.mockRestServiceServer.expect(requestTo("/rest/test"))
                .andRespond(withServerError()); // 발생한 서버 에러 가정
        this.thrown.expect(HttpServerErrorException.class); // 서버 에러 예측
        this.bookRestService.getRestBook(); // 서버 에러 발생 시킨 후 가정이 맞는 지 확인.
    }
}
