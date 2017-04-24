package dakakeen.dakakeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;

public class ViewOfferDetails extends AppCompatActivity implements ResponseHandler {

    private String offerId;
    private Communication communication;
    private Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_details);

        offerId = getIntent().getStringExtra("offerId");

        communication = new Communication();

        try{
            communication.get(communication.getUrl()+"/offer/"+offerId,this);
        }catch (Exception e){

        }
    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            offer.setId(jsonObject.getString("_id"));
            offer.setproviderUsername(jsonObject.getString("providerUsername"));
            offer.setState(jsonObject.getInt("state"));
            offer.setDescription(jsonObject.getString("description"));
            offer.setRating(jsonObject.getDouble("rating"));
            offer.setReview(jsonObject.getString("review"));
            offer.setPrice(jsonObject.getDouble("price"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){

    }
}
