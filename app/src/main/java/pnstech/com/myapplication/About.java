package pnstech.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    private TextView app_version;
    private TextView company_name;
    private TextView all_rights;
    private TextView developer;
    //private TextView ceo;
    private Typeface myfont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        company_name = findViewById(R.id.company_name);
        all_rights = findViewById(R.id.all_rights);
        developer = findViewById(R.id.developer);
        //ceo = findViewById(R.id.ceo);
        app_version = findViewById(R.id.app_version);


        //=================getting version name
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(),0);
            String version = packageInfo.versionName;
            app_version.setText("version "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        myfont= Typeface.createFromAsset(getAssets(),"fonts/ArimaMaduraiRegular.otf");
        app_version.setTypeface(myfont);
        company_name.setTypeface(myfont);
        developer.setTypeface(myfont);
        //ceo.setTypeface(myfont);
        all_rights.setTypeface(myfont);

    }


    public void goToGit(View viewe)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/BlackHatos"));
        startActivity(intent);
    }

    public void goToLinked(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/cyberthreatatnit/"));
        startActivity(intent);
    }

    public void goToInsta(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/shubham11krm/?hl=en"));
        startActivity(intent);
    }

    public void goToFb(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/cyberatnit"));
        startActivity(intent);
    }
}
