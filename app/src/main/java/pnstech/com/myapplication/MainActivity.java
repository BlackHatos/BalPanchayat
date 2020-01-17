//key hash for the app to implement fb login
//8hVoYs/8Kri6mKKbx6UKyPo7fJo=

package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.ImageHeaderParser;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.Inet4Address;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText emailPhone;
    private EditText password;
    private Button click_to_login;
    private Button custom_login;
    private  CallbackManager callbackManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailPhone = (EditText) findViewById(R.id.email_phone);
        password  = (EditText) findViewById(R.id.password);
        click_to_login = (Button) findViewById(R.id.click_to_login);
        custom_login =  (Button) findViewById(R.id.custom_login);
        
        progressDialog = new ProgressDialog(this,R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing



        click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("Authenticating....");
                progressDialog.show();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                if(emailPhone.getText().toString().equals("") || password.getText().toString().equals(""))
                {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), "please fill out the credentials", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        String url = "https://www.iamannitian.co.in/test/login.php";
                        StringRequest sr = new StringRequest(1, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //splitting the string into words

                                        String response_array[] = response.split(",");
                                        if(response_array[0].equals("1"))
                                        {
                                            emailPhone.setText("");
                                            password.setText("");
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(MainActivity.this, DashBoard.class);
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
                                map.put("emailKey", emailPhone.getText().toString().trim());
                                map.put("passwordKey", password.getText().toString().trim());
                                return map;
                            }
                        };

                        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
                        rq.add(sr);
                     }
            }
        });


        //login with fb

        custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbLogin();

            }
        });


    }



    public void insertDataIntoDatabase()
    {

    }




    /*===================== Facebook login starts =============================*/

    public void fbLogin()
    {

        callbackManager = CallbackManager.Factory.create();

        //LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));
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


                Intent intent = new Intent(MainActivity.this, DashBoard.class);
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


}
