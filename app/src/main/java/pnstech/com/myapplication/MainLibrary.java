package pnstech.com.myapplication;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pnstech.myapplication.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainLibrary extends AppCompatActivity {

    private Toolbar mtoolbar;
    private BottomNavigationView bottomNavigationItemView;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_library);

        //creating side three dot menu
        mtoolbar = (Toolbar) findViewById(R.id.toolbarx);
        bottomNavigationItemView = findViewById(R.id.navigation_view);

        mtoolbar.inflateMenu(R.menu.menu_main);

        //test



        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.about:
                        startActivity(new Intent(MainLibrary.this, About.class));
                        break;

                                        }
                return true;
            }
        });



        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        Toast.makeText(getApplicationContext(), "No notifications yet", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.home:
                        startActivity(new Intent(MainLibrary.this, DashBoard.class));
                        break;

                    case R.id.settings:
                        startActivity(new Intent(MainLibrary.this, Settings.class));
                        break;
                }
                return true;
            }
            });

        setUpToolbarMenu(); //enabling three dot menu

        initImageBitmaps();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        //remaining methods
        return super.onCreateOptionsMenu(menu);

    }

    // getting images from internet
    private void initImageBitmaps()
    {
        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/81SBy9jbbHL.jpg");
        mNames.add("The Water Cure");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/9781616208882.jpg");
        mNames.add("Suger Run");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/A1ydJm-AvL.jpg");
        mNames.add("Mouth Of Birds");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/ceb5e9fa5fd3e96f18051f1baf16c62b.jpg");
        mNames.add("Looker");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/A1IHoBxfbGL.jpg");
        mNames.add("Hide and Seek");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/71OAdjrRd8L.jpg");
        mNames.add("Thick");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/Dh0N78JWAAItEWY.jpg");
        mNames.add("Mothers");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/the-cold-is-in-her-bones-9781481488440_hr.jpg");
        mNames.add("The cold");

        mImageUrls.add("https://lithub.com/wp-content/uploads/2019/01/81QwY3R3FVL.jpg");
        mNames.add("The Shadow");

        initRecyclerView();
    }


    // for recycler view
    private void initRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames,  mImageUrls);
        recyclerView.setAdapter(adapter);
    }


    //setting side three dot menu
    private void setUpToolbarMenu()
    {
        mtoolbar = findViewById(R.id.toolbarx);
        mtoolbar.setTitle("Library");
    }

}
