package pnstech.com.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pnstech.myapplication.BookRequestAdapter;
import com.pnstech.myapplication.ReturnBookRequestTags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private RecyclerView recyclerView;
    private BookRequestAdapter requestAdapter;
    private ArrayList<ReturnBookRequestTags> mList;
    private RequestQueue requestQueue;


    private Toolbar mtoolbar;
    private DrawerLayout mdrawerLayout;

    private LinearLayout search_layout;
    private LinearLayout back_space;

    private EditText search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        recyclerView =  findViewById(R.id.request_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

         search_layout = findViewById(R.id.search_layout);
         back_space = findViewById(R.id.back_space);
         search_view = findViewById(R.id.search_view);

        mtoolbar = findViewById(R.id.toolbarx);

        mtoolbar.inflateMenu(R.menu.menu_main);

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                 @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.setting:
                       startActivity(new Intent(Admin.this,Settings.class));
                        break;

                    case R.id.about:
                        startActivity(new Intent(Admin.this,About.class));
                        break;

                    case R.id.search:
                        mtoolbar.setVisibility(GONE); //hides title bar
                        search_layout.setVisibility(VISIBLE); //show search bar
                        break;
                        }
                return true;
            }
        });

        //test successful

      setUpToolbarMenu();
      setUpNavigationDrawerMenu();


        back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_layout.setVisibility(GONE);
                mtoolbar.setVisibility(VISIBLE);
            }
        });


        search_view.addTextChangedListener(new TextWatcher() {
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


        mList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();

    }


    private void filter(String text)
    {
        ArrayList<ReturnBookRequestTags> filteredList = new ArrayList<>();

        if(mList.size() != 0)  //if size is empty then app will crash on searching
        {
            for (ReturnBookRequestTags item : mList) {
                if (item.getBookNamex().toLowerCase().contains(text.toLowerCase()) || item.getUserNamex().toLowerCase().contains(text.toLowerCase()) ||
                        item.getUserDistrictx().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            requestAdapter.search(filteredList);
        }
    }


    private void setUpToolbarMenu() {
        mtoolbar = findViewById(R.id.toolbarx);
        mtoolbar.setTitle("BalPanchayat");
    }




    private void setUpNavigationDrawerMenu() {
        NavigationView navigationView = findViewById(R.id.navigation_id);
        navigationView.setNavigationItemSelectedListener(this);
        mdrawerLayout = findViewById(R.id.drawer_id);
        ActionBarDrawerToggle actionbar_drawer_toogle = new ActionBarDrawerToggle(this, mdrawerLayout, mtoolbar, R.string.drawer_open, R.string.drawer_close);
        mdrawerLayout.addDrawerListener(actionbar_drawer_toogle);
        actionbar_drawer_toogle.syncState();
    }

    private void closeDrawer() {
        mdrawerLayout.closeDrawer(GravityCompat.START); //START can be replaced by END depending upon pupose
    }
    //open drawer

    private void showDrawer() {
        mdrawerLayout.openDrawer(GravityCompat.START);
    }

    public void onBackPress() {
        if (mdrawerLayout.isDrawerOpen(GravityCompat.START))
            closeDrawer();
        else
            super.onBackPressed();
    }

     @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        closeDrawer();
        switch (menuItem.getItemId()) {
            case R.id.send_notify:
                startActivity(new Intent(Admin.this,SendNotification.class));
                break;

            case R.id.add_donar:
                startActivity(new Intent(Admin.this, AddDoner.class));
                break;

            case R.id.add_member:
                startActivity(new Intent(Admin.this, AddTeamMember.class));
                break;

            case R.id.book_upload:
                startActivity(new Intent(Admin.this, UploadBooks.class));
                break;

        }
        return true;

    }

    private void parseJson()
    {

       String url_n = "https://iamannitian.co.in/test/book_requester.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_n, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("request_data");

                           for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String bookIdx = data.getString("requested_book_id");
                                String bookNamex = data.getString("book_name");
                                String  userIdx = data.getString("id");
                                String  userNamex = data.getString("name");
                                String  userDistx = data.getString("district");
                                String  userPhonex = data.getString("phone");
                                String  requestDatex = data.getString("request_date");
                                String  approveDatex = data.getString("approve_date");
                                String  issueDatex = data.getString("issue_date");
                                String  requested_copy = data.getString("requested_copy");
                                String  num_copy = data.getString("num_copy");

                                //checking isApproved  and isReturned

                                String isApprovedx = data.getString("is_approved");
                                String isIssuedx  = data.getString("is_issued");

                                String actualBookIdx = data.getString("actual_book_id");

                                mList.add(new ReturnBookRequestTags(bookIdx, bookNamex, userIdx, userNamex,
                                        userDistx, userPhonex, requestDatex,num_copy,
                                        requested_copy,isApprovedx,isIssuedx, actualBookIdx, approveDatex, issueDatex ));
                            }


                            requestAdapter = new BookRequestAdapter(Admin.this,mList);
                            recyclerView.setAdapter(requestAdapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener(){
            @Override

            public void onErrorResponse(VolleyError error)
            {

            }
        });

        requestQueue.add(request);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
