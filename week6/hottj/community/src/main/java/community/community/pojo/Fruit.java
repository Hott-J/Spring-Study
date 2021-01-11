package community.community.pojo;

import lombok.Data;

@Data
public class Fruit { // Fruit POJO 생성
    private String name; // yml 파일의 list의 key값인 name 과 동일해야한다.
    private String color;
}
