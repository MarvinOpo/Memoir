package com.mvopo.memoir.Interface;

import android.content.Intent;
import android.net.Uri;
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
        String getTitle();
        String getBody();
        Long getBucketId();
        boolean isBucketDone();

        void displayBucket();

        void showSaveBtn();
        void hideSaveBtn();

        void showKeyboard();
        void hideKeyboard();

        void disableFields();
        void enableFields();

        void popFragment();
        void loadImage(String path);

        void addRadioButton(String optionText);
        void addDivider();

        void toastMessage(String message);

        void showOptionDialog(int resource, TextView targetView);
        void startIntent(Intent intent);

        void setAccentColor();
        void setStampVisibility(int visibility);

        void takePersistentPermission(Uri imgUri);
    }

    public interface detailAction{
        View.OnFocusChangeListener getFocusListener();
        View.OnClickListener getClickListener();

        void checkBucketItem(BucketItem bucketItem);

        void validateBucket();
        void saveBucket();
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void populateRadioGroup(String[] options);
        void onOptionSelected(RadioButton rb, TextView targetView);
    }
}
