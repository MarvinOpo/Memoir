package com.mvopo.memoir.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.mvopo.memoir.Interface.SettingContract;
import com.mvopo.memoir.Presenter.SettingPresenter;
import com.mvopo.memoir.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity implements SettingContract.settingView {

    private SettingPresenter presenter;

    private CircleImageView profileCiv, pinkCiv,
            cyanCiv, violetCiv, redCiv;
    private EditText nameEdtx;
    private Switch notificationSwitch;
    private Button saveBtn;

    private ConstraintLayout colorContainer;

    private ViewGroup.LayoutParams pinkParam, cyanParam,
            violetParam, redParam;

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

        nameEdtx = findViewById(R.id.setting_name);

        notificationSwitch = findViewById(R.id.notification_switch);
        saveBtn = findViewById(R.id.setting_save);

        scale = this.getResources().getDisplayMetrics().density;
        resetParams();
        initThemeListener();

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
}
