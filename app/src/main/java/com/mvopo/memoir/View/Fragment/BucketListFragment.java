package com.mvopo.memoir.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mvopo.memoir.Helper.BucketAdapter;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.R;

import java.util.ArrayList;

public class BucketListFragment extends Fragment {

    private ViewPager bucketPager;
    private Button filterPendingBtn, filterDoneBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bucketlist, container, false);

        bucketPager = view.findViewById(R.id.bucket_pager);
        filterPendingBtn = view.findViewById(R.id.filter_pending);
        filterDoneBtn = view.findViewById(R.id.filter_done);
        filterPendingBtn.setSelected(true);

        ArrayList<BucketItem> list = new ArrayList<>();

        BucketItem item = new BucketItem();
        item.setTitle(getContext().getResources().getString(R.string.sample_title));
        item.setBody("The quick brown fox jumps over a lazy dog.");

        list.add(item);

        for(int i = 0; i < 4; i++){
            item = new BucketItem();
            item.setTitle(getContext().getResources().getString(R.string.sample_title));
            item.setBody(getContext().getResources().getString(R.string.sample_body));

            list.add(item);
        }

        BucketAdapter adapter = new BucketAdapter(getContext(), list);
        bucketPager.setAdapter(adapter);
        bucketPager.setClipToPadding(false);

        return view;
    }
}
