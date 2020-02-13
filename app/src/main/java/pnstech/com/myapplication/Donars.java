package pnstech.com.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pnstech.myapplication.DonarAdapter;
import com.pnstech.myapplication.DonarAdapter;
import com.pnstech.myapplication.ReturnDonarTags;
import com.pnstech.myapplication.ReturnDonarTags;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Donars extends AppCompatActivity {


    private RecyclerView donation_recycler_view;

    private SharedPreferences sharedPreferences;

    private DonarAdapter donarAdapter;
    private ArrayList<ReturnDonarTags> mList;
    private RequestQueue requestQueue;


    private Typeface myfont;
    private TextView donar_quote;

    public static String USER_TYPEZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donars);
        
        donar_quote = (TextView) findViewById(R.id.donar_quote);
        myfont=Typeface.createFromAsset(this.getAssets(),"fonts/greatvibe.otf");
        donar_quote.setTypeface(myfont);


        donation_recycler_view = findViewById(R.id.donation_recycler_view);
        donation_recycler_view.setHasFixedSize(true);
        donation_recycler_view.setLayoutManager(new LinearLayoutManager((this)));


        mList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();

        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        USER_TYPEZ = sharedPreferences.getString("userType", "");
        
    }



    public void parseJson()
    {

        String url_n = "https://iamannitian.co.in/test/get_donar.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_n, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("donar_data");

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String donationIdx = data.getString("id");
                                String donationAmmountx = data.getString("donation_ammount");
                                String donationDatex = data.getString("donation_date");
                                String donarNamex = data.getString("donar_name");

                                mList.add(new ReturnDonarTags(donarNamex, donationAmmountx, donationDatex, donationIdx ));
                            }

                            donarAdapter = new DonarAdapter(Donars.this,mList);
                            donation_recycler_view.setAdapter(donarAdapter);

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
    
    public void gotToPayment(View view)
    {
        startActivity(new Intent(Donars.this, Donate.class));
    }

}
