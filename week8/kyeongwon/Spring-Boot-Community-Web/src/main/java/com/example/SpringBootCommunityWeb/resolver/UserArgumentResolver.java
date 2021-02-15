package com.example.SpringBootCommunityWeb.resolver;

import com.example.SpringBootCommunityWeb.annotation.SocialUser;
import com.example.SpringBootCommunityWeb.domain.User;
import com.example.SpringBootCommunityWeb.domain.enums.SocialType;
import com.example.SpringBootCommunityWeb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;

import static com.example.SpringBootCommunityWeb.domain.enums.SocialType.FACEBOOK;
import static com.example.SpringBootCommunityWeb.domain.enums.SocialType.GOOGLE;

// HandlerMethodArgumentResolver 를 상속하여 리졸버 정의
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    Logger logger = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;

    public UserArgumentResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class) != null && parameter.getParameterType().equals(User.class);
    }

    // 세션을 확인하여 User 객체를 가져오는 함수
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        return getUser(user, session);
    }

    // 인증된 User 객체를 만들어 권한을 부여하는 함수
    private User getUser(User user, HttpSession session) {
        if(user == null) {
            try {
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                logger.info("ID" + String.valueOf(authentication.getAuthorizedClientRegistrationId()));
                logger.info(String.valueOf(map));
                User convertUser = convertUser(authentication.getAuthorizedClientRegistrationId(), map);
                logger.info(String.valueOf(convertUser));
                logger.info(convertUser.getName());
                user = userRepository.findByEmail(convertUser.getEmail());
                if (user == null) { user = userRepository.save(convertUser); }

                setRoleIfNotSame(user, authentication, map);
                session.setAttribute("user", user);
            } catch (ClassCastException e) {
                return user;
            }
        }
        return user;
    }

    // 빌더를 사용하여 User 객체를 만들어주는 역할을 하는 함수
    private User convertUser(String authority, Map<String, Object> map) {
        User user = getModernUser(FACEBOOK, map);
        logger.info(String.valueOf("abc" + String.valueOf(user)));
        //if(FACEBOOK.getValue().equals(authority))
        logger.info(String.valueOf(FACEBOOK));
        logger.info(String.valueOf(authority));
            if(FACEBOOK.isEquals(authority))
        {
            logger.info(String.valueOf("face " + FACEBOOK.getValue()));
            logger.info(String.valueOf("authority " + authority));
            return user;
        }
        else if(GOOGLE.isEquals(authority)) return getModernUser(GOOGLE, map);
        return null;
    }

    // FaceBook Gooogle의 공통 명명규칙을 가진 그룹으로 User를 매핑하는 함수
    private User getModernUser(SocialType socialType, Map<String, Object> map) {
        return User.builder()
                .name(String.valueOf(map.get("name")))
                .email(String.valueOf(map.get("email")))
                .principal(String.valueOf(map.get("id")))
                .socialType(socialType)
                .createdDate(LocalDateTime.now())
                .build();
    }

    // 권한을 갖고 있는지 체크하는 함수
    private void setRoleIfNotSame(User user, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
        if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(user.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(user.getSocialType().getRoleType())));
        }
    }
}
