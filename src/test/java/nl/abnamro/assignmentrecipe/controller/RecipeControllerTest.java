package nl.abnamro.assignmentrecipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.assignmentrecipe.model.Ingredient;
import nl.abnamro.assignmentrecipe.model.Recipe;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        recipeRepository.deleteAll();
    }

    @Test
    public void givenRecipeDtoObject_whenCreateRecipe_thenReturnSavedRecipe() throws Exception {

        RecipeDto recipeDto = new RecipeDto("Hamburger", 1, Boolean.FALSE, "Add meat to the bread", List.of("Meat", "Bread"), null);

        ResultActions response = mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto)));

        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.title",
                        is(recipeDto.title())))
                .andExpect(jsonPath("$.servings",
                        is(recipeDto.servings())))
                .andExpect(jsonPath("$.vegetarian",
                        is(recipeDto.vegetarian())))
                .andExpect(jsonPath("$.instructions",
                        is(recipeDto.instructions())))
                .andExpect(jsonPath("$.ingredients",
                        is(recipeDto.ingredients())));

    }

    @Test
    public void givenRecipeDtoObjectWithNullValue_whenCreateRecipe_thenReturnBadRequest() throws Exception {
        RecipeDto recipeDto = new RecipeDto("Hamburger", 1, null, "Add meat to the bread", List.of("Meat", "Bread"), null);

        ResultActions response = mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDto)));

        response.andDo(print())
                 .andExpect(status().isBadRequest());
    }

    @Test
    public void givenListOfRecipes_whenGetAllRecipes_thenReturnRecipesList() throws Exception {
        List<Recipe> listOfRecipes = new ArrayList<>();
        listOfRecipes.add(Recipe.builder().title("Hamburger").servings(1).vegetarian(Boolean.FALSE).instructions("Add meat to the bread").ingredients(List.of(Ingredient.builder().name("Meat").build())).createdAt(Instant.now()).build());
        listOfRecipes.add(Recipe.builder().title("Pizza").servings(3).vegetarian(Boolean.TRUE).instructions("Add dough with cheese and tomato souce").ingredients(List.of(Ingredient.builder().name("Tomato Sauce").build())).createdAt(Instant.now()).build());
        recipeRepository.saveAll(listOfRecipes);

        ResultActions response = mockMvc.perform(get("/recipe"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(listOfRecipes.size())));
    }

    @Test
    public void givenRecipeId_whenGetRecipeById_thenReturnRecipeDtoObject() throws Exception {
        Recipe recipe = Recipe.builder().title("Hamburger").servings(1).vegetarian(Boolean.FALSE).instructions("Add meat to the bread").ingredients(List.of(Ingredient.builder().name("Meat").build())).createdAt(Instant.now()).build();
        recipeRepository.save(recipe);

        ResultActions response = mockMvc.perform(get("/recipe/{id}", recipe.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title",
                        is(recipe.getTitle())))
                .andExpect(jsonPath("$.servings",
                        is(recipe.getServings())))
                .andExpect(jsonPath("$.vegetarian",
                        is(recipe.getVegetarian())))
                .andExpect(jsonPath("$.instructions",
                        is(recipe.getInstructions())))
                .andExpect(jsonPath("$.ingredients.size()",
                        is(recipe.getIngredients().size())));

    }

    @Test
    public void givenInvalidRecipeId_whenGetRecipeById_thenReturnEmpty() throws Exception {
        Integer recipeId = 1;
        ResultActions response = mockMvc.perform(get("/recipe/{id}", recipeId));
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void givenUpdatedRecipe_whenUpdateRecipe_thenReturnUpdateRecipeDtoObject() throws Exception {

        Recipe savedRecipe = Recipe.builder().title("Hamburger").servings(1).vegetarian(Boolean.FALSE).instructions("Add meat to the bread").ingredients(List.of(Ingredient.builder().name("Meat").build())).createdAt(Instant.now()).build();
        recipeRepository.save(savedRecipe);

        RecipeDto updatedRecipe = new RecipeDto("Veggie Hamburger", 1, Boolean.TRUE,"Add soy burger to the bread", List.of("Soy Burger"), null);

        ResultActions response = mockMvc.perform(put("/recipe/{id}", savedRecipe.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRecipe)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title",
                        is(updatedRecipe.title())))
                .andExpect(jsonPath("$.servings",
                        is(updatedRecipe.servings())))
                .andExpect(jsonPath("$.vegetarian",
                        is(updatedRecipe.vegetarian())))
                .andExpect(jsonPath("$.instructions",
                        is(updatedRecipe.instructions())));
    }
    @Test
    public void givenUpdatedRecipeWithWrongId_whenUpdateRecipe_thenReturnNotFound() throws Exception {

        Recipe savedRecipe = Recipe.builder().title("Hamburger").servings(1).vegetarian(Boolean.FALSE).instructions("Add meat to the bread").ingredients(List.of(Ingredient.builder().name("Meat").build())).createdAt(Instant.now()).build();
        recipeRepository.save(savedRecipe);

        RecipeDto updatedRecipe = new RecipeDto("Veggie Hamburger", 1, Boolean.TRUE,"Add soy burger to the bread", List.of("Soy Burger"), null);

        ResultActions response = mockMvc.perform(put("/recipe/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRecipe)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenRecipeId_whenDeleteRecipe_thenReturnNoContent() throws Exception {
        Recipe recipe = Recipe.builder().title("Hamburger").servings(1).vegetarian(Boolean.FALSE).instructions("Add meat to the bread").ingredients(List.of(Ingredient.builder().name("Meat").build())).createdAt(Instant.now()).build();
        recipeRepository.save(recipe);

        ResultActions response = mockMvc.perform(delete("/recipe/{id}", recipe.getId()));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void givenListOfRecipes_whenGetAllRecipesWithFilter_thenReturnRightRecipes() throws Exception {

        Recipe hamburgerRecipe = Recipe.builder().title("Hamburger").servings(1).vegetarian(Boolean.FALSE).instructions("Add meat to the bread").ingredients(List.of(Ingredient.builder().name("Meat").build())).createdAt(Instant.now()).build();
        Recipe pizzaRecipe = Recipe.builder().title("Pizza").servings(3).vegetarian(Boolean.TRUE).instructions("Add dough with cheese and tomato souce").ingredients(List.of(Ingredient.builder().name("Tomato Sauce").build())).createdAt(Instant.now()).build();
        Recipe carbonaraRecipe = Recipe.builder().title("Carbonara Spaghetti").servings(4).vegetarian(Boolean.FALSE).instructions("Add pasta to egg and bacon").ingredients(List.of(Ingredient.builder().name("Pasta").build())).createdAt(Instant.now()).build();
        recipeRepository.saveAll(List.of(hamburgerRecipe, pizzaRecipe, carbonaraRecipe));

        ResultActions response = mockMvc.perform(get("/recipe/filter?title=hamb"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].title",
                        is(hamburgerRecipe.getTitle())))
                .andExpect(jsonPath("$.content[0].servings",
                        is(hamburgerRecipe.getServings())))
                .andExpect(jsonPath("$.content[0].vegetarian",
                        is(hamburgerRecipe.getVegetarian())))
                .andExpect(jsonPath("$.content[0].instructions",
                        is(hamburgerRecipe.getInstructions())));

        response = mockMvc.perform(get("/recipe/filter?vegetarian=true"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].title",
                        is(pizzaRecipe.getTitle())))
                .andExpect(jsonPath("$.content[0].servings",
                        is(pizzaRecipe.getServings())))
                .andExpect(jsonPath("$.content[0].vegetarian",
                        is(pizzaRecipe.getVegetarian())))
                .andExpect(jsonPath("$.content[0].instructions",
                        is(pizzaRecipe.getInstructions())));

        response = mockMvc.perform(get("/recipe/filter?instructions=bacon"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].title",
                        is(carbonaraRecipe.getTitle())))
                .andExpect(jsonPath("$.content[0].servings",
                        is(carbonaraRecipe.getServings())))
                .andExpect(jsonPath("$.content[0].vegetarian",
                        is(carbonaraRecipe.getVegetarian())))
                .andExpect(jsonPath("$.content[0].instructions",
                        is(carbonaraRecipe.getInstructions())));

        response = mockMvc.perform(get("/recipe/filter?ingredient=meat&includeIngredient=false"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(2)));
    }


}
