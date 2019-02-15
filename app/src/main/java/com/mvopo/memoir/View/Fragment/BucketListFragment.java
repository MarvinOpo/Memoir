package com.mvopo.memoir.View.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mvopo.memoir.Helper.BucketAdapter;
import com.mvopo.memoir.Interface.BucketListContract;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.Model.DBApplication;
import com.mvopo.memoir.Presenter.BucketListPresenter;
import com.mvopo.memoir.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

public class BucketListFragment extends Fragment implements BucketListContract.bucketView {

    private BucketListPresenter presenter;

    private ViewPager bucketPager;

    private Button filterPendingBtn, filterDoneBtn, filterEasyBtn,
            filterMediumBtn, filterHardBtn;

    private ShineButton filterAdventureBtn, filterEntertainBtn,
            filterPersonalBtn, filterTravelBtn;

    private FloatingActionButton addBtnFab;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bucketlist, container, false);

        presenter = new BucketListPresenter(this);

        bucketPager = view.findViewById(R.id.bucket_pager);

        filterPendingBtn = view.findViewById(R.id.filter_pending);
        filterDoneBtn = view.findViewById(R.id.filter_done);

        filterEasyBtn = view.findViewById(R.id.filter_easy);
        filterMediumBtn = view.findViewById(R.id.filter_medium);
        filterHardBtn = view.findViewById(R.id.filter_hard);

        filterAdventureBtn = view.findViewById(R.id.filter_adventure);
        filterEntertainBtn = view.findViewById(R.id.filter_entertainment);
        filterPersonalBtn = view.findViewById(R.id.filter_personal);
        filterTravelBtn = view.findViewById(R.id.filter_travel);

        addBtnFab = view.findViewById(R.id.bucket_fab);

        addButtonListeners();
        presenter.getBucketList();

        return view;
    }

    @Override
    public BucketItemDao getBucketDaoInstance() {
        DBApplication dbApp = (DBApplication) getContext().getApplicationContext();
        BucketItemDao bucketDao = dbApp.getDaoSession().getBucketItemDao();
        return bucketDao;
    }

    @Override
    public void addButtonListeners() {
        View.OnClickListener clickListener = presenter.getClickListener();

        filterPendingBtn.setOnClickListener(clickListener);
        filterDoneBtn.setOnClickListener(clickListener);

        filterEasyBtn.setOnClickListener(clickListener);
        filterMediumBtn.setOnClickListener(clickListener);
        filterHardBtn.setOnClickListener(clickListener);

        filterAdventureBtn.setOnClickListener(clickListener);
        filterEntertainBtn.setOnClickListener(clickListener);
        filterPersonalBtn.setOnClickListener(clickListener);
        filterTravelBtn.setOnClickListener(clickListener);

        addBtnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewBucket();
            }
        });
    }

    @Override
    public void addNewBucket() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("bucketItem", null);

        BucketDetailFragment bdf = new BucketDetailFragment();
        bdf.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.fragment_container, bdf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setPagerItems(List<BucketItem> bucketList) {
        BucketAdapter adapter = new BucketAdapter(getContext(), bucketList);
        bucketPager.setAdapter(adapter);
        bucketPager.setClipToPadding(false);
    }
}
