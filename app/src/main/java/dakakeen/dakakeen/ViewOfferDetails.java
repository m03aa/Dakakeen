package dakakeen.dakakeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;

public class ViewOfferDetails extends AppCompatActivity implements ResponseHandler {

    private Communication communication;
    private Offer offer;

    private TextView providerUsername, offerDescription, offerPrice;
    private ImageView offerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_details);

        providerUsername = (TextView) findViewById(R.id.providerUsername);
        offerDescription = (TextView) findViewById(R.id.description);
        offerPrice = (TextView) findViewById(R.id.price);
        offerImage = (ImageView) findViewById(R.id.offerImage);

        offer= (Offer) getIntent().getSerializableExtra("offer");

        communication = new Communication();

        try{
            communication.get(communication.getUrl()+"/offer/"+offer.getId(),this);
        }catch (Exception e){

        }
    }

    public void directToPayment(View view){
        Intent intent = new Intent(getApplicationContext(), AcceptOffer.class);
        intent.putExtra("offer",offer);
        startActivity(intent);
    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {

            JSONObject jsonObject = new JSONObject(new String(responseBody));
            offerDescription.setText(jsonObject.getString("description"));
            offerPrice.setText(Double.toString(offer.getPrice())+" "+Integer.toString(R.string.saudi_riyal));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
