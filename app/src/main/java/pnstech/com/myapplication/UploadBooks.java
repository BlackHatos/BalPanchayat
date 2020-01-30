package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadBooks extends AppCompatActivity {

private ImageView book_cover;
private final int IMAGE_REQUEST_CODE = 1;
private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private Button click_to_upload;
    private EditText book_name;
    private EditText writer_name;
    private EditText contributer_name;


    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_books);


        bottomNavigationView = findViewById(R.id.navigation_view);


        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing

        book_cover = (ImageView) findViewById(R.id.book_cover);
        click_to_upload = (Button) findViewById(R.id.click_to_upload);
        book_name = (EditText) findViewById(R.id.book_name);
        writer_name = (EditText) findViewById(R.id.writer_name);
        contributer_name = (EditText) findViewById(R.id.contributer_name);


        book_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        //======================= setting upload button

                click_to_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!book_name.getText().toString().trim().equals("")
                                && !writer_name.getText().toString().trim().equals("")
                                && !contributer_name.getText().toString().trim().equals("")
                                && bitmap != null)
                        {

                            uploadImage();
                        }
                        else
                        {
                           //Toast.makeText(getApplicationContext(), "please fill out the details",Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        //===========================  bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        startActivity(new Intent(UploadBooks.this, pnstech.com.myapplication.Notification.class));
                        break;

                    case R.id.donars:
                        startActivity(new Intent(UploadBooks.this, Donars.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(UploadBooks.this, Settings.class));
                        break;

                    case R.id.library:
                        startActivity(new Intent(UploadBooks.this, MainLibrary.class));
                        break;

                }
                return true;
            }
        });


                showBadge();

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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                book_cover.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    private void uploadImage()
    {
        progressDialog.setMessage("Processing....");
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        String url = "https://www.iamannitian.co.in/test/upload_books.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        //on dialog dismiss back to interaction mode
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        String response_array[] = response.split(",");
                        if(response_array[0].equals("1")) {


                            book_name.setText("");
                            writer_name.setText("");
                            contributer_name.setText("");
                            book_cover.setImageResource(R.drawable.ic_image_black_24dp);
                        }
                        Toast.makeText(getApplicationContext(), response_array[1], Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                    map.put("bookNameKey", book_name.getText().toString().trim());
                    map.put("bookWriterKey", writer_name.getText().toString().trim());
                    map.put("bookContributerKey", contributer_name.getText().toString().trim());
                    map.put("imageKey", imageToString(bitmap));
                    return map;

            }
        };

        RequestQueue rq = Volley.newRequestQueue(UploadBooks.this);
        rq.add(sr);
    }


    public void showBadge() //showq notfication badge
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
        notificationBadge = LayoutInflater.from(this).inflate(R.layout.notification_badge, menuView,false);
        itemView.addView(notificationBadge);
    }

}
