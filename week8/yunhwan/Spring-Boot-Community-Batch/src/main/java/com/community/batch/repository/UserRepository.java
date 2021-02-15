package com.community.batch.repository;

import com.community.batch.domain.User;
import com.community.batch.domain.enums.Grade;
import com.community.batch.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUpdatedDateBeforeAndStatusEquals(LocalDateTime minusYears, UserStatus status);

    List<User> findByUpdatedDateBeforeAndStatusEqualsAndGradeEquals(LocalDateTime minusYears, UserStatus status, Grade grade);
}
