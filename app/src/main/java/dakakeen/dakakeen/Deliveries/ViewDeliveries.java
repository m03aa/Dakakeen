package dakakeen.dakakeen.Deliveries;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Delivery;
import dakakeen.dakakeen.R;

public class ViewDeliveries extends AppCompatActivity implements ResponseHandler {

    private Communication communication;


    private ListView deliveriesList;
    private ArrayList<Delivery> deliveries = new ArrayList<>();
    private ArrayAdapter<Delivery> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deliveries);

        communication = new Communication(getApplicationContext());

        deliveriesList = (ListView) findViewById(R.id.deliveriesListView);
        arrayAdapter = new ArrayAdapter<Delivery>(getApplicationContext(),android.R.layout.simple_list_item_1,
                android.R.id.text1,deliveries);
        deliveriesList.setAdapter(arrayAdapter);

        deliveriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ViewDeliveryDetails.class);
                intent.putExtra("delivery",deliveries.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        updateList();

    }

    @Override
    public void onSuccess(byte[] responseBody){

        try {
            Log.d("onSuccess", new String(responseBody));
            JSONArray jsonArray= new JSONArray(new String(responseBody));

            for (int i=0; i<jsonArray.length(); i++){
                Log.d("onSuccess","iteration "+i);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Delivery delivery = new Delivery();

                delivery.setId(jsonObject.getString("_id"));
                delivery.setFromAddress(jsonObject.getString("fromAddress"));
                delivery.setToAddress(jsonObject.getString("toAddress"));
                delivery.setExpectedDate(jsonObject.getString("expectedDate"));
                delivery.setOfferId(jsonObject.getJSONObject("offerId").getString("_id"));

                deliveries.add(delivery);
            }
            arrayAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d("onSuccess Exception", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){
        Log.d("onFailure", new String(responseBody));
    }

    public void updateList(){
        deliveries.clear();
        try {
            communication.get(communication.getUrl()+"/delivery",this);
        }catch (Exception e){
            Log.d("Communication Exception", e.getMessage());
        }
    }


}
