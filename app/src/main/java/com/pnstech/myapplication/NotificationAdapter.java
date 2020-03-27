package com.pnstech.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pnstech.com.myapplication.Notification;
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
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position)
    {

        ReturnNotificationTags currentTag = mList.get(position);

        final String notificationId = currentTag.getNotificationId();
        String notificationText = currentTag.getNotificationText();
        String notificationDate = currentTag.getNotificationDate();

        holder.notification_id.setText(notificationId);
        holder.notification_date.setText(notificationDate);
        holder.notification_text.setText(notificationText);

        holder.delete_notify.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("press OK to confirm")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteCardx(notificationId,position);
                            }
                        }).show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NotificationViewHolder  extends  RecyclerView.ViewHolder{

        public TextView notification_text;
        public TextView notification_id;
        public TextView notification_date;
        public TextView delete_notify;
        private Typeface myfont;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            notification_text = itemView.findViewById(R.id.notification_text);
            notification_id = itemView.findViewById(R.id.notification_id); //notification id
            notification_date = itemView.findViewById(R.id.notification_date);
            delete_notify = itemView.findViewById(R.id.delete_notify);

            /*myfont= Typeface.createFromAsset(mContext.getAssets(),"fonts/ArimaMaduraiRegular.otf");
            notification_text.setTypeface(myfont);*/

            if(pnstech.com.myapplication.Notification.USER_TYPEX.equals("1"))
            {
                delete_notify.setVisibility(View.VISIBLE);
            }

        }
    }

    public void deleteCardx(final String idx, final int position)
    {

        String url = "https://www.iamannitian.co.in/test/delete_notify.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1"))
                        {
                            mList.remove(position);
                            notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(mContext,"failed to delete", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("idKey",idx);
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(mContext);
        rq.add(sr);
    }

}
