package com.mvopo.memoir.Interface;

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
    }

    public interface settingAction{
        View.OnClickListener getThemeColorListener();
    }
}
