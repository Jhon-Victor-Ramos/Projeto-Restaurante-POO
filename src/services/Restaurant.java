package services;

import entities.*;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

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

    public void placeOrder(Order order) {
        if (order.getOrderedDishes().isEmpty()) {
            System.out.println("Pedido vazio. Nenhum item foi adicionado.");
            return;
        }

        System.out.println("\n--- Processando novo pedido ---");

        // 1. CALCULAR A DEMANDA TOTAL DE INGREDIENTES PARA O PEDIDO INTEIRO
        Map<Ingredient, Double> requiredIngredients = new HashMap<>();
        for (Dish dish : order.getOrderedDishes()) {
            for (Map.Entry<Ingredient, Double> recipeItem : dish.getRecipe().entrySet()) {
                requiredIngredients.merge(recipeItem.getKey(), recipeItem.getValue(), Double::sum);
            }
        }

        // 2. VERIFICAR se temos estoque para a DEMANDA TOTAL.
        for (Map.Entry<Ingredient, Double> required : requiredIngredients.entrySet()) {
            Ingredient ingredient = required.getKey();
            Double quantityNeeded = required.getValue();

            if (!stock.hasEnough(ingredient, quantityNeeded)) {
                System.out.println("PEDIDO FALHOU: Estoque insuficiente de " + ingredient.getName() + ".");
                System.out.printf("Necessário: %.2f %s | Disponível: consulte o status do estoque.\n", quantityNeeded, ingredient.getUnit());
                return; // Interrompe o método. O pedido não pode ser feito.
            }
        }

        // 3. Se chegamos aqui, é porque temos estoque. VAMOS DAR BAIXA.
        for (Map.Entry<Ingredient, Double> required : requiredIngredients.entrySet()) {
            stock.use(required.getKey(), required.getValue());
        }

        // 4. Adicionar o pedido ao histórico.
        this.orderHistory.add(order);
        System.out.println("Pedido finalizado com sucesso! Total: R$" + String.format("%.2f", order.calculateTotal()));
        stock.printStatus(); // Mostra como o estoque ficou.
    }

    // As Perguntas do Gerente

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

    // Pergunta 3: Quantitativo, em média, de pessoas (pedidos) por dia
    public double getAverageOrdersPerDay() {
        if (orderHistory.isEmpty()) {
            return 0.0;
        }

        Set<LocalDate> uniqueDays = new HashSet<>();

        // 2. Percorremos cada pedido no histórico.
        for (Order order : orderHistory) {
            uniqueDays.add(order.getOrderTimestamp().toLocalDate());
        }

        return (double) orderHistory.size() / uniqueDays.size();
    }

    // Pergunta 4: Avaliação média do restaurante
    public double getAverageRating() {
        // Se a lista de avaliações estiver vazia, não há como calcular a média.
        if (evaluations.isEmpty()) {
            return 0.0; // Retornamos 0 como um valor padrão.
        }

        double totalRating = 0;
        // Percorremos cada avaliação na lista de avaliações.
        for (Evaluation eval : evaluations) {
            totalRating += eval.getRating(); // Somamos a nota de cada avaliação ao total.
        }

        // A média é a soma total das notas dividida pelo número de avaliações.
        return totalRating / evaluations.size();
    }

    // Pergunta 5: Ingredientes mais consumidos
    public Map<Ingredient, Double> getIngredientConsumptionReport() {
        Map<Ingredient, Double> consumptionMap = new HashMap<>();

        for (Order order : orderHistory) {
            for (Dish dish : order.getOrderedDishes()) {
                for (Map.Entry<Ingredient, Double> recipeItem : dish.getRecipe().entrySet()) {
                    Ingredient ingredient = recipeItem.getKey();
                    Double quantityUsed = recipeItem.getValue();

                    consumptionMap.merge(ingredient, quantityUsed, Double::sum);
                }
            }
        }

        return consumptionMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Ingredient, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    // Pergunta 6: Prato que gera maior faturamento
    public Dish getTopGrossingDish() {
        if (orderHistory.isEmpty()) {
            return null;
        }

        Map<Dish, Double> revenuePerDish = new HashMap<>();

        for (Order order : orderHistory) {
            for (Dish dish : order.getOrderedDishes()) {
                revenuePerDish.merge(dish, dish.getPrice(), Double::sum);
            }
        }

        return revenuePerDish.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey) // Extraímos o prato (a chave) dessa entrada.
                .orElse(null); // Se o mapa estiver vazio por algum motivo, retorna null.
    }

    // --- GETTERS (Para o Main poder ler informações) ---
    public String getName() {
        return name;
    }
    public List<Dish> getMenu() {
        return menu;
    }
}