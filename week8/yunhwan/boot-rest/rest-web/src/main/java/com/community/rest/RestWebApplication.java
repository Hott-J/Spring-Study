package com.community.rest;

import com.community.rest.domain.Board;
import com.community.rest.domain.User;
import com.community.rest.enums.BoardType;
import com.community.rest.repository.BoardRepository;
import com.community.rest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class RestWebApplication {

    public static void main(String[] args){
        SpringApplication.run(RestWebApplication.class, args);
    }

    // 이 클래스는 환경 구성 파일임을 알려
    @Configuration
    // @EnableWebSecurity은 웹 보안을 활성하하나 설정을 확장하거나 수정할 때 유용한 것 같다.
    @EnableWebSecurity
    // @PreAuthorize 와 @PostAuthorize 를 사용하기 위해 붙이는 어노테이션
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            CorsConfiguration configuration = new CorsConfiguration();

            // CorsConfiguration 객체를 생성하여 CORS에서 Origin, Method, Header 별로 허용할 값을 설정할 수 있다
            // CorsConfiguration.ALL은 '*'과 같음
            // 모든 경로에 대해 허용함
            configuration.addAllowedOrigin(CorsConfiguration.ALL);
            configuration.addAllowedMethod(CorsConfiguration.ALL);
            configuration.addAllowedHeader(CorsConfiguration.ALL);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();


            // 특정 경로에 대해 CorsConfiguration 객체에서 설정한 값을 CorsConfigurationSource 인터페이스를 구현한
            // UrlBasedCorsConfigurationSource에 적용시킴
            // 여기서는 모든 경로로 설정되어 있음
            source.registerCorsConfiguration("/**", configuration);
            http.httpBasic()
                    .and().authorizeRequests()
                    .anyRequest().permitAll()
                    .and().cors().configurationSource(source) // 앞에서 설정한 값들을 시큐리티 설정에 적용시켜줌
                    .and().csrf().disable();
        }
    }


//    @Bean
//    public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository) {
//        return (args) -> {
//            User user = userRepository.save(User.builder()
//                    .name("havi")
//                    .password("test")
//                    .email("havi@gmail.com")
//                    .createdDate(LocalDateTime.now())
//                    .build());
//
//            IntStream.rangeClosed(1, 200).forEach(index ->
//                    boardRepository.save(Board.builder()
//                            .title("게시글" + index)
//                            .subTitle("순서" + index)
//                            .content("컨텐츠")
//                            .boardType(BoardType.free)
//                            .createdDate(LocalDateTime.now())
//                            .updatedDate(LocalDateTime.now())
//                            .user(user).build())
//            );
//        };
//    }
}
