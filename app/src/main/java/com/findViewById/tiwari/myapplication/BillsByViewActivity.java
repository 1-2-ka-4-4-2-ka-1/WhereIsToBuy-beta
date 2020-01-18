package com.findViewById.tiwari.myapplication;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static com.findViewById.tiwari.myapplication.MainActivity.EDIT_NOTE_REQUEST;


public class BillsByViewActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.findViewById.tiwari.myapplication.EXTRA_ID";

    private AllBillsViewModel mAllBillsViewModel;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_by_view_cativity);



        RecyclerView recyclerView = findViewById(R.id.rv_bill_by_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AllBillsItemsAdapter allBillsItemsAdapter = new AllBillsItemsAdapter();
        recyclerView.setAdapter(allBillsItemsAdapter);


        id= CheckSendBillsActivity.billid;

        mAllBillsViewModel= ViewModelProviders.of(this).get(AllBillsViewModel.class);
        mAllBillsViewModel.getBillsBy(id).observe(this, new Observer<List<AllBillsItem>>() {
            @Override
            public void onChanged(@Nullable List<AllBillsItem> allBillsItems) {
                allBillsItemsAdapter.submitList(allBillsItems);
            }
        });

//        mAllBillsViewModel.getAllBills().observe(this, new Observer<List<AllBillsItem>>() {
//            @Override
//            public void onChanged(@Nullable List<AllBillsItem> allBillsItems) {
//                allBillsItemsAdapter.submitList(allBillsItems);
//            }
//        });



        allBillsItemsAdapter.setcOnItemClickListener(new AllBillsItemsAdapter.cOnItemClickListener() {
            @Override
            public void cOnItemClick(AllBillsItem allBillsItem) {
                Intent intent = new Intent(BillsByViewActivity.this, EditBillActivity.class);

                intent.putExtra(EditBillActivity.EXTRA_ID,allBillsItem.getBill_id());
                intent.putExtra(EditBillActivity.EXTRA_DESC,allBillsItem.getMitem_desc());
                intent.putExtra(EditBillActivity.EXTRA_UNIT,allBillsItem.getMitem_unit());
                intent.putExtra(EditBillActivity.EXTRA_RATE,allBillsItem.getMitem_rate());
                intent.putExtra(EditBillActivity.EXTRA_QTY,allBillsItem.getMitem_qty());
                intent.putExtra(EditBillActivity.EXTRA_AMOUNT,allBillsItem.getMitem_amount());
                intent.putExtra(EditBillActivity.EXTRA_SAVED,1);

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){


            String item_desc = data.getStringExtra(EditBillActivity.EXTRA_DESC);
            String item_unit = data.getStringExtra(EditBillActivity.EXTRA_UNIT);
            double item_rate = data.getDoubleExtra(EditBillActivity.EXTRA_RATE,0);
            double item_qty = data.getDoubleExtra(EditBillActivity.EXTRA_QTY,0);
            double item_amount = data.getDoubleExtra(EditBillActivity.EXTRA_AMOUNT,0);

            AllBillsItem AllBilsItem = new AllBillsItem((id),item_desc,item_unit,item_rate,item_qty,item_amount,0L,"",0);
            mAllBillsViewModel.update(AllBilsItem);

            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();

        }
    }
}
