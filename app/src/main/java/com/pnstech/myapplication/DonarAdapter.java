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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void onBindViewHolder(@NonNull DonarViewHolder holder, final int position)
    {

        ReturnDonarTags currentTag = mList.get(position);

        final String donationId = currentTag.getDonationID();
        String donarName = currentTag.getDonarName();
        String donationDate = currentTag.getDonationDate();
        String donationAmmount = currentTag.getDonationAmmount();

        holder.donation_id.setText(donationId);
        holder.donation_date.setText(donationDate);
        holder.donation_ammount.setText("Donated Rs. "+donationAmmount+"/-");
        holder.donar_name.setText(donarName);

        holder.delete_donar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {



                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("press OK to confirm")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteCardk(donationId,position);
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

    public class DonarViewHolder  extends  RecyclerView.ViewHolder{

        public TextView donar_name;
        public TextView donation_id;
        public TextView donation_date;
        public TextView donation_ammount;
        public TextView delete_donar;
        
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
            delete_donar = itemView.findViewById(R.id.delete_donar);

            if(pnstech.com.myapplication.Donars.USER_TYPEZ.equals("1"))
            {
                delete_donar.setVisibility(View.VISIBLE);
            }

        }
    }

    public void deleteCardk(final String idx, final int position)
    {
       String url = "https://www.iamannitian.co.in/test/delete_donar.php";
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
