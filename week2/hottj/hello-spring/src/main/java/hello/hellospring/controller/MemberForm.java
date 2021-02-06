package hello.hellospring.controller;

public class MemberForm {
    private String name; //html에서 입력된 name이 들어온다.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
