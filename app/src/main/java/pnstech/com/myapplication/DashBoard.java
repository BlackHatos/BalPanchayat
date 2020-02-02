package pnstech.com.myapplication;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.OVER_SCROLL_ALWAYS;
import static android.view.View.VISIBLE;

public class DashBoard extends AppCompatActivity {

    private TextView user_name;
    private ImageView user_image;
    private SharedPreferences sharedPreferences;
    private Button leave_admin;
    private RelativeLayout relativeLayout;

    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        relativeLayout = findViewById(R.id.relative_layout);

        user_name = (TextView) findViewById(R.id.user_name);
        user_image = (ImageView) findViewById(R.id.user_image);

        bottomNavigationView = findViewById(R.id.navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        removeBadge(); //call remove badge method
                        notificationBadge.setVisibility(GONE);
                        startActivity(new Intent(DashBoard.this, pnstech.com.myapplication.Notification.class));
                      break;

                    case R.id.home:
                        //startActivity(new Intent(DashBoard.this, DashBoard.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(DashBoard.this, Settings.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(DashBoard.this, Profile.class));
                        break;
                }
                return true;
            }
        });


         sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
         user_name.setText(sharedPreferences.getString("userName", ""));

         getImage();
         showBadge();
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

        RequestQueue rq = Volley.newRequestQueue(DashBoard.this);
        rq.add(sr);
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
              //  Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();

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


    //method to visit to profile
    public void goToProfile(View view){
        startActivity(new Intent(DashBoard.this,Profile.class));
    }

    //method to go to library

    public void goToLibrary(View view){
        boolean isConnected  = checkInternetConnection();
        if(isConnected)
        startActivity(new Intent(DashBoard.this,MainLibrary.class));
        else
        {
            final Snackbar snackbar = Snackbar.make(relativeLayout,"You need an active internet connection",Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }


    //method to go to Admin...........

    public void goToAdmin(View view)
    {
        String code = sharedPreferences.getString("userType","");

        boolean isConnected  = checkInternetConnection();
        if(isConnected)
        {
            if(code.equals("1"))
            {
                //go to send notification
                startActivity(new Intent(DashBoard.this, SendNotification.class));
            }
            else
            {
                notAllowedPopup(view);
            }
        }
        else
        {
            final Snackbar snackbar = Snackbar.make(relativeLayout,"You need an active internet connection",Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    public void goToDonate(View view)
    {

        boolean isConnected  = checkInternetConnection();
        if(isConnected)
            startActivity(new Intent(DashBoard.this,Donars.class));
        else
        {
            final Snackbar snackbar = Snackbar.make(relativeLayout,"You need an active internet connection",Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    public void goToTeam(View view)
    {
        boolean isConnected  = checkInternetConnection();
        if(isConnected)
            startActivity(new Intent(DashBoard.this, Team.class));
        else
        {
            final Snackbar snackbar = Snackbar.make(relativeLayout,"You need an active internet connection",Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    public void   notAllowedPopup(View view)
    {

        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.not_admin,null);
        boolean focusable = false;
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);
        popupWindow.setAnimationStyle(R.style.windowAnimationTransition);
        popupWindow.showAtLocation(view , Gravity.CENTER,0,0);

        leave_admin = (Button) popupView.findViewById(R.id.leave_admin);
        leave_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss(); //dismiss popup on button click
            }
        });

    }

    public boolean checkInternetConnection()
    {
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo=  connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
