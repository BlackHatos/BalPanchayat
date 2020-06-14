package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Profile extends AppCompatActivity {

    private ProgressDialog progressDialog;

    //the top profile section
    private TextView get_user_name;
    private TextView get_user_address;

    //default profile sectioon
    private TextView get_user_namex;
    private TextView get_user_emailx;
    private TextView get_user_dist;
    private TextView get_user_state;
    private TextView get_user_phonex;


    //update profile sewction
    private EditText set_user_name;
    private EditText set_user_email;
    private EditText set_user_phone;
    private EditText set_user_district;
    private EditText set_user_state;
    private EditText set_user_password;


    //user image
    private ImageView save_image;
    private TextView image_name_to_display;
    private final int IMAGE_REQUEST_CODE=1;
    ImageView user_profile_pic;
    private Bitmap bitmap;
    private ImageLoader imageLoader;
    private String url =null;


    private SharedPreferences sharedPreferences;
    private FloatingActionButton message_button;



    //========================= New Editing  Starts
    private ImageView  edit_pic_pen;
    private LinearLayout contain_user_pic;
    private LinearLayout show_hint_pic;

    private ImageView  edit_new_pass;
    private ImageView save_password;
    private LinearLayout contains_passwoprd;
    private LinearLayout password_to_display;



    private ImageView  edit_new_phone;
    private ImageView save_phone;
    private LinearLayout contains_phone;
    private LinearLayout phone_to_display;



    private ImageView  edit_new_email;
    private ImageView save_email;
    private LinearLayout contains_email;
    private LinearLayout email_to_display;

    private ImageView  edit_new_state;
    private ImageView save_state;
    private LinearLayout contains_state;
    private LinearLayout state_to_display;



    private ImageView  edit_new_name;
    private ImageView save_name;
    private LinearLayout contains_name;
    private LinearLayout name_to_display;


    private ImageView  edit_new_dist;
    private ImageView save_dist;
    private LinearLayout contains_dist;
    private LinearLayout dist_to_display;

   //=============================  New Editing Ends

    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bottomNavigationView = findViewById(R.id.navigation_view);

        bottomNavigationView.setSelectedItemId(R.id.profile);
        //progress dialog
        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing


        //default profile section
        get_user_name = (TextView) findViewById(R.id.get_user_name);
        get_user_address = (TextView) findViewById(R.id.get_user_address);
        user_profile_pic = (ImageView) findViewById(R.id.user_profile_pic);

        //default profile section
        get_user_dist = (TextView) findViewById(R.id.get_user_dist);
        get_user_state  = (TextView) findViewById(R.id.get_user_state);
        get_user_emailx = (TextView) findViewById(R.id.get_user_emailx);
        get_user_phonex = (TextView) findViewById(R.id.get_user_phonex);
        get_user_namex = (TextView) findViewById(R.id.get_user_namex);


        // updating profile detail section
        set_user_name = (EditText) findViewById(R.id.set_user_name);
        set_user_email = (EditText) findViewById(R.id.set_user_email);
        set_user_phone = (EditText) findViewById(R.id.set_user_phone);
        set_user_district = (EditText) findViewById(R.id.set_user_district);
        set_user_state = (EditText) findViewById(R.id.set_user_state);


        set_user_password = (EditText)  findViewById(R.id.set_user_password);


        image_name_to_display = (TextView) findViewById(R.id.image_name_to_display);
        save_image = (ImageView) findViewById(R.id.save_image);

        edit_new_pass = (ImageView) findViewById(R.id.edit_new_pass);
        save_password = (ImageView) findViewById(R.id.save_password);
        contains_passwoprd = (LinearLayout) findViewById(R.id.contains_password);
        password_to_display = (LinearLayout) findViewById(R.id.password_to_display);



        edit_new_phone = (ImageView) findViewById(R.id.edit_new_phone);
        save_phone = (ImageView) findViewById(R.id.save_phone);
        contains_phone = (LinearLayout) findViewById(R.id.contains_phone);
        phone_to_display = (LinearLayout) findViewById(R.id.phone_to_display);

        edit_new_email = (ImageView) findViewById(R.id.edit_new_email);
        save_email = (ImageView) findViewById(R.id.save_email);
        contains_email = (LinearLayout) findViewById(R.id.contains_email);
        email_to_display = (LinearLayout) findViewById(R.id.email_to_display);

        edit_new_state = (ImageView) findViewById(R.id.edit_new_state);
        save_state = (ImageView) findViewById(R.id.save_state);
        contains_state = (LinearLayout) findViewById(R.id.contains_state);
        state_to_display = (LinearLayout) findViewById(R.id.state_to_display);

        edit_new_dist = (ImageView) findViewById(R.id.edit_new_dist);
        save_dist = (ImageView) findViewById(R.id.save_dist);
        contains_dist = (LinearLayout) findViewById(R.id.contains_dist);
        dist_to_display = (LinearLayout) findViewById(R.id.dist_to_display);



        edit_new_name = (ImageView) findViewById(R.id.edit_new_name);
        save_name = (ImageView) findViewById(R.id.save_name);
        contains_name = (LinearLayout) findViewById(R.id.contains_name);
        name_to_display = (LinearLayout) findViewById(R.id.name_to_display);


        //============================== New Editing Starts

        // =====================>>>>>>>> 1 : image editing and saving  <<<<<<===============

        edit_pic_pen = (ImageView) findViewById(R.id.edit_pic_pen);
        contain_user_pic = (LinearLayout) findViewById(R.id.contains_pic_name);
        show_hint_pic = (LinearLayout) findViewById(R.id.show_hint_pic);

        edit_pic_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contain_user_pic.setVisibility(GONE);
                show_hint_pic.setVisibility(VISIBLE);

            }
        });


        image_name_to_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


        // =================>>>>>>>>> 2 : Editing saving password  <<<<<<<<<<<================


        edit_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains_passwoprd.setVisibility(GONE);
                password_to_display.setVisibility(VISIBLE);
            }
        });

        save_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


        // =================>>>>>>>>> 3 : Editing saving mobile number  <<<<<<<<<<<================

        edit_new_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains_phone.setVisibility(GONE);
                phone_to_display.setVisibility(VISIBLE);
            }

        });

        save_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        // =================>>>>>>>>> 4 : Editing saving mobile number  <<<<<<<<<<<================

        edit_new_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains_email.setVisibility(GONE);
                email_to_display.setVisibility(VISIBLE);

            }

        });

        save_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        edit_new_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains_state.setVisibility(GONE);
                state_to_display.setVisibility(VISIBLE);

            }

        });

        save_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });



        edit_new_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains_dist.setVisibility(GONE);
                dist_to_display.setVisibility(VISIBLE);

            }

        });

        save_dist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });



        edit_new_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contains_name.setVisibility(GONE);
                name_to_display.setVisibility(VISIBLE);

            }

        });

        save_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        //========================== NEW Editing Ends


