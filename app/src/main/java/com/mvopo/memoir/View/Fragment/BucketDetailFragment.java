package com.mvopo.memoir.View.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mvopo.memoir.Helper.GlideApp;
import com.mvopo.memoir.Interface.BucketDetailContract;
import com.mvopo.memoir.Model.BucketItem;
import com.mvopo.memoir.Model.BucketItemDao;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.Model.DBApplication;
import com.mvopo.memoir.Presenter.BucketDetailPresenter;
import com.mvopo.memoir.R;

import java.io.File;

public class BucketDetailFragment extends Fragment implements BucketDetailContract.detailView {

    private BucketDetailPresenter presenter;

    private ImageView bucketImageIv;
    private EditText titleEdtx, bodyEdtx;
    private TextView categoryTv, difficultyTv;
    private Button saveBtn, editBtn, doneBtn;
    private RadioGroup optionGroup;
    private LinearLayout.LayoutParams rbParam;

    private BucketItem bucketItem;
    private String imgPath;

    private View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, Constants.IMAGE_PICK_CODE);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bucketItem = getArguments().getParcelable("bucketItem");

        presenter = new BucketDetailPresenter(this);

        View view = inflater.inflate(R.layout.fragment_bucket_detail, container, false);

        bucketImageIv = view.findViewById(R.id.bucket_detail_image);
        titleEdtx = view.findViewById(R.id.bucket_detail_title);
        bodyEdtx = view.findViewById(R.id.bucket_detail_body);
        categoryTv = view.findViewById(R.id.bucket_detail_category);
        difficultyTv = view.findViewById(R.id.bucket_detail_difficulty);

        saveBtn = view.findViewById(R.id.bucket_detail_save);
        editBtn = view.findViewById(R.id.bucket_detail_edit);
        doneBtn = view.findViewById(R.id.bucket_detail_done);

        bucketImageIv.setOnClickListener(imageClickListener);
        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionDialog(R.array.category, categoryTv);
            }
        });
        difficultyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionDialog(R.array.difficulty, difficultyTv);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveBucket();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveBtn();
            }
        });

        presenter.checkBucketItem(bucketItem);

        titleEdtx.setOnFocusChangeListener(presenter.getFocusListener());
        bodyEdtx.setOnFocusChangeListener(presenter.getFocusListener());
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public BucketItem getUpdatedBucket() {
        bucketItem.setImage(imgPath);
        bucketItem.setTitle(titleEdtx.getText().toString().trim());
        bucketItem.setBody(bodyEdtx.getText().toString().trim());
        bucketItem.setCategory(categoryTv.getText().toString().trim());
        bucketItem.setDifficulty(difficultyTv.getText().toString().trim());
        bucketItem.setIsDone(doneBtn.isSelected());

        return bucketItem;
    }

    @Override
    public BucketItem getNewBucket() {
        BucketItem bucketItem = new BucketItem();
        bucketItem.setImage(imgPath);
        bucketItem.setTitle(titleEdtx.getText().toString().trim());
        bucketItem.setBody(bodyEdtx.getText().toString().trim());
        bucketItem.setCategory(categoryTv.getText().toString().trim());
        bucketItem.setDifficulty(difficultyTv.getText().toString().trim());
        bucketItem.setIsDone(doneBtn.isSelected());

        return bucketItem;
    }

    @Override
    public BucketItemDao getBucketDaoInstance() {
        DBApplication dbApp = (DBApplication) getActivity().getApplicationContext();
        BucketItemDao bucketDao = dbApp.getDaoSession().getBucketItemDao();
        return bucketDao;
    }

    @Override
    public void displayBucket() {

        loadImage(bucketItem.getImage());

        titleEdtx.setText(bucketItem.getTitle());
        bodyEdtx.setText(bucketItem.getBody());
        categoryTv.setText(bucketItem.getCategory());
        difficultyTv.setText(bucketItem.getDifficulty());

        hideSaveBtn();
    }

    @Override
    public void showSaveBtn() {
        saveBtn.setVisibility(View.VISIBLE);
        editBtn.setVisibility(View.GONE);
        doneBtn.setVisibility(View.GONE);

        enableFields();
    }

    @Override
    public void hideSaveBtn() {
        saveBtn.setVisibility(View.GONE);
        editBtn.setVisibility(View.VISIBLE);
        doneBtn.setVisibility(View.VISIBLE);

        disableFields();
    }

    @Override
    public void disableFields() {
        bucketImageIv.setOnClickListener(null);
        titleEdtx.setEnabled(false);
        bodyEdtx.setEnabled(false);
        categoryTv.setEnabled(false);
        difficultyTv.setEnabled(false);
    }

    @Override
    public void enableFields() {
        bucketImageIv.setOnClickListener(imageClickListener);
        titleEdtx.setEnabled(true);
        bodyEdtx.setEnabled(true);
        categoryTv.setEnabled(true);
        difficultyTv.setEnabled(true);

        titleEdtx.requestFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(titleEdtx, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void popFragment() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        getActivity().getSupportFragmentManager().popBackStack();

    }

    @Override
    public void loadImage(String path) {
        imgPath = path;

        GlideApp.with(getContext())
                .load(path)
                .placeholder(R.drawable.add_image)
                .error(R.drawable.add_image)
                .centerCrop()
                .into(bucketImageIv);
    }

    @Override
    public void addDivider() {
        View lineView = new View(getContext());
        lineView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        lineView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));

        optionGroup.addView(lineView);
    }

    @Override
    public void addRadioButton(String optionText) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setText(optionText);
        radioButton.setLayoutParams(rbParam);

        optionGroup.addView(radioButton);
    }

    @Override
    public void showOptionDialog(int resource, final TextView targetView) {
        optionGroup = new RadioGroup(getContext());
        rbParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rbParam.setMargins(5, 5, 5, 5);

        presenter.populateRadioGroup(getResources().getStringArray(resource));

        View view = getLayoutInflater().inflate(R.layout.options_dialog, null);
        ScrollView optionHolder = view.findViewById(R.id.option_holder);
        Button optionBtn = view.findViewById(R.id.optionBtn);

        optionHolder.addView(optionGroup);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        final AlertDialog optionDialog = builder.create();
        optionDialog.show();

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton rb = optionGroup.findViewById(optionGroup.getCheckedRadioButtonId());
                presenter.onOptionSelected(rb, targetView);
                optionDialog.dismiss();
            }
        });
    }
}
