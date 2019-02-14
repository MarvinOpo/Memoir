package com.mvopo.memoir.Interface;

import android.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;

import org.w3c.dom.Text;

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
