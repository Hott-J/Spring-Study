package com.example.SpringBootCommunityRest.domain.projection;

import com.example.SpringBootCommunityRest.domain.User;
import org.springframework.data.rest.core.config.Projection;

// User 이름만 노출하도록 설정
@Projection(name="getOnlyName", types={User.class})
public interface UserOnlyContainName {

    String getName();
}
