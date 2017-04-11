package dakakeen.dakakeen;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewOrderDetails extends AppCompatActivity {

    private TextView orderTitle, orderDescription, orderCategory;
    private ImageView orderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        orderDescription = (TextView)findViewById(R.id.orderDescription);
        orderCategory = (TextView)findViewById(R.id.orderCategory);
        orderImage = (ImageView)findViewById(R.id.orderImageView);

    }



    public void directToEditOrder(View view){
        Intent intent = new Intent(getApplicationContext(),EditOrder.class);
        //intent.putExtra("orderId",);
        intent.putExtra("orderTitle","Hat");
        startActivity(intent);
        finish();
    }



    public void deleteOrder(View view){
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(R.string.delete_order)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //we will add the delete request here
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Nothing will happen :)
                    }
                })
                .show();
    }
}
