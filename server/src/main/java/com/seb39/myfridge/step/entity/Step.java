package com.seb39.myfridge.step.entity;

import com.seb39.myfridge.recipe.entity.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long id;

    private int sequence;

    private String title;

    @Lob
    private String content;

    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public void addRecipe(Recipe recipe) {
        if (this.recipe != null) {
            this.recipe.getSteps().remove(this);
        }
        this.recipe = recipe;
        recipe.getSteps().add(this);
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
