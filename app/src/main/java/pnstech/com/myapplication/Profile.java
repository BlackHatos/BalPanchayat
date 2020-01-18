package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private BottomNavigationView bottomNavigationItemView;
    private LinearLayout edit_profile;
    private  LinearLayout update_profile;
    private LinearLayout cancel_update;

    private LinearLayout hide_profile;
    private  LinearLayout show_profile;


    //the top profile section
    private TextView get_user_name;
    private TextView get_user_address;

    //default profile sectioon
    private TextView get_user_emailx;
    private TextView get_user_addressx;
    private TextView get_user_phonex;


    //update profile sewction

    private EditText set_user_name;
    private EditText set_user_email;
    private EditText set_user_phone;
    private EditText set_user_district;
    private EditText set_user_state;
    private EditText set_user_password;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bottomNavigationItemView = findViewById(R.id.navigation_view);

        //progress dialog
        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing

        //edit update profile (buttons)
        edit_profile = (LinearLayout) findViewById(R.id.edit_profile);
        update_profile = (LinearLayout) findViewById(R.id.update_profile);
        hide_profile = (LinearLayout) findViewById(R.id.hide_profile);
        show_profile = (LinearLayout) findViewById(R.id.show_profile);
        cancel_update = (LinearLayout) findViewById(R.id.cancel_update);



        //default profile section
        get_user_name = (TextView) findViewById(R.id.get_user_name);
        get_user_address = (TextView) findViewById(R.id.get_user_address);

        //default profile section
        get_user_addressx = (TextView) findViewById(R.id.get_user_addressx);
        get_user_emailx = (TextView) findViewById(R.id.get_user_emailx);
        get_user_phonex = (TextView) findViewById(R.id.get_user_phonex);


        // updating profile detail section
        set_user_name = (EditText) findViewById(R.id.set_user_name);
        set_user_email = (EditText) findViewById(R.id.set_user_email);
        set_user_phone = (EditText) findViewById(R.id.set_user_phone);
        set_user_district = (EditText) findViewById(R.id.set_user_district);
        set_user_state = (EditText) findViewById(R.id.set_user_state);
        set_user_password = (EditText)  findViewById(R.id.set_user_password);



//========================== shared prefs starts
        //shared preference to set profile data
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        //top of the profile
        get_user_name.setText(sharedPreferences.getString("userName",""));
        get_user_address.setText(sharedPreferences.getString("userAddress",""));

        //default profile section
        get_user_emailx.setText(sharedPreferences.getString("userEmail",""));
        get_user_addressx.setText(sharedPreferences.getString("userAddress",""));
        get_user_phonex.setText(sharedPreferences.getString("userPhone", ""));

        //setting EditTexts of update profile section
        set_user_name.setText(sharedPreferences.getString("userName",""));
        set_user_email.setText(sharedPreferences.getString("userEmail",""));
        set_user_phone.setText(sharedPreferences.getString("userPhone", ""));
        set_user_district.setText(sharedPreferences.getString("userDistrict",""));
        set_user_state.setText(sharedPreferences.getString("userState",""));
        set_user_password.setText("");

//================================  shared prefs end

   cancel_update.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {

           hide_profile.setVisibility(View.VISIBLE);
           show_profile.setVisibility(View.GONE);
       }
   });

        //edit profile section
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_profile.setVisibility(View.GONE);
                show_profile.setVisibility(View.VISIBLE);

            }
        });


        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();

                hide_profile.setVisibility(View.VISIBLE);
                show_profile.setVisibility(View.GONE);

                }
        });


        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        Toast.makeText(getApplicationContext(), "No notifications yet", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.home:
                        startActivity(new Intent(Profile.this, DashBoard.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(Profile.this, Settings.class));
                        break;

                }
                return true;
            }
        });
    }


    public void updateProfile()
    {
        progressDialog.setMessage("Processing....");
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        String url = "https://www.iamannitian.co.in/test/update.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //splitting the string into words

                        String response_array[] = response.split(",");
                        if(response_array[0].equals("1"))
                        {
                            progressDialog.dismiss();
                            //on dialog dismiss back to interaction mode
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Intent intent = new Intent(Profile.this, Profile.class);
                            startActivity(intent);

                        }
                        else if(response_array[0].equals("0")) //print message if error
                        {
                            progressDialog.dismiss();
                            //on dialog dismiss back to interaction mode
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(getApplicationContext(),response_array[1],Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //on dialog dismiss back to interaction mode
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getApplicationContext(), "failed to update", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("nameKey", set_user_name.getText().toString().trim());
                map.put("emailKey", set_user_email.getText().toString().trim());
                map.put("phoneKey", set_user_phone.getText().toString().trim());
                map.put("passwordKey", set_user_password.getText().toString().trim());
                map.put("districtKey", set_user_district.getText().toString().trim());
                map.put("stateKey", set_user_state.getText().toString().trim());
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        rq.add(sr);
    }



}
