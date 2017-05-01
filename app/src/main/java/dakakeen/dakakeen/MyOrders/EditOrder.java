package dakakeen.dakakeen.MyOrders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.Communication.ResponseHandler;

public class EditOrder extends AppCompatActivity implements ResponseHandler {


    private static int RESULT_LOAD_IMAGE = 1;
    private EditText orderTitle, orderDescription;
    private Spinner orderCategory;
    private ImageView orderImage;
    private Order order;
    private Communication communication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        communication = new Communication(getApplicationContext());

        orderTitle= (EditText)findViewById(R.id.orderTitleEditText);
        orderDescription = (EditText)findViewById(R.id.orderDescriptionEditText);
        orderCategory = (Spinner)findViewById(R.id.categorySpinner);

        order = (Order)getIntent().getSerializableExtra("order");

        orderTitle.setText(order.getTitle());
        orderDescription.setText(order.getDescription());
        orderCategory.setSelection(order.getCategory()+1);


        Button buttonLoadImage = (Button) findViewById(R.id.uploadImageButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            orderImage = (ImageView) findViewById(R.id.orderImageView);
            orderImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    //Edit the current Order
    public void submitOrder(View view){

        if (checkOrderInfo()){
            //send order data to the server
            RequestParams params = new RequestParams();
            params.put("customerUsername",order.getUsername());
            params.put("orderId",order.getId());
            params.put("title",order.getTitle());
            params.put("description",order.getDescription());
            params.put("Category",order.getCategory());

            try {
                communication.put(communication.getUrl()+"/myorders", params, this);
            } catch (Exception e){

            }

        }
        else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkOrderInfo(){
        if (!orderTitle.getText().toString().isEmpty() && !orderDescription.getText().toString().isEmpty()
                && orderCategory.getSelectedItemPosition() != 0){
            order.setTitle(orderTitle.getText().toString());
            order.setDescription(orderDescription.getText().toString());
            order.setCategory(orderCategory.getSelectedItemPosition()-1);

            if(!order.getTitle().trim().matches("") && !order.getDescription().trim().matches("")){
                return true;
            }
            else return false;
        }
        else {
            return false;
        }
    }

    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.order_success,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


}

