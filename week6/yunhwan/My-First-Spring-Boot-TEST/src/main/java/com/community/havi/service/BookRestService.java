package com.community.havi.service;

import com.community.havi.domain.Book;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookRestService {

    private final RestTemplate restTemplate;

    public BookRestService(RestTemplateBuilder restTemplateBuilder) {
        //빌더 패턴을 이용한 설정, 사실 root를 ""하는게 올바른 URI 같다.
        this.restTemplate = restTemplateBuilder.rootUri("/rest/test").build();
    }

    public Book getRestBook() {
        // url의 요청에 대한 HTTP 응답을 book 객체 타입으로 변환해서 응답
        return this.restTemplate.getForObject("/rest/test", Book.class);
    }
}
