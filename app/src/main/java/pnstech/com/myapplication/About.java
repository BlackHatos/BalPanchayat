package pnstech.com.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }


    public void goToGit(View viewe)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/pnstech"));
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
