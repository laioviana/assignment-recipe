package nl.abnamro.assignmentrecipe.service;

import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.model.dto.UpdateRecipeDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RecipeService {
    Optional<RecipeDto> createRecipe(RecipeDto recipeDto);

    Page<RecipeDto> listAllRecipes(Integer page, Integer size);

    Optional<RecipeDto> updateRecipe(Long recipeId, UpdateRecipeDto recipeDto);

    Optional<RecipeDto> getRecipeById(Long recipeId);

    Page<RecipeDto> listAllRecipesByFilter(Optional<String> title, Optional<Boolean> vegetarian, Optional<Integer> servings, Optional<String> instructions, Optional<String> ingredient, Boolean includeIngredient, Integer page, Integer size);

    void deleteRecipe(Long recipeId);
}
