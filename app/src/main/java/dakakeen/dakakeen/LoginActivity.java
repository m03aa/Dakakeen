package dakakeen.dakakeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
    }

    public void checkLogin(View view){
        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty() ){
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }
        else if(username.getText().toString().contains(" ") ||   password.getText().toString().contains(" ")){
            Toast.makeText(getApplicationContext(),R.string.username_password_contain_space,Toast.LENGTH_LONG).show();
        }
        /*check database
        else if(){

        }*/
        else{
            Toast.makeText(getApplicationContext(),R.string.Login_successful,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),TabsActivity.class);
            intent.putExtra("username",username.getText().toString());
            intent.putExtra("role",1);
            startActivity(intent);
            finish();
        }
    }
}
