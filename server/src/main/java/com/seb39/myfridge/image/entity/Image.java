package com.seb39.myfridge.image.entity;

import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int idx;

    private String originalName;

    private String saveName;

    private String imagePath;

    private Long size;

    private String isUpdated;

    private String isDeleted;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;


    public void addRecipeImage(Recipe recipe) {
        recipe.setImage(this);
        this.setRecipe(recipe);
    }

    public void addStepImage(Step step) {
        step.setImage(this);
        this.setStep(step);
    }
}
