package com.mvopo.memoir.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.R;

import java.util.ArrayList;

public class BucketAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BucketItem> bucketItems;

    public BucketAdapter(Context context, ArrayList<BucketItem> bucketItems) {
        this.context = context;
        this.bucketItems = bucketItems;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bucketItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.bucket_item, container, false);

        TextView bodyTv = view.findViewById(R.id.bucket_item_body);

        bodyTv.setText(bucketItems.get(position).getBody());

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
