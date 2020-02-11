package com.findViewById.tiwari.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    private ImageView mCreateNewBill;
    private ImageView mSendAllBils;
    private ImageView mNotification;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        //Screen Orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mCreateNewBill = findViewById(R.id.iv_create_new_bill);
        mSendAllBils = findViewById(R.id.iv_send_all_bills);
        mNotification = findViewById(R.id.iv_notification_icon);

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

        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(DashBoardActivity.this,NotificationsActivity.class);
               startActivity(intent);

            }
        });



//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference dataReference = firebaseDatabase.getReference("admin");
//        DatabaseReference  notificationRef = dataReference.child("notification").getRef();
//        final List<String> mNotificationsList = new ArrayList<>();
//        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                 if(dataSnapshot.getChildrenCount()>0){
//                    mNotification.setImageResource(R.drawable.ic_notifications_active);
//                }else {
//                    mNotification.setImageResource(R.drawable.ic_notifications_inactive);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });


    }

    @Override
    protected void onResume() {
        super.onResume();



        sharedPreferences = getSharedPreferences("saved_password",MODE_PRIVATE);
        String userId= sharedPreferences.getString("user_id","none");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataReference = firebaseDatabase.getReference("data");
        DatabaseReference  notificationRef = dataReference.child("salesmen_"+userId).child("notifications").getRef();
        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    mNotification.setImageResource(R.drawable.ic_notifications_active);
                }else {
                    mNotification.setImageResource(R.drawable.ic_notifications_inactive);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SplashScreenActivity.activity_splash.finish();
    }
}
