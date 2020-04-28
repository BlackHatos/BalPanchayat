package pnstech.com.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pnstech.myapplication.NotificationAdapter;
import com.pnstech.myapplication.ReturnNotificationTags;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Notification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<ReturnNotificationTags> mList;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    public static  String USER_TYPEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        recyclerView =  findViewById(R.id.notification_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        mList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        USER_TYPEX = sharedPreferences.getString("userType", "");
    }


    public void parseJson()
    {

        String url_n = "https://iamannitian.co.in/test/get_notification.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_n, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("notify_data");

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String notificationIdx = data.getString("id");
                                String notificationTextx = data.getString("notification");
                                String notificationDatex = data.getString("date");

                                mList.add(new ReturnNotificationTags(notificationIdx, notificationTextx, notificationDatex ));
                            }

                            notificationAdapter = new NotificationAdapter(Notification.this,mList);
                            recyclerView.setAdapter(notificationAdapter);

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener(){
            @Override

            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
}
