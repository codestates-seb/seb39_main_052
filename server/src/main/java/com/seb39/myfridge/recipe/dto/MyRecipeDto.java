package com.seb39.myfridge.recipe.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.recipe.enums.RecipeSort;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class MyRecipeDto {

    @Getter
    @Setter
    public static class Mine{
        private Long id;
        private String title;
        private String imagePath;
        private int view;
        private int heartCounts;
        private boolean heartExist;
        private int commentCounts;
        private LocalDateTime lastModifiedAt;

        @QueryProjection
        public Mine(Long id, String title, String imagePath, int view, int heartCounts, int commentCounts, LocalDateTime lastModifiedAt) {
            this.id = id;
            this.title = title;
            this.imagePath = imagePath;
            this.view = view;
            this.heartCounts = heartCounts;
            this.commentCounts = commentCounts;
            this.lastModifiedAt = lastModifiedAt;
        }

        @Override
        public String toString() {
            return "Mine{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", imagePath='" + imagePath + '\'' +
                    ", view=" + view +
                    ", heartCounts=" + heartCounts +
                    ", heartExist=" + heartExist +
                    ", commentCounts=" + commentCounts +
                    ", lastModifiedAt=" + lastModifiedAt +
                    '}';
        }
    }

    @Getter
    @Setter
    public static class Favorite{
        private Long id;
        private String title;
        private String imagePath;
        private MemberDto.Response member;
        private LocalDateTime lastModifiedAt;

        @QueryProjection
        public Favorite(Long id, String title, String imagePath, Long memberId, String memberName, String profileImagePath, LocalDateTime lastModifiedAt) {
            MemberDto.Response member = new MemberDto.Response();
            member.setId(memberId);
            member.setName(memberName);
            member.setProfileImagePath(profileImagePath);
            this.member = member;

            this.id = id;
            this.title = title;
            this.imagePath = imagePath;
            this.lastModifiedAt = lastModifiedAt;
        }

        @Override
        public String toString() {
            return "Favorite{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", imagePath='" + imagePath + '\'' +
                    ", member=" + member +
                    ", lastModifiedAt=" + lastModifiedAt +
                    '}';
        }
    }
}
