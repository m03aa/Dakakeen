package dakakeen.dakakeen;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moath on 4/12/2017.
 */

public class Communication {

    private final String url = "https://dakaken.cfapps.io";

    public String getUrl() {
        return url;
    }

    public String handelError(byte[] responseBody){
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            return jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
