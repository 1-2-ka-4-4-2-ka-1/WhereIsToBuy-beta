package com.findViewById.tiwari.myapplication;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheckSendBillsActivity extends AppCompatActivity {


    public RecyclerView recyclerView;
    public CheckSendBillsRecyclerViewAdapter checkSendBillsRecyclerViewAdapter;
    public RecyclerView.LayoutManager manager;
    public ArrayList<MapppedShopsBillsModel> mMappedItems = null;


    public FloatingActionButton mSendData;


    public static String billid="";


    private SharedPreferences sharedPreferences;


    public static AllBillsViewModel mAllBillsVieWModel;
    FirebaseDatabase databaseFirebase;


    private List<Map> l = new ArrayList<>();

    private  Map<String,ShopDetailsModel> shopsData = new HashMap<>();
    private  Map<String,AllBillsItem> billsData = new HashMap<>();
    private  Map<String,MapppedShopsBillsModel> mappedData = new HashMap<>();
    private  Map<String,String> salesmenId = new HashMap<>();

    private  MappedShopsBillsStorageClass storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_send_bills);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mMappedItems = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_check_send_recyclerView);
        manager = new LinearLayoutManager(getApplicationContext());
        checkSendBillsRecyclerViewAdapter = new CheckSendBillsRecyclerViewAdapter(mMappedItems);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(checkSendBillsRecyclerViewAdapter);

        mSendData = findViewById(R.id.fb_check_send_data);

        storage = new MappedShopsBillsStorageClass(CheckSendBillsActivity.this);
        getMappedItemsList();


        sharedPreferences = getSharedPreferences("saved_password",MODE_PRIVATE);

        mAllBillsVieWModel = ViewModelProviders.of(this).get(AllBillsViewModel.class);
        databaseFirebase = FirebaseDatabase.getInstance();



        checkSendBillsRecyclerViewAdapter.setOnItemClickedListener(new CheckSendBillsRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void mItemSelectedListener(int position) {

                if (!mMappedItems.get(position).isSelected()) {

                    mMappedItems.get(position).setSelected(true);
                    checkSendBillsRecyclerViewAdapter.notifyItemChanged(position);
                    billid=String.valueOf(mMappedItems.get(position).getmBillId());
                    Intent intent = new Intent(CheckSendBillsActivity.this, BillsByViewActivity.class);
//                    intent.putExtra(BillsByViewActivity.EXTRA_ID, (String.valueOf(mMappedItems.get(position).getmBillId())));
//                    Log.i("Search with id",String.valueOf(mMappedItems.get(position).getmBillId()));
                    startActivity(intent);

                } else {

                    mMappedItems.get(position).setSelected(false);
                    checkSendBillsRecyclerViewAdapter.notifyItemChanged(position);
                }

            }
        });

        mSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirmSendData();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                storage.deleteMappedItemByBillId(mMappedItems.get(viewHolder.getAdapterPosition()).getmBillId());
                mAllBillsVieWModel.deleteAllBillsBy(String.valueOf(mMappedItems.get(viewHolder.getAdapterPosition()).getmBillId()));
                mMappedItems.remove(viewHolder.getAdapterPosition());
                checkSendBillsRecyclerViewAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);


    }


    public void getMappedItemsList() {


        if (mMappedItems != null && storage.loadMappedItems() != null) {
            ArrayList<MapppedShopsBillsModel> m = storage.loadMappedItems();

            mMappedItems.addAll(m);

            if (mMappedItems.size() == 0) {
                Toast.makeText(CheckSendBillsActivity.this, "List Is Empty", Toast.LENGTH_LONG).show();
                return;
            }

            Toast.makeText(CheckSendBillsActivity.this, "Done", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CheckSendBillsActivity.this, "List is null", Toast.LENGTH_LONG).show();
        }
        checkSendBillsRecyclerViewAdapter.notifyDataSetChanged();


    }

    public void deleteMappedItems(){
        MappedShopsBillsStorageClass storage = new MappedShopsBillsStorageClass(CheckSendBillsActivity.this);
        storage.clearCachedItems();
        mMappedItems.clear();
        checkSendBillsRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void doSendData() {

//        DatabaseReference salesRef= usersRef.child("salesmen1").child("shop1").getRef();
//
//
//        Map<String,MapppedShopsBillsModel> data= new HashMap<>();
//
//
//       for(int i=0;i<mMappedItems.size();i++){
//           data.put("Shop1"+i,mMappedItems.get(i));
//       }
//
//        salesRef.setValue(data);
//
//        ArrayList<AllBillsItem> allBillsItems = new ArrayList<>();
//
//        AllBillsViewModel mAllBillsViewModel;
//        mAllBillsViewModel = ViewModelProviders.of(this).get(AllBillsViewModel.class);






        ShopsStorageClass storage = new ShopsStorageClass(CheckSendBillsActivity.this);
        ArrayList<ShopDetailsModel> s = new ArrayList<>();
        if(storage.loadShops()!=null)
        {
            s.addAll(storage.loadShops());

           shopsData= new HashMap<>();

            for(int i=0;i<s.size();i++){
                shopsData.put("shops_"+s.get(i).getmShopId(),s.get(i));
            }

        }


        AllBillsViewModel mAllBillsViewModel;
        mAllBillsViewModel = ViewModelProviders.of(this).get(AllBillsViewModel.class);


        mappedData= new HashMap<>();

        for(int i=0;i<mMappedItems.size();i++){
            mappedData.put("mapping_"+mMappedItems.get(i).getmShopId()+"_"+mMappedItems.get(i).getmBillId(),mMappedItems.get(i));
        }

        salesmenId.put("salesmen",sharedPreferences.getString("user_id","none"));

        l.add(shopsData);
        l.add(billsData);
        l.add(mappedData);
        l.add(salesmenId);

       new CheckSendBillsActivity.UploadData().execute(l);



        final DatabaseReference billsRef = databaseFirebase.getReference("data/salesmen_"+l.get(3).get("salesmen")+"/bills");

        mAllBillsViewModel.getAllBills().observe(this, new Observer<List<AllBillsItem>>() {
            @Override
            public void onChanged(@Nullable List<AllBillsItem> allBillsItems) {

                Map<String,AllBillsItem> data= new HashMap<>();

                for(int i=0;i<allBillsItems.size();i++){
                    data.put("Bill_"+i+allBillsItems.get(i).mShopId,allBillsItems.get(i));
                }

                billsRef.push().setValue(data);
            }
        });

        checkSendBillsRecyclerViewAdapter.notifyDataSetChanged();
        deleteMappedItems();
    }


    public void doCheckUserExists(){

        SharedPreferences sf = getSharedPreferences("saved_password",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sf.edit();
        final String id=sf.getString("user_id","null");

        DatabaseReference usersRef = databaseFirebase.getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){
                    doSendData();
                }
                else {
                    Toast.makeText(getApplicationContext(),"You are no longer a user",Toast.LENGTH_SHORT).show();
                    editor.putBoolean("logged_in", false);
                    editor.apply();
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void doConfirmSendData(){

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Send")
                .setMessage("Confirm send and clear list ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doCheckUserExists();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }


    private static class UploadData extends AsyncTask<List<Map>,String,String> {



        @Override
        protected String doInBackground(List<Map>... lists) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dataReference = firebaseDatabase.getReference("data");

            DatabaseReference  salesmenRef = dataReference.child("salesmen_"+lists[0].get(3).get("salesmen"));
            
            salesmenRef.child("shops").setValue(lists[0].get(0));
//            salesmenRef.child("bills").setValue(lists[0].get(1));
            salesmenRef.child("mapping").push().setValue(lists[0].get(2));

            return lists[0].get(3).get("salesmen").toString();
        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dataReference = firebaseDatabase.getReference("admin");
            DatabaseReference  notificationRef = dataReference.child("notification");
            notificationRef.push().setValue("New bills received by "+aVoid);
            mAllBillsVieWModel.deleteAllBills();

        }
    }

}






