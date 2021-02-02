package com.community.pojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropertyTest {
    @Autowired
    FruitProperty fruitProperty;

    @Test
    public void Test() {
        List<Fruit> fruitData = fruitProperty.getList();

        assertThat(fruitData.get(0).getName(), is("banana"));
        assertThat(fruitData.get(0).getColor(), is("yellow"));

        assertThat(fruitData.get(1).getName(), is("apple"));
        assertThat(fruitData.get(1).getColor(), is("red"));

        assertThat(fruitData.get(2).getName(), is("water melon"));
        assertThat(fruitData.get(2).getColor(), is("green"));

        assertThat(fruitProperty.getColorName(), is("bbb"));
    }
}
