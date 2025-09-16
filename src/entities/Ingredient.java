package entities;

public class Ingredient {
    private final String id;
    private final String name;
    private final String unit; // Ex: "gramas", "unidades"

    public Ingredient(String id, String name, String unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getUnit() { return unit; }

    @Override
    public String toString() {
        return this.name;
    }
}