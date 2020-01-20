package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DashBoard extends AppCompatActivity {

    private TextView user_name;
    private ImageView user_image;
    private BottomNavigationView bottomNavigationItemView;
    private SharedPreferences sharedPreferences;

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


         sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
         user_name.setText(sharedPreferences.getString("userName", ""));

         getImage();
    }


    private void getImage()
    {

        String url = "https://www.iamannitian.co.in/test/load_image.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //json response

                        byte [] decodedString  = Base64.decode(response, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                        if(decodedByte != null)
                        {
                            user_image.setImageBitmap(decodedByte);
                        }
                        else
                        {
                            user_image.setImageResource(R.drawable.user);
                        }

                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("idKey",sharedPreferences.getString("userId",""));
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(DashBoard.this);
        rq.add(sr);
    }




    /*=========================== shared preferences saving user data starts ============================*/



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
