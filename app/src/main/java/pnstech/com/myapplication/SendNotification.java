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

    private BottomNavigationView bottomNavigationView;
    private View notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        bottomNavigationView = findViewById(R.id.navigation_view);


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

        //===========================  bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.notify:
                        removeBadge();
                        notificationBadge.setVisibility(GONE);
                        startActivity(new Intent(SendNotification.this, pnstech.com.myapplication.Notification.class));
                        break;

                    case R.id.donars:
                        startActivity(new Intent(SendNotification.this, Donars.class));
                        break;

                    case R.id.settings:
                        startActivity(new Intent(SendNotification.this, Settings.class));
                        break;

                    case R.id.library:
                        startActivity(new Intent(SendNotification.this, MainLibrary.class));
                        break;

                }
                return true;
            }
        });


        showBadge();

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

        RequestQueue rq = Volley.newRequestQueue(SendNotification.this);
        rq.add(sr);
    }

    public void showBadge() //show notfication badge
    {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(0);
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

    //testing
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.send_notify:
                startActivity(new Intent(SendNotification.this,UploadBooks.class));
                break;

            case R.id.add_donar:
                startActivity(new Intent(SendNotification.this,AddDoner.class));
                break;

            case R.id.add_member:
                startActivity(new Intent(SendNotification.this,AddTeamMember.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //testing


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

}
