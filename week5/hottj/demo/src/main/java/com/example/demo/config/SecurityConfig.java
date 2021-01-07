package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.net.Authenticator;

@Configuration // 스프링부트가 기동하면서 메모리에 설정 정보와 같이 로딩
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.inMemoryAuthentication()
                .withUser("hottj") // ID
                .password("{noop}0000") //인코딩없이 사용 no operation , Password.
                .roles("USERS");
    }
    // yml에 설정해놓은 아이디 비번으로는 로그인이 되지 않는다.
}
