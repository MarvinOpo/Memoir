package com.mvopo.memoir.Interface;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

import com.mvopo.memoir.View.Fragment.BucketListFragment;
import com.mvopo.memoir.View.Fragment.JournalFragment;

public class MainContract {

    public interface mainView {

        JournalFragment getJournalFragment();
        BucketListFragment getBucketListFragment();

        boolean isStorageGranted();
        boolean shouldCheckPermission();
        boolean shouldShowPermissionRequest();
        void requestPermission();
        void showPermissionDialog();

        void showFragment(Fragment fragment);

    }
    public interface mainAction {

        void checkPermission();
        void onPermissionResult(int requestCode, int[] grantResults);

        BottomNavigationView.OnNavigationItemSelectedListener getBottomNavListener();
    }
}
