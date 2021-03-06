package com.android.entrepreneurclub.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entrepreneurclub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText name, number, city, pgender, age, Companyname, businessName;
    TextView Gender;
    Button Update;
    ImageView profileimage;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    private Toolbar mToolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = (Toolbar) findViewById(R.id.profile_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Profile");


        profileimage=findViewById(R.id.profile_image);
        name = (EditText) findViewById(R.id.profilename);
        number = (EditText) findViewById(R.id.profilenumber);
        city = (EditText) findViewById(R.id.profilecity);
        pgender = (EditText) findViewById(R.id.profilegender);
        age = (EditText) findViewById(R.id.profileAge);
        Companyname = findViewById(R.id.profileCompanyname);
        Update = findViewById(R.id.Update);
        Gender = findViewById(R.id.Gender);
        businessName = findViewById(R.id.profileBusinessCategory);


        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

         databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               final String image=dataSnapshot.child("image").getValue().toString().trim();
               String Pname = dataSnapshot.child("name").getValue().toString().trim();
               name.setText(Pname);
               pgender.setText(dataSnapshot.child("gender").getValue().toString().trim());
               number.setText(dataSnapshot.child("phone").getValue().toString().trim());
               Companyname.setText(dataSnapshot.child("company_name").getValue().toString().trim());
               businessName.setText(dataSnapshot.child("businessName").getValue().toString().trim());
               age.setText(dataSnapshot.child("age").getValue().toString().trim());
               city.setText(dataSnapshot.child("locality").getValue().toString().trim());


            /*   Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(profileimage, new Callback() {
                   @Override
                   public void onSuccess() {

                   }

                   @Override
                   public void onError() {
                       Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.default_avatar).into(profileimage);
                   }

               });
 */

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.EditProfile:
                Update.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setCursorVisible(true);
                age.setEnabled(true);
                Companyname.setEnabled(true);
                number.setEnabled(true);
                city.setEnabled(true);
                Gender.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                pgender.setBackgroundColor(getResources().getColor(R.color.md_grey_300));
                pgender.setTextColor(Color.GRAY);






                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name1 = name.getText().toString();
                        String address = city.getText().toString();
                        String phone = number.getText().toString();
                        String age1 = age.getText().toString();
                        String companyname=Companyname.getText().toString();

                        Map info = new HashMap();
                        info.put("name", name1);
                        info.put("locality", address);
                        info.put("phone", phone);
                        info.put("age", age1);
                        info.put("company_name",companyname);

                      /* docref.update(info).addOnCompleteListener(ProfileActivity.this, new OnCompleteListener() {
                           @Override
                           public void onComplete(@NonNull Task task) {
                              if(task.isSuccessful()) {
                                  Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                  Update.setVisibility(View.GONE);
                                  name.setEnabled(false);
                                  name.setCursorVisible(false);
                                  city.setEnabled(false);
                                  number.setEnabled(false);
                                  age.setEnabled(false);
                                  Gender.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                  pgender.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                  pgender.setTextColor(Color.BLACK);
                              }
                               else
                               {
                                   Toast.makeText(ProfileActivity.this, "Error Updating Profile", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });*/

                      databaseReference.updateChildren(info).addOnCompleteListener(new OnCompleteListener() {
                          @Override
                          public void onComplete(@NonNull Task task) {
                              if(task.isSuccessful()) {
                                  Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                  Update.setVisibility(View.GONE);
                                  name.setEnabled(false);
                                  name.setCursorVisible(false);
                                  city.setEnabled(false);
                                  number.setEnabled(false);
                                  age.setEnabled(false);
                                  Gender.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                  pgender.setBackgroundColor(getResources().getColor(R.color.md_white_1000));
                                  pgender.setTextColor(Color.BLACK);
                              }
                              else
                              {
                                  Toast.makeText(ProfileActivity.this, "Error Updating Profile", Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
                    }
                });
                break;
        }
        return true;
    }
}

