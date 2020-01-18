package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

       final SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        final String user_id = sharedPreferences.getString("userId", "");

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

}
