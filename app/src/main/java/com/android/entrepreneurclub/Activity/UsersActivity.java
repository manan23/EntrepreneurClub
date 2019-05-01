package com.android.entrepreneurclub.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.entrepreneurclub.Models.Users;
import com.android.entrepreneurclub.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private RecyclerView mUsersList,minvestorList;
    FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter,firebaseRecyclerAdapter1;
    private DatabaseReference mUsersDatabase;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mLayoutManager = new LinearLayoutManager(this);

        mUsersList = (RecyclerView) findViewById(R.id.entreprenuer_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);


        minvestorList = (RecyclerView) findViewById(R.id.investor_list);
        minvestorList.setHasFixedSize(true);
        minvestorList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

       user_entreprenuer();
       user_investor();
       firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter1.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
        firebaseRecyclerAdapter1.startListening();

    }
/*
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, Users users, int position) {

                usersViewHolder.setDisplayName(users.getName());
                usersViewHolder.setUserStatus(users.getStatus());
                usersViewHolder.setUserImage(users.getThumb_image(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(UsersActivity.this, OtherProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }

*/


public void user_entreprenuer()
{
    Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("type").equalTo("Entreprenuer");

    FirebaseRecyclerOptions<Users> options =
            new FirebaseRecyclerOptions.Builder<Users>()
                    .setQuery(query, Users.class)
                    .build();
    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options)


    {
        @NonNull
        @Override
        public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.users_single_layout, parent, false);
            return new UsersViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position, @NonNull Users users) {
            final String user_id = getRef(position).getKey();


            usersViewHolder.setDisplayName(users.getName());
            usersViewHolder.setUserStatus(users.getStatus());
            usersViewHolder.setUserImage(users.getThumb_image(), getApplicationContext());


            usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent profileIntent = new Intent(UsersActivity.this, OtherProfileActivity.class);
                    profileIntent.putExtra("user_id", user_id);
                    startActivity(profileIntent);

                }
            });

        }

    };


    mUsersList.setAdapter(firebaseRecyclerAdapter);
}
public void user_investor()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("type").equalTo("Investor");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
        firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options)


        {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout, parent, false);
                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position, @NonNull Users users) {
                final String user_id = getRef(position).getKey();


                usersViewHolder.setDisplayName(users.getName());
                usersViewHolder.setUserStatus(users.getStatus());
                usersViewHolder.setUserImage(users.getThumb_image(), getApplicationContext());


                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(UsersActivity.this, OtherProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });

            }

        };


        minvestorList.setAdapter(firebaseRecyclerAdapter1);
    }

public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }


    }

}
