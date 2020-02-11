package com.findViewById.tiwari.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements FilterListenerInterface ,  DatePickerDialog.OnDateSetListener ,AddNoteDialogue.DialogueListener{

    public static Activity activity_main;

    private AutoCompleteTextView mShopSearchView;
    private TextView mDate;
    private Button mAddNewShop;
    FloatingActionButton mSaveBill;
    FloatingActionButton mAddNewItem;

    public  ArrayList<BillItem> mBillItems;

    private AutoCompleteTextView shopSearchView;

    private BillViewModel mBillItemMidel;
    private AllBillsViewModel mAllBillsViewModel;

    public static final int EDIT_NOTE_REQUEST = 2;
    public static final int ADD_SHOP_REQUEST = 1;

    public static long timeInMills;
    public static long shopId;
    public static String mNote = "";

    FirebaseDatabase database;
    DatabaseReference shopItemsRef;
    public ArrayList<ShopItemModel> shop_itemArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activity_main = this;

        database = FirebaseDatabase.getInstance();


        mShopSearchView = findViewById(R.id.sv_shop_search);
        mDate = findViewById(R.id.tv_date);
        mAddNewShop = findViewById(R.id.btn_add_shop);
        mSaveBill = findViewById(R.id.fb_save_bill);
        mAddNewItem = findViewById(R.id.fb_add_new_item);


        String date = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        mDate.setText(date);
        Log.i("date",date);
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


        mAllBillsViewModel = ViewModelProviders.of(this).get(AllBillsViewModel.class);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                mBillItemMidel.delete(billItemsAdapter.getBillItemAt(viewHolder.getAdapterPosition()));
               // billItemsAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        billItemsAdapter.setcOnItemClickListener(new BillItemsAdapter.cOnItemClickListener() {
            @Override
            public void cOnItemClick(BillItem BillItem) {

                Intent intent = new Intent(MainActivity.this, EditBillActivity.class);

                intent.putExtra(EditBillActivity.EXTRA_ID_LABLE,BillItem.getMitem_id_label());
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
        //proceedToBillPreview();

        if(mShopSearchView.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please Select Shop",Toast.LENGTH_LONG).show();
        }
        else{
           AlertDialog alertDialog= new AlertDialog.Builder(this).create();
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

            alertDialog.setTitle("Confirm Save");

            alertDialog.setMessage("Confirm save and clear list ?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doCheckUserExists();
                        }

                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Add Note", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doGetNote();
                }
            });
            alertDialog.show();
        }
    }



    public void doGetNote(){
        final AddNoteDialogue addNoteDialogue = new AddNoteDialogue("",0);
        addNoteDialogue.show(getSupportFragmentManager(), "Add Note");
    }


    @Override
    public void addNoteDialogue(String note) {

        mNote = note;
    }

    public void doCheckUserExists(){

        SharedPreferences sf = getSharedPreferences("saved_password",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sf.edit();
       final String id=sf.getString("user_id","null");

       DatabaseReference usersRef = database.getReference("users");
       usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(id).exists()){
                   doSaveBill();
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

    public void doSaveBill(){

        timeInMills = System.currentTimeMillis()%1000000;

        if(shopId == 0) {
            Toast.makeText(MainActivity.this,"Shop details Wrong",Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<BillItem> billItems = new ArrayList<>();

        if(mBillItemMidel.getAllBills().getValue() != null){
            if(mBillItemMidel.getAllBills().getValue().size() == 0)
            {
                Toast.makeText(MainActivity.this,"Can Not Save Empty Bill",Toast.LENGTH_LONG).show();
                return;
            }

        }

        try {


            billItems.addAll(mBillItemMidel.getAllBills().getValue());

            for (int i = 0; i < billItems.size(); i++) {

                billItems.get(i).setBill_id(timeInMills);
                mAllBillsViewModel.insert( new AllBillsItem((billItems.get(i).getBill_id()).toString(),billItems.get(i).getMitem_desc(),billItems.get(i).getMitem_unit(),billItems.get(i).getMitem_rate(),billItems.get(i).getMitem_qty(),billItems.get(i).getMitem_amount(),shopId,  mDate.getText().toString() , billItems.size()));
            }

            SharedPreferences sf = getSharedPreferences("saved_password",MODE_PRIVATE);
            String salesmen = sf.getString("user_id","null");
            MapppedShopsBillsModel mapppedShopsBillsModels = new MapppedShopsBillsModel(shopId,Integer.parseInt(Long.toString(timeInMills).trim()), mDate.getText().toString(),billItems.size(),mShopSearchView.getText().toString(),salesmen,mNote);
            mNote="";
            MappedShopsBillsStorageClass storage = new MappedShopsBillsStorageClass(MainActivity.activity_main);
            storage.insertMappedItems(mapppedShopsBillsModels);
            Toast.makeText(MainActivity.this,"Bill Saved",Toast.LENGTH_LONG).show();
            mBillItemMidel.deleteAllBills();

            //shopId billId date
        }catch (Exception e){

            Toast toast = Toast.makeText(this, "Something Went Wrong"+e, Toast.LENGTH_SHORT);
            View v = toast.getView();
            v.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            toast.show();

        }

        //proceedToBillPreview();

    }


    public void proceedToBillPreview() {
// create a new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(421, 595, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
       // canvas.drawColor(Color.RED);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);


        canvas.drawText("Shop:", 100-50, 90-30, paint);


        paint.setStrokeWidth(0.4f);
        paint.setTextSize(10);
        canvas.drawLine(100-50, 100-30, 450-50, 100-30, paint);
        canvas.drawLine(100-50, 130-40, 450-50, 130-40, paint);
        canvas.drawText("02/01/20", 490-130, 70-50, paint);

        paint.setColor(Color.rgb(12, 108, 138));

        paint.setTextSize(8);
        paint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("Rough Estimate", 170, 15, paint);
        paint.setTextSize(25);
        canvas.drawText("REENA", 165, 35, paint);


        paint.setStrokeWidth(1);
        paint.setTextSize(10);
        paint.setColor(Color.BLACK);

        canvas.drawText("Qty", 90 + 3-50, 170-50, paint);
        canvas.drawText("Unit", 120 + 10-50, 170-50, paint);
        canvas.drawText("Desc of good", 155 + 40-50, 170-50, paint);
        canvas.drawText("Rate", 335 + 5-50, 170-50, paint);
        canvas.drawText("Amount", 400-50, 170-50, paint);


        //canvas.drawLine(90,160,90,750,paint);
//        canvas.drawLine(90-50, 190-50, 90-50, 595-80, paint);
//        canvas.drawLine(120-50, 190-50, 120-50, 595-80, paint);
//        canvas.drawLine(165-50, 190-50, 165-50, 595-80, paint);
//        canvas.drawLine(325-50, 190-50, 325-50-10, 595-80, paint);
//        canvas.drawLine(390-50-10, 190-50, 390-50-10, 595-80, paint);
//        canvas.drawLine(460-50-10, 190-50, 460-50-10, 595-80, paint);


        canvas.drawText("Note:", 80-50, 595-60, paint);
        canvas.drawText("Total", 390-50, 595-50, paint);


        paint.setTextSize(8);
        int j = 25;

        int total = 0;

        String desc = "नींबू अचार  ";

        int max = 17;
        Paint p = new Paint();
        p.setColor(Color.WHITE);

        int o=25;
        for (int i = 0; i < max; i++) {
            if(i%2==0)
            {
                p.setColor(Color.parseColor("#f0fcfc"));
            }else p.setColor(Color.parseColor("#e8e8e8"));

            canvas.drawRect(30,170 + o-50,405,170 +o-50+25,p);



           o+=22;


            j = j + 10;
            canvas.drawText(Integer.toString(i + 1), 90 + 10-50, 170 + j-50, paint);
            canvas.drawText("Kg", 120 + 15-50, 170 + j-50,paint);
            canvas.drawText("561.00", 325 + 5-50, 170 + j-50, paint);
            canvas.drawText("561.00", 400-50, 170 + j-50, paint);
            int m = 0;
            int n = 0;
            if (desc.length() - m < 25) {
                n = desc.length();
                // j+=10;
            } else {
                n = 25;
                max = 15;
            }
            for (int k = 0; k <= desc.length() / 25; k++) {
                canvas.drawText(desc.substring(m, n), 135 + 40-50, 170 + j-50, paint);
                m = n;
                if (desc.length() - m < 25) {
                    n = desc.length();
                } else {
                    n = 25;
                    j += 12;

                }

                j += 12;
            }

            total += 561;
        }


        canvas.drawLine(90-50, 190-50, 90-50, 595-80+5, paint);
        canvas.drawLine(120-50, 190-50, 120-50, 595-80+5, paint);
        canvas.drawLine(165-50, 190-50, 165-50, 595-80+5, paint);
        canvas.drawLine(325-50-10, 190-50, 325-50-10, 595-80+5, paint);
        canvas.drawLine(390-50-10, 190-50, 390-50-10, 595-80+5, paint);
        canvas.drawLine(460-50-10, 190-50, 460-50-10, 595-80+5, paint);
        //canvas.drawText("12312312312312312312312",135+40,190,paint);
        //canvas.drawText("561.00",390,170+j,paint);

        // finish the page
        document.finishPage(page);


        // Create Page 2
//        pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 2).create();
//        page = document.startPage(pageInfo);
//        canvas = page.getCanvas();
//        paint = new Paint();
//        paint.setColor(Color.BLUE);
//        canvas.drawCircle(200, 200, 100, paint);
//        document.finishPage(page);

        // write the document content
        //String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fileName.pdf";
        String targetPdf =this.getFilesDir() + "/fileName.pdf";
        File filePath = new File(targetPdf);

        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done" + targetPdf, Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();


        showPdf();
    }



    public void showPdf(){

        Intent display = new Intent(MainActivity.this,PdfPreviewer.class);
        startActivity(display);

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


            Long id = data.getLongExtra(EditBillActivity.EXTRA_ID,-1);

            if(id == -1){
                Toast.makeText(getApplicationContext(),"Can't be updated",Toast.LENGTH_LONG).show();
                return;
            }

            String item_id_label = data.getStringExtra(EditBillActivity.EXTRA_ID_LABLE);
            String item_desc = data.getStringExtra(EditBillActivity.EXTRA_DESC);
            String item_unit = data.getStringExtra(EditBillActivity.EXTRA_UNIT);
            double item_rate = data.getDoubleExtra(EditBillActivity.EXTRA_RATE,0);
            double item_qty = data.getDoubleExtra(EditBillActivity.EXTRA_QTY,0);
            double item_amount = data.getDoubleExtra(EditBillActivity.EXTRA_AMOUNT,0);

            BillItem billItem = new BillItem(item_id_label,item_desc,item_unit,item_rate,item_qty,item_amount);
            billItem.setBill_id(id);
            mBillItemMidel.update(billItem);

            Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();

        }else if(requestCode == ADD_SHOP_REQUEST && resultCode == RESULT_OK){

            Toast.makeText(getApplicationContext(),data.getStringExtra(AddShopFormActivity.EXTRA_SHOPNAME),Toast.LENGTH_LONG).show();
            mShopSearchView.setText(data.getStringExtra(AddShopFormActivity.EXTRA_SHOPNAME));
            shopId = Long.parseLong(data.getStringExtra(AddShopFormActivity.EXTRA_ID));

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

                shopId = storage.getShopByDesc(shopSearchView.getText().toString().trim()).getmShopId();
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

                        timeInMills = System.currentTimeMillis()%10000;

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
