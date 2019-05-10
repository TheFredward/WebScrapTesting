package com.example.fredward.recyclerviewjsoup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
//TODO: Follow this link and see if this method can be implemented, ...
// TODO: https://developer.android.com/training/basics/fragments/communicating

//TODO: if possible create a fragment (read up on itt...seems to be for multiple screen sizes etc.
//TODO: create a nav menu (review ANDROIDBASICEXINTENTS
//TODO: Research how to set image as a background, most likely will need extra positions
public class MainActivity extends AppCompatActivity implements MyAdapter.OnClickResponse {
    //TODO:(Completed) copy from the Jsoup practice and then move forward from there
    private static final String TAG = "MainActivity";
    private String URL = "https://www.wallpapermaiden.com/category/anime/768x1280";
    String nextURL = "https://www.wallpapermaiden.com/category/anime/1080x1920?page=";
    private String source, imageName;
    private DrawerLayout mDrawerLayout;
    private Button mButton;
    int pageCount = 1;
    ArrayList<String> imageSource = new ArrayList<>();
    ArrayList<String> imgNameList = new ArrayList<>();
    ProgressDialog progressDialog;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.NextButton);

        //Start and begin fetching the data
        Content content = new Content();
        content.execute();
        //Set the toolbar that was created in activity_main
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout_main);
        //set up the nav drawer
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        //rotation sync handle
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.Nav_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Most_Views:
                        break;
                    case R.id.popular:
                        break;
                    case R.id.screen_size:
                        break;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;

            }
        });


    }


    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initRecyclerView();
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //init fetching of data
            Log.i(TAG, "initial fetching of data");
            try {
                //save the HTML of the website
                Document document = Jsoup.connect(URL).get();
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
            return null;
        }
    }

    void initRecyclerView() {
        Log.i(TAG, "start recycler view");
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recall need to pass this again for onResponseClick method
        adapter = new MyAdapter(this, imgNameList, imageSource, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * @param i position of the item that is clicked in the recycler view
     *          this is used to handle clicks from the main page
     */

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

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
