package com.example.SpringBootCommunityBatch.repository;

import com.example.SpringBootCommunityBatch.domain.User;
import com.example.SpringBootCommunityBatch.domain.enums.Grade;
import com.example.SpringBootCommunityBatch.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime localDateTime, UserStatus userStatus);
}
