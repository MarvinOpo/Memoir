package com.mvopo.memoir.Helper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.R;
import com.mvopo.memoir.View.Fragment.BucketDetailFragment;
import com.mvopo.memoir.View.MainActivity;

import java.util.List;

public class BucketAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<BucketItem> bucketItems;

    public BucketAdapter(Context context, List<BucketItem> bucketItems) {
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
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.bucket_item, container, false);

        TextView titleTv = view.findViewById(R.id.bucket_item_title);
        TextView categDiffTv = view.findViewById(R.id.bucket_item_category_difficulty);
        TextView bodyTv = view.findViewById(R.id.bucket_item_body);

        ImageView bucketImageIv = view.findViewById(R.id.bucket_item_image);
        ImageView doneIv = view.findViewById(R.id.bucket_item_done);

        BucketItem item = bucketItems.get(position);
        titleTv.setText(item.getTitle());

        String category = item.getCategory();
        String difficulty = item.getDifficulty();

        if(category.isEmpty()) category = "Category";
        if(difficulty.isEmpty()) difficulty = "Difficulty";
        if(item.getIsDone()) doneIv.setVisibility(View.VISIBLE);

        String categDiff = category + " | " + difficulty;

        categDiffTv.setText(categDiff);
        bodyTv.setText(item.getBody());

        GlideApp.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.no_photo)
                .error(R.drawable.no_photo)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bucketImageIv);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDetail(position);
            }
        });
        bodyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemDetail(position);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    public void showItemDetail(int position){
        Bundle bundle = new Bundle();

        if(bucketItems.get(position).getId() == null) {
            bundle.putParcelable("bucketItem", null);
        }else{
            bundle.putParcelable("bucketItem", bucketItems.get(position));
        }

        BucketDetailFragment bdf = new BucketDetailFragment();
        bdf.setArguments(bundle);

        FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragment_container, bdf)
                .addToBackStack(null)
                .commit();
    }
}
