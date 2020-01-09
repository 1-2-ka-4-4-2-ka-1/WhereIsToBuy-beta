package com.findViewById.tiwari.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ImageView mLogoImage;
    private Button mLoginButton;
    private EditText mLoginField;
    private EditText mPasswordField;
    private TextView mForgotPasswordLabel;


    public static Activity activity_login;


    public static final String SAVED_PASS = "saved_password";
    public static final String USER_ID = "user_id";
    public static final String USER_PASS = "user_pass";
    public static final String LOGGED_IN = "logged_in";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor login_sfeditor;


    FirebaseDatabase databaseFirebase;
    DatabaseReference usersRef;
    //DatabaseReference shopItemsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        databaseFirebase = FirebaseDatabase.getInstance();
        usersRef = databaseFirebase.getReference("users");



        sharedPreferences = getSharedPreferences(SAVED_PASS, MODE_PRIVATE);




        mLogoImage = findViewById(R.id.id_logo_login);
        mLoginField = findViewById(R.id.id_ed_loginField);
        mPasswordField = findViewById(R.id.id_ed_passwordField);
        mLoginButton = findViewById(R.id.loginButton);
        mForgotPasswordLabel = findViewById(R.id.id_tv_forgot_pass);

        activity_login = this;


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.setClickable(false);

                if(!(mLoginField.getText().toString().trim().equals("") ||  mPasswordField.getText().toString().trim().equals("") ))
                doSignIn(mLoginField.getText().toString(), mPasswordField.getText().toString());

                else
                    Toast.makeText(LoginActivity.this, "Please fill required Fields", Toast.LENGTH_LONG).show();

            }
        });

        mForgotPasswordLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doForgotPassword();
            }
        });
    }


    public void doSignIn(final String loginId, final String password) {

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(loginId).exists()) {


                    if (!loginId.trim().isEmpty()) {

                        String id = dataSnapshot.child(loginId).child("username").getValue().toString();
                        String pass = dataSnapshot.child(loginId).child("password").getValue().toString();

                        UserModel login = new UserModel(id, pass);


                        if (login.getmPassword().equals(password)) {

                            Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            login_sfeditor = sharedPreferences.edit();
                            login_sfeditor.putBoolean(LOGGED_IN, true);
                            login_sfeditor.putString(USER_ID, id);
                            login_sfeditor.putString(USER_PASS, pass);
                            login_sfeditor.apply();
                            login_sfeditor.commit();


                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
                            mLoginButton.setClickable(true);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No user Found", Toast.LENGTH_LONG).show();
                    mLoginButton.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mLoginButton.setClickable(true);
            }
        });
    }


    public void doForgotPassword() {

        final AlertDialog.Builder mDialogueBuilder = new AlertDialog.Builder(LoginActivity.this);

        AlertDialog mForgotPasswordDialogue = mDialogueBuilder.setMessage(" You can call Admin to Recover \n   9030770456 ")
                               .setTitle("Forgot Password")
                               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       return ;
                                   }
                               }).setCancelable(false)
                               .create();

        mForgotPasswordDialogue.show();
    }


}