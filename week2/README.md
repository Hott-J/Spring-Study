### :smile: Form

- **form**
  - 입력 양식 전체를 감싸는 태그
  - form은 컨트롤 요소(control element)로 구성된다. 텍스트, 버튼, 라디오 등이 컨트롤
  - name : form의 이름, 서버로 보내질 때 이름의 값으로 데이터 전송
  - action : form이 전송되는 서버 url 또는 html 링크
  - method : 전송 방법 설정. get은 default, post는 데이터를 url에 공개하지 않고 숨겨서 전송하는 방법
  - autocomplete : 자동 완성. on으로 하면 form 전체에 자동 완성 허용
```html
<form name="profile" action="/action_page.php" method="get" 
      autocomplete="on">
  <input type="text" name="id">
  <select>
    <option value="blue">
  </select>
</form>
```
