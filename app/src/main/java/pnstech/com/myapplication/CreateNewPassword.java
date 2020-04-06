package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreateNewPassword extends AppCompatActivity {

    private EditText password;
    private EditText re_password;
    private Button click_to_change;

    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private SharedPreferences sharedPreferences;
    private TextView app_name;
    private Typeface myfont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_password);

        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        click_to_change = findViewById(R.id.click_to_change);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        progressDialog = new ProgressDialog(this,R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false);

        click_to_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        app_name = (TextView) findViewById(R.id.app_name);
        // custom_login =  (Button) findViewById(R.id.custom_login);

        myfont=Typeface.createFromAsset(this.getAssets(),"fonts/gvr.otf");
        app_name.setTypeface(myfont);


        sharedPreferences = getSharedPreferences("forgotPassword", MODE_PRIVATE);

    }

    private void changePassword()
    {

        progressDialog.setMessage("Processing....");
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        if(password.getText().toString().trim().equals("") || re_password.getText().toString().trim().equals(""))
        {
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        if(password.getText().toString().trim().length()<6)
        {
            Toast.makeText(getApplicationContext(),"password is too short", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {

              if (password.getText().toString().trim().equals(re_password.getText().toString().trim())) {
                String url = "https://www.iamannitian.co.in/test/change_password.php";
                StringRequest sr = new StringRequest(1, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                if(response.equals("1"))
                                {
                                    password.setText("");
                                    re_password.setText("");
                                    goToLogin();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"failed to change password", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() { //error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("passwordKey", password.getText().toString().trim());
                        map.put("emailKey",sharedPreferences.getString("emailToChangePassword",""));
                        return map;
                    }
                };

                RequestQueue rq = Volley.newRequestQueue(CreateNewPassword.this);
                rq.add(sr);
            }

            else {
                Toast.makeText(getApplicationContext(), "password do not match", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }

    }

    private void goToLogin()
    {
        final Snackbar snackbar = Snackbar.make(linearLayout,"password changed successfully press ok to login",Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(CreateNewPassword.this, MainActivity.class));
               finish();
            }
        });
        snackbar.show();
    }

}



