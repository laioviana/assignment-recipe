package nl.abnamro.assignmentrecipe.mapper;

import nl.abnamro.assignmentrecipe.model.Ingredient;
import nl.abnamro.assignmentrecipe.model.Recipe;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;

import java.time.Instant;

public class RecipeMapper {

    public static Recipe fromDto (RecipeDto recipeDto){
        return Recipe.builder()
                .title(recipeDto.title())
                .instructions(recipeDto.instructions())
                .servings(recipeDto.servings())
                .vegetarian(recipeDto.vegetarian())
                .createdAt(Instant.now())
                .ingredients(recipeDto.ingredients().stream().map(ingredientName -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(ingredientName);
                    return ingredient;
                }).toList())
                .build();
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
