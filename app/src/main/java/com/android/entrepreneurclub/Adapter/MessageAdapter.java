package com.android.entrepreneurclub.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.entrepreneurclub.Activity.ChatActivity;
import com.android.entrepreneurclub.Models.Messages;
import com.android.entrepreneurclub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> mMessageList ;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
  Context mctx;
    public MessageAdapter(List<Messages> mMessageList, ChatActivity ctx){
        this.mMessageList=mMessageList;
        this.mctx=ctx;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent,false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView textMessage,textMessageReceiver,senderTime,receiverTime;
        CircleImageView profileImage;
        LinearLayout receiverLayout,senderLayout;
        public MessageViewHolder(View itemView) {
            super(itemView);

            textMessage= (TextView) itemView.findViewById(R.id.text_message);
            profileImage= (CircleImageView) itemView.findViewById(R.id.message_display_image);
            textMessageReceiver = (TextView) itemView.findViewById(R.id.text_message_receiver);
            receiverLayout = (LinearLayout) itemView.findViewById(R.id.layout_receiver);
            senderLayout = (LinearLayout) itemView.findViewById(R.id.layout_sender);
            senderTime =  (TextView) itemView.findViewById(R.id.text_message_time);
            receiverTime =  (TextView) itemView.findViewById(R.id.text_message_time_receiver);

        }



    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {

        Messages message = mMessageList.get(position);
        DatabaseReference dbrefUser = FirebaseDatabase.getInstance().getReference().child("Users");

        String currentUserid = mAuth.getCurrentUser().getUid();
        String from_userId = message.getFrom();

        if(from_userId.equals(currentUserid)){
            holder.receiverLayout.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.GONE);
            //holder.textMessage.setVisibility(View.VISIBLE);
            //holder.textMessageReceiver.setVisibility(View.GONE);
            holder.senderLayout.setVisibility(View.VISIBLE);
            holder.textMessage.setText(message.getMessage());
            holder.senderTime.setText( message.getTime());

        }
        else{
            // holder.textMessage.setVisibility(View.GONE);
            holder.profileImage.setVisibility(View.VISIBLE);
            //holder.textMessageReceiver.setVisibility(View.VISIBLE);
            holder.senderLayout.setVisibility(View.GONE);
            holder.receiverLayout.setVisibility(View.VISIBLE);
            holder.textMessageReceiver.setText(message.getMessage());
            dbrefUser.child(from_userId).child("thumbnail").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  //  String profileImg = dataSnapshot.getValue().toString();
                    //Picasso.with(mctx).load(profileImg).networkPolicy(NetworkPolicy.OFFLINE).into(holder.profileImage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.receiverTime.setText(message.getTime());

        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}
