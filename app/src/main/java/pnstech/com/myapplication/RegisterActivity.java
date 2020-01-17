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
import android.view.WindowManager;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
    private  CallbackManager callbackManager;

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
                        || phone.getText().toString().equals("") || password.getText().toString().equals("")
                        || confirm_password.getText().toString().equals("")  ){

                    progressDialog.dismiss();
                    //on dialog dismiss back to interaction mode
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Toast.makeText(getApplicationContext(), "Please fill out the credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(password.getText().toString().trim().equals(confirm_password.getText().toString().trim()))
                    {
                        String url = "https://www.iamannitian.co.in/test/register.php";
                        StringRequest sr = new StringRequest(1, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //if registration successful
                                        String response_array[] = response.split(",");
                                        if(response_array[0].equals("1"))
                                        {
                                            name.setText("");
                                            email.setText("");
                                            phone.setText("");
                                            password.setText("");
                                            confirm_password.setText("");

                                            progressDialog.dismiss();
                                            Intent intent = new Intent(RegisterActivity.this, DashBoard.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //finish all previous activities
                                            startActivity(intent);

                                        }
                                        else if(response_array[0].equals("0"))
                                            {
                                                progressDialog.dismiss();
                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                Toast.makeText(getApplicationContext(),response_array[1], Toast.LENGTH_LONG).show();
                                           }

                                    }
                                }, new Response.ErrorListener() { //error
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"registration failed", Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map =  new HashMap<>();
                                map.put("nameKey", name.getText().toString().trim());
                                map.put("emailKey", email.getText().toString().trim());
                                map.put("phoneKey", phone.getText().toString().trim());
                                map.put("passwordKey", password.getText().toString().trim());
                                return map;
                            }
                        };

                        RequestQueue rq = Volley.newRequestQueue(RegisterActivity.this);
                        rq.add(sr);
                    }
                    else
                    {
                        password.setText("");
                        confirm_password.setText("");
                        progressDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), "password do not match", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });
    }





    /*===================== Facebook login starts =============================*/

    public void fbRegister(View view)
    {

        callbackManager = CallbackManager.Factory.create();

        //LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
        // LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("public_actions"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //  Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                //Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                //Toast.makeText(getApplicationContext(), exception+" ", Toast.LENGTH_LONG).show();
            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if(currentAccessToken == null)
            {
                Toast.makeText(getApplicationContext(),"user  logged out", Toast.LENGTH_LONG).show();
            }
            else
            {
                loadUserData(currentAccessToken);
            }
        }
    };




    private void loadUserData(AccessToken accessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url =  "https://graph.facebook.com/"+id+"/picture?type=normal";


                    // Toast.makeText(getApplicationContext(),first_name+" "+last_name+" "+email+" "+id,Toast.LENGTH_LONG).show();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                //go to next activity


                Intent intent = new Intent(RegisterActivity.this, DashBoard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //finish all previous activities
                startActivity(intent);


            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //call this method to logout
    public void fbLogout()
    {
        LoginManager.getInstance().logOut();
    }


    /*===================== Facebook login end =============================*/


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
