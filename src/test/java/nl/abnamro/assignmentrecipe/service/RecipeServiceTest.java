package nl.abnamro.assignmentrecipe.service;

import nl.abnamro.assignmentrecipe.model.Recipe;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.repository.IngredientRepository;
import nl.abnamro.assignmentrecipe.repository.RecipeRepository;
import nl.abnamro.assignmentrecipe.service.impl.RecipeServiceImpl;
import nl.abnamro.assignmentrecipe.util.CreationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Test
    void createRecipe() {
        Recipe recipeHamburger = CreationUtils.createRecipeHamburger();
        when(recipeRepository.save(any())).thenReturn(recipeHamburger);
        final RecipeDto result = recipeService.createRecipe(CreationUtils.createRecipeDtoHamburger()).get();
        assertEquals(recipeHamburger.getTitle(), result.title());
        assertEquals(recipeHamburger.getServings(), result.servings());
        assertEquals(recipeHamburger.getInstructions(), result.instructions());
        assertEquals(recipeHamburger.getVegetarian(), result.vegetarian());
        assertEquals(recipeHamburger.getIngredients().size(), result.ingredients().size());
    }

    @Test
    void updateRecipe() {
        Recipe recipeHamburger = CreationUtils.createRecipeHamburger();
        when(recipeRepository.save(any())).thenReturn(recipeHamburger);
        when(recipeRepository.findById(any())).thenReturn(Optional.ofNullable(recipeHamburger));
        final RecipeDto result = recipeService.updateRecipe(1L,CreationUtils.createUpdateRecipeDtoHamburger()).get();
        assertEquals(recipeHamburger.getTitle(), result.title());
        assertEquals(recipeHamburger.getServings(), result.servings());
        assertEquals(recipeHamburger.getInstructions(), result.instructions());
        assertEquals(recipeHamburger.getVegetarian(), result.vegetarian());
        assertEquals(recipeHamburger.getIngredients().size(), result.ingredients().size());
    }

    @Test
    void getRecipeById() {
        Recipe recipeHamburger = CreationUtils.createRecipeHamburger();
        when(recipeRepository.findById(any())).thenReturn(Optional.ofNullable(recipeHamburger));
        final RecipeDto result = recipeService.getRecipeById(1L).get();
        assertEquals(recipeHamburger.getTitle(), result.title());
        assertEquals(recipeHamburger.getServings(), result.servings());
        assertEquals(recipeHamburger.getInstructions(), result.instructions());
        assertEquals(recipeHamburger.getVegetarian(), result.vegetarian());
        assertEquals(recipeHamburger.getIngredients().size(), result.ingredients().size());
    }

    @Test
    void getAllRecipe() {
        Recipe recipeHamburger = CreationUtils.createRecipeHamburger();
        Recipe recipePizza = CreationUtils.createRecipePizza();
        Pageable pageable = PageRequest.of(0, 10);
        when(recipeRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(recipeHamburger, recipePizza)));
        Page<RecipeDto> result = recipeService.listAllRecipes(0, 10);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void getRecipesByFilter() {
        Recipe recipeHamburger = CreationUtils.createRecipeHamburger();
        when(recipeRepository.findAllByFilter(any(),any(),any(),any(),any(),any(),any())).thenReturn(new PageImpl<>(List.of(recipeHamburger)));
        Page<RecipeDto> result = recipeService.listAllRecipesByFilter(Optional.of("hamb"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), null, 0, 10);
        assertEquals(recipeHamburger.getTitle(), result.stream().findFirst().get().title());
        assertEquals(recipeHamburger.getServings(), result.stream().findFirst().get().servings());
        assertEquals(recipeHamburger.getInstructions(), result.stream().findFirst().get().instructions());
        assertEquals(recipeHamburger.getVegetarian(), result.stream().findFirst().get().vegetarian());
        assertEquals(recipeHamburger.getIngredients().size(), result.stream().findFirst().get().ingredients().size());
    }

}
