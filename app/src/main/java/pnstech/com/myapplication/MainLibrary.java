package pnstech.com.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ClipData;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pnstech.myapplication.RecyclerViewAdapter;
import com.pnstech.myapplication.ReturnTags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class MainLibrary extends AppCompatActivity  implements RecyclerViewAdapter.OnItemClickListener{

    //private Toolbar mtoolbar;
    private FloatingActionButton message_button;
    private SharedPreferences sharedPreferences;

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_BOOK_NAME = "bookName";
    public static final String EXTRA_BOOK_AUTHOR = "bookWriter";
    public static final String EXTRA_BOOK_DATE = "bookDate";
    public static final String EXTRA_BOOK_ID = "bookID";



    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<ReturnTags> mList;
    private RequestQueue requestQueue;


    //notification badge
    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;
    private TextView search_activity;

    public static String USER_TYPE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_library);



        recyclerView =  findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true); //recycler view dont change its width and height
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        mList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();

        bottomNavigationView = findViewById(R.id.navigation_view);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        removeBadge();
                        notificationBadge.setVisibility(GONE);
                        startActivity(new Intent(MainLibrary.this, pnstech.com.myapplication.Notification.class));
                        break;

                    case R.id.home: //clear all the previous taks
                        Intent intent = new Intent(MainLibrary.this, DashBoard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.settings:
                        startActivity(new Intent(MainLibrary.this, Settings.class));
                        break;

                    case R.id.profile:
                        startActivity(new Intent(MainLibrary.this, Profile.class));
                        break;
                }
                return true;
            }
            });

       showBadge();

       // getting user type
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        USER_TYPE = sharedPreferences.getString("userType","");


        search_activity = (EditText) findViewById(R.id.search_view);


        search_activity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


    }


    private void filter(String text)
    {
        ArrayList<ReturnTags> filteredList = new ArrayList<>();

       if(mList.size() != 0) //check if the list is empty (if empty then it will give error on search)
       {
           for (ReturnTags item : mList) //here mlist is filtered list
           {
               if (item.getBookName().toLowerCase().contains(text.toLowerCase()) || item.getBookWriter().toLowerCase().contains(text.toLowerCase())) {
                   filteredList.add(item);
               }
           }

           recyclerViewAdapter.filterList(filteredList);
       }
    }


    public void showBadge() //show notfication badge
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView,false);

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        String notifyCount = sharedPreferences.getString("notifyCount", "");

        TextView badge_count =  notificationBadge.findViewById(R.id.notify_count);

        if(!notifyCount.equals("0"))
            badge_count.setText(notifyCount);

        else
            notificationBadge.setVisibility(GONE);

        itemView.addView(notificationBadge);
    }

    public void removeBadge()
    {

        String url = "https://www.iamannitian.co.in/test/remove_badge.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1")) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("notifyCount",Integer.toString(0));
                            editor.apply();
                            notificationBadge.setVisibility(GONE);

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
                map.put("idKey",sharedPreferences.getString("userId",""));
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(MainLibrary.this);
        rq.add(sr);
    }



    private void parseJson()
    {
        String url_n = "https://iamannitian.co.in/test/recycler_view.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_n, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("book_data");

                           for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String bookIdx = data.getString("id");
                                String bookNamex = data.getString("book_name");
                                String writerNamex = data.getString("writer_name");
                                String datex = data.getString("datex");
                                String urlx = "https://iamannitian.co.in/test/book_covers/"+data.getString("url");

                                mList.add(new ReturnTags(urlx, bookNamex, writerNamex, datex, bookIdx));
                            }

                            recyclerViewAdapter = new RecyclerViewAdapter(MainLibrary.this,mList);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            recyclerViewAdapter.setOnItemClickListener(MainLibrary.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    }, new Response.ErrorListener(){
                    @Override

                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                         });

          requestQueue.add(request);
    }


    //opening card

    @Override
    public void onItemClick(int position) {
        Intent intent =  new Intent(this, OnBookClick.class);
        ReturnTags clickedItem =  mList.get(position);
        intent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        intent.putExtra(EXTRA_BOOK_NAME, clickedItem.getBookName());
        intent.putExtra(EXTRA_BOOK_AUTHOR, clickedItem.getBookWriter());
        intent.putExtra(EXTRA_BOOK_DATE, clickedItem.getBookDate());
        intent.putExtra(EXTRA_BOOK_ID, clickedItem.getbookId());
        startActivity(intent);
    }


}
