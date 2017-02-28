package dakakeen.dakakeen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity {

    //Activity Layouts
    LinearLayout roleLayout;
    LinearLayout registerLayout;
    //roleLayout components
    RadioGroup registerRole;
    RadioButton customer;
    RadioButton provider;
    //registerLayout components
    EditText username;
    EditText password;
    EditText email;
    EditText name;
    EditText nationalId;
    EditText phone;
    EditText address;
    Button registerSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Activity Layouts
        roleLayout = (LinearLayout)findViewById(R.id.roleLinearLayout);
        registerLayout = (LinearLayout)findViewById(R.id.registerLinearLayout);
        //roleLayout components
        registerRole = (RadioGroup)findViewById(R.id.registerRole);
        customer = (RadioButton)findViewById(R.id.customerRadioButton);
        provider = (RadioButton)findViewById(R.id.providerRadioButton);
        //registerLayout components
        username = (EditText)findViewById(R.id.registerUsernameEditText);
        password = (EditText)findViewById(R.id.registerPasswordEditText);
        email = (EditText)findViewById(R.id.registerEmailEditText);
        name = (EditText)findViewById(R.id.registerNameEditText);
        nationalId = (EditText)findViewById(R.id.registerNationalIdEditText);
        phone = (EditText)findViewById(R.id.registerPhoneEditText);
        address = (EditText)findViewById(R.id.registerAddressEditText);
        registerSubmit = (Button) findViewById(R.id.registerSubmitButton);

    }

    public void displayRegisterForm(View view){
        if(registerRole.getCheckedRadioButtonId() == customer.getId()){
            roleLayout.setVisibility(View.INVISIBLE);
            registerLayout.setVisibility(View.VISIBLE);
        }
        else{
            roleLayout.setVisibility(View.INVISIBLE);
            name.setVisibility(View.VISIBLE);
            nationalId.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.VISIBLE);
        }

    }
}
