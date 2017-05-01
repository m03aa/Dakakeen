package dakakeen.dakakeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;

public class MakePayment extends AppCompatActivity implements ResponseHandler {

    private Offer offer;

    private TextView price;
    private EditText holderName, cardNumber, cvcNumber, emonth, eyear;

    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        communication = new Communication(getApplicationContext());

        offer = (Offer) getIntent().getSerializableExtra("offer");

        price = (TextView) findViewById(R.id.price);
        price.setText(Double.toString(offer.getPrice())+" "+Integer.toString(R.string.saudi_riyal));

        holderName = (EditText) findViewById(R.id.holderName);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        cvcNumber = (EditText) findViewById(R.id.cvcNumber);
        emonth = (EditText) findViewById(R.id.emonth);
        eyear = (EditText) findViewById(R.id.eyear);



    }

    public void makePayment(View view){

        if (checkPaymentInfo()){
            RequestParams params = new RequestParams();
            params.put("offerId", offer.getId());
            params.put("emonth", offer.payment.getEmonth());
            params.put("eyear", offer.payment.getEyear());
            params.put("cvc", offer.payment.getCvc());
            params.put("number", offer.payment.getNumber());

            if (offer.payment.getHolderName() != null)
                params.put("name", offer.payment.getHolderName());

            try {
                communication.post(communication.getUrl()+"/offers/accept", params, this);
            } catch (Exception e){

            }
        }

    }

    public boolean checkPaymentInfo(){

        if(!cardNumber.getText().toString().isEmpty() && !cvcNumber.getText().toString().isEmpty()
                && !eyear.getText().toString().isEmpty() && !emonth.getText().toString().isEmpty()){

            offer.payment.setNumber(cardNumber.getText().toString());
            offer.payment.setCvc(cvcNumber.getText().toString());
            offer.payment.setEyear(eyear.getText().toString());
            offer.payment.setEmonth(emonth.getText().toString());

            if(!holderName.getText().toString().isEmpty())
                offer.payment.setHolderName(holderName.getText().toString());

            return true;

        } else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.payment_success,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){

        Toast.makeText(getApplicationContext(), communication.handelError(responseBody) ,Toast.LENGTH_SHORT).show();
    }

}
