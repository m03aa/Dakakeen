package dakakeen.dakakeen;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import dakakeen.dakakeen.Authentication.LoginActivity;
import dakakeen.dakakeen.Authentication.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void directToRegister(View view){
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
    }

    public void directToLogin(View view){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
    }
}
