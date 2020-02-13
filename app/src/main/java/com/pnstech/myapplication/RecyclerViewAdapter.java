package com.pnstech.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import pnstech.com.myapplication.DashBoard;
import pnstech.com.myapplication.R;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private Context mContext;
    private ArrayList<ReturnTags> mList;

    //creating the card clicker
    private OnItemClickListener mListener;

    public interface  OnItemClickListener
    {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public RecyclerViewAdapter(Context mContext, ArrayList<ReturnTags> mList)
    {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.book_horizontal_scroll, parent, false);
        return  new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
            ReturnTags currentTag = mList.get(position);
            String imageUrl = currentTag.getImageUrl();
            String bookName = currentTag.getBookName();
            String bookWriter = currentTag.getBookWriter();
            String bookContributer  = currentTag.getBookContributer();
            String bookDate  = currentTag.getBookDate();
            final String bookId = currentTag.getbookId();

            holder.book_name.setText(bookName);
            holder.book_author.setText(bookWriter);
            holder.book_contributer.setText("Contributed by "+bookContributer);
            holder.book_date.setText(bookDate);
            holder.book_id.setText(bookId); //no need to do this

            //image fixing

           Glide.with(mContext)
                   .load(imageUrl)
                   .fitCenter()
                   .centerInside()
                   .into(holder.book_image);


           holder.delete_card.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                        deleteCard(bookId,position);
               }
           });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filterList(ArrayList<ReturnTags> filteredList)
    {
        mList = filteredList;
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder  extends  RecyclerView.ViewHolder{

        public ImageView book_image;
        public  TextView book_name;
        public TextView book_author;
        public TextView book_contributer;
        public TextView book_date;
        public TextView book_id;
        public TextView delete_card;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            book_image = itemView.findViewById(R.id.book_image);
            book_name = itemView.findViewById(R.id.book_name);
            book_author = itemView.findViewById(R.id.book_author);
            book_contributer = itemView.findViewById(R.id.book_contributer);
            book_date = itemView.findViewById(R.id.book_date);
            book_id = itemView.findViewById(R.id.book_id); //book id
            delete_card  =itemView.findViewById(R.id.delete_card);

            if(pnstech.com.myapplication.MainLibrary.USER_TYPE.equals("1"))
            {
                delete_card.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(mListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }


    public void deleteCard(final String idx, final int position)
    {
        String url = "https://www.iamannitian.co.in/test/delete_book.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {

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
        })
        {
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
