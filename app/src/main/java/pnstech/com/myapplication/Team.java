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
import com.pnstech.myapplication.ReturnTeamTags;
import com.pnstech.myapplication.TeamAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Team extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeamAdapter teamAdapter;
    private ArrayList<ReturnTeamTags> mList;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    public  static  String USER_TYPEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);


        recyclerView =  findViewById(R.id.team_recycler_view);
        recyclerView.setHasFixedSize(true); //recycler view dont change its width and height
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        mList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        USER_TYPEY = sharedPreferences.getString("userType", "");
        
    }


    private void parseJson()
    {
        String url_n = "https://iamannitian.co.in/test/get_team.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_n, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("team_data");

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String memberIdx = data.getString("id");
                                String memberNamex = data.getString("member_name");
                                String memberPositionx = data.getString("member_position");
                                String memberPhonex = data.getString("member_phone");
                                String urlx = "https://iamannitian.co.in/test/team/"+data.getString("url");

                                mList.add(new ReturnTeamTags(urlx, memberNamex, memberPositionx, memberIdx, memberPhonex));
                            }

                            teamAdapter = new TeamAdapter(Team.this,mList);
                            recyclerView.setAdapter(teamAdapter);

                        } catch (JSONException e) {
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
