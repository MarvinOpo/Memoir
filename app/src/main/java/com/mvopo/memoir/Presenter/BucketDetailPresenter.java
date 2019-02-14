package com.mvopo.memoir.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mvopo.memoir.Interface.BucketDetailContract;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.R;

public class BucketDetailPresenter implements BucketDetailContract.detailAction {

    private BucketDetailContract.detailView detailView;
    private boolean forUpdate = false;

    public BucketDetailPresenter(BucketDetailContract.detailView detailView) {
        this.detailView = detailView;
    }

    @Override
    public View.OnFocusChangeListener getFocusListener() {
        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    final EditText edtx = (EditText) view;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edtx.setSelection(edtx.length());
                        }
                    }, 20);

                }
            }
        };

        return listener;
    }

    @Override
    public void checkBucketItem(BucketItem bucketItem) {
        if(bucketItem != null){
            detailView.displayBucket();
            detailView.hideSaveBtn();

            forUpdate = true;
        }
    }

    @Override
    public void saveBucket() {
        BucketItemDao bucketDao = detailView.getBucketDaoInstance();
        BucketItem bucketItem;
        if(forUpdate){
            bucketItem = detailView.getUpdatedBucket();
            bucketDao.update(bucketItem);
        }else{
            bucketItem = detailView.getNewBucket();
            bucketDao.insert(bucketItem);
        }

        detailView.popFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case Constants.IMAGE_PICK_CODE:
                    String path = data.getData().toString();
                    detailView.loadImage(path);
                    break;
            }
        }
    }

    @Override
    public void populateRadioGroup(String[] options) {
        for(int i = 0; i < options.length; i++){
            detailView.addRadioButton(options[i]);
            detailView.addDivider();
        }
    }

    @Override
    public void onOptionSelected(RadioButton rb, TextView targetView) {
        if(rb != null){
            targetView.setText(rb.getText().toString());
        }
    }
}
