package entities;

import java.util.Map;
import java.util.HashMap;

public class Dish {
    private final String id;
    private String name;
    private double price;
    private final Map<Ingredient, Double> recipe;

    public Dish(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.recipe = new HashMap<>();
    }

    // Método para adicionar um passo na receita
    public void addIngredientToRecipe(Ingredient ingredient, double quantity) {
        this.recipe.put(ingredient, quantity);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Map<Ingredient, Double> getRecipe() { return this.recipe; }
}