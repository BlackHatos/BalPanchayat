package pnstech.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.pnstech.myapplication.HorizontalRecyclerAdapter;

import java.util.ArrayList;

public class Library extends AppCompatActivity {

    private  static final  String TAG = "MainActivity";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        initImageBitmaps();
    }


    private void initImageBitmaps()
    {
        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/81SBy9jbbHL.jpg");
        mNames.add("the Water Cure");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/9781616208882.jpg");
        mNames.add("Suger Run");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/A1ydJm-AvL.jpg");
        mNames.add("Mouth Of Birds");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/ceb5e9fa5fd3e96f18051f1baf16c62b.jpg");
        mNames.add("Looker");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/A1IHoBxfbGL.jpg");
        mNames.add("Hide and Seek");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/71OAdjrRd8L.jpg");
        mNames.add("Thick");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/Dh0N78JWAAItEWY.jpg");
        mNames.add("Mothers");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/the-cold-is-in-her-bones-9781481488440_hr.jpg");
        mNames.add("The cold");

        mImageUris.add("https://lithub.com/wp-content/uploads/2019/01/81QwY3R3FVL.jpg");
        mNames.add("The Shadow");

        initRecyclerView();
    }


    private void initRecyclerView()
    {
        Log.d(TAG, "initRecyclerView: ");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView =null; //=  findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        HorizontalRecyclerAdapter  adapter = new HorizontalRecyclerAdapter(this, mNames, mImageUris);
        recyclerView.setAdapter(adapter);
    }

}
