# 🔶 Level3 API 구현을 위한 HATEOAS
```java
EntityModel<User> resource = new EntityModel<>(user);
WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());

resource.add(linkTo.withRel("all-users"));

return resource;
```
![image](https://user-images.githubusercontent.com/46257667/105514702-27e6cc00-5d17-11eb-9446-94aee93f9028.png)

✔ 다른 API로 넘어갈 수 있도록 도와준다.

>❓ 왜 필요한건데

❗ REST API 단점
* 앤드포인트 URL 변경 시 모두 수정해야 한다.
* 자원의 상태를 고려하지 않는다.

LEVEL 3 하이퍼 미디어 링크를 사용하여 서버가 클라이언트에게 자원의 보내면서 클라이언트는 다음 작업을 할 수 있는 URL을 보내준다.

