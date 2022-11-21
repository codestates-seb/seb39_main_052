package com.seb39.myfridge.domain.heart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeartResponse {
    private Long recipeId;
    private int heartCounts;

    private HeartResponse(Long recipeId, int heartCounts) {
        this.recipeId = recipeId;
        this.heartCounts = heartCounts;
    }

    public static HeartResponse of(Long recipeId, int heartCounts){
        return new HeartResponse(recipeId,heartCounts);
    }
}
