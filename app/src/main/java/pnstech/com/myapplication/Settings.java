package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private static String link ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getLink();
    }

    public void goToShare(View view) //sharing tbe app link to the wjatsapp
    {
        final PackageManager pm=getPackageManager();
        final Intent a = new Intent(Intent.ACTION_SEND);
        Toast.makeText(getApplicationContext(), "Wait..", Toast.LENGTH_LONG).show();



        a.setType("text/link");
        String shareBody = "Download BalPanchayat app, follow the link given below."+
                "\n\t\t\t\t\t\t-Team BalPanchayat"+
                "\n"+"----------------------------"+"\n"+ link;


        String shareSub = "Team BalPanchayat";
        a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        a.putExtra(Intent.EXTRA_TEXT, shareBody);
        try {

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            a.setPackage("com.whatsapp");//important it will select only whatsapp
        }catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(), "plese install whatsapp", Toast.LENGTH_SHORT)
                    .show();
        }catch(Exception e){
            e.printStackTrace();
        }
        startActivity(a);

    }

    public void goToAbout(View view)
    {
        startActivity(new Intent(Settings.this,About.class));
    }

    public void goTooLogout(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(Settings.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //finish all previous activities
        startActivity(intent);
    }

    public void goToTrack(View view)
    {
        startActivity(new Intent(Settings.this, TrackRequest.class));
    }


    public void getLink()
    {

        String url = "https://www.iamannitian.co.in/test/get_link.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        link = response;
                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("testKey", "hello");
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(Settings.this);
        rq.add(sr);
    }


}
