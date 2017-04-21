package dakakeen.dakakeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Provider;

public class TopProviders extends AppCompatActivity implements ResponseHandler {

    private int category;
    private ListView topProviders;
    private ArrayList<Provider> providers = new ArrayList<>();
    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_providers);

        category= getIntent().getIntExtra("category",0);

        topProviders = (ListView) findViewById(R.id.TopProvidersList);

        communication = new Communication();
        try {
            communication.get(communication.getUrl() + "/top/" + category,this);
        }
        catch (Exception e){

        }

        ArrayAdapter<Provider> adapter = new ArrayAdapter<Provider>(getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1,providers);

        topProviders.setAdapter(adapter);

    }


    @Override
    public void onSuccess(byte[] responseBody){
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Provider provider = new Provider();

                provider.setName(jsonObject.getString("_id"));
                provider.setAverageRating(jsonObject.getDouble("total"));

                providers.add(provider);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
