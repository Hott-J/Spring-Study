package com.example.SpringBootCommunityRest.domain.projection;

import com.example.SpringBootCommunityRest.domain.Board;
import org.springframework.data.rest.core.config.Projection;

@Projection(name="getOnlyTitle", types={Board.class})
public interface BoardOnlyContainTitle {

    String getTitle();
}
