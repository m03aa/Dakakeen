package dakakeen.dakakeen.MyOrders;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import cz.msebera.android.httpclient.Header;
import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.CustomerFunctions.ViewOffersForOrder;

public class ViewOrderDetails extends AppCompatActivity implements ResponseHandler {

    private TextView orderTitle, orderDescription;
    private ImageView orderImage;
    private Order order;
    private Communication communication;
    private boolean isDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);

        communication = new Communication(getApplicationContext());

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        orderDescription = (TextView)findViewById(R.id.orderDescription);
        orderImage = (ImageView)findViewById(R.id.orderImageView);


        //get the passed order from ViewMyOrders
        order = (Order)getIntent().getSerializableExtra("order");

        //check if we already have all the order information
        if (order.getId().isEmpty() || order.getUsername().isEmpty() || order.getTitle().isEmpty() || order.getDescription() == null
                || order.getCategory() == 0){

            //get Order information for the server
            try {
                communication.get(communication.getUrl() + "/order/" + order.getId(), this);
            }catch (Exception e){

            }

        }
    }

    @Override
    public void onSuccess(byte[] responseBody) {
        try {
            if(isDelete){
                Toast.makeText(getApplicationContext(),R.string.order_deleted,Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
            final JSONObject jsonObject = new JSONObject(new String(responseBody));
                order.setDescription(jsonObject.getString("description"));
                order.setCategory(jsonObject.getInt("Category"));
                orderTitle.setText(order.getTitle());
                orderDescription.setText(order.getDescription());

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
                                        .into(orderImage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        } catch (JSONException e) {
            Log.d("catch", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody) {
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }



    public void directToEditOrder(View view){
        Intent intent = new Intent(getApplicationContext(),EditOrder.class);
        intent.putExtra("order",order);

        startActivity(intent);
        finish();
    }



    public void deleteOrder(View view){
        /*new AlertDialog.Builder(getApplicationContext())
                .setMessage(R.string.delete_order)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {*/
                        isDelete =true;
                        communication.delete(communication.getUrl()+"/myorders/"+order.getId(),ViewOrderDetails.this);
                        finish();
                    /*}
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing will happen :)
                    }
                })
                .show();*/
    }


    public void getOffers(View view){
        Intent intent = new Intent(getApplicationContext(), ViewOffersForOrder.class);
        intent.putExtra("order",order);
        startActivity(intent);
    }
}
