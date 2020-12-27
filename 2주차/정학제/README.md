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
    - **이 속성은 데이터를 어디로 보낼 것인지 지정한다.** 이 값은 반드시 *유효한 URL*이어야 한다.만약 이 속성을 지정하지 않으면 데이터는 폼이 있는 페이지의 URL로 보내질 것이다.
  ```html
  <form action="./login.do">
    form 내용
  </form>
  ```
  - *method 속성*
    - **이 속성은 데이터를 어떻게 보낼 것인지 정의한다.** HTTP protocol 은 요청 방법에 대해 다양한 방법들을 제공한다.  HTML 폼 데이터는 오직 2가지 방법으로만 전송 할 수 있는데 바로 GET 방식과 POST방식이 있다.
    - **get**
      - GET은 서버로부터 정보를 조회하기 위해 설계된 메소드입니다.
GET은 요청을 전송할 때 필요한 데이터를 Body에 담지 않고, 쿼리스트링을 통해 전송합니다. URL의 끝에 ?와 함께 이름과 값으로 쌍을 이루는 요청 파라미터를 쿼리스트링이라고 부릅니다. 만약, 요청 파라미터가 여러 개이면 &로 연결합니다. 쿼리스트링을 사용하게 되면 URL에 조회 조건을 표시하기 때문에 특정 페이지를 링크하거나 북마크할 수 있습니다. 쿼리스트링을 포함한 URL의 샘플은 아래와 같습니다. 여기서 요청 파라미터명은 name1, name2이고, 각각의 파라미터는 value1, value2라는 값으로 서버에 요청을 보내게 됩니다.
`www.example-url.com/resources?name1=value1&name2=value2`
그리고 GET은 불필요한 요청을 제한하기 위해 요청이 캐시될 수 있습니다. js, css, 이미지 같은 정적 컨텐츠는 데이터양이 크고, 변경될 일이 적어서 반복해서 동일한 요청을 보낼 필요가 없습니다. 정적 컨텐츠를 요청하고 나면 브라우저에서는 요청을 캐시해두고, 동일한 요청이 발생할 때 서버로 요청을 보내지 않고 캐시된 데이터를 사용합니다. 그래서 프론트엔드 개발을 하다보면 정적 컨텐츠가 캐시돼 컨텐츠를 변경해도 내용이 바뀌지 않는 경우가 종종 발생합니다. 이 때는 브라우저의 캐시를 지워주면 다시 컨텐츠를 조회하기 위해 서버로 요청을 보내게 됩니다.
    - **post**
      - POST는 리소스를 생성/변경하기 위해 설계되었기 때문에 GET과 달리 전송해야될 데이터를 HTTP 메세지의 Body에 담아서 전송합니다. HTTP 메세지의 Body는 길이의 제한없이 데이터를 전송할 수 있습니다. 그래서 POST 요청은 GET과 달리 대용량 데이터를 전송할 수 있습니다. 이처럼 POST는 데이터가 Body로 전송되고 내용이 눈에 보이지 않아 GET보다 보안적인 면에서 안전하다고 생각할 수 있지만, POST 요청도 크롬 개발자 도구, Fiddler와 같은 툴로 요청 내용을 확인할 수 있기 때문에 민감한 데이터의 경우에는 반드시 암호화해 전송해야 합니다. 그리고 POST로 요청을 보낼 때는 요청 헤더의 Content-Type에 요청 데이터의 타입을 표시해야 합니다. 데이터 타입을 표시하지 않으면 서버는 내용이나 URL에 포함된 리소스의 확장자명 등으로 데이터 타입을 유추합니다. 만약, 알 수 없는 경우에는 `application/octet-stream`로 요청을 처리합니다.

- *PostMapping*
  - 데이터를 Form 같은 곳에 넣어서 전달하고 등록할 때 주로 사용
  
- *GetMapping*
  - 데이터를 조회하는데 주로 사용
  
![스프링](https://user-images.githubusercontent.com/47052106/103180508-85832680-48d9-11eb-82a2-fdaafe855a04.JPG) 
name이 서버로 넘어올 때 Key가 된다.
![스프링1](https://user-images.githubusercontent.com/47052106/103180510-86b45380-48d9-11eb-83f0-099241b960f9.JPG) 
name에 해당하는 value가 들어가게 된다.
![스프링3](https://user-images.githubusercontent.com/47052106/103180511-86b45380-48d9-11eb-8708-86867d8def82.JPG)

### :smile: 회원 웹 기능 - 조회

![스프링1](https://user-images.githubusercontent.com/47052106/103181156-4953c400-48e1-11eb-8cd5-b91e6d46982a.JPG)
*model.addAttribute* 메소드를 통해 "members"가 key가 된다.
`model.addAttribute("변수이름","변수에 넣을 데이터값");`
![스프링2](https://user-images.githubusercontent.com/47052106/103181158-4fe23b80-48e1-11eb-8635-f6ce0cb2b9ce.JPG)
${members}로 값을 전부 가져온다.
`${변수이름}`


