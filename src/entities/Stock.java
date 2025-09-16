package entities;

import java.util.Map;
import java.util.HashMap;

public class Stock {
    private final Map<Ingredient, Double> quantities;

    public Stock() {
        this.quantities = new HashMap<>();
    }

    // Para abastecer o estoque
    public void add(Ingredient ingredient, double quantity) {
        this.quantities.merge(ingredient, quantity, Double::sum);
    }

    // Para usar um item do estoque
    public void use(Ingredient ingredient, double quantity) {
        this.add(ingredient, -quantity); // Usar é apenas adicionar uma quantidade negativa.
    }

    // Para verificar se temos o suficiente
    public boolean hasEnough(Ingredient ingredient, double quantityNeeded) {
        return this.quantities.getOrDefault(ingredient, 0.0) >= quantityNeeded;
    }

    // Método para imprimir o status (muito útil para testar!)
    public void printStatus() {
        System.out.println("--- ESTOQUE ---");
        this.quantities.forEach((ingredient, quantity) -> {
            System.out.printf("- %s: %.2f %s\n", ingredient.getName(), quantity, ingredient.getUnit());
        });
        System.out.println("---------------");
    }
}