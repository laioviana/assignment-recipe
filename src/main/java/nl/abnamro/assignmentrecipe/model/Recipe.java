package nl.abnamro.assignmentrecipe.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "servings", nullable = false)
    private Integer servings;

    @NotNull
    @Column(name = "vegetarian", nullable = false)
    private Boolean vegetarian = false;

    @NotNull
    @Column(name = "instructions", nullable = false)
    private String instructions;

    @NotNull
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe")
    private List<Ingredient> ingredients;

    @Column(name = "created_at")
    private Instant createdAt;
}

