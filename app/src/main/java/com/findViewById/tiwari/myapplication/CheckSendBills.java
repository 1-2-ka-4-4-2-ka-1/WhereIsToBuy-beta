package com.findViewById.tiwari.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckSendBills extends AppCompatActivity {


    public RecyclerView recyclerView;
    public CheckSendBillsRecyclerViewAdapter checkSendBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager manager;
    public  ArrayList<MapppedShopsBillsModel> mMappedItems =null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_send_bills);


        mMappedItems = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_check_send_recyclerView);
        manager = new LinearLayoutManager(getApplicationContext());
        checkSendBillsRecyclerViewAdapter = new CheckSendBillsRecyclerViewAdapter(mMappedItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(checkSendBillsRecyclerViewAdapter);

        getMappedItemsList();


        checkSendBillsRecyclerViewAdapter.setOnItemClickedListener(new CheckSendBillsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void mItemSelectedListener(int position) {

                if (!mMappedItems.get(position).isSelected()) {

                    mMappedItems.get(position).setSelected(true);
                    checkSendBillsRecyclerViewAdapter.notifyItemChanged(position);

                } else {

                    mMappedItems.get(position).setSelected(false);
                    checkSendBillsRecyclerViewAdapter.notifyItemChanged(position);
                }

            }
        });
    }


    public void getMappedItemsList(){

        MappedShopsBillsStorageClass storage = new MappedShopsBillsStorageClass(CheckSendBills.this);


        if(mMappedItems != null && storage.loadMappedItems() != null ){
            ArrayList<MapppedShopsBillsModel> m= storage.loadMappedItems();

            mMappedItems.addAll(m);

            if(mMappedItems.size()==0)
            {
                Toast.makeText(CheckSendBills.this,"List Is Empty",Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(CheckSendBills.this,"Done",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(CheckSendBills.this,"List is null",Toast.LENGTH_LONG).show();
        }
        checkSendBillsRecyclerViewAdapter.notifyDataSetChanged();


    }



}
