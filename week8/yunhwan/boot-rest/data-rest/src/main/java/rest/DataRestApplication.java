package rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rest.event.BoardEventHandler;
//import rest.domain.Board;
//import rest.domain.User;
//import rest.domain.enums.BoardType;
//import rest.repository.BoardRepository;
//import rest.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class DataRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRestApplication.class, args);
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
    /**
     * CORS 설정
     */
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

        /**
         * 인메모리 방식으로 User 정보를 관리해주는 InMemoryUserDetailsManager를 선언함
         * 인메모리 방식은 애플리케이션이 실행될 때 메모리에서 User 정보를 관리하도록 하는 방식임
         * 만약 User에 관한 정보를 애플리케이션 실행 중에 수정하면 애플리케이션이 셧다운될 때 수정했던 정보가 사라짐
         * @return
         */
        @Bean
        InMemoryUserDetailsManager userDetailsManager() {
            User.UserBuilder commonUser = User.withUsername("commonUser").password("{noop}common").roles("USER");
            /*
                일반 User인 commonUser를 생성하고 직접 테스트할 User인 havi도 생성하여 두 User 각각 USER, ADMIN 권한을 부여함
                스프링 시큐리티에서 제공하는 UserBuilder를 사용하면 간편하게 User를 생성할 수 있음
                스프링 부트 2.0(Spring Security 5.0 이상)부터는 암호화 인코딩 방식을 지정해야 함
                예를 들어 sha256 방식은 {sha256}과 같이 지정해야 함
                예제에서는 인코딩 방식을 지정하지 않기 위해 {noop}으로 표기했음
             */
            User.UserBuilder havi = User.withUsername("havi").password("{noop}test").roles("USER", "ADMIN");

            List<UserDetails> userDetailsList = new ArrayList<>();
            userDetailsList.add(commonUser.build());
            /*
                ImMemoryUserDetailsManager 생성 시 필요한 User 목록을 생성함
                UserBuilder를 사용했으므로 build() 메서드로 User를 만들 수 있음
             */
            userDetailsList.add(havi.build());

            return new InMemoryUserDetailsManager(userDetailsList);
        }
    }

    /**
     * 이벤트 핸들러를 등록하기 위해 빈으로 등록
     * @return
     */
    @Bean
    BoardEventHandler boardEventHandler() {
        return new BoardEventHandler();
    }
}
