
package pnstech.com.myapplication;

        import android.annotation.SuppressLint;
        import android.content.Intent;

        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.SearchView;
        import android.support.v7.widget.Toolbar;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.Volley;
        import com.pnstech.myapplication.RecyclerViewAdapter;
        import com.pnstech.myapplication.ReturnTags;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.lang.ref.ReferenceQueue;
        import java.util.ArrayList;

        import static com.android.volley.Request.*;

public class SearchActivity extends AppCompatActivity  implements RecyclerViewAdapter.OnItemClickListener{


    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_BOOK_NAME = "bookName";

    public static final String EXTRA_BOOK_AUTHOR = "bookWriter";
    public static final String EXTRA_BOOK_CONTRIBUTER = "bookContributer";
    public static final String EXTRA_BOOK_DATE = "bookDate";
    public static final String EXTRA_BOOK_ID = "bookID";


    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<ReturnTags> mList;
    private RequestQueue requestQueue;

    private EditText search_activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView =  findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true); //recycler view dont change its width and height
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        search_activity = (EditText) findViewById(R.id.search_view);


        search_activity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });



        mList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJson();

        }


        private void filter(String text)
        {
            ArrayList<ReturnTags> filteredList = new ArrayList<>();

            for (ReturnTags item: mList)
            {
              if(item.getBookName().toLowerCase().contains(text.toLowerCase()))
              {
                  filteredList.add(item);
              }
            }

            recyclerViewAdapter.filterList(filteredList);
        }



    private void parseJson()
    {
        String url_n = "https://iamannitian.co.in/test/recycler_view.php";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_n, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("book_data");

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data = jsonArray.getJSONObject(i);

                                String bookIdx = data.getString("id");
                                String bookNamex = data.getString("book_name");
                                String writerNamex = data.getString("writer_name");
                                String contributerNamex = data.getString("contributer_name");
                                String datex = data.getString("datex");
                                String urlx = "https://iamannitian.co.in/test/book_covers/"+data.getString("url");

                                mList.add(new ReturnTags(urlx, bookNamex, writerNamex, contributerNamex, datex, bookIdx));
                            }

                            recyclerViewAdapter = new RecyclerViewAdapter(SearchActivity.this,mList);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            recyclerViewAdapter.setOnItemClickListener(SearchActivity.this);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        //remaining methods
        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public void onItemClick(int position) {
        Intent intent =  new Intent(this, OnBookClick.class);
        ReturnTags clickedItem =  mList.get(position);
        intent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        intent.putExtra(EXTRA_BOOK_NAME, clickedItem.getBookName());
        intent.putExtra(EXTRA_BOOK_AUTHOR, clickedItem.getBookWriter());
        intent.putExtra(EXTRA_BOOK_CONTRIBUTER, clickedItem.getBookContributer());
        intent.putExtra(EXTRA_BOOK_DATE, clickedItem.getBookDate());
        intent.putExtra(EXTRA_BOOK_ID, clickedItem.getbookId());

        startActivity(intent);
    }
}
