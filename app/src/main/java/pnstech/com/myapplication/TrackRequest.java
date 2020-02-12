package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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

public class TrackRequest extends AppCompatActivity {

    private LinearLayout badge_1;
    private LinearLayout badge_2;
    private LinearLayout badge_3;
    private View line_before_approve;
    private View line_before_issue;
    private LinearLayout track_request;

    private TextView book_name;
    private TextView author_name;
    private TextView request_date;
    private TextView approve_date;
    private TextView hours;
    private TextView minutes;
    private TextView seconds;
    private TextView issue_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_request);

        badge_1 = findViewById(R.id.badge_1);
        badge_2 = findViewById(R.id.badge_2);
        badge_3 = findViewById(R.id.badge_3);
        track_request = findViewById(R.id.track_request);
        line_before_approve = findViewById(R.id.line_before_approve);
        line_before_issue = findViewById(R.id.line_before_issue);


        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);
        book_name = findViewById(R.id.book_name);
        author_name = findViewById(R.id.author_name);
        request_date = findViewById(R.id.request_date);
        approve_date = findViewById(R.id.approve_date);
        issue_date = findViewById(R.id.issue_date);


        trackRequest();

    }


    private void trackRequest()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        final String userId = sharedPreferences.getString("userId","");

        String url = "https://www.iamannitian.co.in/test/track_request.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String response_array [] = response.split(",");

                    }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map =  new HashMap<>();
                map.put("idKey",userId );
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(TrackRequest.this);
        rq.add(sr);
    }
}
