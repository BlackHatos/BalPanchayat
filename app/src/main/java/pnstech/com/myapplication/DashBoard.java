package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoard extends AppCompatActivity {

    private TextView user_name;
    private ImageView user_image;
    private BottomNavigationView bottomNavigationItemView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        user_name = (TextView) findViewById(R.id.user_name);
        user_image = (ImageView) findViewById(R.id.user_image);

        bottomNavigationItemView = findViewById(R.id.navigation_view);


        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        Toast.makeText(getApplicationContext(), "No notifications yet", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.home:
                        //startActivity(new Intent(DashBoard.this, DashBoard.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(DashBoard.this, Settings.class));
                        break;
                }
                return true;
            }
        });


        getUserDataFromSharedPrefs();
    }



    /*=========================== shared preferences saving user data starts ============================*/

    private void getUserDataFromSharedPrefs() {
       SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        user_name.setText(sharedPreferences.getString("userName", ""));
    }


    /*=========================== shared preferences saving user data ends ============================*/


    //mthod to visit to profile

    public void goToProfile(View view){
        startActivity(new Intent(DashBoard.this,Profile.class));
    }

    //method to go to library

    public void goToLibrary(View view){
        startActivity(new Intent(DashBoard.this,MainLibrary.class));
    }


    //method to go to Admin...........



}
