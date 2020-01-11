package com.findViewById.tiwari.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements FilterListenerInterface ,  DatePickerDialog.OnDateSetListener{

    public static Activity activity_main;

    private AutoCompleteTextView mShopSearchView;
    private TextView mDate;
    private Button mAddNewShop;
    FloatingActionButton mSaveBill;
    FloatingActionButton mAddNewItem;

    private AutoCompleteTextView shopSearchView;

    private BillViewModel mBillItemMidel;

    public static final int EDIT_NOTE_REQUEST = 2;
    public static final int ADD_SHOP_REQUEST = 1;



    public static long timeInMills;

    public static long shopId;




    FirebaseDatabase database;
    DatabaseReference shopItemsRef;
    public ArrayList<ShopItemModel> shop_itemArrayList;



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


        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });


        mAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mShopSearchView.clearFocus();

                ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
                ArrayList<ShopItemModel> shop_items =storage.loadItems();

                if(shop_items!= null) {
                   if(shop_items.size() != 0) {
                       shoItemsDialogue();
                   }
                }else{
                   Toast.makeText(MainActivity.this, " No Items Available  ", Toast.LENGTH_SHORT).show();
                     LodeItemsDataAsyncTask lodeItemsDataAsyncTask=new LodeItemsDataAsyncTask();
                     lodeItemsDataAsyncTask.execute();
                }

            }
        });


        mSaveBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConfirmSaveBill();
            }
        });


        mAddNewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddNewShop();
            }
        });

        shopSearchView=findViewById(R.id.sv_shop_search);

        doSetupShopSearchAdapter();
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

    public void doConfirmSaveBill(){

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Save")
                .setMessage("Confirm save and clear list ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doSaveBill();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    public void doSaveBill(){

        timeInMills = System.currentTimeMillis()%1000;

        if(shopId == 0) {
            Toast.makeText(MainActivity.this,"Shop details Wrong",Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<BillItem> billItems = new ArrayList<>();

        if(mBillItemMidel.getAllBills().getValue() != null){
            if(mBillItemMidel.getAllBills().getValue().size() == 0)
            {
                Toast.makeText(MainActivity.this,"Can Not Save Emty Bill",Toast.LENGTH_LONG).show();
                return;
            }

        }

        try {


            billItems.addAll(mBillItemMidel.getAllBills().getValue());

            for (int i = 0; i < billItems.size(); i++) {

                billItems.get(i).setBill_id(Integer.parseInt(Long.toString(timeInMills)));
            }


            MapppedShopsBillsModel mapppedShopsBillsModels = new MapppedShopsBillsModel(shopId, billItems.get(0).getBill_id(), mDate.getText().toString(),billItems.size(),mShopSearchView.getText().toString());

            MappedShopsBillsStorageClass storage = new MappedShopsBillsStorageClass(MainActivity.activity_main);
            storage.insertMappedItems(mapppedShopsBillsModels);
            Toast.makeText(MainActivity.this,"Bill Saved",Toast.LENGTH_LONG).show();

            //shopId billId date
        }catch (Exception e){

            Toast toast = Toast.makeText(this, "Something Went Wrong"+e, Toast.LENGTH_SHORT);
            View v = toast.getView();
            v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            toast.show();

        }

    }

    public void shoItemsDialogue(){
        //arrayList

        ShowItemsDialogue items_dialog = new ShowItemsDialogue();
        items_dialog.show(getSupportFragmentManager(),"show items");

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

        }else if(requestCode == ADD_SHOP_REQUEST && resultCode == RESULT_OK){

            Toast.makeText(getApplicationContext(),data.getStringExtra(AddShopFormActivity.EXTRA_SHOPNAME),Toast.LENGTH_LONG).show();
            mShopSearchView.setText(data.getStringExtra(AddShopFormActivity.EXTRA_SHOPNAME));
            shopId = Long.parseLong(data.getStringExtra(AddShopFormActivity.EXTRA_ID));

            mShopSearchView.clearFocus();

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
                if(hasFocus){
                    if(mAddNewShop.getVisibility() == View.VISIBLE)
                    {
                        mAddNewShop.setVisibility(View.GONE);
                    }

                }
            }
        });

    }

    public void  doSetupShopSearchAdapter(){


        final ShopsStorageClass storage = new ShopsStorageClass(MainActivity.activity_main);
        List<ShopDetailsModel> list =  storage.loadShops();



        ArrayList<String> shopsNamesList =new ArrayList<>();

        if( list!=null )
        for(int i=0;i<list.size();i++)
        {
            shopsNamesList.add(list.get(i).getmShopName());
        }

        final AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, R.layout.text_suggestionss_layout ,shopsNamesList);
        adapter.setFilterListeners(this);
        shopSearchView.setAdapter(adapter);
        shopSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                shopId = Long.parseLong(storage.getShopByDesc(shopSearchView.getText().toString().trim()).getmContactno())%1000;


            }
        });

    }

    public void doAddNewShop(){
        Intent intent = new Intent(MainActivity.this, AddShopFormActivity.class);
        startActivityForResult(intent,ADD_SHOP_REQUEST);
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


    private void pickDate() {
        DialogFragment date_picker = new DatePickerFragment();
        date_picker.show(getSupportFragmentManager(),"date picker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c =Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy ");
        String currentDate = dateFormat.format(c.getTime());
        mDate.setText(currentDate);
    }


    public class  LodeItemsDataAsyncTask extends AsyncTask<Void , Void , Void>{

        @Override
        protected void onPreExecute() {

            Toast.makeText(MainActivity.this,"Executing",Toast.LENGTH_SHORT);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            doLoadShopItemsData();

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {


            Toast.makeText(MainActivity.this,"Done"+values+"%",Toast.LENGTH_SHORT);
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Toast.makeText(MainActivity.this,"Done",Toast.LENGTH_LONG);
            //shoItemsDialogue();
            super.onPostExecute(aVoid);
        }
    }



   public void doLoadShopItemsData(){

       database = FirebaseDatabase.getInstance();
       shopItemsRef= database.getReference("Shop-items");

        shopItemsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int itemCount = (int)dataSnapshot.getChildrenCount();
                itemCount--;

                String itemDesc="";
                String itemRate="";
                String itemUnit="";

                    shop_itemArrayList = new ArrayList<>();
                    while (itemCount-- != 0)
                    {
                        itemDesc=  dataSnapshot.child(Integer.toString(itemCount)).child("item").getValue().toString();
                        itemRate= dataSnapshot.child(Integer.toString(itemCount)).child("rate").getValue().toString();
                        itemUnit=  dataSnapshot.child(Integer.toString(itemCount)).child("unit").getValue().toString();

                        timeInMills = System.currentTimeMillis()%1000;

                        shop_itemArrayList.add(new ShopItemModel(Long.toString(timeInMills),itemDesc,itemUnit,Double.parseDouble(itemRate),1,Double.parseDouble(itemRate)));
                        Log.i("Data:...........",dataSnapshot.child(Integer.toString(itemCount)).getValue().getClass().getName());

                    }

                    ShopItemsStorageClass storage = new ShopItemsStorageClass(getApplicationContext());
                    storage.storeItems(shop_itemArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG);
            }

        });



    }


}
