package com.mvopo.memoir.Interface;

import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.Model.JournalDao;
import com.mvopo.memoir.Model.MarkedDaysDao;

public class ProfileContract {

    public interface profileView{
        MarkedDaysDao getMarkedDao();
        BucketItemDao getBucketDao();

        void setHeartCount(int count);
        void setSmileCount(int count);
        void setSadCount(int count);
        void setAngryCount(int count);

        void setDoneCount(int count);
        void setPendingCount(int count);
        void setTotalCount();

        void loadProfile(String image, String name);
    }

    public interface profileAction{
        void getReactCount(String mood);
        void getBucketCount(int isDone);
    }
}
