package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
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
    private TextView reward_code;
    private String rewardCode;
    private Button ok_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_books);

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
                            Toast.makeText(getApplicationContext(), "please fill out the details",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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

  /*                          View view = getWindow().getDecorView().getRootView();
                            //calling show popup method
                            showPopup(view); //passing view object
*/
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



   /*
    public void showPopup(View view)
    {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.reward_popup,null);

        reward_code = (TextView) popupView.findViewById(R.id.reward_code); //popup code
        reward_code.setText(rewardCode);
        boolean focusable = false;
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView,width,height,focusable);
          popupWindow.setAnimationStyle(R.style.windowAnimationTransition);
        popupWindow.showAtLocation(view , Gravity.CENTER,0,0);


        ok_code = (Button) popupView.findViewById(R.id.ok_code);
        ok_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss(); //dismiss popup on button click
            }
        });

    }



    private String getRandomString()
    {
        String random_string;
        String alphaNumericString  =  "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvwx";
        StringBuilder sb  = new StringBuilder(6);
        for(int i=0; i<6; i++)
        {
            int index = (int) (alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }

        return sb.toString();
    }*/
}
