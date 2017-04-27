package dakakeen.dakakeen.MyOrders;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.ViewOffersForOrder;

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

        communication = new Communication();

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        orderDescription = (TextView)findViewById(R.id.orderDescription);
        orderImage = (ImageView)findViewById(R.id.orderImageView);


        //get the passed order from ViewOrdersFragment
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
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            order.setDescription(jsonObject.getString("description"));
            order.setCategory(jsonObject.getInt("Category"));
            orderTitle.setText(order.getTitle());
            orderDescription.setText(order.getDescription());
            }
        } catch (JSONException e) {
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
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(R.string.delete_order)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isDelete =true;
                        communication.delete(communication.getUrl()+"/myorders/"+order.getId(),ViewOrderDetails.this);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing will happen :)
                    }
                })
                .show();
    }


    public void getOffers(View view){
        Intent intent = new Intent(getApplicationContext(), ViewOffersForOrder.class);
        intent.putExtra("orderId",order.getId());
        startActivity(intent);
    }
}
