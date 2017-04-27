package dakakeen.dakakeen.Enities;

/**
 * Created by Moath on 21/04/17.
 */

public class Provider {

    private String name;
    private double averageRating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString(){
        return name;
    }
}
