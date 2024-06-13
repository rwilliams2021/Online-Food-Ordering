package com.richard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(of = { "id" })
public class Food {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Long price;
    @JsonIgnore
    @ManyToOne
    private Category foodCategory;
    @Column(length = 1000)
    @ElementCollection
    private List<String> images;
    private boolean available;
    @ManyToOne
    private Restaurant restaurant;
    private boolean isVegetarian;
    private boolean isNonVegetarian;
    private boolean isSeasonal;
    @ManyToMany
    private List<IngredientItem> ingredients = new ArrayList<>();
    private LocalDateTime creationDate;
}
