package dakakeen.dakakeen.CustomerFunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.R;

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

        offer = (Offer) getIntent().getSerializableExtra("offer");

        communication = new Communication(getApplicationContext());

        try{
            communication.get(communication.getUrl()+"/offer/"+offer.getId(),this);
        }catch (Exception e){

        }
    }

    public void directToPayment(View view){
        Intent intent = new Intent(getApplicationContext(), MakePayment.class);
        intent.putExtra("offer",offer);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {

            final JSONObject jsonObject = new JSONObject(new String(responseBody));
            offer.provider.setName(jsonObject.getString("providerUsername"));
            providerUsername.setText(offer.provider.getName());
            offer.setDescription(jsonObject.getString("description"));
            offerDescription.setText(offer.getDescription());
            offer.setPrice(jsonObject.getInt("price"));
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
