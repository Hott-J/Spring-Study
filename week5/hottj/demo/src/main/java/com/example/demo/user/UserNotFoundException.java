package com.example.demo.user;
// HTTP Status Code
// 2XX -> OK
// 4XX -> Client 잘못
// 5XX -> Server 잘못

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 아래 오류는 앞으로 NOT_FOUND 로 처리된다. 서버측 오류 중 데이터가 없는건 404로 처리
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message); // 부모 클래스쪽에서 전달받은 메시지를 반환
    }
}
