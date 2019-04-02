package com.example.fredward.recyclerviewjsoup;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;

public class DetailedActivity extends AppCompatActivity {
    private static final String TAG = "Detailed_activity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_layout);
        Log.i(TAG, "detailed activity initiated");
        Button wallpaperButton = findViewById(R.id.button);
        //Set the toolbar that was created in detail_XML
        Toolbar toolbar = findViewById(R.id.FragmentToolbar);
        toolbar.setNavigationIcon(R.drawable.back_button);
        toolbar.setTitle(R.string.fragment_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        if (getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")) {
            //if true then pass the data to a local variable

            String imageURL = getIntent().getStringExtra("image_url");
            String imageName = getIntent().getStringExtra("image_name");

            setImages(imageName, imageURL);
            setWallpaper(imageURL, wallpaperButton);
        }

    }
    private void setImages(String imageName, String Image) {
        TextView nameImage = findViewById(R.id.image_desc);
        nameImage.setText(imageName);
        ImageView imageView = findViewById(R.id.detail_image);

        Glide.with(this)
                .asBitmap()
                .load(Image)
                .into(imageView);
    }

    //TODO: Look over documentation on glide specifically Target and all the overrides
    //TODO: Find if there is a better way to control re-sizeing and reducing pixelated images
    private void setWallpaper(final String image, Button wallpaperButton) {
        wallpaperButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(DetailedActivity.this)
                        .asBitmap()
                        .load(image)
                        .apply(RequestOptions.centerCropTransform())
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try {
                                    WallpaperManager.getInstance(DetailedActivity.this).setBitmap(resource);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }

        });
    }

}
