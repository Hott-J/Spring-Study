# :cherry_blossom: Spring 

---

## :one: 웹 MVC 개발

### :smile: 회원 웹 기능 - 홈 화면 추가

- 홈 컨트롤러에 *@GetMapping* 으로 '/' 가 되어있으므로 home.html을 찾는다. 있으므로 기존에 static 폴더에 있던 index.html은 실행되지 않는다.
  - 스프링 컨테이너안에 있는 관련 컨트롤러를 먼저 찾고, 없으면 static 파일들을 찾는다. **우선순위**

![스프링2](https://user-images.githubusercontent.com/47052106/103139411-18c42c80-471f-11eb-81ae-9c759d3691bf.JPG)

### :smile: 회원 웹 기능 - 등록

- form 태그
  - 값을 입력할 수 있는 html 태그
  - *action 속성*
    - action 속성은 <form> 태그에 입력된 내용을 처리하는 서버 프로그램의 **URI를 지정**하는 역할을 한다. 속성값은 아래와 같이 처리할 프로그램의 경로를 지정한다.
  ```html
  <form action="./login.do">
    form 내용
  </form>
  ```
  - *method 속성*
    - **get**
      - get 방식은 주소 표시줄에 입력한 내용이 나타나며 256byte~4096byte까지의 데이터만을 서버로 전송할 수 있다. 주소 표시줄에 입력한 내용이 노출되기 때문에 보안상의 문제가 민감한 경우에는 사용하지 않는다. 주소줄에는 ?name=value&name=value 형태로 나타난다.
    - **post**
      - post 방식은 입력된 내용의 크기에 제한을 받지 않고 입력한 내용이 노출되지 않기 때문에 회원가입, 로그인 시 등에 많이 사용된다.
