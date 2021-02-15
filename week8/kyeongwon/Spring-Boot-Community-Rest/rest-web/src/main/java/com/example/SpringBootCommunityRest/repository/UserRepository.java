package com.example.SpringBootCommunityRest.repository;

import com.example.SpringBootCommunityRest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
