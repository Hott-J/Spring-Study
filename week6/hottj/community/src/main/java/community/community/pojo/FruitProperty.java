package community.community.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties("fruit")
public class FruitProperty {
//    private List<Map> list;
    private List<Fruit> list; // Fruit POJO 타입 반영
}
