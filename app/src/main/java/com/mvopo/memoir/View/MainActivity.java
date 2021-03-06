package com.mvopo.memoir.View;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.mvopo.memoir.Helper.AlarmReceiver;
import com.mvopo.memoir.Helper.BootReceiver;
import com.mvopo.memoir.Interface.MainContract;
import com.mvopo.memoir.Model.Constants;
import com.mvopo.memoir.Presenter.MainPresenter;
import com.mvopo.memoir.R;
import com.mvopo.memoir.View.Fragment.BucketListFragment;
import com.mvopo.memoir.View.Fragment.JournalFragment;

public class MainActivity extends AppCompatActivity implements MainContract.mainView {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private BottomNavigationView bottom_nav;
    private MainPresenter presenter;

    private JournalFragment journalFragment;
    private BucketListFragment bucketlistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int themeStyle = getIntent().getExtras().getInt("theme");
        setTheme(themeStyle);

        setContentView(R.layout.activity_main);

        journalFragment = new JournalFragment();
        bucketlistFragment = new BucketListFragment();

        fragmentManager = getSupportFragmentManager();
        showFragment(journalFragment);

        presenter = new MainPresenter(this);

        bottom_nav = findViewById(R.id.navigation);
        bottom_nav.setOnNavigationItemSelectedListener(presenter.getBottomNavListener());

        presenter.checkPermission();
        presenter.checkAlarmPermission();
    }

    @Override
    public JournalFragment getJournalFragment() {
        return journalFragment;
    }

    @Override
    public BucketListFragment getBucketListFragment() {
        return bucketlistFragment;
    }

    @Override
    public boolean notificationAllowed() {
        SharedPreferences myPref = this.getSharedPreferences("Memoir", MODE_PRIVATE);
        boolean allowed = myPref.getBoolean("notification", true);

        return allowed;
    }

    @Override
    public boolean alarmIsUp() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.setAction(Constants.ALARM_ACTION);

        boolean alarmUp = (PendingIntent.getBroadcast(this, Constants.ALARM_CODE, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);

        return alarmUp;
    }

    @Override
    public boolean isStorageGranted() {
        return ActivityCompat.checkSelfPermission( this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ;
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
    public void showFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void startAlarmIntent() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.setAction(Constants.ALARM_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Constants.ALARM_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        presenter.setNotifier(manager, pendingIntent);
    }

    @Override
    public void setComponentSetting() {
        ComponentName bootReceiver = new ComponentName(this, BootReceiver.class);
        PackageManager packageManager = getPackageManager();
        packageManager.setComponentEnabledSetting(bootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onPermissionResult(requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit app?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
