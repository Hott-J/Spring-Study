package com.example.SpringBootCommunityRest.repository;

import com.example.SpringBootCommunityRest.domain.User;
import com.example.SpringBootCommunityRest.domain.projection.UserOnlyContainName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = UserOnlyContainName.class)
public interface UserRepository extends JpaRepository<User, Long> {
}
