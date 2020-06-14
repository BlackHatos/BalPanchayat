package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIME_OUT=3000;
    private SharedPreferences sharedPreferences, coronaCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

         sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
          final String user_id = sharedPreferences.getString("userId", "");

                 updateNotification(user_id);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run()
            {
                if(user_id.equals(""))
                {
                    Intent intent= new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent= new Intent(SplashScreen.this, DashBoard.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);

    }


    public void updateNotification(final String user_id)
    {
         // sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

        String url = "https://www.iamannitian.co.in/test/get_count.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String response_array[] = response.split(",");
                        if(response_array[0].equals("1")) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("notifyCount", response_array[1]);
                            editor.putString("track_value", response_array[2]);
                            editor.apply();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("idKey",user_id);
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(SplashScreen.this);
        rq.add(sr);
    }

}
