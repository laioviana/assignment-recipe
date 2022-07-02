package nl.abnamro.assignmentrecipe.service.impl;

import lombok.extern.slf4j.Slf4j;
import nl.abnamro.assignmentrecipe.mapper.RecipeMapper;
import nl.abnamro.assignmentrecipe.model.Ingredient;
import nl.abnamro.assignmentrecipe.model.Recipe;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.model.dto.UpdateRecipeDto;
import nl.abnamro.assignmentrecipe.repository.IngredientRepository;
import nl.abnamro.assignmentrecipe.repository.RecipeRepository;
import nl.abnamro.assignmentrecipe.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Optional<RecipeDto> createRecipe(RecipeDto recipeDto) {
        return Optional.of(recipeRepository.save(RecipeMapper.fromDto(recipeDto)))
                .map(RecipeMapper::toDto);
    }

    @Override
    public Page<RecipeDto> listAllRecipes(Integer page, Integer size) {
        return recipeRepository.findAll(PageRequest.of(page, size))
                .map(RecipeMapper::toDto);
    }

    @Override
    public Optional<RecipeDto> updateRecipe(Long recipeId, UpdateRecipeDto recipeDto) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> {
                    recipe.setTitle(recipeDto.title().orElse(recipe.getTitle()));
                    recipe.setInstructions(recipeDto.instructions().orElse(recipe.getInstructions()));
                    recipe.setServings(recipeDto.servings().orElse(recipe.getServings()));
                    recipe.setVegetarian(recipeDto.vegetarian().orElse(recipe.getVegetarian()));
                    updateIngredient(recipe, recipeDto.ingredients());
                    return recipeRepository.save(recipe);
                }).map(RecipeMapper::toDto);
    }

    @Override
    public Optional<RecipeDto> getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(RecipeMapper::toDto);
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    @Override
    public Page<RecipeDto> listAllRecipesByFilter(Optional<String> title,
                                                  Optional<Boolean> vegetarian,
                                                  Optional<Integer> servings,
                                                  Optional<String> instructions,
                                                  Optional<String> ingredient,
                                                  Boolean includeIngredient,
                                                  Integer page, Integer size) {
        return recipeRepository.findAllByFilter(title.orElse(null),
                vegetarian.orElse(null),
                servings.orElse(null),
                instructions.orElse(null),
                ingredient.orElse(null),
                includeIngredient,
                PageRequest.of(page, size))
                .map(RecipeMapper::toDto);
    }

    private void updateIngredient(Recipe recipe, List<String> ingredients) {
        ingredientRepository.deleteAll(ingredientRepository.findAllByRecipe_Id(recipe.getId()));
        ingredients.stream().map(ingredient -> {
            Ingredient newIngredient = new Ingredient();
            newIngredient.setRecipe(recipe);
            newIngredient.setName(ingredient);
            return ingredientRepository.save(newIngredient);
        });
    }


}
