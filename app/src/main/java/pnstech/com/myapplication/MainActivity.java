package pnstech.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRegister(View view)
    {
        Intent intent= new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void getPassword(View view)
    {
        Intent intent= new Intent(MainActivity.this, ForgotPassword.class);
        startActivity(intent);
    }

    public void test(View view)
    {
        Intent intent= new Intent(MainActivity.this, DashBoard.class);
        startActivity(intent);
    }

}
