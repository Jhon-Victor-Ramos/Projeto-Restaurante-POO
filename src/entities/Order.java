package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final Client client;
    private final List<Dish> orderedDishes;
    private final LocalDateTime orderTimestamp;

    public Order(Client client) {
        this.client = client;
        this.orderedDishes = new ArrayList<>();
        this.orderTimestamp = LocalDateTime.now();
    }

    public void addDish(Dish dish) {
        this.orderedDishes.add(dish);
    }

    public double calculateTotal() {
        double total = 0;
        for (Dish dish : this.orderedDishes) {
            total += dish.getPrice();
        }
        return total;
    }

    public List<Dish> getOrderedDishes() {
        return this.orderedDishes;
    }

    public Client getClient() {
        return client;
    }
    public LocalDateTime getOrderTimestamp() {
        return orderTimestamp;
    }
}