// No arquivo Restaurant.java
package services;

// 1. Importando todas as entidades que o Restaurante precisa gerenciar.
import entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant {

    // --- ATRIBUTOS (As "Gavetas" do Restaurante) ---
    private final String name;
    private final Stock stock;
    private final List<Dish> menu;
    private final List<Order> orderHistory;
    private final List<Evaluation> evaluations;

    // --- CONSTRUTOR (A "Inauguração" do Restaurante) ---
    public Restaurant(String name, Stock stock) {
        this.name = name;
        this.stock = stock;
        // As listas começam vazias, prontas para serem preenchidas.
        this.menu = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
        this.evaluations = new ArrayList<>();
    }

    // --- MÉTODOS DE AÇÃO (O "Dia a Dia") ---

    public void addDishToMenu(Dish dish) {
        this.menu.add(dish);
    }

    public void addEvaluation(Evaluation eval) {
        this.evaluations.add(eval);
    }

    // O MÉTODO MAIS IMPORTANTE DE AÇÃO!
    public void placeOrder(Order order) {
        System.out.println("\n--- Processando novo pedido ---");

        // 1. VERIFICAR se temos estoque para TODOS os pratos do pedido.
        for (Dish dish : order.getOrderedDishes()) {
            for (Map.Entry<Ingredient, Double> recipeItem : dish.getRecipe().entrySet()) {
                Ingredient ingredient = recipeItem.getKey();
                Double quantityNeeded = recipeItem.getValue();

                if (!stock.hasEnough(ingredient, quantityNeeded)) {
                    System.out.println("PEDIDO FALHOU: Estoque insuficiente de " + ingredient.getName() + " para fazer " + dish.getName());
                    return; // Interrompe o método. O pedido não pode ser feito.
                }
            }
        }

        // 2. Se chegamos aqui, é porque temos estoque. VAMOS DAR BAIXA.
        for (Dish dish : order.getOrderedDishes()) {
            for (Map.Entry<Ingredient, Double> recipeItem : dish.getRecipe().entrySet()) {
                Ingredient ingredient = recipeItem.getKey();
                Double quantityToUse = recipeItem.getValue();
                stock.use(ingredient, quantityToUse);
            }
        }

        // 3. Adicionar o pedido ao histórico.
        this.orderHistory.add(order);
        System.out.println("Pedido finalizado com sucesso! Total: R$" + String.format("%.2f", order.calculateTotal()));
        stock.printStatus(); // Mostra como o estoque ficou.
    }

    // --- MÉTODOS DE TOMADA DE DECISÃO (As "Perguntas do Gerente") ---

    // Pergunta 1: Gasto médio por pedido
    public double getAverageSpendingPerOrder() {
        if (orderHistory.isEmpty()) {
            return 0.0;
        }
        double totalRevenue = 0;
        for (Order order : orderHistory) {
            totalRevenue += order.calculateTotal();
        }
        return totalRevenue / orderHistory.size();
    }

    // Pergunta 2: Prato mais pedido
    public Dish getMostPopularDish() {
        if (orderHistory.isEmpty()) {
            return null;
        }

        Map<Dish, Integer> dishCount = new HashMap<>();
        for (Order order : orderHistory) {
            for (Dish dish : order.getOrderedDishes()) {
                dishCount.merge(dish, 1, Integer::sum);
            }
        }

        Dish mostPopular = null;
        int maxCount = 0;
        for (Map.Entry<Dish, Integer> entry : dishCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopular = entry.getKey();
            }
        }
        return mostPopular;
    }

    // Adicione os outros 4 métodos de perguntas de negócio aqui...

    // --- GETTERS (Para o Main poder ler informações) ---
    public String getName() {
        return name;
    }
    public List<Dish> getMenu() {
        return menu;
    }
}