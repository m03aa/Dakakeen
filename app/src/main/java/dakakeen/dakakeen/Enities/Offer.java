package dakakeen.dakakeen.Enities;

import java.io.Serializable;

/**
 * Created by Moath on 22/04/17.
 */

public class Offer implements Serializable {

    private String id,description, review, rating;
    private double price;
    private int state;
    public Order order;
    public Provider provider;
    public Payment payment;

    public Offer(){
        order = new Order();
        provider = new Provider();
        payment = new Payment();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public String toString(){
        return order.getTitle();
    }
}
