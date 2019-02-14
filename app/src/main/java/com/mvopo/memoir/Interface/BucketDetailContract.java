package com.mvopo.memoir.Interface;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;

public class BucketDetailContract {

    public interface detailView{
        BucketItem getUpdatedBucket();
        BucketItem getNewBucket();
        BucketItemDao getBucketDaoInstance();

        void displayBucket();

        void showSaveBtn();
        void hideSaveBtn();

        void disableFields();
        void enableFields();

        void popFragment();
        void loadImage(String path);

        void addRadioButton(String optionText);
        void addDivider();

        void showOptionDialog(int resource, TextView targetView);
    }

    public interface detailAction{
        View.OnFocusChangeListener getFocusListener();

        void checkBucketItem(BucketItem bucketItem);

        void saveBucket();
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void populateRadioGroup(String[] options);
        void onOptionSelected(RadioButton rb, TextView targetView);
    }
}
