package com.mvopo.memoir.Presenter;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.mvopo.memoir.Interface.MainContract;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.R;

public class MainPresenter implements MainContract.mainAction {

    MainContract.mainView mainView;

    public MainPresenter(MainContract.mainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void checkPermission() {
        if (mainView.shouldCheckPermission()) {
            if (!mainView.isStorageGranted()) {
                if (mainView.shouldShowPermissionRequest()) {
                    mainView.showPermissionDialog();
                } else {
                    mainView.requestPermission();
                }
            }
        }
    }

    @Override
    public void onPermissionResult(int requestCode, int[] grantResults) {
        switch (requestCode){
            case Constants.READ_STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    mainView.showPermissionDialog();
                }
        }
    }

    @Override
    public BottomNavigationView.OnNavigationItemSelectedListener getBottomNavListener() {
        BottomNavigationView.OnNavigationItemSelectedListener listener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_nav_journal:
                                mainView.showFragment(mainView.getJournalFragment());
                                break;
                            case R.id.bottom_nav_bucket:
                                mainView.showFragment(mainView.getBucketListFragment());
                                break;
                            case R.id.bottom_nav_todo:
                                break;
                        }

                        return true;
                    }
                };

        return listener;
    }
}
