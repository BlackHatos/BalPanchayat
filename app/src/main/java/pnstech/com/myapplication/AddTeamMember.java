package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddTeamMember extends AppCompatActivity
{
    private ImageView member_pic;
    private final int IMAGE_REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private Button click_to_add;

    private EditText member_name;
    private EditText member_position;
    private EditText member_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_member);

        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing

        member_pic = (ImageView) findViewById(R.id.member_pic);
        click_to_add = (Button) findViewById(R.id.click_to_add);
        member_name = (EditText) findViewById(R.id.member_name);
        member_position = (EditText) findViewById(R.id.member_position);
        member_phone = (EditText) findViewById(R.id.member_phone);

        member_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        //======================= setting upload button

        click_to_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!member_name.getText().toString().trim().equals("")
                        && !member_phone.getText().toString().trim().equals("")
                        && !member_position.getText().toString().trim().equals("")
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

    }


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
                member_pic.setImageBitmap(bitmap);

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

        String url = "https://www.iamannitian.co.in/test/add_member.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        //on dialog dismiss back to interaction mode
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        String response_array[] = response.split(",");
                        if(response_array[0].equals("1")) {

                            member_name.setText("");
                            member_position.setText("");
                            member_phone.setText("");
                            member_pic.setImageResource(R.drawable.ic_image_black_24dp);
                        }
                        Toast.makeText(getApplicationContext(), response_array[1], Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getApplication(),  error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("memberNameKey", member_name.getText().toString().trim());
                map.put("memberPositionKey", member_position.getText().toString().trim());
                map.put("phoneKey", member_phone.getText().toString().trim());
                map.put("imageKey", imageToString(bitmap));
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(AddTeamMember.this);
        rq.add(sr);
    }

}
