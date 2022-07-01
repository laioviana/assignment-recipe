package nl.abnamro.assignmentrecipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Test
    public void givenRecipeDtoObject_whenCreateRecipe_thenReturnSavedRecipe() throws Exception {

        RecipeDto recipeDto = new RecipeDto("Hamburger", 1, Boolean.FALSE, "Add meat to the bread", List.of("Meat", "Bread"), null);

        when(recipeService.createRecipe(any())).thenReturn(Optional.of(new RecipeDto("Hamburger", 1, Boolean.FALSE, "Add meat to the bread", List.of("Meat", "Bread"), Instant.now())));

        ResultActions response = mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipeDto)));

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
                .content(asJsonString(recipeDto)));

        response.andDo(print())
                 .andExpect(status().isBadRequest());
    }

    @Test
    public void givenListOfRecipes_whenGetAllRecipes_thenReturnRecipesList() throws Exception {
        List<RecipeDto> listOfRecipes = new ArrayList<>();
        listOfRecipes.add(new RecipeDto("Hamburger", 1 ,Boolean.FALSE ,"Add meat to the bread", List.of("Meat"),Instant.now()));
        listOfRecipes.add(new RecipeDto("Pizza", 3 ,Boolean.TRUE ,"Add dough with cheese and tomato souce", List.of("Tomato Sauce"),Instant.now()));
        when(recipeService.listAllRecipes(any(),any())).thenReturn(new PageImpl<>(listOfRecipes));

        ResultActions response = mockMvc.perform(get("/recipe"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(listOfRecipes.size())));
    }

    @Test
    public void givenRecipeId_whenGetRecipeById_thenReturnRecipeDtoObject() throws Exception {
        RecipeDto recipeDto = new RecipeDto("Hamburger", 1 ,Boolean.FALSE ,"Add meat to the bread", List.of("Meat"),Instant.now());
        when(recipeService.getRecipeById(1)).thenReturn(Optional.of(recipeDto));
        ResultActions response = mockMvc.perform(get("/recipe/{id}", 1));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title",
                        is(recipeDto.title())))
                .andExpect(jsonPath("$.servings",
                        is(recipeDto.servings())))
                .andExpect(jsonPath("$.vegetarian",
                        is(recipeDto.vegetarian())))
                .andExpect(jsonPath("$.instructions",
                        is(recipeDto.instructions())))
                .andExpect(jsonPath("$.ingredients.size()",
                        is(recipeDto.ingredients().size())));

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
        RecipeDto updatedRecipe = new RecipeDto("Veggie Hamburger", 1, Boolean.TRUE,"Add soy burger to the bread", List.of("Soy Burger"), null);

        when(recipeService.updateRecipe(any(),any())).thenReturn(Optional.of(updatedRecipe));

        ResultActions response = mockMvc.perform(put("/recipe/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRecipe)));

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
        RecipeDto updatedRecipe = new RecipeDto("Veggie Hamburger", 1, Boolean.TRUE,"Add soy burger to the bread", List.of("Soy Burger"), null);

        ResultActions response = mockMvc.perform(put("/recipe/{id}", 12)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedRecipe)));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenRecipeId_whenDeleteRecipe_thenReturnNoContent() throws Exception {
        ResultActions response = mockMvc.perform(delete("/recipe/{id}", 1));

        response.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void givenListOfRecipes_whenGetAllRecipesWithFilter_thenReturnRightRecipes() throws Exception {

        RecipeDto hamburgerRecipe = new RecipeDto("Hamburger", 1 ,Boolean.FALSE ,"Add meat to the bread", List.of("Meat"),Instant.now());
        RecipeDto pizzaRecipe = new RecipeDto("Pizza", 3 ,Boolean.TRUE ,"Add dough with cheese and tomato souce", List.of("Tomato Sauce"),Instant.now());
        RecipeDto carbonaraRecipe = new RecipeDto("Carbonara Spaghetti", 4 ,Boolean.FALSE ,"Add pasta to egg and bacon", List.of("Pasta"),Instant.now());

        when(recipeService.listAllRecipesByFilter(eq(Optional.of("hamb")),eq(Optional.empty()),eq(Optional.empty()),eq(Optional.empty()),eq(Optional.empty()),any(),any(),any())).thenReturn(new PageImpl<>(List.of(hamburgerRecipe)));
        when(recipeService.listAllRecipesByFilter(eq(Optional.empty()),eq(Optional.of(Boolean.TRUE)),eq(Optional.empty()),eq(Optional.empty()),eq(Optional.empty()),any(),any(),any())).thenReturn(new PageImpl<>(List.of(pizzaRecipe)));
        when(recipeService.listAllRecipesByFilter(eq(Optional.empty()),eq(Optional.empty()),eq(Optional.empty()),eq(Optional.of("bacon")),eq(Optional.empty()),any(),any(),any())).thenReturn(new PageImpl<>(List.of(carbonaraRecipe)));
        when(recipeService.listAllRecipesByFilter(eq(Optional.empty()),eq(Optional.empty()),eq(Optional.empty()),eq(Optional.empty()),eq(Optional.of("meat")),eq(Boolean.FALSE),any(),any())).thenReturn(new PageImpl<>(List.of(pizzaRecipe,carbonaraRecipe)));
        ResultActions response = mockMvc.perform(get("/recipe/filter?title=hamb"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].title",
                        is(hamburgerRecipe.title())))
                .andExpect(jsonPath("$.content[0].servings",
                        is(hamburgerRecipe.servings())))
                .andExpect(jsonPath("$.content[0].vegetarian",
                        is(hamburgerRecipe.vegetarian())))
                .andExpect(jsonPath("$.content[0].instructions",
                        is(hamburgerRecipe.instructions())));

        response = mockMvc.perform(get("/recipe/filter?vegetarian=true"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].title",
                        is(pizzaRecipe.title())))
                .andExpect(jsonPath("$.content[0].servings",
                        is(pizzaRecipe.servings())))
                .andExpect(jsonPath("$.content[0].vegetarian",
                        is(pizzaRecipe.vegetarian())))
                .andExpect(jsonPath("$.content[0].instructions",
                        is(pizzaRecipe.instructions())));

        response = mockMvc.perform(get("/recipe/filter?instructions=bacon"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].title",
                        is(carbonaraRecipe.title())))
                .andExpect(jsonPath("$.content[0].servings",
                        is(carbonaraRecipe.servings())))
                .andExpect(jsonPath("$.content[0].vegetarian",
                        is(carbonaraRecipe.vegetarian())))
                .andExpect(jsonPath("$.content[0].instructions",
                        is(carbonaraRecipe.instructions())));

        response = mockMvc.perform(get("/recipe/filter?ingredient=meat&includeIngredient=false"));
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(2)));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
