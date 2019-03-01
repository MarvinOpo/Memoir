package com.mvopo.memoir.View.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mvopo.memoir.Helper.GlideApp;
import com.mvopo.memoir.Interface.ProfileContract;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.Model.DBApplication;
import com.mvopo.memoir.Model.JournalDao;
import com.mvopo.memoir.Model.MarkedDays;
import com.mvopo.memoir.Model.MarkedDaysDao;
import com.mvopo.memoir.Presenter.ProfilePresenter;
import com.mvopo.memoir.R;
import com.mvopo.memoir.View.SettingActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements ProfileContract.profileView {

    private DBApplication dbApp;
    private ProfilePresenter presenter;

    private CircleImageView profileCiv;

    private TextView nameTv;

    private TextView heartCountTv, smileCountTv,
            sadCountTv, angryCountTv, doneCountTv,
            pendingCountTv, totalCountTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbApp = (DBApplication) getContext().getApplicationContext();
        presenter = new ProfilePresenter(this);

        profileCiv = view.findViewById(R.id.profile_image);
        nameTv = view.findViewById(R.id.profile_name);

        heartCountTv = view.findViewById(R.id.heart_count);
        smileCountTv = view.findViewById(R.id.smile_count);
        sadCountTv = view.findViewById(R.id.sad_count);
        angryCountTv = view.findViewById(R.id.angry_count);

        doneCountTv = view.findViewById(R.id.done_count);
        pendingCountTv = view.findViewById(R.id.pending_count);
        totalCountTv = view.findViewById(R.id.total_count);

        SharedPreferences myPref = getContext().getSharedPreferences("Memoir", MODE_PRIVATE);
        String image = myPref.getString("image", "");
        String name = myPref.getString("name", "");

        loadProfile(image, name);

        presenter.getReactCount("heart");
        presenter.getReactCount("happy");
        presenter.getReactCount("sad");
        presenter.getReactCount("angry");

        presenter.getBucketCount(1);
        presenter.getBucketCount(0);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.setting_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getContext(), SettingActivity.class);
        startActivity(intent);
        ((Activity) getContext()).finish();

        return true;
    }

    @Override
    public MarkedDaysDao getMarkedDao() {
        MarkedDaysDao markedDao = dbApp.getDaoSession().getMarkedDaysDao();
        return markedDao;
    }

    @Override
    public BucketItemDao getBucketDao() {
        BucketItemDao bucketDao = dbApp.getDaoSession().getBucketItemDao();
        return bucketDao;
    }

    @Override
    public void setHeartCount(int count) {
        heartCountTv.setText(String.valueOf(count));
    }

    @Override
    public void setSmileCount(int count) {
        smileCountTv.setText(String.valueOf(count));
    }

    @Override
    public void setSadCount(int count) {
        sadCountTv.setText(String.valueOf(count));
    }

    @Override
    public void setAngryCount(int count) {
        angryCountTv.setText(String.valueOf(count));
    }

    @Override
    public void setDoneCount(int count) {
        doneCountTv.setText(String.valueOf(count));
    }

    @Override
    public void setPendingCount(int count) {
        pendingCountTv.setText(String.valueOf(count));
        setTotalCount();
    }

    @Override
    public void setTotalCount() {
        int done = Integer.parseInt(doneCountTv.getText().toString());
        int pending = Integer.parseInt(pendingCountTv.getText().toString());

        totalCountTv.setText(String.valueOf(done + pending));
    }

    @Override
    public void loadProfile(String image, String name) {
        GlideApp.with(getContext())
                .load(image)
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileCiv);

        nameTv.setText(name);
    }
}
