package com.findViewById.tiwari.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditBillActivity extends AppCompatActivity {

    public TextView mitem_id_label;
    public EditText mitem_desc;
    public TextView  mitem_unit;
    public EditText  mitem_rate;
    public EditText  mitem_qty;
    public TextView  mitem_amount;


    private FloatingActionButton msave_edit;


    public static final String EXTRA_ID = "com.telitel.tiwari.architecturecomponents.EXTRA_ID";
    public static final String EXTRA_RATE = "com.telitel.tiwari.architecturecomponents.EXTRA_RATE";
    public static final String EXTRA_DESC = "com.telitel.tiwari.architecturecomponents.EXTRA_DESC";
    public static final String EXTRA_AMOUNT = "com.telitel.tiwari.architecturecomponents.EXTRA_AMOUNT";
    public static final String EXTRA_QTY = "com.telitel.tiwari.architecturecomponents.EXTRA_QTY";
    public static final String EXTRA_UNIT = "com.telitel.tiwari.architecturecomponents.EXTRA_UNIT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bill);

        mitem_id_label= findViewById(R.id.tv_item_id);
        mitem_desc=findViewById(R.id.ed_description);
        mitem_unit=findViewById(R.id.tv_unit);
        mitem_rate=findViewById(R.id.ed_rate);
        mitem_qty=findViewById(R.id.ed_quantity);
        mitem_amount=findViewById(R.id.tv_subAmount);

        msave_edit=findViewById(R.id.fb_save_edit_bill);

        msave_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveBill();
            }
        });



        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");

            mitem_id_label.setText(intent.getStringExtra(EXTRA_ID));
            mitem_rate.setText(intent.getStringExtra(EXTRA_RATE));
            mitem_desc.setText(intent.getStringExtra(EXTRA_DESC));
            mitem_amount.setText(intent.getStringExtra(EXTRA_AMOUNT));
            mitem_qty.setText(intent.getStringExtra(EXTRA_QTY));
            mitem_unit.setText(intent.getStringExtra(EXTRA_UNIT));


        }


    }



    public void doSaveBill() {

         //String item_id_label = mitem_id_label.getText().toString();
         String item_desc =  mitem_desc.getText().toString();
         String item_unit = mitem_unit.getText().toString();;
         double item_rate = Double.parseDouble(mitem_rate.getText().toString());
         double item_qty = Double.parseDouble(mitem_qty.getText().toString());
         double item_amount = Double.parseDouble(mitem_amount.getText().toString());

        try {
            if (item_desc.trim().isEmpty() || item_rate == 0 || item_qty == 0) {
                Toast.makeText(this, "CanNot Be Empty", Toast.LENGTH_SHORT);
                return;
            }
        }catch (Exception e){
            Toast.makeText(EditBillActivity.this,"Make sure fields are correct",Toast.LENGTH_SHORT);
        }

        Intent data = new Intent();
        //data.putExtra(EXTRA_ID, item_id_label);
        data.putExtra(EXTRA_DESC, item_desc);
        data.putExtra(EXTRA_UNIT, item_unit);
        data.putExtra(EXTRA_RATE, item_rate);
        data.putExtra(EXTRA_QTY, item_qty);
        data.putExtra(EXTRA_AMOUNT, item_amount);


        int id = getIntent().getIntExtra(EXTRA_ID,1);
        if(id!= -1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
    }




}
