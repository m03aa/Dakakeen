package dakakeen.dakakeen.CustomerFunctions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.R;

public class RequestRefundAndReportDefect extends AppCompatActivity implements ResponseHandler {

    private String offerId, reason;
    private int type;
    private Communication communication;

    private EditText reasonET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_refund_and_report_defect);

        offerId = getIntent().getStringExtra("offerId");
        type = getIntent().getIntExtra("type",0);
        communication = new Communication(getApplicationContext());

        reasonET = (EditText) findViewById(R.id.reason);
    }


    public void submit(View view){

        reason = reasonET.getText().toString();

        if (reason.isEmpty() || reason.trim().matches(" "))
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        else {
            RequestParams params = new RequestParams();
            params.put("offerId",offerId);
            params.put("reason",reason);
            try {
                if (type == 0)
                    communication.post(communication.getUrl()+"/offers/defect",params,this);
                else
                    communication.post(communication.getUrl()+"/offers/refund",params,this);
            } catch (Exception e){
                Log.d("Communication Exception", e.getMessage());
            }
        }
    }


    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.submission_success,Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),new String(responseBody),Toast.LENGTH_SHORT).show();
    }

}
