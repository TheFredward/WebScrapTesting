package com.example.fredward.recyclerviewjsoup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class NextPage extends AppCompatActivity implements MyAdapter.OnClickResponse  {
    private static final String TAG = "NEXT Page data";
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<String> imageSource = new ArrayList<>();
    ArrayList<String> imgNameList = new ArrayList<>();
    String nextURL = "https://www.wallpapermaiden.com/category/anime/1080x1920?page=2";
    private String source, imageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIntent().getStringExtra("pageCount");
        fetchData();
        initRecyclerView();

    }
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recall need to pass this again for onResponseClick method
        adapter = new MyAdapter(this, imgNameList, imageSource, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnResponseClick(int i) {
        final String source = imageSource.get(i);
        final String Name = imgNameList.get(i);
        Log.i(TAG, "onClick listener active");
        //create an intent to pass the information to detailed activity
        //need context and thet designated class
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("image_url", source);
        intent.putExtra("image_name", Name);
        //context is needed to start the activity
        startActivity(intent);
    }

    private void fetchData(){
        Log.i(TAG, "initial fetching of data");
        try {
            //save the HTML of the website
            Document document = Jsoup.connect(nextURL).get();
            //get the image using the src and img css tag
            Elements images = document.getElementsByTag("img");
            for (Element el : images) {
                source = el.absUrl("src");
                imageName = el.attr("alt");
                //adding the image and the name of the image from website
                imgNameList.add(imageName);
                imageSource.add(source);
            }
            Log.i(TAG, "image links(list): " + imageSource.toString());
            Log.i(TAG, "image names: " + imgNameList.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}