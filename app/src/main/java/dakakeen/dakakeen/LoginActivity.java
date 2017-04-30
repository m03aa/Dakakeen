package dakakeen.dakakeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Account;

public class LoginActivity extends AppCompatActivity implements ResponseHandler{

    private EditText username, password;
    private Account account = new Account();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
    }

    public void checkLogin(View view){

        account.setUsername(username.getText().toString());
        account.setPassword(password.getText().toString());

        if(account.getUsername().isEmpty() || account.getPassword().isEmpty() ){
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }
        else if(account.getUsername().contains(" ") || account.getPassword().contains(" ")){
            Toast.makeText(getApplicationContext(),R.string.username_password_contain_space,Toast.LENGTH_LONG).show();
        }
        else{

            RequestParams params = new RequestParams();
            params.put("username", account.getUsername());
            params.put("password", account.getPassword());

            try {
                Communication.post(Communication.getUrl()+"/auth/login", params, this);
            } catch (Exception e){
                Log.i("Exception", e.getMessage());
            }
        }
    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));

            account.setRole(jsonObject.getInt("role"));
            account.setEmail(jsonObject.getString("email"));
            account.setName(jsonObject.getString("name"));
            account.setAddress(jsonObject.getString("address"));
            account.setPhone(jsonObject.getString("phone"));
            //check omar - no nationalId !!
            account.setBanned(jsonObject.getBoolean("banned"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(),R.string.Login_successful,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),TabsActivity.class);
        intent.putExtra("account",account);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),Communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
