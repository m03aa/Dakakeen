package dakakeen.dakakeen.MyOffers;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;


import java.io.File;
import java.io.FileNotFoundException;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.MyOrders.CreateOrder;
import dakakeen.dakakeen.R;

public class CreateOffer extends AppCompatActivity implements ResponseHandler{

    private static int RESULT_LOAD_IMAGE = 1;
    private EditText offerDescription, offerPrice;
    private TextView orderTitle;
    private ImageView offerImage;

    private Communication communication;
    private Offer offer;
    private File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);

        communication = new Communication(getApplicationContext());

        offer = new Offer();

        offer.order = (Order) getIntent().getSerializableExtra("order");

        orderTitle = (TextView) findViewById(R.id.orderTitle);
        offerDescription = (EditText)findViewById(R.id.offerDescriptionEditText);
        offerPrice = (EditText)findViewById(R.id.priceEditText);

        orderTitle.setText(offer.order.getTitle());


        Button buttonLoadImage = (Button) findViewById(R.id.uploadPictureButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
                //ask for permission to access the gallery
                ActivityCompat.requestPermissions(CreateOffer.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
            image = new File(picturePath);

            cursor.close();

            offerImage = (ImageView) findViewById(R.id.offerImageView);
            offerImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    public void submitOffer(View view){
        if (checkOfferInfo()){

            RequestParams params = new RequestParams();
            params.put("orderId", offer.order.getId());
            params.put("price",offer.getPrice());
            params.put("description", offer.getDescription());
            if (image != null) {
                try {
                    params.put("image", image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            params.setForceMultipartEntityContentType(true);

            try {
                communication.post(communication.getUrl()+"/myoffers", params, this);
            } catch (Exception e){
                Log.e("Communication Exception", e.getMessage());
            }
        }
        else {
            Toast.makeText(getApplicationContext(),R.string.all_fields_required,Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkOfferInfo(){
        offer.setDescription(offerDescription.getText().toString());
        offer.setPrice(Double.parseDouble(offerPrice.getText().toString()));

        if (!offer.getDescription().isEmpty() && offer.getPrice() != 0.0){

            if (!offer.getDescription().trim().matches(""))
                return true;
            else return false;
        }
        else
            return true;
    }


    @Override
    public void onSuccess(byte[] responseBody){
        Toast.makeText(getApplicationContext(),R.string.offer_success,Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(byte[] responseBody){
        Toast.makeText(getApplicationContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }

}
