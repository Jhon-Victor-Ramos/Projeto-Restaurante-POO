package entities;

public class Client {
    private final String id;
    private final String name;

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return this.name;
    }
}