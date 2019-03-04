package com.mvopo.memoir.Presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.mvopo.memoir.Interface.MainContract;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.R;
import com.mvopo.memoir.View.Fragment.ProfileFragment;

import java.util.Calendar;

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
    public void setNotifier(AlarmManager manager, PendingIntent intent) {
        if(mainView.notificationAllowed()) {
            if(!mainView.alarmIsUp()) {
                Log.e("MainPresenter", "First time alarm set");

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                if (manager != null) {
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent);
                    }
                }

                mainView.setComponentSetting();
            }else{
                Log.e("MainPresenter", "Alarm is already set");
            }
        }else{
            if (manager != null) {
                manager.cancel(intent);
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
                            case R.id.bottom_nav_profile:
                                mainView.showFragment(new ProfileFragment());
                                break;
                        }

                        return true;
                    }
                };

        return listener;
    }

    @Override
    public void checkAlarmPermission() {
        if(mainView.notificationAllowed()){
            mainView.startAlarmIntent();
        }
    }
}
