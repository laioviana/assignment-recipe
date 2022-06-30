package nl.abnamro.assignmentrecipe.model.dto;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public record RecipeDto(@NotNull String title,
                        @NotNull Integer servings,
                        @NotNull Boolean vegetarian,
                        @NotNull String instructions,
                        @NotNull List<String> ingredients,
                       Instant createdAt) {
}
