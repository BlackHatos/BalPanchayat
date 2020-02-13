package com.pnstech.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

import pnstech.com.myapplication.Admin;
import pnstech.com.myapplication.DashBoard;
import pnstech.com.myapplication.Notification;
import pnstech.com.myapplication.R;
import pnstech.com.myapplication.RegisterActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BookRequestAdapter  extends RecyclerView.Adapter<BookRequestAdapter.BookRequestViewHolder>{

    private Context mContext;
    private ArrayList<ReturnBookRequestTags> mList;

      public BookRequestAdapter(Context mContext, ArrayList<ReturnBookRequestTags> mList)
    {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public BookRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.request_book_scroll, parent, false);
        return  new BookRequestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRequestViewHolder holder, final int position)
    {

        ReturnBookRequestTags currentTag = mList.get(position);

        final String bookIdy  = currentTag.getBookIdx();
        String bookNamey  = currentTag.getBookNamex();
        final String userIdy =  currentTag.getUserIdx();
        String userNamey = currentTag.getUserNamex();
        String userDistricty  = currentTag.getUserDistrictx();
        String userPhoney  = currentTag.getUserPhonex();
        String requestDatey = currentTag.getRequestDatex();
        String approveDatey = currentTag.getApproveDatex();
        String issueDatey = currentTag.getIssueDatex();


        //checking availability status
        String totalCopy = currentTag.getTotalCopyx();
        String requestedCopy = currentTag.getRequestedCopyx();

        //checking if approved or returned

        String isApproved = currentTag.getIsApprovedy();
        String isIssued = currentTag.getIsIssued();

        String actualBookId = currentTag.getActualBookId();


        if(Integer.parseInt(requestedCopy) <= Integer.parseInt(totalCopy))
        {
            holder.approve_button.setVisibility(VISIBLE);
            holder.issue_button.setVisibility(GONE);
            holder.return_button.setVisibility(GONE);
            holder.request_date.setText(requestDatey);

            holder.user_name.setText("Requested by "+userNamey);

            if(isApproved.equals("1"))
            {
                holder.approve_button.setVisibility(GONE);
                holder.issue_button.setVisibility(VISIBLE);
                holder.return_button.setVisibility(GONE);
                holder.request_date.setText(approveDatey);
            }

            if(isIssued.equals("1"))
            {
                holder.approve_button.setVisibility(GONE);
                holder.issue_button.setVisibility(GONE);
                holder.return_button.setVisibility(VISIBLE);
                holder.user_name.setText("Issued to "+userNamey);
                holder.actual_book_id.setText("ID: "+actualBookId); //actual book id
                holder.actual_book_id.setVisibility(VISIBLE);
                holder.request_date.setText(issueDatey);
            }
        }

        holder.user_id.setText(userIdy);
        holder.user_phone.setText(userPhoney);
        holder.user_district.setText(userDistricty);
        holder.book_id.setText(bookIdy);
        holder.book_name.setText("Book: "+bookNamey);


        holder.approve_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("press OK to confirm")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changeDetail(userIdy, bookIdy);
                            }
                        }).show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorPrimaryDark);


            }
        });


        holder.issue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                showPopup(view, userIdy);
            }
        });

        holder.return_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Are you sure?")
                        .setMessage("press OK to confirm")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                     returnBook(userIdy);
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


    //filter from the list

    public void search(ArrayList<ReturnBookRequestTags> filteredList)
    {
        mList = filteredList;
        notifyDataSetChanged();
    }


    public class BookRequestViewHolder  extends  RecyclerView.ViewHolder{

        public TextView book_id;
        public TextView book_name;
        public TextView request_date;
        public TextView user_id;
        public TextView user_name;
        public TextView user_district;
        public TextView user_phone;
        public Button approve_button;
        public Button issue_button;
        public Button return_button;
        public TextView actual_book_id;

        public BookRequestViewHolder(@NonNull View itemView) {
            super(itemView);

             book_id = itemView.findViewById(R.id.book_id);
             user_id = itemView.findViewById(R.id.user_id); //notification id
             request_date = itemView.findViewById(R.id.request_date);
             book_name= itemView.findViewById(R.id.book_name);
             user_district= itemView.findViewById(R.id.user_district);
             user_phone= itemView.findViewById(R.id.user_phone);
             user_name= itemView.findViewById(R.id.user_name);
             approve_button = itemView.findViewById(R.id.approve_button);
             issue_button = itemView.findViewById(R.id.issue_button);
             return_button = itemView.findViewById(R.id.return_button);
             actual_book_id = itemView.findViewById(R.id.actual_book_id);
        }
    }


    public void changeDetail(final String userId, final String bookId)
    {
        String url = "https://www.iamannitian.co.in/test/approval.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String response_array[] = response.split(",");

                        if(response_array[0].equals("1"))
                        {
                             mContext.startActivity(new Intent(mContext,Admin.class));
                            ((Admin) mContext).finish();
                        }
                        Toast.makeText(mContext,response_array[1],Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("bookIdKey",bookId);
                map.put("userIdKey",userId);
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(mContext);
        rq.add(sr);
    }

    public void showPopup(View view, final String userId)
    {
        final View popupView = LayoutInflater.from(mContext).inflate(R.layout.get_reward_popup, null);
        boolean focusable = false;
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);
        popupWindow.setAnimationStyle(R.style.windowAnimationTransition);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.showAtLocation(view , Gravity.CENTER,0,0);

         final EditText enter_book_id =  popupView.findViewById(R.id.enter_book_id);
         Button click_to_submit_id = popupView.findViewById(R.id.click_to_submit_id);

         click_to_submit_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String book_actual_id = enter_book_id.getText().toString().trim();
                if(!book_actual_id.equals(""))
                {
                    goToServer(enter_book_id.getText().toString().trim(),userId);
                }

            }
        });

    }

    public void goToServer(final String bookActualId, final String userId)
    {

       String url = "https://www.iamannitian.co.in/test/issue_book.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1"))
                        {
                            Toast.makeText(mContext,"book issued successfully",Toast.LENGTH_LONG).show();
                            mContext.startActivity(new Intent(mContext,Admin.class));
                            ((Admin) mContext).finish();
                        }
                        else
                        {
                            Toast.makeText(mContext,"failed to issue the book",Toast.LENGTH_LONG).show();
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
                map.put("bookIdKey",bookActualId);
                map.put("userIdKey",userId);
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(mContext);
        rq.add(sr);
    }


    public void returnBook(final String userId)
    {

        String url = "https://www.iamannitian.co.in/test/return_book.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1"))
                        {
                            Toast.makeText(mContext,"book returned successfully",Toast.LENGTH_LONG).show();
                            mContext.startActivity(new Intent(mContext,Admin.class));
                            ((Admin) mContext).finish();
                        }
                        else
                        {
                            Toast.makeText(mContext,"failed to return the book",Toast.LENGTH_LONG).show();
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
                map.put("userIdKey",userId);
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(mContext);
        rq.add(sr);
    }

}
