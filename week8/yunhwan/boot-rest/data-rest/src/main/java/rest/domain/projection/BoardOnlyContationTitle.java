package rest.domain.projection;

import org.springframework.data.rest.core.config.Projection;
import rest.domain.Board;

// 보드의 제목만 표시하는 프로젝션
@Projection(name = "getOnlyTitie", types = {Board.class})
public interface BoardOnlyContationTitle {
        String getTitle();
}
