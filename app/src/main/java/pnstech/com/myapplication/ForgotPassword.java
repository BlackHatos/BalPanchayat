package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button  click_to_change;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;
    private TextView app_name;
    private Typeface myfont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.email);
        click_to_change = (Button) findViewById(R.id.click_to_change);

        progressDialog = new ProgressDialog(this,R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        app_name = (TextView) findViewById(R.id.app_name);
        // custom_login =  (Button) findViewById(R.id.custom_login);

        myfont=Typeface.createFromAsset(this.getAssets(),"fonts/gvr.otf");
        app_name.setTypeface(myfont);

        click_to_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("forgotPassword", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("emailToChangePassword",email.getText().toString().trim());
                editor.apply();
                sendEmail();
            }
        });
    }

    private void sendEmail()
    {

        progressDialog.setMessage("Processing....");
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        if(email.getText().toString().equals(""))
        {
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else
        {
            String url = "https://www.iamannitian.co.in/test/send_mail.php";
            StringRequest sr = new StringRequest(1, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if(response.equals("1"))
                            {
                                email.setText("");
                                showSuccessMessage();
                            }
                            else
                                {
                                Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
                              }
                        }
                    }, new Response.ErrorListener() { //error
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), "failed! try again", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map =  new HashMap<>();
                    map.put("emailKey", email.getText().toString().trim());
                    return map;
                }
            };

            RequestQueue rq = Volley.newRequestQueue(ForgotPassword.this);
            rq.add(sr);
        }
    }

    private void showSuccessMessage()
    {
        final Snackbar snackbar = Snackbar.make(linearLayout,"password reset email sent successfully",Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }


    }


