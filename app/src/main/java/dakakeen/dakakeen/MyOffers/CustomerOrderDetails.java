package dakakeen.dakakeen.MyOffers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.MyOrders.EditOrder;
import dakakeen.dakakeen.R;

public class CustomerOrderDetails extends AppCompatActivity implements ResponseHandler {

    private ImageView orderImage;
    private TextView orderTitle, orderDescription;
    private Order order;
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_details);

        order = (Order) getIntent().getSerializableExtra("order");

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        orderDescription = (TextView) findViewById(R.id.orderDescription);
        orderImage = (ImageView) findViewById(R.id.orderImage);

        communication = new Communication();

        //check if we already have all the order information
        if (order.getId().isEmpty() || order.getUsername().isEmpty() || order.getTitle().isEmpty() || order.getDescription() == null
                || order.getCategory() == 0) {

            //get Order information for the server
            //communication.get(communication.getUrl() + "/order/" + order.getId(), this);
        }

    }



    public void CreateOffer(View view){
        Intent intent = new Intent(getApplicationContext(),CreateOffer.class);
        intent.putExtra("order",order);
        startActivity(intent);
        finish();
    }



    @Override
    public void onSuccess(byte[] responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            order.setDescription(jsonObject.getString("description"));
            order.setCategory(jsonObject.getInt("Category"));
            orderTitle.setText(order.getTitle());
            orderDescription.setText(order.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody) {
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
