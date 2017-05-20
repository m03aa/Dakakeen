package dakakeen.dakakeen.Deliveries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Delivery;
import dakakeen.dakakeen.R;

public class ViewDeliveryDetails extends AppCompatActivity implements ResponseHandler {

    private Communication communication;
    private Delivery delivery;

    private TextView deliveryID, fromAddress, toAddress, expectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_delivery_details);

        communication = new Communication(getApplicationContext());
        delivery = (Delivery) getIntent().getSerializableExtra("delivery");

        deliveryID = (TextView) findViewById(R.id.deliveryID);
        fromAddress = (TextView) findViewById(R.id.fromAddress);
        toAddress = (TextView) findViewById(R.id.toAddress);
        expectedDate = (TextView) findViewById(R.id.expectedDate);

        deliveryID.setText(delivery.getId());
        fromAddress.setText(delivery.getFromAddress());
        toAddress.setText(delivery.getToAddress());
        expectedDate.setText(delivery.getExpectedDate());

    }

    public void submitDelivery(View view){

        try {
            communication.put(communication.getUrl()+"/delivery/"+delivery.getOfferId(),null,this);
        }catch (Exception e){
            Log.d("Communication Exception", e.getMessage());
        }
    }

    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.submission_success,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){
        Log.d("onFailure", new String(responseBody));
    }

}
