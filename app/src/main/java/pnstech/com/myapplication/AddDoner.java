package pnstech.com.myapplication;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class AddDoner extends AppCompatActivity {

    private TextView donar_name;
    private TextView donation_ammount;
    private Button click_to_add;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doner);

        progressDialog = new ProgressDialog(this, R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing


        donar_name = (TextView)findViewById(R.id.donar_name);
        donation_ammount = (TextView)findViewById(R.id.donation_ammount);
        click_to_add = (Button) findViewById(R.id.click_to_add);

         click_to_add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 addDoner();
             }
         });

    }


    public void addDoner()
    {
        progressDialog.setMessage("Processing....");
        progressDialog.show();
        // disable user interaction when progressdialog appears
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);


        if(donar_name.getText().toString().equals("") || donation_ammount.getText().toString().equals(""))
        {
            progressDialog.dismiss();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
          //  Toast.makeText(getApplicationContext(), "please write something", Toast.LENGTH_SHORT).show();
        }
        else
        {

            String url  = "https://iamannitian.co.in/test/add_donar.php";
            StringRequest sr = new StringRequest(1, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            String response_array[] = response.split(",");
                            if(response_array[0].equals("1"))
                            {
                                donar_name.setText("");
                                donation_ammount.setText("");
                            }

                            Toast.makeText(getApplicationContext(),response_array[1], Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() { //error
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), "failed to add", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map =  new HashMap<>();
                    map.put("donarKey", donar_name.getText().toString().trim());
                    map.put("ammountKey", donation_ammount.getText().toString().trim());
                    return map;
                }
            };

            RequestQueue rq = Volley.newRequestQueue(AddDoner.this);
            rq.add(sr);
        }


    }

}
