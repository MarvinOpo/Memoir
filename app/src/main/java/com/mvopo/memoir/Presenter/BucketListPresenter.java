package com.mvopo.memoir.Presenter;

import android.view.View;

import com.mvopo.memoir.Interface.BucketListContract;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.R;

import java.util.ArrayList;
import java.util.List;

public class BucketListPresenter implements BucketListContract.bucketAction {

    private BucketListContract.bucketView bucketView;
    private ArrayList<String> queryFilter = new ArrayList<>();

    public BucketListPresenter(BucketListContract.bucketView bucketView) {
        this.bucketView = bucketView;
    }

    @Override
    public View.OnClickListener getClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String filter = "";

                switch (view.getId()) {
                    case R.id.filter_pending:
                        filter = "IS_DONE = '0'";
                        break;
                    case R.id.filter_done:
                        filter = "IS_DONE = '1'";
                        break;
                    case R.id.filter_easy:
                        filter = "DIFFICULTY = 'Easy'";
                        break;
                    case R.id.filter_medium:
                        filter = "DIFFICULTY = 'Medium'";
                        break;
                    case R.id.filter_hard:
                        filter = "DIFFICULTY = 'Hard'";
                        break;
                    case R.id.filter_adventure:
                        filter = "CATEGORY = 'Adventure'";
                        break;
                    case R.id.filter_entertainment:
                        filter = "CATEGORY = 'Entertainment'";
                        break;
                    case R.id.filter_personal:
                        filter = "CATEGORY = 'Personal'";
                        break;
                    case R.id.filter_travel:
                        filter = "CATEGORY = 'Travel'";
                        break;
                }

                if (view.isSelected()) {
                    view.setSelected(false);
                    queryFilter.remove(filter);
                } else {
                    view.setSelected(true);
                    queryFilter.add(filter);
                }

                getBucketList();
            }
        };

        return listener;
    }

    @Override
    public void getBucketList() {
        BucketItemDao bucketDao = bucketView.getBucketDaoInstance();

        String whereClause = "";

        if (!queryFilter.isEmpty()){
            for(int i = 0; i < queryFilter.size(); i++){
                if(whereClause.isEmpty()){
                    whereClause += "WHERE " + queryFilter.get(i);
                }else {
                    whereClause += " OR " + queryFilter.get(i);
                }
            }
        }

        List<BucketItem> bucketList = bucketDao.queryRaw(whereClause + " ORDER BY _id desc");

        BucketItem item = new BucketItem();
        item.setImage("");
        item.setTitle("");
        item.setBody("");
        item.setCategory("");
        item.setDifficulty("");
        item.setIsDone(false);
        item.setId(null);

        bucketList.add(item);

        bucketView.setPagerItems(bucketList);
    }
}
