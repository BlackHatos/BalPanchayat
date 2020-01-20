package pnstech.com.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.PoolingByteArrayOutputStream;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    //user image
    private LinearLayout set_user_image;
    private TextView image_name_to_display;
    private final int IMAGE_REQUEST_CODE=1;
    ImageView user_profile_pic;
    private Bitmap bitmap;
    private ImageLoader imageLoader;
    private String url =null;

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
        user_profile_pic = (ImageView) findViewById(R.id.user_profile_pic);

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
        set_user_image = (LinearLayout) findViewById(R.id.set_user_image);
        image_name_to_display = (TextView) findViewById(R.id.image_name_to_display);




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




//=================== edit, update and cancel button visibility
        set_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             selectImage();
            }
        });



   cancel_update.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {

           hide_profile.setVisibility(View.VISIBLE);
           show_profile.setVisibility(View.GONE);
       }
   });


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hide_profile.setVisibility(View.GONE);
                show_profile.setVisibility(View.VISIBLE);

            }
        });

//============================ updating profile
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();

                }
        });

        //===========================  bottom navigation
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


        getImage(); //getting profile picture of the user

    }



    private void getImage()
    {

        String url = "https://www.iamannitian.co.in/test/load_image.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //json response

                      byte [] decodedString  = Base64.decode(response, Base64.DEFAULT);
                      Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                      if(decodedByte != null)
                      {
                          user_profile_pic.setImageBitmap(decodedByte);
                      }
                      else
                      {
                          user_profile_pic.setImageResource(R.drawable.user);
                      }

                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("idKey",sharedPreferences.getString("userId",""));
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        rq.add(sr);
    }






    //==================== selecting and uploading image

    public void selectImage()
    {
        Intent img = new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(img,IMAGE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null)
       {
            Uri path  = data.getData();
            setImageName(path); //setting path name

           try {
               bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }


    public void setImageName(Uri path)
    {

        //getting a random number each time
         String rand_num = String.valueOf(Math.random());
         String randFloat [] = rand_num.split("\\."); //split number by dot

         //getting first name of the user
         String userFullName = sharedPreferences.getString("userName", "");
         String [] userName = userFullName.split("\\s+");
         String userFirstName = userName[0];

         //final image name
         String imageName = userFirstName+""+randFloat[1];

         image_name_to_display.setText(imageName);

    }



    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
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


                            //====================  updating shared prefrences starts

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId",response_array[1]);
                            editor.putString("userName", response_array[2]);
                            editor.putString("userEmail",response_array[3]);
                            editor.putString("userPhone", response_array[4]);
                            editor.putString("userDistrict",response_array[5]);
                            editor.putString("userState", response_array[6]);
                            editor.putString("userAddress",response_array[5]+", "+response_array[6]);
                            editor.apply();

                            //================== updatig shared prefs ends

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
                map.put("idKey",sharedPreferences.getString("userId",""));
                map.put("nameKey", set_user_name.getText().toString().trim());
                map.put("emailKey", set_user_email.getText().toString().trim());
                map.put("phoneKey", set_user_phone.getText().toString().trim());
                map.put("passwordKey", set_user_password.getText().toString().trim());
                map.put("districtKey", set_user_district.getText().toString().trim());
                map.put("stateKey", set_user_state.getText().toString().trim());

                //image data
                if(image_name_to_display.getText().toString().trim().equals("Select profile picture".trim())) {
                    map.put("imageNameKey", "");
                    map.put("imageKey", "");
                }
                else
                {
                    map.put("imageNameKey", image_name_to_display.getText().toString().trim());
                    map.put("imageKey", imageToString(bitmap));
                }
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(Profile.this);
        rq.add(sr);
    }



}