//========================== shared prefs starts
        //shared preference to set profile data
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        //top of the profile
        get_user_name.setText(sharedPreferences.getString("userName",""));
        get_user_address.setText(sharedPreferences.getString("userAddress",""));


        //default profile section
        get_user_emailx.setText(sharedPreferences.getString("userEmail",""));
        get_user_dist.setText(sharedPreferences.getString("userDistrict",""));
        get_user_state.setText(sharedPreferences.getString("userState",""));
        get_user_phonex.setText(sharedPreferences.getString("userPhone", ""));
        get_user_namex.setText(sharedPreferences.getString("userName", ""));

        //setting EditTexts of update profile section
        set_user_name.setText(sharedPreferences.getString("userName",""));
        set_user_email.setText(sharedPreferences.getString("userEmail",""));
        set_user_phone.setText(sharedPreferences.getString("userPhone", ""));
        set_user_district.setText(sharedPreferences.getString("userDistrict",""));
        set_user_state.setText(sharedPreferences.getString("userState",""));
        set_user_password.setText("");


       //================== floating action button

       message_button = (FloatingActionButton) findViewById(R.id.message_button);

       message_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) { //send email message
               Intent intent = new Intent(android.content.Intent.ACTION_SEND);
               intent.setData(Uri.parse("mailto:"));
               String[] to = {"pnssoftwares7@gmail.com"};
               intent.putExtra(Intent.EXTRA_EMAIL, to);
               intent.putExtra(Intent.EXTRA_SUBJECT, "Have you any query or suggestion?");
               intent.putExtra(Intent.EXTRA_TEXT, "Write here....");
               intent.setType("message/rfc822");// this is must
               Intent.createChooser(intent, "Choose Email"); //second argument is optional
               startActivity(intent);
           }
       });



        //===========================  bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.profile:
                        startActivity(new Intent(Profile.this, pnstech.com.myapplication.Profile.class));
                        overridePendingTransition(0,0);
                         break;
                    case R.id.notify:
                        removeBadge();
                        notificationBadge.setVisibility(GONE);
                        startActivity(new Intent(Profile.this, pnstech.com.myapplication.Notification.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.home://this will clear all the previous activities
                        Intent intent = new Intent(Profile.this, DashBoard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        break;
                    case R.id.settings:
                        startActivity(new Intent(Profile.this, Settings.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return true;
            }
        });


        getImage(); //getting profile picture of the user
      showBadge();
    }

    public void showBadge() //show notification badge
    {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(2);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView,false);

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        String notifyCount = sharedPreferences.getString("notifyCount", "");

        TextView badge_count =  notificationBadge.findViewById(R.id.notify_count);

        if(!notifyCount.equals("0"))
            badge_count.setText(notifyCount);

        else
            notificationBadge.setVisibility(GONE);

        itemView.addView(notificationBadge);
    }

    public void removeBadge()
    {

        String url = "https://www.iamannitian.co.in/test/remove_badge.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1")) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("notifyCount",Integer.toString(0));
                            editor.apply();
                            notificationBadge.setVisibility(GONE);

                        }
                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {

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
              //  Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();
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

    private void selectImage()
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
               user_profile_pic.setImageBitmap(bitmap);

           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }


    private void setImageName(Uri path)
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



    private void updateProfile()
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
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
