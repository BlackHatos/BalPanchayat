package com.pnstech.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pnstech.com.myapplication.R;

public class TeamAdapter  extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder>{

    private Context mContext;
    private ArrayList<ReturnTeamTags> mList;

    public TeamAdapter(Context mContext, ArrayList<ReturnTeamTags> mList)
    {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.team_scroll, parent, false);
        return  new TeamViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, final int position) {
        ReturnTeamTags currentTag = mList.get(position);
        String imageUrl = currentTag.getImageUrl(); //return image url
        String memberName = currentTag.getMemberName();
        String  positionName = currentTag.getMemberPosition();
        final String memberId  = currentTag.getMemberId();
        String memberPhone = currentTag.getMemberPhone();


        holder.member_name.setText(memberName);
        holder.position_name.setText(positionName);
        holder.member_id.setText(memberId);
        holder.member_phone.setText("Contact: +91-"+memberPhone);

        //image fixing

        Glide.with(mContext)
                .load(imageUrl)
                .fitCenter()
                .centerInside()
                .into(holder.pic);

        holder.delete_memeber.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("press OK to confirm")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteCardZ(memberId,position);
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

    public void filterList(ArrayList<ReturnTeamTags> filteredList)
    {
        mList = filteredList;
        notifyDataSetChanged();
    }

    public class TeamViewHolder  extends  RecyclerView.ViewHolder{

        public ImageView pic;
        public  TextView member_name;
        public TextView position_name;
        public TextView member_id;
        public TextView member_phone;
        public TextView delete_memeber;

        public TeamViewHolder(@NonNull View itemView)
        {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            member_name = itemView.findViewById(R.id.member_name);
            position_name = itemView.findViewById(R.id.position_name);
            member_id = itemView.findViewById(R.id.member_id);
            member_phone = itemView.findViewById(R.id.member_phoe);
            delete_memeber =  itemView.findViewById(R.id.delete_member);

            if(pnstech.com.myapplication.Team.USER_TYPEY.equals("1"))
            {
                delete_memeber.setVisibility(View.VISIBLE);
            }

        }
    }

    public void deleteCardZ(final String idx, final int position)
    {
        String url = "https://www.iamannitian.co.in/test/delete_member.php";
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
