package dakakeen.dakakeen;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateOrder extends AppCompatActivity {


    private static int RESULT_LOAD_IMAGE = 1;
    private EditText orderTitle, orderDescription;
    private Spinner orderCategory;
    private ImageView orderImage;
    private String title, description, category;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        orderTitle= (EditText)findViewById(R.id.orderTitleEditText);
        orderDescription = (EditText)findViewById(R.id.orderDescriptionEditText);
        orderCategory = (Spinner)findViewById(R.id.categorySpinner);

        Button buttonLoadImage = (Button) findViewById(R.id.uploadImageButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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


    public void submitOrder(View view){
        if (checkOrderInfo()){
            //we will change it later
            Toast.makeText(getApplicationContext(),R.string.order_success,Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkOrderInfo(){
        if (!orderTitle.getText().toString().isEmpty() && !orderDescription.getText().toString().isEmpty()
                && orderCategory.getSelectedItemPosition() != 0){
            title = orderTitle.getText().toString();
            description = orderDescription.getText().toString();
            category = orderCategory.getSelectedItem().toString();

            if(!title.trim().matches("") && !description.trim().matches("")){
                return true;
            }
            else return false;
        }
        else {
            return false;
        }
    }



}

