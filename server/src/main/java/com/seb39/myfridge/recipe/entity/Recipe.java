package com.seb39.myfridge.recipe.entity;

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
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    private String title;

    @ColumnDefault("0")
    private int view;

    private String imagePath;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy = "recipe")
    private List<Step> steps = new ArrayList<>();

    public void addSteps(Step step) {
        this.steps.add(step);
        step.setRecipe(this);
    }


    /**
     * 1. Member 엔티티와 연동 필요
     * 2. RecipeIngredients 엔티티와 연동 필요
     */
}
