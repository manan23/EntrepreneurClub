package com.android.entrepreneurclub.Investor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.entrepreneurclub.Activity.ProfileActivity;
import com.android.entrepreneurclub.Activity.StartActivity;
import com.android.entrepreneurclub.Activity.UsersActivity;
import com.android.entrepreneurclub.R;
import com.android.entrepreneurclub.fragment.ChatsFragment;
import com.android.entrepreneurclub.fragment.FriendsFragment;
import com.android.entrepreneurclub.fragment.RequestsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class Investor_Dashboard extends AppCompatActivity {

    private TextView mTextMessage;
    private Toolbar mToolbar;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm  = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft.replace(R.id.content_doc,new RequestsFragment()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    ft.replace(R.id.content_doc,new ChatsFragment()).commit();
                    return true;
                case R.id.navigation_notifications:
                    ft.replace(R.id.content_doc,new FriendsFragment()).commit();
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor__dashboard);

        mToolbar = (Toolbar) findViewById(R.id.investor_dashboard_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Entrepreneur Club");
        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }
        else
        {
            sendToStart();
        }


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void sendToStart() {

        Intent startIntent = new Intent(Investor_Dashboard.this, StartActivity.class);
        startActivity(startIntent);
        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_investor_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.main_logout_btn) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if(item.getItemId() == R.id.main_all_btn){

            Intent settingsIntent = new Intent(Investor_Dashboard.this, UsersActivity.class);
            startActivity(settingsIntent);

        }
        if(item.getItemId() ==R.id.profile) {

            startActivity(new Intent(this, ProfileActivity.class));

        }
     return true;
    }

}
