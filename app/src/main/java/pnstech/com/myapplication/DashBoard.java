package pnstech.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
    }


    public void home(View view)
    {
        startActivity(new Intent(DashBoard.this,MainActivity.class));
    }


    public void library(View view){
        startActivity(new Intent(DashBoard.this,MainLibrary.class));
    }
}
