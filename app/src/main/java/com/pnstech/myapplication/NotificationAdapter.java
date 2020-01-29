package com.pnstech.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import pnstech.com.myapplication.R;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private Context mContext;
    private ArrayList<ReturnNotificationTags> mList;


    public NotificationAdapter(Context mContext, ArrayList<ReturnNotificationTags> mList)
    {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.notification_vertical_scroll, parent, false);
        return  new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position)
    {

        ReturnNotificationTags currentTag = mList.get(position);

        String notificationId = currentTag.getNotificationId();
        String notificationText = currentTag.getNotificationText();
        String notificationDate = currentTag.getNotificationDate();


        holder.notification_id.setText(notificationId);
        holder.notification_date.setText(notificationDate);
        holder.notification_text.setText(notificationText);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class NotificationViewHolder  extends  RecyclerView.ViewHolder{

        public TextView notification_text;
        public TextView notification_id;
        public TextView notification_date;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            notification_text = itemView.findViewById(R.id.notification_text);
            notification_id = itemView.findViewById(R.id.notification_id); //notification id
            notification_date = itemView.findViewById(R.id.notification_date);


        }
    }

}
