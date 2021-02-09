package com.yunhwan.springbootcommunityweb.web.repository;

import com.yunhwan.springbootcommunityweb.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
