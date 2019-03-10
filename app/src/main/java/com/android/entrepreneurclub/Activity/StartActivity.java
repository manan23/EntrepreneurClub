package com.android.entrepreneurclub.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.entrepreneurclub.Entrepreneur.entrepreneur_dashboard;
import com.android.entrepreneurclub.Investor.Investor_Dashboard;
import com.android.entrepreneurclub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn;
    private Button mLoginBtn;
    private ProgressDialog mLoginProgress;

    private DatabaseReference mUserRef;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mRegBtn = (Button) findViewById(R.id.start_reg_btn);
        mLoginBtn = (Button) findViewById(R.id.start_login_btn);

        mLoginProgress = new ProgressDialog(this);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login_intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(login_intent);

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mLoginProgress.setTitle("Logging In");
        mLoginProgress.setMessage("Please wait while we check your credentials.");
        mLoginProgress.setCanceledOnTouchOutside(false);
        mLoginProgress.show();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (auth.getCurrentUser() != null) {



            FirebaseUser user = auth.getCurrentUser();
            final String uid = user.getUid();
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

            mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String type = dataSnapshot.child("type").getValue().toString().trim();
                    if (type.equals("Investor")) {
                        mLoginProgress.dismiss();
                        mUserRef.child("online").setValue(true);
                        startActivity(new Intent(StartActivity.this, Investor_Dashboard.class));
                        finish();
                    } else {
                        mLoginProgress.dismiss();
                        mUserRef.child("online").setValue(true);
                        startActivity(new Intent(StartActivity.this, entrepreneur_dashboard.class));
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mLoginProgress.hide();
                    Toast.makeText(StartActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            mLoginProgress.dismiss();

        }
        }






}
