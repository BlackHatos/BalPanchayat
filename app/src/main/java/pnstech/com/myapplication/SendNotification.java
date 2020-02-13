package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import static android.view.View.GONE;

public class SendNotification extends AppCompatActivity {

    private EditText send_notify;
    private Button send_notify_button;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);



        send_notify = (EditText) findViewById(R.id.send_notify);
        send_notify_button = (Button) findViewById(R.id.send_notification_button);

        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing

        send_notify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });


        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);

    }

    private void sendNotification()
    {
        progressDialog.setMessage("Processing....");
        progressDialog.show();
        // disable user interaction when progressdialog appears
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


        if(send_notify.getText().toString().equals(""))
        {
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(getApplicationContext(), "please write something", Toast.LENGTH_SHORT).show();
        }
        else
        {

            String url  = "https://iamannitian.co.in/test/send_notification.php";
            StringRequest sr = new StringRequest(1, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            String response_array[] = response.split(",");
                            if(response_array[0].equals("1"))
                            {
                                send_notify.setText("");
                            }

                            Toast.makeText(getApplicationContext(),response_array[1], Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() { //error
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), "sent fail", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map =  new HashMap<>();
                    map.put("notifyKey", send_notify.getText().toString().trim());
                    return map;
                }
            };

            RequestQueue rq = Volley.newRequestQueue(SendNotification.this);
            rq.add(sr);
        }

    }



}
