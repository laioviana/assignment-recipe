package nl.abnamro.assignmentrecipe.repository;

import nl.abnamro.assignmentrecipe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByRecipe_Id(Long recipeId);
}