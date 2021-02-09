package com.yunhwan.springbootcommunityweb.web.repository;

import com.yunhwan.springbootcommunityweb.web.domain.Board;
import com.yunhwan.springbootcommunityweb.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUser(User user);
}
