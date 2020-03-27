package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
    private Button click_to_login;

    private ProgressDialog progressDialog;
  //  private  CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        click_to_login = (Button) findViewById(R.id.click_to_login);

        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing

        click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // show progress bar first
                progressDialog.setMessage("Processing....");
                progressDialog.show();
                // disable user interaction when progressdialog appears
                 getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                if(name.getText().toString().equals("")  || email.getText().toString().equals("")
                        || phone.getText().toString().equals("") || password.getText().toString().equals("")){

                    progressDialog.dismiss();
                    //on dialog dismiss back to interaction mode
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    //oast.makeText(getApplicationContext(), "Please fill out the credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                       String url = "https://www.iamannitian.co.in/test/register.php";
                        StringRequest sr = new StringRequest(1, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //splitting the string into words

                                        String response_array[] = response.split(",");
                                        if(response_array[0].equals("1"))
                                        {
                                            name.setText("");
                                            email.setText("");
                                            phone.setText("");
                                            password.setText("");
                                            progressDialog.dismiss();

                                            /*=========================== shared preferences saving user data starts ============================*/

                                            SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("userId",response_array[1]);
                                            editor.putString("userName", response_array[2]);
                                            editor.putString("userEmail",response_array[3]);
                                            editor.putString("userPhone", response_array[4]);
                                            editor.putString("userDistrict",response_array[5]);
                                            editor.putString("userState", response_array[6]);
                                            editor.putString("userType",response_array[7]);
                                            editor.putString("userAddress",response_array[5]+", "+response_array[6]);


                                            editor.putString("notifyCount",response_array[8]);
                                            editor.putString("track_value",response_array[9]);

                                            editor.apply();

                                            /*=========================== shared preferences saving user data starts ============================*/

                                            Intent intent = new Intent(RegisterActivity.this, DashBoard.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //finish all previous activities
                                            startActivity(intent);

                                        }
                                        else if(response_array[0].equals("0"))
                                        {
                                            progressDialog.dismiss();
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            Toast.makeText(getApplicationContext(),response_array[1],Toast.LENGTH_LONG).show();
                                        }


                                    }
                                }, new Response.ErrorListener() { //error
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                              Toast.makeText(getApplicationContext(), "login failed", Toast.LENGTH_SHORT).show();

                            }
                        }){
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map =  new HashMap<>();
                                map.put("nameKey", name.getText().toString().trim());
                                map.put("emailKey", email.getText().toString().trim());
                                map.put("phoneKey", phone.getText().toString().trim());
                                map.put("passwordKey", password.getText().toString().trim());
                                map.put("districtKey", "District");
                                map.put("stateKey", "State");
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
        finish();
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
