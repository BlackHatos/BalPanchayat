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

import java.util.ArrayList;

import pnstech.com.myapplication.R;

public class HorizontalRecyclerAdapter  extends RecyclerView.Adapter<HorizontalRecyclerAdapter.ViewHolder>{

    private static final  String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUris = new ArrayList<>();
    private Context mContext;

    public HorizontalRecyclerAdapter(Context context,ArrayList<String> names, ArrayList<String> imageUris)
    {
        mNames = names;
        mImageUris = imageUris;
        mContext = context;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_horizontal_scroll,parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Glide.with(mContext)
                .asBitmap()
                .load(mImageUris.get(position))
                .into(holder.image);

        holder.text.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on an image " +mNames.get(position));
                Toast.makeText(mContext,mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    //inner class

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.book_image);
            text = itemView.findViewById(R.id.book_name);

        }
    }

}
