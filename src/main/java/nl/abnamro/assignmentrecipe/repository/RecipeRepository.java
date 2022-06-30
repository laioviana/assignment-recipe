package nl.abnamro.assignmentrecipe.repository;

import nl.abnamro.assignmentrecipe.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
/*    @Query("""
            SELECT r FROM Recipe r INNER JOIN r.ingredients ingredients
            WHERE (?1 is null OR r.title LIKE LOWER(CONCAT('%', ?1, '%')))
            AND (?2 IS NULL or r.vegetarian = ?2)
            AND r.servings = ?3
            AND r.instructions LIKE CONCAT('%', ?4, '%') 
            AND ingredients.name LIKE CONCAT('%', ?5, '%')""")
    Page<Recipe> findAllByFilter(String title, Boolean vegetarian, Integer servings, String instructions, String ingredient, Pageable pageable);*/
    @Query("""
                SELECT r FROM Recipe r INNER JOIN r.ingredients ingredients
                WHERE (?1 IS NULL OR r.title LIKE LOWER(CONCAT('%', ?1, '%')))
                AND (?2 IS NULL OR r.vegetarian = ?2)
                AND (?3 IS NULL OR r.servings = ?3)
                AND (?4 IS NULL OR r.instructions LIKE LOWER(CONCAT('%', ?4, '%')))
                AND ((?5 IS NULL OR ?6 = false) OR ingredients.name LIKE LOWER(CONCAT('%', ?5, '%')))
                AND ((?5 IS NULL OR ?6 = true) OR ingredients.name NOT LIKE LOWER(CONCAT('%', ?5, '%')))""")
    Page<Recipe> findAllByFilter(String title, Boolean vegetarian, Integer servings, String instructions, String ingredient, Boolean includeIngredient, Pageable pageable);

}