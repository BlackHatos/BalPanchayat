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
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        ReturnTeamTags currentTag = mList.get(position);
        String imageUrl = currentTag.getImageUrl(); //return image url
        String memberName = currentTag.getMemberName();
        String  positionName = currentTag.getMemberPosition();
        String memberId  = currentTag.getMemberId();
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

        public TeamViewHolder(@NonNull View itemView)
        {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            member_name = itemView.findViewById(R.id.member_name);
            position_name = itemView.findViewById(R.id.position_name);
            member_id = itemView.findViewById(R.id.member_id);
            member_phone = itemView.findViewById(R.id.member_phoe);
        }
    }

}
