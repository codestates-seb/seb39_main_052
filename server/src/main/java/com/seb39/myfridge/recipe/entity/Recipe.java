package com.seb39.myfridge.recipe.entity;

import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.heart.entity.Heart;
import com.seb39.myfridge.helper.BaseTimeEntity;
import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.step.entity.Step;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    private String title;

    @ColumnDefault("0")
    private int view;

//    private String imagePath;

    //몇 인분
    private int portion;

    //요리 소요 시간 ex) 1시간, 30분
    private String time;


    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Step> steps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<Heart> hearts = new ArrayList<>();

    /**
     * image
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;
}
