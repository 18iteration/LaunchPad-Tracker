package com.example.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mImageUrls;
    private ArrayList<String> mNames;
    private ArrayList<String> mDates;
    private ArrayList<String> mYoutubeLink;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> imageUrls, ArrayList<String> names, ArrayList<String> dates, ArrayList<String> youtubeLink) {
        mImageUrls = imageUrls;
        mNames = names;
        mDates = dates;
        mYoutubeLink = youtubeLink;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);

        holder.name.setText(mNames.get(position));
        holder.date.setText(mDates.get(position));
        holder.youtubeLink.setText(mYoutubeLink.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name;
        TextView date;
        TextView youtubeLink;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            youtubeLink = itemView.findViewById(R.id.youtubeLink);
        }
    }
}