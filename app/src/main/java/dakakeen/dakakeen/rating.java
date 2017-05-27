package dakakeen.dakakeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import dakakeen.dakakeen.Enities.Offer;

public class rating extends AppCompatActivity {

        private Offer offer;
        private RatingBar ratingBar;

        private TextView noRaiting,reviweWord,review ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        offer= (Offer) getIntent().getSerializableExtra("offer");
        noRaiting=(TextView) findViewById(R.id.noRaitingTextView);

            ratingBar = (RatingBar) findViewById(R.id.providerRatingBar);
            review = (TextView) findViewById(R.id.providerReview);
            reviweWord=(TextView)findViewById(R.id.textView13);
        if (offer.getRating()!=null) {
            ratingBar.setRating(Float.parseFloat(offer.getRating()));
            review.setText(offer.getReview());
            ratingBar.setVisibility(View.VISIBLE);
            review.setVisibility(View.VISIBLE);
            reviweWord.setVisibility(View.VISIBLE);
        }else {
            noRaiting.setVisibility(View.VISIBLE);



        }



    }
}
