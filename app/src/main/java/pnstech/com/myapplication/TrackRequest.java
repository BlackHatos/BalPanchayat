package pnstech.com.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TrackRequest extends AppCompatActivity {

    private LinearLayout badge_1;
    private LinearLayout badge_2;
    private LinearLayout badge_3;
    private View line_before_approve;
    private View line_before_issue;
    private LinearLayout pre_msg;

    private TextView book_name;
    private TextView author_name;
    private TextView request_date;
    private TextView approve_date;
    private TextView issue_date;
    private TextView message;
    private TextView end_msg;

    private ScrollView scrollView;

    private TextView track;
    private Typeface myfont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_request);

        badge_1 = findViewById(R.id.badge_1);
        badge_2 = findViewById(R.id.badge_2);
        badge_3 = findViewById(R.id.badge_3);
        line_before_approve = findViewById(R.id.line_before_approve);
        line_before_issue = findViewById(R.id.line_before_issue);
        pre_msg = findViewById(R.id.pre_msg);


        book_name = findViewById(R.id.book_name);
        author_name = findViewById(R.id.author_name);
        request_date = findViewById(R.id.request_date);
        approve_date = findViewById(R.id.approve_date);
        issue_date = findViewById(R.id.issue_date);
        message = findViewById(R.id.message);
        end_msg = findViewById(R.id.end_msg);

        scrollView = findViewById(R.id.scroll_view);

        track = findViewById(R.id.track);

        myfont= Typeface.createFromAsset(this.getAssets(),"fonts/ArimaMaduraiRegular.otf");
        track.setTypeface(myfont);
        message.setTypeface(myfont);
        end_msg.setTypeface(myfont);


        trackRequest();

    }


    private void trackRequest()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
        final String userId = sharedPreferences.getString("userId","");
        final String track_value = sharedPreferences.getString("track_value","");

        if(track_value.equals("1"))
        {
            pre_msg.setVisibility(GONE);
        }

        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String url = "https://www.iamannitian.co.in/test/track_request.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String response_array [] = response.split("-");

                        if(response_array[0].equals("1"))
                        {



                            book_name.setText(response_array[1]);
                            author_name.setText(response_array[2]);

                            if(response_array[3].equals("1")) //if requested
                            {

                                badge_1.setBackgroundColor(Color.GREEN);
                                request_date.setVisibility(VISIBLE);
                                request_date.setText(response_array[6]);
                                pre_msg.setVisibility(GONE);
                                scrollView.setVisibility(VISIBLE);
                            }
                            else
                            {
                                scrollView.setVisibility(GONE);
                            }

                            if(response_array[4].equals("1")) //if approved
                            {
                                badge_2.setBackgroundColor(Color.GREEN);
                                line_before_approve.setBackgroundColor(Color.GREEN);
                                approve_date.setVisibility(VISIBLE);
                                approve_date.setText(response_array[7]);
                                message.setVisibility(VISIBLE);
                                message.setText("Your request for this book has been approved. " +
                                                 "Please visit the central library to get it.");
                            }
                            if(response_array[5].equals("1")) //if issued
                            {
                                badge_3.setBackgroundColor(Color.GREEN);
                                line_before_issue.setBackgroundColor(Color.GREEN);
                                issue_date.setVisibility(VISIBLE);
                                issue_date.setText(response_array[8]);
                                message.setVisibility(VISIBLE);
                                message.setText("The book has been issued to you. " +
                                                "Please return it back to the central library within 15 days.");
                            }

                        }

                    }
                }, new Response.ErrorListener() {
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

    public void goToRequest(View view)
    {
        startActivity(new Intent(TrackRequest.this, MainLibrary.class));
    }

}
