package com.example.SpringBootCommunityRest.repository;

import com.example.SpringBootCommunityRest.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
