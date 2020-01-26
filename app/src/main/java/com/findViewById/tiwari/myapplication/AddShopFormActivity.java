package com.findViewById.tiwari.myapplication;

import android.content.Intent;
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
    private  int Rating;
    private FloatingActionButton mSubmitButton;

    public static final String EXTRA_SHOPNAME = "com.findViewById.tiwari.myapplication.EXTRA_SHOPNAME";
    public static final String EXTRA_ID = "com.findViewById.tiwari.myapplication.EXTRA_ID";


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
         mRating.setStepSize(1.0f);

         mSubmitButton = findViewById(R.id.fb_submit_add_shop);

         mSubmitButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 doAddShop();
             }
         });

        mRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               //Toast.makeText(AddShopFormActivity.this,(int)rating+"",Toast.LENGTH_LONG).show();
                Rating = (int) rating;
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
         String Group =  mGroup.getText().toString().trim();


         if(ShopName.equals("") || Address.equals("")  || Contactno.equals("") || Rating == 0 || Group.equals("") ){

             Toast.makeText(getApplicationContext(),"Shop name + contact no + Address + Rating + Group",Toast.LENGTH_LONG).show();
         }
        else {


             Long timeInMills = System.currentTimeMillis();
             ShopDetailsModel s = new ShopDetailsModel(ShopName, AliasName, Address, Area, Location, Sublocation, Landmark, Contactno, Group, Rating, timeInMills);

             ShopsStorageClass storage = new ShopsStorageClass(MainActivity.activity_main);
             storage.addNewShop(s);

             Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();

             Intent data = new Intent();


             data.putExtra(EXTRA_SHOPNAME, ShopName);
             data.putExtra(EXTRA_ID, timeInMills.toString());

             setResult(RESULT_OK, data);
             finish();
         }
    }


}
