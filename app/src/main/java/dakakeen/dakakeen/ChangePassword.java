package dakakeen.dakakeen;

import android.app.DownloadManager;
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

public class ChangePassword extends AppCompatActivity implements ResponseHandler {
    private String username;
    private String password;
    private Communication communication;
    EditText oldpass;
    EditText newPasswordEditText;
    EditText verifyPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        username=getIntent().getStringExtra("username");
        password=getIntent().getStringExtra("password");
        communication = new Communication(getApplicationContext());



    }

    public void changePassword(View view){
         oldpass=(EditText)findViewById(R.id.oldPasswordEditText);
        newPasswordEditText=(EditText)findViewById(R.id.NewPasswordEditText);
         verifyPasswordEditText = (EditText)findViewById(R.id.VerifyPasswordEditText);
        String oldPassWord=oldpass.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String verifyPassword= verifyPasswordEditText.getText().toString();
        // how to check old password!!!!
        if(!password.equals(oldPassWord)){
            Toast.makeText(getApplicationContext(),R.string.old_password_not_correct,Toast.LENGTH_LONG).show();
            return;

        }
        if(newPassword.length()<6){
            Toast.makeText(getApplicationContext(),R.string.short_password,Toast.LENGTH_SHORT).show();
            return;

        }

        if(!newPassword.equals(verifyPassword)){
            Toast.makeText(getApplicationContext(),R.string.new_and_verified_password_not_match,Toast.LENGTH_LONG).show();
            return;

        }

        try {
            // ask omer about old password!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            RequestParams Params = new RequestParams();
            Params.put("username",username);
            Log.d("username",username);
            Log.d("pass",oldPassWord);
            Params.put("oldpassword",oldPassWord);
            Params.put("password",newPassword);
            communication.post(communication.getUrl()+"/auth/change",Params,this);

        }catch (Exception e){
            Log.d("something happend ","password dose not changed");

        }






    }

    @Override
    public void onSuccess(byte[] responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            //username=jsonObject.getString("username");
            //password=jsonObject.getString("password");
            Toast.makeText(getApplication(),R.string.password_changed_successfully,Toast.LENGTH_LONG);
            oldpass.setText("");
            newPasswordEditText.setText("");
            verifyPasswordEditText.setText("");



        }catch (JSONException e){
            e.printStackTrace();

        }

    }

    @Override
    public void onFailure(byte[] responseBody) {
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_LONG).show();

    }
}
