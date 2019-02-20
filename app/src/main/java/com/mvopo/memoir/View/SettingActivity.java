package com.mvopo.memoir.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.bumptech.glide.request.RequestOptions;
import com.mvopo.memoir.Helper.GlideApp;
import com.mvopo.memoir.Interface.SettingContract;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.Presenter.SettingPresenter;
import com.mvopo.memoir.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements SettingContract.settingView {

    private SettingPresenter presenter;

    private CircleImageView profileCiv, pinkCiv,
            cyanCiv, violetCiv, redCiv, greenCiv,
            grayCiv, blackCiv;
    private EditText nameEdtx;
    private Switch notificationSwitch;
    private Button saveBtn;

    private ConstraintLayout colorContainer;

    private ViewGroup.LayoutParams pinkParam, cyanParam,
            violetParam, redParam, greenParam, grayParam,
            blackParam;

    private int selectedTheme;
    private String imgPath;

    private float scale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        selectedTheme = getIntent().getIntExtra("theme", R.style.AppTheme_Pink);

        presenter = new SettingPresenter(this);

        colorContainer = findViewById(R.id.color_container);

        profileCiv = findViewById(R.id.profile_image);
        pinkCiv = findViewById(R.id.theme_pink);
        cyanCiv = findViewById(R.id.theme_cyan);
        violetCiv = findViewById(R.id.theme_violet);
        redCiv = findViewById(R.id.theme_red);
        greenCiv = findViewById(R.id.theme_green);
        grayCiv = findViewById(R.id.theme_gray);
        blackCiv = findViewById(R.id.theme_black);

        nameEdtx = findViewById(R.id.setting_name);

        notificationSwitch = findViewById(R.id.notification_switch);
        saveBtn = findViewById(R.id.setting_save);

        scale = this.getResources().getDisplayMetrics().density;
        resetParams();
        selectThemeColor(pinkCiv);
        initThemeListener();

        profileCiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.checkPermission();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myPref = SettingActivity.this.getSharedPreferences("Memoir", MODE_PRIVATE);
                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("name", nameEdtx.getText().toString().trim());
                editor.putString("image", imgPath);
                editor.putBoolean("notification", notificationSwitch.isChecked());
                editor.putInt("theme", selectedTheme);

                editor.apply();
                startMainIntent();
            }
        });

        SharedPreferences myPref = this.getSharedPreferences("Memoir", MODE_PRIVATE);
        String image = myPref.getString("image", "");
        String name = myPref.getString("name", "");

        loadImage(image);
        nameEdtx.setText(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit app?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @Override
    public void initThemeListener() {
        View.OnClickListener themeListener = presenter.getThemeColorListener();

        pinkCiv.setOnClickListener(themeListener);
        cyanCiv.setOnClickListener(themeListener);
        violetCiv.setOnClickListener(themeListener);
        redCiv.setOnClickListener(themeListener);
        greenCiv.setOnClickListener(themeListener);
        grayCiv.setOnClickListener(themeListener);
        blackCiv.setOnClickListener(themeListener);
    }

    @Override
    public void resetParams() {
        final int pixels = (int) (30 * scale + 0.5f);

        pinkParam = pinkCiv.getLayoutParams();
        pinkParam.height = pixels;
        pinkParam.width = pixels;

        cyanParam = cyanCiv.getLayoutParams();
        cyanParam.height = pixels;
        cyanParam.width = pixels;

        violetParam = violetCiv.getLayoutParams();
        violetParam.height = pixels;
        violetParam.width = pixels;

        redParam = redCiv.getLayoutParams();
        redParam.height = pixels;
        redParam.width = pixels;

        greenParam = greenCiv.getLayoutParams();
        greenParam.height = pixels;
        greenParam.width = pixels;

        grayParam = grayCiv.getLayoutParams();
        grayParam.height = pixels;
        grayParam.width = pixels;

        blackParam = blackCiv.getLayoutParams();
        blackParam.height = pixels;
        blackParam.width = pixels;

        unselectThemeColors();
    }

    @Override
    public void unselectThemeColors() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pinkCiv.setLayoutParams(pinkParam);
                cyanCiv.setLayoutParams(cyanParam);
                violetCiv.setLayoutParams(violetParam);
                redCiv.setLayoutParams(redParam);
                greenCiv.setLayoutParams(greenParam);
                grayCiv.setLayoutParams(grayParam);
                blackCiv.setLayoutParams(blackParam);
            }
        });
    }

    @Override
    public void selectThemeColor(View view) {
        final int pixels = (int) (50 * scale + 0.5f);

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = pixels;
        params.width = pixels;

        view.setLayoutParams(params);
    }

    @Override
    public void setContainerColor(int colorID, int accentID) {
        int color = getResources().getColor(colorID);
        int colorAccent = getResources().getColor(accentID);

        colorContainer.setBackgroundColor(color);
        nameEdtx.setHintTextColor(colorAccent);
    }

    @Override
    public void setSelectedTheme(int styleID) {
        selectedTheme = styleID;
    }

    @Override
    public void startMainIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("theme", selectedTheme);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean isStorageGranted() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean shouldCheckPermission() {
        return Build.VERSION.SDK_INT >= 23;
    }

    @Override
    public boolean shouldShowPermissionRequest() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.READ_STORAGE_CODE);
    }

    @Override
    public void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_title);
        builder.setMessage(R.string.permission_message);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Later", null);
        builder.show();
    }

    @Override
    public void startIntent(Intent intent) {
        startActivityForResult(intent, Constants.IMAGE_PICK_CODE);
    }

    @Override
    public void loadImage(String path) {
        imgPath = path;

        GlideApp.with(this)
                .load(path)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .centerCrop()
                .into(profileCiv);
    }
}
