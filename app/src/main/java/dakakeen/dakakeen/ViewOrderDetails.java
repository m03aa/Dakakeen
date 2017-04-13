package dakakeen.dakakeen;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewOrderDetails extends AppCompatActivity {

    private TextView orderTitle, orderDescription;
    private ImageView orderImage;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);



        orderTitle = (TextView) findViewById(R.id.orderTitle);
        orderDescription = (TextView)findViewById(R.id.orderDescription);
        orderImage = (ImageView)findViewById(R.id.orderImageView);


        //get the passed order from ViewOrdersFragment
        order = (Order)getIntent().getSerializableExtra("order");

        //check if we already have all the order information
        if (order.getId().isEmpty() || order.getUsername().isEmpty() || order.getTitle().isEmpty() || order.getDescription() == null
                || order.getCategory() == 0){

            //get Order information for the server
            final Communication communication = new Communication();

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(getApplicationContext(), communication.getUrl() + "/order/" + order.getId(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
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
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //we will add the delete request here
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing will happen :)
                    }
                })
                .show();*/
    }
}
