package com.yunhwan.springbootcommunityweb.web.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.yunhwan.springbootcommunityweb.web.domain.enums.SocialType.*;


/*
* 각 소셜미디어 리소스 정보를 빈으로 등록
* @ConfigurationProperties를 이용해 application.xxx설정 키 값을 묶어서 빈으로 등록할 수 있다.
* */
@Configuration
// 웹 시큐리티 기능을 이용하겠다는 어노테이션
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 시큐리티 최적화 설정 함수 오바라이드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests() // 인증 매커니즘 설정
                .antMatchers("/", "/oauth2/**", "/login/**",  "/css/**", "/images/**", "/js/**", "/console/**").permitAll() // 요청 패턴을 리스트 형식으로 설정하고 접근을 허용할 곳 지정
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                .antMatchers("/google").hasAuthority(GOOGLE.getRoleType())
                .anyRequest().authenticated() // 설정한 요청 이외의 리퀘스트는 인증된 사용자만 허용되도록 변경
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/loginSuccess") // 로그인 성공 시 url
                .failureUrl("/loginFailure") // 로그인 실패 시 url
                .and()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")) // 인증되지 않는 사람이 적용되는 지점
                .and()
                .formLogin()
                .successForwardUrl("/board/list") // 로그인 성공시의 포워딩 되는 url
                .and()
                .logout()
                .logoutUrl("/logout") // 로그아웃이 처리되는 url
                .logoutSuccessUrl("/") // 로그아웃 성공 시 연결되는 url
                .deleteCookies("JSESSIONID") // 로그아웃 시에 쿠키 삭제
                .invalidateHttpSession(true) // 로그아웃 시에 세션 삭제
                .and()
                .addFilterBefore(filter, CsrfFilter.class)
                .csrf().disable();
    }

    // 페이스북과 구글 OAuth2 등록
    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
        if ("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }
        if ("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();
        }
        return null;
    }
}
