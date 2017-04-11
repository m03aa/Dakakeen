package dakakeen.dakakeen;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateOffer extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private EditText offerDescription, offerPrice;
    private ImageView offerImage;
    String description;
    int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        offerDescription = (EditText)findViewById(R.id.offerDescriptionEditText);
        offerPrice = (EditText)findViewById(R.id.priceEditText);


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

            offerImage = (ImageView) findViewById(R.id.offerImageView);
            offerImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    public void submitOffer(View view){
        if (checkOfferInfo()){
            //we will change it later
            Toast.makeText(getApplicationContext(),R.string.offer_success,Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkOfferInfo(){
        if (offerDescription.getText().toString().isEmpty() && offerPrice.getText().toString().isEmpty() ){
            description = offerDescription.getText().toString();
            price = Integer.parseInt(offerPrice.getText().toString());

            if (!description.trim().matches("") && price != 0)
                return true;
            else return false;
        }
        else
            return true;
    }
}
