package nl.abnamro.assignmentrecipe.mapper;

import nl.abnamro.assignmentrecipe.model.Ingredient;
import nl.abnamro.assignmentrecipe.model.Recipe;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;

import java.time.Instant;

public class RecipeMapper {

    public static Recipe fromDto (RecipeDto recipeDto){
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDto.title());
        recipe.setInstructions(recipeDto.instructions());
        recipe.setServings(recipeDto.servings());
        recipe.setVegetarian(recipeDto.vegetarian());
        recipe.setCreatedAt(Instant.now());
        recipe.setIngredients(recipeDto.ingredients().stream().map(ingredientName -> {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            return ingredient;
        }).toList());
        return recipe;
    }

    public static RecipeDto toDto (Recipe recipe){
        return new RecipeDto(recipe.getTitle(),
                recipe.getServings(),
                recipe.getVegetarian(),
                recipe.getInstructions(),
                recipe.getIngredients().stream().map(Ingredient::getName).toList(),
                recipe.getCreatedAt());
    }
}
