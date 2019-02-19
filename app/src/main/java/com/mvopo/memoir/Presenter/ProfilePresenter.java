package com.mvopo.memoir.Presenter;

import android.database.Cursor;
import android.util.Log;

import com.mvopo.memoir.Interface.ProfileContract;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.Model.JournalDao;
import com.mvopo.memoir.Model.MarkedDaysDao;

import java.util.ArrayList;

public class ProfilePresenter implements ProfileContract.profileAction {

    private ProfileContract.profileView profileView;

    private MarkedDaysDao markedDao;
    private BucketItemDao bucketDao;

    public ProfilePresenter(ProfileContract.profileView profileView) {
        this.profileView = profileView;

        markedDao = profileView.getMarkedDao();
        bucketDao = profileView.getBucketDao();
    }

    @Override
    public void getReactCount(String mood) {
        String sql = "SELECT date FROM marked_days_tbl WHERE mood=?";
        Cursor c = markedDao.getDatabase().rawQuery(sql, new String[]{mood});

        switch (mood){
            case "heart":
                profileView.setHeartCount(c.getCount());
                break;
            case "happy":
                profileView.setSmileCount(c.getCount());
                break;
            case "sad":
                profileView.setSadCount(c.getCount());
                break;
            case "angry":
                profileView.setAngryCount(c.getCount());
                break;
        }

        c.close();
    }

    @Override
    public void getBucketCount(int isDone) {
        String sql = "SELECT is_done FROM bucket_list_tbl WHERE is_done=?";
        Cursor c = bucketDao.getDatabase().rawQuery(sql, new String[]{String.valueOf(isDone)});

        if(isDone == 1){
            profileView.setDoneCount(c.getCount());
        }else{
            profileView.setPendingCount(c.getCount());
        }

        c.close();
    }
}
