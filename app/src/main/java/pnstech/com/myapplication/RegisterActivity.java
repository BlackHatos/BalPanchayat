package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText confirm_password;
    private Button click_to_login;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        click_to_login = (Button) findViewById(R.id.click_to_login);

        progressDialog = new ProgressDialog(this);

        click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // show progress bar first
                progressDialog.setMessage("Processing....");
                progressDialog.show();

                if(name.getText().toString().equals("")  || email.getText().toString().equals("")
                        || phone.getText().toString().equals("") || password.getText().toString().equals("")
                        || confirm_password.getText().toString().equals("")  )
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please fill the credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = "https://www.iamannitian.co.in/test/register.php";
                       StringRequest sr = new StringRequest(1, url,
                               new Response.Listener<String>() {
                                   @Override
                                   public void onResponse(String response) {
                                       progressDialog.dismiss();
                                       //if registration successful

                                       name.setText("");
                                       email.setText("");
                                       phone.setText("");
                                       password.setText("");

                                    startActivity(new Intent(RegisterActivity.this, DashBoard.class));
                                    finish();

                                   }
                               }, new Response.ErrorListener() { //error
                           @Override
                           public void onErrorResponse(VolleyError error) {
                               progressDialog.dismiss();
                              Toast.makeText(getApplicationContext(), error+" ", Toast.LENGTH_SHORT).show();
                           }
                       }){
                           @Override
                           public Map<String, String> getParams() throws AuthFailureError {
                              Map<String, String> map =  new HashMap<>();
                              map.put("nameKey", name.getText().toString());
                               map.put("emailKey", email.getText().toString());
                               map.put("phoneKey", phone.getText().toString());
                               map.put("passwordKey", password.getText().toString());
                               return map;
                           }
                       };

                    RequestQueue rq = Volley.newRequestQueue(RegisterActivity.this);
                    rq.add(sr);
                }
            }
        });
    }


    public void goToLogin(View view)
    {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /*public void showPopup(View view)
    {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup,null);
        boolean focusable = false;
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);
          popupWindow.setAnimationStyle(R.style.windowAnimationTransition);
        popupWindow.showAtLocation(view ,Gravity.CENTER,0,0);

        //closing window after a given time
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.dismiss();
                   }
        },3000);


        //then go to dashboard activity
        Intent intent = new Intent(RegisterActivity.this, DashBoard.class);
        startActivity(intent);
        finish();

       /*
       ** close window on touch

       popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });*/

    //}
}
