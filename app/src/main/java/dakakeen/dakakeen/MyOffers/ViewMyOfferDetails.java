package dakakeen.dakakeen.MyOffers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;
import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.CustomerFunctions.MakePayment;
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.R;

public class ViewMyOfferDetails extends AppCompatActivity implements ResponseHandler {

    private Communication communication;
    private Offer offer;
    private boolean isDelete = false, isDelivery = false;
    private int state;

    private TextView providerUsername, offerDescription, offerPrice;
    private ImageView offerImage;
    private Button acceptOfferButton, submitDelivaryButton;
    private LinearLayout activeOfferLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_details);

        providerUsername = (TextView) findViewById(R.id.providerUsername);
        offerDescription = (TextView) findViewById(R.id.description);
        offerPrice = (TextView) findViewById(R.id.price);
        offerImage = (ImageView) findViewById(R.id.offerImage);
        acceptOfferButton = (Button) findViewById(R.id.acceptOfferButton);
        activeOfferLayout = (LinearLayout) findViewById(R.id.ActiveOfferLayout);
        submitDelivaryButton = (Button) findViewById(R.id.submitForDeliveryButton);

        offer = (Offer) getIntent().getSerializableExtra("offer");
        state = getIntent().getIntExtra("state",0);


        acceptOfferButton.setVisibility(View.INVISIBLE);
        switch (state){
            case 0:
                activeOfferLayout.setVisibility(View.VISIBLE);
                break;
            case 2:
                submitDelivaryButton.setVisibility(View.VISIBLE);
                break;
        }

        communication = new Communication(getApplicationContext());

        try{
            communication.get(communication.getUrl()+"/offer/"+offer.getId(),this);
        }catch (Exception e){

        }
    }

    public void directToEditOffer(View view){
        Intent intent = new Intent(getApplicationContext(),EditOffer.class);
        intent.putExtra("offer",offer);
        startActivity(intent);
    }

    public void deleteOffer(View view){
        isDelete = true;
        communication.delete(communication.getUrl() + "/myoffers/" + offer.getId(), this);
    }

    public void submitDelivery(View view){
        isDelivery = true;
        communication.put(communication.getUrl()+"/myoffers/submit/"+offer.getId(),null,this);
    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {
            if (isDelete){
                Toast.makeText(getApplicationContext(), R.string.offer_deleted, Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(isDelivery){
                Toast.makeText(getApplicationContext(), R.string.offer_submitted_delivery, Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                final JSONObject jsonObject = new JSONObject(new String(responseBody));
                offer.provider.setName(jsonObject.getString("providerUsername"));
                providerUsername.setText(offer.provider.getName());
                offer.setDescription(jsonObject.getString("description"));
                offerDescription.setText(offer.getDescription());
                offerPrice.setText(Double.toString(offer.getPrice())+" "+ getApplicationContext().getString(R.string.saudi_riyal));

                //to download the image from the server and set it in the image view
                if (jsonObject.getString("picture") != null){
                    AsyncHttpClient client = new AsyncHttpClient();

                    client.get(jsonObject.getString("picture"), new FileAsyncHttpResponseHandler(getApplicationContext()) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            Log.d("image", Integer.toString(statusCode));

                            try {
                                Picasso.with(getApplicationContext())
                                        .load(jsonObject.getString("picture"))
                                        .fit()
                                        .into(offerImage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){
        Log.d("onFailure", new String(responseBody));
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
