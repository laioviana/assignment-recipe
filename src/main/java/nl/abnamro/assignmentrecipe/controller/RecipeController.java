package nl.abnamro.assignmentrecipe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import nl.abnamro.assignmentrecipe.model.dto.RecipeDto;
import nl.abnamro.assignmentrecipe.model.dto.UpdateRecipeDto;
import nl.abnamro.assignmentrecipe.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping
    public Page<RecipeDto> listAllRecipes(@RequestParam(name = "page", defaultValue = "0") int page,
                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Listing all recipes. page {} size {}", page, size);
        return recipeService.listAllRecipes(page, size);
    }

    @GetMapping("/filter")
    public Page<RecipeDto> listAllRecipesByFilter(@RequestParam(name = "title") Optional<String> title,
                                                  @RequestParam(name = "vegetarian") Optional<Boolean> vegetarian,
                                                  @RequestParam(name = "servings") Optional<Integer> servings,
                                                  @RequestParam(name = "instructions") Optional<String> instructions,
                                                  @RequestParam(name = "ingredient") Optional<String> ingredient,
                                                  @RequestParam(name = "includeIngredient", defaultValue = "true") Boolean includeIngredient,
                                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Listing recipes by filters");
        return recipeService.listAllRecipesByFilter(title, vegetarian, servings, instructions, ingredient, includeIngredient, page, size);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Integer recipeId) {
        log.info("Listing recipe with id: {}", recipeId);
        return recipeService.getRecipeById(recipeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<RecipeDto> createRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        log.info("Creating recipe with title: {}", recipeDto.title());
        return recipeService.createRecipe(recipeDto);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable Integer recipeId, @Valid @RequestBody UpdateRecipeDto updateRecipeDto) {
        log.info("Updating recipe {}.", recipeId);
        return recipeService.updateRecipe(recipeId, updateRecipeDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{recipeId}")
    public void deleteRecipe(@PathVariable Integer recipeId) {
        log.info("Deleting recipe with id = {}.", recipeId);
        recipeService.deleteRecipe(recipeId);
    }

}
