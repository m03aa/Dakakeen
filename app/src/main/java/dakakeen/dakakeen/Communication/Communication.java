package dakakeen.dakakeen.Communication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by moath on 4/12/2017.
 */

public class Communication {

    private final String url = "https://dakaken.cfapps.io";
    private AsyncHttpClient client;

    public Communication(){
        client = new AsyncHttpClient();
    }

    public void get(String url, final ResponseHandler handler){

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.onSuccess(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.onFailure(responseBody);
            }
        });
    }

    public void post(String url, RequestParams params, final ResponseHandler handler){

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.onSuccess(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.onFailure(responseBody);
            }
        });
    }

    public void put(String url, RequestParams params, final ResponseHandler handler){
        client.put(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.onSuccess(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.onFailure(responseBody);
            }
        });
    }

    public void delete(String url, final ResponseHandler handler){
        client.delete(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.onSuccess(responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.onFailure(responseBody);
            }
        });
    }

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
