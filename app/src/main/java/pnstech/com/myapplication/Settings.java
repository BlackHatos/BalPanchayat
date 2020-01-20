package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void gotoShare(View view)
    {
          //
    }

    public void goToAbout(View view)
    {
        startActivity(new Intent(Settings.this,About.class));
    }

    public void goToLogout(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(Settings.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //finish all previous activities
        startActivity(intent);
    }

}
