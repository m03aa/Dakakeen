package dakakeen.dakakeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
    }

    public void checkLogin(View view){
        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }
        /*check database
        else if(){

        }*/
        else{
            Toast.makeText(getApplicationContext(),R.string.Login_successful,Toast.LENGTH_SHORT).show();
            /*Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
            startActivity(intent);*/
            finish();
        }
    }
}
