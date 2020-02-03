package pnstech.com.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static pnstech.com.myapplication.MainLibrary.EXTRA_BOOK_AUTHOR;
import static pnstech.com.myapplication.MainLibrary.EXTRA_BOOK_CONTRIBUTER;
import static pnstech.com.myapplication.MainLibrary.EXTRA_BOOK_DATE;
import static pnstech.com.myapplication.MainLibrary.EXTRA_BOOK_ID;
import static pnstech.com.myapplication.MainLibrary.EXTRA_BOOK_NAME;
import static pnstech.com.myapplication.MainLibrary.EXTRA_URL;

public class OnBookClick extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_book_click);

        relativeLayout = findViewById(R.id.relative_layout);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String bookName = intent.getStringExtra(EXTRA_BOOK_NAME);
        String bookAuthor = intent.getStringExtra(EXTRA_BOOK_AUTHOR);
        String bookContributer  = intent.getStringExtra(EXTRA_BOOK_CONTRIBUTER);
        String bookDate = intent.getStringExtra(EXTRA_BOOK_DATE);
        final String bookId = intent.getStringExtra(EXTRA_BOOK_ID); //book id


        ImageView book_cover =(ImageView) findViewById(R.id.book_cover);
        TextView book_name = (TextView) findViewById(R.id.book_name);
        TextView book_author = (TextView) findViewById(R.id.book_author);
        TextView book_contributer = (TextView) findViewById(R.id.book_contributer);
        TextView book_date = (TextView) findViewById(R.id.book_date);
        Button click_to_get_book = (Button) findViewById(R.id.click_to_get_book);


        //book is currently owned by
        TextView current_taker = (TextView) findViewById(R.id.current_taker);


        //load book cover

        Glide.with(this)
                .load(imageUrl)
                .fitCenter()
                .centerInside()
                .into(book_cover);

         book_name.setText(bookName);
         book_author.setText(bookAuthor);
         book_contributer.setText("Contributed by "+bookContributer);
         book_date.setText(bookDate);

         //creating shared preferences to own the book



    click_to_get_book.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getBook(bookId);

        }
    });


        progressDialog = new ProgressDialog(this,R.style.progressDialogTheme);
        progressDialog.setCanceledOnTouchOutside(false); //prevent disappearing

    }


    private void getBook(final String   bookId)
    {
        progressDialog.setMessage("Processing Your Request...");
        progressDialog.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        String url = "https://www.iamannitian.co.in/test/request_for_book.php";
        StringRequest sr = new StringRequest(1, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                         showMessage(response);
                          }
                }, new Response.ErrorListener() { //error
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(getApplicationContext(), "request failed", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
                Map<String, String> map =  new HashMap<>();
                map.put("userIdKey", sharedPreferences.getString("userId", "").trim());
                map.put("bookIdKey", bookId);
                return map;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(OnBookClick.this);
        rq.add(sr);
    }

    public void showMessage(String response)
    {
        final Snackbar snackbar = Snackbar.make(relativeLayout,response,Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
