package com.seb39.myfridge.ingredient.entity;

import com.seb39.myfridge.recipe.entity.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_id")
    private Long id;

    private String quantity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public void addRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.getRecipeIngredients().add(this);
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        ingredient.getRecipeIngredients().add(this);
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "id=" + id +
                ", quantity='" + quantity + '\'' +
                ", recipe=" + recipe +
                ", ingredient=" + ingredient +
                '}';
    }
}
