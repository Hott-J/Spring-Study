package com.example.demo.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service // 서비스 빈으로 등록 @Component 라고 해도 되지만 좀 더 명시적인 서비스 사용
public class UserDaoService {
    private static List<User> users = new ArrayList<>(); // DB 역할하는 리스트

    private static int usersCount = 3;
    static{ //static 으로 만들었기때문에 static 블록에서 사용 가능하다
        users.add(new User(1,"Hottj1",new Date()));
        users.add(new User(2,"Hottj2",new Date()));
        users.add(new User(3,"Hottj3",new Date()));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if (user.getId() == null){
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) {//lombok 을 사용해서 바로 getId() 사용 가능하다
                return user;
            }
        }
        return null;
    }

    public User deleteById(int id){
        Iterator<User> iterator = users.iterator();

        while(iterator.hasNext()){
            User user = iterator.next();

            if(user.getId() == id){
                iterator.remove();
                return user;
            }
        }
        return null;
    }
}
