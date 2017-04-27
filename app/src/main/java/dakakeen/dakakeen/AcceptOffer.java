package dakakeen.dakakeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;

public class AcceptOffer extends AppCompatActivity implements ResponseHandler {

    private Offer offer;
    private Communication communication;

    private TextView price;
    private EditText holderName, cardNumber, cvcNumber, emonth, eyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_offer);

        offer = (Offer) getIntent().getSerializableExtra("offer");

        price = (TextView) findViewById(R.id.price);
        price.setText(Double.toString(offer.getPrice())+" "+Integer.toString(R.string.saudi_riyal));

        holderName = (EditText) findViewById(R.id.holderName);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        cvcNumber = (EditText) findViewById(R.id.cvcNumber);
        emonth = (EditText) findViewById(R.id.emonth);
        eyear = (EditText) findViewById(R.id.eyear);

        communication = new Communication();


    }

    public void makePayment(View view){
        if (checkPaymentInfo()){

            //communication.post();

        }

    }

    public boolean checkPaymentInfo(){

        if(!holderName.getText().toString().isEmpty() && !cardNumber.getText().toString().isEmpty() &&
                !cvcNumber.getText().toString().isEmpty() && !eyear.getText().toString().isEmpty() &&
                !emonth.getText().toString().isEmpty() && !holderName.getText().toString().trim().matches("")){

            if (Integer.parseInt(cvcNumber.getText().toString()) < 1){
                Toast.makeText(getApplicationContext(),R.string.expire_date_invalid,Toast.LENGTH_SHORT).show();
                return false;

            }
            else
                if ((Integer.parseInt(eyear.getText().toString()) < 2017 && Integer.parseInt(eyear.getText().toString()) > 2050)
                    || (Integer.parseInt(emonth.getText().toString()) < 1 && Integer.parseInt(emonth.getText().toString()) > 12)){
                    Toast.makeText(getApplicationContext(),R.string.cvc_invalid,Toast.LENGTH_SHORT).show();
                    return false;

                }
            else
                if (Integer.parseInt(cardNumber.getText().toString()) < 1){
                    Toast.makeText(getApplicationContext(),R.string.card_number_invalid,Toast.LENGTH_SHORT).show();
                    return false;
                }

            else
                return true;

        } else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    @Override
    public void onSuccess(byte[] responseBody){

    }

    @Override
    public void onFailure(byte[] responseBody){

    }

}
