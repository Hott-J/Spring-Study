# :cherry_blossom: Spring 

---

## :one: 웹 MVC 개발

### :smile: 회원 웹 기능 - 홈 화면 추가

- 홈 컨트롤러에 *@GetMapping* 으로 '/' 가 되어있으므로 home.html을 찾는다. 있으므로 기존에 static 폴더에 있던 index.html은 실행되지 않는다.
  - 스프링 컨테이너안에 있는 관련 컨트롤러를 먼저 찾고, 없으면 static 파일들을 찾는다. **우선순위**

![스프링2](https://user-images.githubusercontent.com/47052106/103139411-18c42c80-471f-11eb-81ae-9c759d3691bf.JPG)
