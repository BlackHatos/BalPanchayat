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

public class DonarAdapter  extends RecyclerView.Adapter<DonarAdapter.DonarViewHolder>{

    private Context mContext;
    private ArrayList<ReturnDonarTags> mList;


    public DonarAdapter(Context mContext, ArrayList<ReturnDonarTags> mList)
    {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public DonarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.donar_scroll, parent, false);
        return  new DonarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DonarViewHolder holder, int position)
    {

        ReturnDonarTags currentTag = mList.get(position);

        String donationId = currentTag.getDonationID();
        String donarName = currentTag.getDonarName();
        String donationDate = currentTag.getDonationDate();
        String donationAmmount = currentTag.getDonationAmmount();


        holder.donation_id.setText(donationId);
        holder.donation_date.setText(donationDate);
        holder.donation_ammount.setText("Donated Rs. "+donationAmmount+"/-");
        holder.donar_name.setText(donarName);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class DonarViewHolder  extends  RecyclerView.ViewHolder{

        public TextView donar_name;
        public TextView donation_id;
        public TextView donation_date;
        public TextView donation_ammount;
        
        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public DonarViewHolder(@NonNull View itemView) {
            super(itemView);

            donar_name = itemView.findViewById(R.id.donar_name);
            donation_date = itemView.findViewById(R.id.donation_date); //notification id
            donation_ammount = itemView.findViewById(R.id.donation_ammount);
            donation_id  = itemView.findViewById(R.id.donation_id);

        }
    }

}
