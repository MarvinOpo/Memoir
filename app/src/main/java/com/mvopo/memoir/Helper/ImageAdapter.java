package com.mvopo.memoir.Helper;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mvopo.memoir.R;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> photos;

    public ImageAdapter(Context c, ArrayList<String> photos) {
        mContext = c;
        this.photos = photos;

    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(5, 5, 5, 0);

        if (photos.get(position).equals("No Photo")) {
            imageView.setImageResource(R.drawable.no_photo);
            return imageView;
        }

        Uri uri = Uri.fromFile(new File(photos.get(position)));

        GlideApp.with(mContext)
                .load(uri)
                .placeholder(R.drawable.no_photo)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        return imageView;
    }
}