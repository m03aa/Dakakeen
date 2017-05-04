package dakakeen.dakakeen.MyOffers;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;

public class EditOffer extends AppCompatActivity implements ResponseHandler{

    private EditText offerDescription, offerPrice;
    private TextView orderTitle;
    private LinearLayout imageLayout;

    private Communication communication;
    private Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        communication = new Communication(getApplicationContext());

        offer = (Offer) getIntent().getSerializableExtra("offer");

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        offerDescription = (EditText)findViewById(R.id.offerDescriptionEditText);
        offerPrice = (EditText)findViewById(R.id.priceEditText);

        imageLayout = (LinearLayout) findViewById(R.id.ImageLinearLayout);
        imageLayout.setVisibility(View.INVISIBLE);

        orderTitle.setText(offer.order.getTitle());
        offerDescription.setText(offer.getDescription());
        offerPrice.setText(Double.toString(offer.getPrice()));
    }


    public void submitOffer(View view){
        if (checkOfferInfo()){

            RequestParams params = new RequestParams();
            params.put("offerId", offer.getId());
            params.put("price",offer.getPrice());
            params.put("description", offer.getDescription());

            try {
                communication.put(communication.getUrl()+"/myoffers", params, this);
            } catch (Exception e){
                Log.e("Communication Exception", e.getMessage());
            }
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkOfferInfo(){
        offer.setDescription(offerDescription.getText().toString());
        offer.setPrice(Double.parseDouble(offerPrice.getText().toString()));

        if (!offer.getDescription().isEmpty() && offer.getPrice() != 0){

            if (!offer.getDescription().trim().matches(""))
                return true;
            else return false;
        }
        else
            return true;
    }


    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.offer_updated,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }

}
