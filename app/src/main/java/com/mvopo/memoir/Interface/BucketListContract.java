package com.mvopo.memoir.Interface;

import android.view.View;

import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;

import java.util.List;

public class BucketListContract {

    public interface bucketView{
        BucketItemDao getBucketDaoInstance();

        void addButtonListeners();
        void addNewBucket();

        void setPagerItems(List<BucketItem> bucketList);
    }

    public interface bucketAction{
        View.OnClickListener getClickListener();

        void getBucketList();
    }
}
