package dakakeen.dakakeen.MyOrders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.CustomerFunctions.RequestRefundAndReportDefect;
import dakakeen.dakakeen.CustomerFunctions.RateOffer;

public class ViewClosedOrderDetails extends AppCompatActivity implements ResponseHandler {

    private Order order;
    private Communication communication;
    private boolean orderDetails = true;
    private ImageView imageView;

    private TextView orderTitle, orderDescription, offerState, offerPrice, offerDescription;
    private LinearLayout deliveredOrderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_closed_order_details);

        order = (Order) getIntent().getSerializableExtra("order");
        communication = new Communication(getApplicationContext());

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        orderDescription = (TextView) findViewById(R.id.orderDescription);
        offerState = (TextView) findViewById(R.id.offerState);
        offerPrice = (TextView) findViewById(R.id.offerPrice);
        offerDescription = (TextView) findViewById(R.id.offerDescription);
        deliveredOrderLayout = (LinearLayout) findViewById(R.id.deliveredOrderLayout);
        imageView=(ImageView)findViewById(R.id.closedOrderDetails);

        if (order.getOffer().getState() != 3)
            deliveredOrderLayout.setVisibility(View.INVISIBLE);

        try{
            communication.get(communication.getUrl() + "/order/" + order.getId(), this);
        } catch (Exception e){
            Log.d("Communication Exception", e.getMessage());
        }

    }

    @Override
    public void onSuccess(byte[] responseBody){

         //JSONObject jsonObject = null;
        try {
            // herrrrrrrrreeeeee i made changes
            final JSONObject jsonObject = new JSONObject(new String(responseBody));
            if (orderDetails){
                //jsonObject = new JSONObject(new String(responseBody));
                order.setDescription(jsonObject.getString("description"));
                order.setCategory(jsonObject.getInt("Category"));

                orderTitle.setText(order.getTitle());
                orderDescription.setText(order.getDescription());

                orderDetails = false;

                communication.get(communication.getUrl()+"/offer/"+order.getOffer().getId(),this);
            }
            else {
                //jsonObject = new JSONObject(new String(responseBody));
                order.getOffer().provider.setName(jsonObject.getString("providerUsername"));
                order.getOffer().setDescription(jsonObject.getString("description"));
                offerDescription.setText(order.getOffer().getDescription());
                order.getOffer().setPrice(jsonObject.getInt("price"));
                offerPrice.setText(Double.toString(order.getOffer().getPrice())+" "+ getApplicationContext().getString(R.string.saudi_riyal));
                order.getOffer().setRating(jsonObject.getString("rating"));
                // heeeeeeeeeeeeeeeerrrrrrrrrrrrrreeeeeeeeee i made changes
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
                                        .into(imageView);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                switch (order.getOffer().getState()){
                    case 2:
                        offerState.setText(R.string.accepted);
                        break;
                    case 3:
                        offerState.setText(R.string.delivered);
                        break;
                    case 4:
                        offerState.setText(R.string.onRoute);
                        break;
                }
            }

        } catch (JSONException e) {
            Log.d("OnSuccess Exception", e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(byte[] responseBody){
        Log.d("OnFailure Exception", new String(responseBody));
    }

    public void DirectToReportDefect(View view){
        Intent intent = new Intent(getApplicationContext(), RequestRefundAndReportDefect.class);
        intent.putExtra("type", 0);
        intent.putExtra("offerId", order.getOffer().getId());
        startActivity(intent);
        finish();
    }

    public void DirectToRequestRefund (View view){
        Intent intent = new Intent(getApplicationContext(), RequestRefundAndReportDefect.class);
        intent.putExtra("type", 1);
        intent.putExtra("offerId", order.getOffer().getId());
        startActivity(intent);
        finish();
    }

    public void DirectToRateOffer(View view){
        if (order.getOffer().getRating() == null){
            Intent intent = new Intent(getApplicationContext(), RateOffer.class);
            intent.putExtra("offerId", order.getOffer().getId());
            startActivity(intent);
            finish();
        }
        else
            Toast.makeText(getApplicationContext(),R.string.already_rate,Toast.LENGTH_SHORT).show();
    }
}
