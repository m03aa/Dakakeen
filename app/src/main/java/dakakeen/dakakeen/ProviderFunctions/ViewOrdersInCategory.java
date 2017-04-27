package dakakeen.dakakeen.ProviderFunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.Communication.ResponseHandler;

public class ViewOrdersInCategory extends AppCompatActivity implements ResponseHandler {

    private int category;
    private Communication communication;
    private ListView ordersList;
    public static ArrayList<Order> orders = new ArrayList<>();
    public static ArrayAdapter<Order> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_in_category);

        category = getIntent().getIntExtra("category",0);
        communication = new Communication();

        //to fill the listView with orders from the selected category
        orders.clear();
        ordersList = (ListView) findViewById(R.id.ordersInCategoryList);
        adapter = new ArrayAdapter<Order>(getApplicationContext(),android.R.layout.simple_list_item_1,
                android.R.id.text1, orders);
        ordersList.setAdapter(adapter);

        //to display the selected order details
        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),CustomerOrderDetails.class);
                intent.putExtra("order",orders.get(position));
                startActivity(intent);
            }
        });

        communication.get(communication.getUrl() + "/orders/"+category,this);

    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order order = new Order();
                order.setId(jsonObject.getString("_id"));
                order.setTitle(jsonObject.getString("title"));
                order.setState(jsonObject.getInt("state"));
                orders.add(order);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody) {
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
