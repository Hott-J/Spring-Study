package rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import rest.domain.Board;
import rest.domain.projection.BoardOnlyContationTitle;

import java.util.List;

@RepositoryRestResource(excerptProjection = BoardOnlyContationTitle.class)
public interface BoardRepository extends JpaRepository<Board, Long> {


    //jpa인터페이스를 상속받는 메서드를 오버라이드하여 ADMIN 권한 지정
    // @PreAuthorize 로 save 메서드에 ADMIN 권한 지정
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    <S extends Board> S save(S entity);

    @RestResource
    List<Board> findByTitle(@Param("title") String title);
}
