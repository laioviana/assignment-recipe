package nl.abnamro.assignmentrecipe.util;

import nl.abnamro.assignmentrecipe.model.Ingredient;
import nl.abnamro.assignmentrecipe.model.Recipe;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.model.dto.UpdateRecipeDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class CreationUtils {

    private CreationUtils(){
    }

    public static RecipeDto createRecipeDtoHamburger() {
        return new RecipeDto("Hamburger", 1, Boolean.FALSE, "Add meat to the bread", List.of("Meat", "Bread"), null);
    }

    public static RecipeDto createRecipeDtoPizza() {
        return new RecipeDto("Pizza", 3 ,Boolean.TRUE ,"Add dough with cheese and tomato souce", List.of("Tomato Sauce"), null);
    }

    public static RecipeDto createRecipeDtoCarbonara() {
        return new RecipeDto("Carbonara Spaghetti", 4 ,Boolean.FALSE ,"Add pasta to egg and bacon", List.of("Pasta"), null);
    }

    public static Recipe createRecipeHamburger(){
        return Recipe.builder()
                .id(1)
                .title("Hamburger")
                .servings(1)
                .vegetarian(Boolean.FALSE)
                .instructions("Add meat to the bread")
                .ingredients(List.of(Ingredient.builder().id(1L).name("Meat").build()))
                .createdAt(Instant.now())
                .build();
    }

    public static Recipe createRecipePizza(){
        return Recipe.builder()
                .id(2)
                .title("Pizza")
                .servings(3)
                .vegetarian(Boolean.TRUE)
                .instructions("Add dough with cheese and tomato souce")
                .ingredients(List.of(Ingredient.builder().id(2L).name("Tomato Sauce").build()))
                .createdAt(Instant.now())
                .build();
    }

    public static UpdateRecipeDto createUpdateRecipeDtoHamburger() {
        return new UpdateRecipeDto(Optional.of("Veggi Hamburger"), Optional.of(1), Optional.of(Boolean.FALSE), Optional.of("Add soy burger to the bread"), List.of("Meat", "Bread"));
    }

}
