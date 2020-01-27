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
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            ReturnTags currentTag = mList.get(position);
            String imageUrl = currentTag.getImageUrl();
            String bookName = currentTag.getBookName();
            String bookWriter = currentTag.getBookWriter();
            String bookContributer  = currentTag.getBookContributer();
            String bookDate  = currentTag.getBookDate();
            String bookId = currentTag.getbookId();

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

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            book_image = itemView.findViewById(R.id.book_image);
            book_name = itemView.findViewById(R.id.book_name);
            book_author = itemView.findViewById(R.id.book_author);
            book_contributer = itemView.findViewById(R.id.book_contributer);
            book_date = itemView.findViewById(R.id.book_date);
            book_id = itemView.findViewById(R.id.book_id); //book id


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

}
