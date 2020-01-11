package com.findViewById.tiwari.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DashBoardActivity extends AppCompatActivity {

    private ImageView mCreateNewBill;
    private ImageView mSendAllBils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        mCreateNewBill = findViewById(R.id.iv_create_new_bill);
        mSendAllBils = findViewById(R.id.iv_send_all_bills);

        mCreateNewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCreateNewBill();
            }
        });

        mSendAllBils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendAllBills();
            }
        });
    }

    public  void doCreateNewBill(){
        Intent intent = new Intent(DashBoardActivity.this,MainActivity.class);
        startActivity(intent);
    }


    public void doSendAllBills(){
        Intent intent = new Intent(DashBoardActivity.this, CheckSendBillsActivity.class);
        startActivity(intent);
    }

}
