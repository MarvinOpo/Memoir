package com.mvopo.memoir.Presenter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import com.mvopo.memoir.Interface.SettingContract;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.R;

public class SettingPresenter implements SettingContract.settingAction {

    private SettingContract.settingView settingView;

    public SettingPresenter(SettingContract.settingView settingView) {
        this.settingView = settingView;
    }


    @Override
    public View.OnClickListener getThemeColorListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingView.resetParams();
                settingView.selectThemeColor(view);

                switch (view.getId()){
                    case R.id.theme_pink:
                        settingView.setContainerColor(R.color.pink, R.color.pinkAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Pink);
                        break;
                    case R.id.theme_cyan:
                        settingView.setContainerColor(R.color.cyan, R.color.cyanAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Cyan);
                        break;
                    case R.id.theme_violet:
                        settingView.setContainerColor(R.color.violet, R.color.violetAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Violet);
                        break;
                    case R.id.theme_red:
                        settingView.setContainerColor(R.color.red, R.color.redAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Red);
                        break;
                    case R.id.theme_green:
                        settingView.setContainerColor(R.color.green, R.color.greenAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Green);
                        break;
                    case R.id.theme_gray:
                        settingView.setContainerColor(R.color.gray, R.color.grayAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Gray);
                        break;
                    case R.id.theme_black:
                        settingView.setContainerColor(R.color.blackPrimary, R.color.blackAccent);
                        settingView.setSelectedTheme(R.style.AppTheme_Black);
                        break;
                }
            }
        };

        return listener;
    }

    public void checkPermission() {
        if (settingView.shouldCheckPermission()) {
            if (!settingView.isStorageGranted()) {
                if (settingView.shouldShowPermissionRequest()) {
                    settingView.showPermissionDialog();
                } else {
                    settingView.requestPermission();
                }
            }else{
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                settingView.startIntent(intent);
            }
        }
    }

    @Override
    public void onPermissionResult(int requestCode, int[] grantResults) {
        switch (requestCode){
            case Constants.READ_STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    settingView.showPermissionDialog();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case Constants.IMAGE_PICK_CODE:
                    String path = data.getData().toString();
                    settingView.loadImage(path);
                    break;
            }
        }
    }
}
