package dakakeen.dakakeen.CustomerFunctions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.CustomAdapters.CustomProviderAdapter;
import dakakeen.dakakeen.Enities.Provider;
import dakakeen.dakakeen.R;

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

        communication = new Communication(getApplicationContext());
        try {
            communication.get(communication.getUrl() + "/top/" + category,this);
        }
        catch (Exception e){

        }

        CustomProviderAdapter adapter = new CustomProviderAdapter(getApplicationContext(), providers);

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
