package com.findViewById.tiwari.myapplication;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddShopFormActivity extends AppCompatActivity {

    private EditText mShopName;
    private EditText mAliasName;
    private EditText mAddress;
    private EditText mArea;
    private EditText mLocation;
    private EditText mSublocation;
    private EditText mLandmark;
    private EditText mContactno;
    private EditText mGroup;

    private RatingBar mRating;

    private FloatingActionButton mSubmitButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_form);

         mShopName = findViewById(R.id.ed_shop_name);
         mAliasName = findViewById(R.id.ed_alias_name);
         mAddress = findViewById(R.id.ed_address);
         mArea = findViewById(R.id.ed_area);
         mLocation = findViewById(R.id.ed_location);
         mSublocation = findViewById(R.id.ed_sublocation);
         mLandmark = findViewById(R.id.ed_landmark);
         mContactno = findViewById(R.id.ed_contact);
         mGroup = findViewById(R.id.ed_group);

         mRating = findViewById(R.id.sb_rating);

         mSubmitButton = findViewById(R.id.fb_submit_add_shop);


         mSubmitButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 doAddShop();
             }
         });

    }

    public void doAddShop(){

         String ShopName = mShopName.getText().toString().trim();
         String AliasName =  mAliasName.getText().toString().trim();
         String Address =  mAddress.getText().toString().trim();
         String Area =  mArea.getText().toString().trim();
         String Location =  mLocation.getText().toString().trim();
         String Sublocation =  mSublocation.getText().toString().trim();
         String Landmark =  mLandmark.getText().toString().trim();
         String Contactno = mContactno.getText().toString().trim() ;
         String Group =  mGroup.getText().toString().trim();;

         int Rating = mRating.getNumStars();


        ShopDetailsModel s = new ShopDetailsModel( ShopName ,  AliasName ,  Address ,  Area ,  Location ,  Sublocation ,  Landmark ,  Contactno , Group ,  Rating );

        ShopsStorageClass storage = new ShopsStorageClass(MainActivity.activity_main);
        storage.addNewShop(s);

        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();

    }


}
