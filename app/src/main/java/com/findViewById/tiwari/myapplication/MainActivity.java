package com.findViewById.tiwari.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterListenerInterface{

    public static Activity activity_main;

    private AutoCompleteTextView mShopSearchView;
    private TextView mDate;
    private Button mAddNewShop;
    FloatingActionButton mSaveBill;
    FloatingActionButton mAddNewItem;

    private AutoCompleteTextView shopSearchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = this;


        mShopSearchView = findViewById(R.id.sv_shop_search);
        mDate = findViewById(R.id.tv_date);
        mAddNewShop = findViewById(R.id.btn_add_shop);
        mSaveBill = findViewById(R.id.fb_save_bill);
        mAddNewItem = findViewById(R.id.fb_add_new_item);


        mAddNewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddNewShop();
            }
        });

        shopSearchView=findViewById(R.id.sv_shop_search);

        doSetupShopSearchAdapter();

        setFocusChangedListener();

    }


    public void setFocusChangedListener(){

        shopSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(mAddNewShop.getVisibility() == View.VISIBLE)
                    {
                        mAddNewShop.setVisibility(View.GONE);
                    }

                }
            }
        });

    }

    public void  doSetupShopSearchAdapter(){


        ShopsStorageClass storage = new ShopsStorageClass(MainActivity.activity_main);
        List<ShopDetailsModel> list =  storage.loadShops();

        ArrayList<String> shopsNamesList =new ArrayList<>();

        for(int i=0;i<list.size();i++)
        {
            shopsNamesList.add(list.get(i).getmShopName());
        }

        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, R.layout.text_suggestionss_layout ,shopsNamesList);
        adapter.setFilterListeners(this);
        shopSearchView.setAdapter(adapter);
    }

    public void doAddNewShop(){
        Intent intent = new Intent(MainActivity.this, AddShopFormActivity.class);
        startActivity(intent);
    }


    @Override
    public void filteringFinished(int filteredShopsCount) {

        if(filteredShopsCount == 0) {
            Log.i("LOG_TAG", " filteringFinished  count = " + filteredShopsCount);
            Toast toast = Toast.makeText(this, filteredShopsCount + " Items matched your search", Toast.LENGTH_SHORT);
            View v = toast.getView();
            v.getBackground().setColorFilter(Color.rgb(10, 190, 207), PorterDuff.Mode.SRC_IN);
            toast.show();

            mAddNewShop.setVisibility(View.VISIBLE);
        }
        else {
            mAddNewShop.setVisibility(View.GONE);
        }
    }
}
