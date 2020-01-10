package com.findViewById.tiwari.myapplication;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

    private BillViewModel mBillItemMidel;

    public static final int EDIT_NOTE_REQUEST = 2;


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

        //doSetupShopSearchAdapter();
        setFocusChangedListener();


        RecyclerView recyclerView = findViewById(R.id.bill_unit_container_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final BillItemsAdapter billItemsAdapter = new BillItemsAdapter();
        recyclerView.setAdapter(billItemsAdapter);

        mBillItemMidel= ViewModelProviders.of(this).get(BillViewModel.class);
        mBillItemMidel.getAllBills().observe(this, new Observer<List<BillItem>>() {
            @Override
            public void onChanged(@Nullable List<BillItem> notes) {
                //UpdateRecyclerView

                billItemsAdapter.submitList(notes);
                //Toast.makeText(getApplicationContext(),"called",Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                mBillItemMidel.delete(billItemsAdapter.getBillItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        billItemsAdapter.setcOnItemClickListener(new BillItemsAdapter.cOnItemClickListener() {
            @Override
            public void cOnItemClick(BillItem BillItem) {

                Intent intent = new Intent(MainActivity.this, EditBillActivity.class);

                intent.putExtra(EditBillActivity.EXTRA_ID,BillItem.getBill_id());
                intent.putExtra(EditBillActivity.EXTRA_DESC,BillItem.getMitem_desc());
                intent.putExtra(EditBillActivity.EXTRA_UNIT,BillItem.getMitem_unit());
                intent.putExtra(EditBillActivity.EXTRA_RATE,BillItem.getMitem_rate());
                intent.putExtra(EditBillActivity.EXTRA_QTY,BillItem.getMitem_qty());
                intent.putExtra(EditBillActivity.EXTRA_AMOUNT,BillItem.getMitem_amount());

                startActivityForResult(intent,EDIT_NOTE_REQUEST);


            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){


            int id = data.getIntExtra(EditBillActivity.EXTRA_ID,-1);

            if(id == -1){
                Toast.makeText(getApplicationContext(),"Can't be updated",Toast.LENGTH_LONG).show();
                return;
            }

            String item_desc = data.getStringExtra(EditBillActivity.EXTRA_DESC);
            String item_unit = data.getStringExtra(EditBillActivity.EXTRA_UNIT);
            double item_rate = data.getDoubleExtra(EditBillActivity.EXTRA_RATE,0);
            double item_qty = data.getDoubleExtra(EditBillActivity.EXTRA_QTY,0);
            double item_amount = data.getDoubleExtra(EditBillActivity.EXTRA_AMOUNT,0);

            BillItem billItem = new BillItem(Integer.toString(id),item_desc,item_unit,item_rate,item_qty,item_amount);
            billItem.setBill_id(id);
            mBillItemMidel.update(billItem);

            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not Saved",Toast.LENGTH_LONG).show();
        }
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
