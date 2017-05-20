package dakakeen.dakakeen.Enities;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by moath on 5/20/2017.
 */

public class Delivery implements Serializable {

    private String id;
    private String fromAddress;
    private String toAddress;
    private String expectedDate;
    private String offerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString(){
        return "Delivery ID: \n" + id;
    }
}
