package com.mvopo.memoir.Interface;

import android.content.Intent;
import android.view.View;

public class SettingContract {

    public interface settingView{
        void initThemeListener();

        void resetParams();

        void unselectThemeColors();
        void selectThemeColor(View view);

        void setContainerColor(int colorID, int accentID);

        void setSelectedTheme(int styleID);
        void startMainIntent();

        boolean isStorageGranted();
        boolean shouldCheckPermission();
        boolean shouldShowPermissionRequest();
        void requestPermission();
        void showPermissionDialog();

        void startIntent(Intent intent);

        void loadImage(String path);
    }

    public interface settingAction{
        View.OnClickListener getThemeColorListener();

        void checkPermission();
        void onPermissionResult(int requestCode, int[] grantResults);


        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
