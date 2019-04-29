package com.android.entrepreneurclub.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entrepreneurclub.Activity.OtherProfileActivity;
import com.android.entrepreneurclub.Models.Users;
import com.android.entrepreneurclub.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {


    private RecyclerView mRequestList,msentList;

    private DatabaseReference mRequestDatabase;
    private DatabaseReference mUsersDatabase;
    DatabaseReference dbrefRoot = FirebaseDatabase.getInstance().getReference();
    FirebaseRecyclerAdapter<Users, RequestsFragment.UsersViewHolder> friendsRecyclerViewAdapter,friendsRecyclerViewAdapter1;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public String mCurrent_state = "not_friends";

    private View mMainView;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_requests, container, false);


        mRequestList = (RecyclerView) mMainView.findViewById(R.id.request_List);
        msentList=(RecyclerView) mMainView.findViewById(R.id.sent_List);


        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mRequestDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mRequestList.setHasFixedSize(true);
        mRequestList.setLayoutManager(new LinearLayoutManager(getContext()));

        msentList.setHasFixedSize(true);
        msentList.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Request_rec();
        friendsRecyclerViewAdapter.startListening();
        Request_sent();
        friendsRecyclerViewAdapter1.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        friendsRecyclerViewAdapter.stopListening();
        friendsRecyclerViewAdapter1.stopListening();
    }
/*
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, Friends friends, int i) {

                friendsViewHolder.setDate(friends.getDate());

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if (dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);

                        }

                        friendsViewHolder.setName(userName);
                        friendsViewHolder.setUserImage(userThumb, getContext());

                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if (i == 0) {

                                            Intent profileIntent = new Intent(getContext(), OtherProfileActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);

                                        }

                                        if (i == 1) {

                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            */

    public void Request_rec() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id).orderByChild("request_type").equalTo("received");
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
        friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @NonNull
            @Override
            public RequestsFragment.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_request_layout, parent, false);
                return new RequestsFragment.UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestsFragment.UsersViewHolder usersViewHolder, int i, @NonNull final Users user) {

                final String list_user_id = getRef(i).getKey();

                mRequestDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                String userName = dataSnapshot1.child("name").getValue().toString();
                                String userThumb = dataSnapshot1.child("thumb_image").getValue().toString();
                                Log.d("name ", "" + userName + " " + userThumb);
                                String userStatus = dataSnapshot1.child("status").getValue().toString();


                                if (dataSnapshot1.hasChild("online")) {

                                    String userOnline = dataSnapshot1.child("online").getValue().toString();
                                    usersViewHolder.setUserOnline(userOnline);

                                }

                                usersViewHolder.setDisplayName(userName);
                                usersViewHolder.setUserStatus(userStatus);
                                usersViewHolder.setUserImage(userThumb, getContext());

                                usersViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder bd = new AlertDialog.Builder(getContext());
                                        bd.setTitle("Cancel Request").setMessage("Are you sure you want to cancel the request ?");
                                        bd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Map remove = new HashMap();
                                                remove.put("Friend_req/" + mCurrent_user_id + "/" + list_user_id, null);
                                                remove.put("Friend_req/" + list_user_id + "/" + mCurrent_user_id, null);
                                                dbrefRoot.updateChildren(remove, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        if (databaseError == null) {
                                                            Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                });
                                            }
                                        });
                                        bd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        AlertDialog ad = bd.create();
                                        ad.show();
                                    }
                                });
                                usersViewHolder.check.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                                        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                                        Map friendsMap = new HashMap();
                                                        friendsMap.put("Friends/" + mCurrent_user_id + "/" + list_user_id + "/date", currentDate);
                                                        friendsMap.put("Friends/" + list_user_id + "/" + mCurrent_user_id + "/date", currentDate);


                                                        friendsMap.put("Friend_req/" + mCurrent_user_id + "/" + list_user_id, null);
                                                        friendsMap.put("Friend_req/" + list_user_id + "/" + mCurrent_user_id, null);


                                                        dbrefRoot.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                                                if (databaseError == null) {
                                                                    mCurrent_state = "friends";
                                                                    OtherProfileActivity.mProfileSendReqBtn.setText("Unfriend this Person");

                                                                    OtherProfileActivity.mDeclineBtn.setVisibility(View.INVISIBLE);
                                                                    OtherProfileActivity.mDeclineBtn.setEnabled(false);
                                                                    HashMap<String, String> notifDetails = new HashMap<>();
                                                                    notifDetails.put("from", mCurrent_user_id);

                                                                    notifDetails.put("type", "confirmed");

                                                                    dbrefRoot.child("notifications").child(list_user_id).push().setValue(notifDetails);
                                                                } else {

                                                                    String error = databaseError.getMessage();

                                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();


                                                                }

                                                            }
                                                        });




                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent profileIntent = new Intent(getContext(), OtherProfileActivity.class);
                                profileIntent.putExtra("user_id", list_user_id);
                                startActivity(profileIntent);
                            }
                        });


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mRequestList.setAdapter(friendsRecyclerViewAdapter);
    }
    public void Request_sent()
    {
        Query query = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id).orderByChild("request_type").equalTo("sent");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
        friendsRecyclerViewAdapter1 = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {
            @NonNull
            @Override
            public RequestsFragment.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_request_layout, parent, false);
                return new RequestsFragment.UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestsFragment.UsersViewHolder usersViewHolder, int i, @NonNull final Users user) {

                final String list_user_id = getRef(i).getKey();

                mRequestDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                                String userName = dataSnapshot1.child("name").getValue().toString();
                                String userThumb = dataSnapshot1.child("thumb_image").getValue().toString();
                               // Log.d("name ", "" + userName + " " + userThumb);
                                String userStatus = dataSnapshot1.child("status").getValue().toString();


                                if (dataSnapshot1.hasChild("online")) {

                                    String userOnline = dataSnapshot1.child("online").getValue().toString();
                                    usersViewHolder.setUserOnline(userOnline);

                                }

                                usersViewHolder.setDisplayName(userName);
                                usersViewHolder.setUserStatus(userStatus);
                                usersViewHolder.setUserImage(userThumb, getContext());

                                usersViewHolder.cancel.setVisibility(View.INVISIBLE);
                                usersViewHolder.check.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent profileIntent = new Intent(getContext(), OtherProfileActivity.class);
                                profileIntent.putExtra("user_id", list_user_id);
                                startActivity(profileIntent);
                            }
                        });


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        msentList.setAdapter(friendsRecyclerViewAdapter1);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ImageButton cancel, check;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            cancel = mView.findViewById(R.id.cancel_req);
            check = mView.findViewById(R.id.check_req);

        }

        public void setDisplayName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_request_name);
            userNameView.setText(name);

        }

        public void setUserStatus(String status) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_request_status);
            userStatusView.setText(status);


        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_request_image);

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if (online_status.equals("true")) {

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }


}
