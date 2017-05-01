package dakakeen.dakakeen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Account;

public class RegisterActivity extends AppCompatActivity implements ResponseHandler {

    //Activity Layouts
    private LinearLayout roleLayout, registerLayout;
    //roleLayout components
    private RadioGroup registerRole;
    private RadioButton customer;
    //registerLayout components
    private EditText username, password, email, name, nationalId, phone, address;

    private Communication communication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        communication = new Communication(getApplicationContext());

        //Activity Layouts
        roleLayout = (LinearLayout)findViewById(R.id.roleLinearLayout);
        registerLayout = (LinearLayout)findViewById(R.id.registerLinearLayout);
        //roleLayout components
        registerRole = (RadioGroup)findViewById(R.id.registerRole);
        customer = (RadioButton)findViewById(R.id.customerRadioButton);
        //registerLayout components
        username = (EditText)findViewById(R.id.registerUsernameEditText);
        password = (EditText)findViewById(R.id.registerPasswordEditText);
        email = (EditText)findViewById(R.id.registerEmailEditText);
        name = (EditText)findViewById(R.id.registerNameEditText);
        nationalId = (EditText)findViewById(R.id.registerNationalIdEditText);
        phone = (EditText)findViewById(R.id.registerPhoneEditText);
        address = (EditText)findViewById(R.id.registerAddressEditText);

    }

    public void displayRegisterForm(View view){
        if(registerRole.getCheckedRadioButtonId() == customer.getId()){
            roleLayout.setVisibility(View.INVISIBLE);
            registerLayout.setVisibility(View.VISIBLE);
        }
        else{
            roleLayout.setVisibility(View.INVISIBLE);
            nationalId.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.VISIBLE);
        }
    }

    public void checkRegister(View view){

        Account account = new Account();
        RequestParams params = new RequestParams();

        //for customer and provider
        account.setUsername(username.getText().toString());
        account.setPassword(password.getText().toString());
        account.setEmail(email.getText().toString());
        account.setAddress(address.getText().toString());
        //for provider only
        account.setName(name.getText().toString());
        account.setNationalId(nationalId.getText().toString());
        account.setPhone(phone.getText().toString());

        //check if there is a missing field
        if(account.getUsername().isEmpty() || account.getPassword().isEmpty() || account.getEmail().isEmpty()
                || account.getAddress().isEmpty() || account.getName().isEmpty() || account.getPhone().isEmpty())
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();

            //if username or password contains spaces
        else
        if(account.getUsername().contains(" ") ||   account.getPassword().contains(" "))
            Toast.makeText(getApplicationContext(),R.string.username_password_contain_space,Toast.LENGTH_LONG).show();

            //if the password length less than 6
        else
        if (account.getPassword().length() < 6)
            Toast.makeText(getApplicationContext(),R.string.short_password,Toast.LENGTH_SHORT).show();
            //check email format
        else
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(account.getEmail()).matches())
            Toast.makeText(getApplicationContext(),R.string.email_correct,Toast.LENGTH_SHORT).show();
        else {
            params.put("username", account.getUsername());
            params.put("password", account.getPassword());
            params.put("email", account.getEmail());
            params.put("address", account.getAddress());
            params.put("name", account.getName());
            params.put("phone", account.getPhone());

            //for provider
            if (registerRole.getCheckedRadioButtonId() != customer.getId()){
                if (account.getNationalId().isEmpty())
                    Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
            //check nationalId length
            if(account.getNationalId().length() != 10)
                Toast.makeText(getApplicationContext(),R.string.number_incorrect,Toast.LENGTH_SHORT).show();

                    //check phone length
                else if (account.getPhone().length() != 10)
                    Toast.makeText(getApplicationContext(),R.string.number_incorrect,Toast.LENGTH_SHORT).show();

                    //if everything is fine create new customer account
                else {
                    params.put("role", 2);
                    params.put("nationalId", account.getNationalId());
                }

            }
            else {
                params.put("role", 1);
            }

            try {
                communication.post(communication.getUrl()+"/auth/register", params, this);
            } catch (Exception e){
                Log.e("Communication Exception", e.getMessage());
            }
        }
    }





    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.register_successful,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(), communication.handelError(responseBody), Toast.LENGTH_SHORT).show();
    }


}
