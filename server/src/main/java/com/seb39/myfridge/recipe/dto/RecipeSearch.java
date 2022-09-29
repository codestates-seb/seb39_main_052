package com.seb39.myfridge.recipe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.querydsl.core.annotations.QueryProjection;
import com.seb39.myfridge.member.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class RecipeSearch {

    @Getter
    @Setter
    public static class Request{
        private String title = "";
        private List<String> ingredients = new ArrayList<>();
        private int page = 1;
        private SortType sortType = SortType.RECENT;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String title;
        private MemberDto.Response member;
        private String imagePath;
        private int heartCounts;
        private int view;
        private LocalDateTime lastModifiedAt;
        private boolean heartExist = false;

        public Response() {
        }

        @QueryProjection
        public Response(Long id, String title, Long memberId, String memberName, String memberProfileImagePath, String imagePath, int heartCounts, int view, LocalDateTime lastModifiedAt) {
            MemberDto.Response member = new MemberDto.Response();
            member.setId(memberId);
            member.setName(memberName);
            member.setProfileImagePath(memberProfileImagePath);

            this.id = id;
            this.title = title;
            this.member = member;
            this.imagePath = imagePath;
            this.heartCounts = heartCounts;
            this.view = view;
            this.lastModifiedAt = lastModifiedAt;
        }
    }

    public enum SortType{
        VIEW,
        RECENT,
        HEART;
    }
}
