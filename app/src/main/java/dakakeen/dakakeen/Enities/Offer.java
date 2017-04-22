package dakakeen.dakakeen.Enities;

/**
 * Created by Moath on 22/04/17.
 */

public class Offer {

    private String id,description, orderId, providerUsername;
    private double price,rating;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getproviderUsername() {
        return providerUsername;
    }

    public void setproviderUsername(String providerUsername) {
        this.providerUsername = providerUsername;
    }

    @Override
    public String toString(){
        return providerUsername;
    }
}
