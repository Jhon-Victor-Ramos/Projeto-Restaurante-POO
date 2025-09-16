package entities;

public class Evaluation {
    private final Client client;
    private final int rating;
    private final String comment;

    public Evaluation(Client client, int rating, String comment) {
        this.client = client;
        this.rating = rating;
        this.comment = comment;
    }

    public Client getClient() { return client; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}