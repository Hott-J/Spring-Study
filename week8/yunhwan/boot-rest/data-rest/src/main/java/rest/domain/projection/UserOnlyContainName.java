package rest.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import rest.domain.User;

// name - 해당 프로젝션을 사용하기 위한 키값을 지정
// types - 해당 프로젝션이 어떤 도메인에 바딩 될지 나타냄
// 프로퍼티 값으로 제공되는 name은 해당 프로젝션을 사용하기 위한 키값을 지정, types는 프로젝션이 바인딩되는 도메인
@Projection(name = "getOnlyName", types = { User.class })
public interface UserOnlyContainName {
    String getName();
}
