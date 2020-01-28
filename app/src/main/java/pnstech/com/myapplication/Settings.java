package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void goToShare(View view) //sharing tbe app link to the wjatsapp
    {
        final PackageManager pm=getPackageManager();
        final Intent a = new Intent(Intent.ACTION_SEND);
        Toast.makeText(getApplicationContext(), "Wait..", Toast.LENGTH_LONG).show();



        a.setType("text/link");
        String shareBody = "Download BalPanchayat app for regular updates."+
                "\n\t\t\t\t\t-Team BalPanchayat"+
                "\n"+"----------------------------"+"\n"+
                "https://www.iamannitian.co.in";

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

    public void goToLogout(View view)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(Settings.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //finish all previous activities
        startActivity(intent);
    }

}
