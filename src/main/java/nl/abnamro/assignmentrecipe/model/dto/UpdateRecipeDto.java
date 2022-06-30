package nl.abnamro.assignmentrecipe.model.dto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public record UpdateRecipeDto(@NotNull Optional<String> title,
                        @NotNull Optional<Integer> servings,
                        @NotNull Optional<Boolean> vegetarian,
                        @NotNull Optional<String> instructions,
                        @NotNull List<String> ingredients) {
}
