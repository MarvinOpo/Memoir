package com.mvopo.memoir.Presenter;

import android.view.View;

import com.mvopo.memoir.Interface.SettingContract;
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
                }
            }
        };

        return listener;
    }
}
