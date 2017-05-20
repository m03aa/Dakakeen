package dakakeen.dakakeen.CustomerFunctions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.R;

public class RateOffer extends AppCompatActivity implements ResponseHandler {

    private String offerId;
    private Communication communication;
    private String review;


    private RatingBar ratingBar;
    private EditText reviewET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_offer);

        offerId = getIntent().getStringExtra("offerId");
        communication = new Communication(getApplicationContext());

        ratingBar = (RatingBar) findViewById(R.id.rating);
        reviewET = (EditText) findViewById(R.id.review);
        ratingBar.setRating(1);


    }

    public void submit(View view){

        review = reviewET.getText().toString();

        if (review.isEmpty() || review.trim().matches(" ")){
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }
        else {
            RequestParams params = new RequestParams();
            params.put("offerId",offerId);
            Log.d("offerId",offerId);
            params.put("review",review);
            Log.d("rating",Float.toString(ratingBar.getRating()));
            if (ratingBar.getRating() < 1)
                params.put("rating",1);
            else
                params.put("rating",ratingBar.getRating());

            try{
                communication.post(communication.getUrl()+"/offers/rate",params,this);
            } catch (Exception e){
                Log.d("Communication Exception", e.getMessage());
            }
        }

    }

    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.submission_success,Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_SHORT).show();
    }
}
