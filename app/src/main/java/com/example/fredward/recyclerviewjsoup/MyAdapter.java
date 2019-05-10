package com.example.fredward.recyclerviewjsoup;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    //Create private variables that will be passed and used
    private static final String TAG = "MyAdapter.class";
    private ArrayList<String> mData;
    private ArrayList<String> mimageLinks;
    Context mContext;
    private OnClickResponse monClickResponse;
    private String URL = "https://www.wallpapermaiden.com/category/anime/1080x1920?page=";
    int pageCount;

    //Class constructor context, name of the images, and the image source,context again for onclicklistener
    public MyAdapter(Context mcontext, ArrayList<String> webData, ArrayList<String> images, OnClickResponse onClickResponse) {
        this.mData = webData;
        this.mimageLinks = images;
        mContext = mcontext;
        this.monClickResponse = onClickResponse;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        //this will test and determine which layout will be inflated pased on the position
        if (i == R.layout.recyclerview_rows) {
            Log.i(TAG, "Start/Continue with the images");
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_rows,
                    viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.next_button,
                    viewGroup, false);
        }
        return new MyViewHolder(view, monClickResponse);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder viewHolder, final int i) {

        //this will set an onClick on the button
        if (i == mimageLinks.size()) {
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //after it has been clicked i need to re-fetch data and
                    //display page two properly....
                    Log.i(TAG, "the next button has been clicked");
                    pageCount = pageCount + 1;
                    Log.i(TAG, "Website: " + URL + pageCount);
                    Intent intent = new Intent(mContext,NextPage.class);
                    intent.putExtra("pageCount",pageCount);
                    mContext.startActivity(intent);
                }
            });
        } else {
            //if not at the end of the mImageLinks then continue populating the
            //recyclerview
            //Attach the data to each of the Views
            Log.i(TAG, "fetching data...");
            //Use Glide to set image
            final String images = mimageLinks.get(i);
            final String imageName = mData.get(i);
            Glide.with(mContext)
                    .asBitmap()
                    .load(images)
                    .into(viewHolder.imageView);
            //set the name of the current image being displayed
            viewHolder.textView.setText(imageName);

        }


    }

    //this +1 is used to represent the button, so once its at the end+1 it will show button
    @Override
    public int getItemCount() {
        return mimageLinks.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;
        //This is used for on click listener
        LinearLayout parentLayout;
        OnClickResponse onClickResponse;
        Button button;

        public MyViewHolder(@NonNull View itemView, OnClickResponse onClickResponse) {
            super(itemView);
            //get the views that are in recyclerView_row layout
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
            parentLayout = itemView.findViewById(R.id.parentPanel);
            button = itemView.findViewById(R.id.NextButton);
            this.onClickResponse = onClickResponse;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //determines which one was clicked
            onClickResponse.OnResponseClick(getAdapterPosition());

        }
    }

    /**
     * @param position gets the position to determine if at the end of the page
     *                 This will be used to display or hide the next button
     * @return the layout next_button if it is at the end of mImageLinks
     */

    @Override
    public int getItemViewType(int position) {
        //this is to check if the it is at the end of the recyclerview_rows
        //which will then return button layout
        return (position == mimageLinks.size()) ? R.layout.next_button : R.layout.recyclerview_rows;
    }

    //create an interface to handle onclick methods
    public interface OnClickResponse {

        void OnResponseClick(int i);
    }

}
