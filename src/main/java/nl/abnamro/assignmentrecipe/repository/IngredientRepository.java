package nl.abnamro.assignmentrecipe.repository;

import nl.abnamro.assignmentrecipe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    List<Ingredient> findAllByRecipe_Id(Integer recipeId);
}