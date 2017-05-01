package dakakeen.dakakeen.CustomerFunctions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.CustomAdapters.CustomOfferAdapter;
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.R;

public class ViewOffersForOrder extends AppCompatActivity implements ResponseHandler {

    private ArrayList<Offer> offers = new ArrayList<>();
    private String orderId;
    private Communication communication;

    private ListView offersList;
    private CustomOfferAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers_for_order);

        orderId= getIntent().getStringExtra("orderId");
        communication = new Communication(getApplicationContext());

        offersList = (ListView) findViewById(R.id.offersForOrderList);
        adapter = new CustomOfferAdapter(getApplicationContext(),offers);
        offersList.setAdapter(adapter);

        try {
            communication.get(communication.getUrl()+"/offers/"+orderId,this);
        }catch (Exception e){

        }

        offersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ViewOfferDetails.class);
                intent.putExtra("offer",offers.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));

            for (int i=0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Offer offer = new Offer();

                offer.setId(jsonObject.getString("_id"));
                offer.getProvider().setName(jsonObject.getString("providerUsername"));
                offer.setPrice(jsonObject.getInt("price"));

                offers.add(offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
