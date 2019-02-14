package com.mvopo.memoir.View.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mvopo.memoir.Helper.EventDecorator;
import com.mvopo.memoir.Helper.ImageAdapter;
import com.mvopo.memoir.Interface.JournalContract;
import com.mvopo.memoir.Model.DBApplication;
import com.mvopo.memoir.Model.Journal;
import com.mvopo.memoir.Model.JournalDao;
import com.mvopo.memoir.Model.MarkedDaysDao;
import com.mvopo.memoir.Presenter.JournalPresenter;
import com.mvopo.memoir.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.File;
import java.util.ArrayList;

public class JournalFragment extends Fragment implements JournalContract.journalView {

    private DBApplication dbApp;
    private JournalPresenter presenter;

    private MaterialCalendarView calendarView;

    private GridView photoContainerGv;
    private TextView dayLabelTv;
    private EditText journalEdtx;
    private ImageView saveBtn;
    private ProgressBar progressBar;

    private ShineButton heartBtn, happyBtn, sadBtn, angryBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        dbApp = (DBApplication) getContext().getApplicationContext();

        presenter = new JournalPresenter(this);

        progressBar = view.findViewById(R.id.journal_loader);

        calendarView = view.findViewById(R.id.journal_calendar);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                presenter.onDateSelected(date);
            }
        });

        dayLabelTv = view.findViewById(R.id.journal_day_label);
        photoContainerGv = view.findViewById(R.id.journal_photo_container);
        photoContainerGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.onImageClick(i);
            }
        });

        journalEdtx = view.findViewById(R.id.journal_edtx);
        journalEdtx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                presenter.onJournalFocusChange(b);
            }
        });

        saveBtn = view.findViewById(R.id.journal_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Journal journal = new Journal();
                journal.setJournalTxt(journalEdtx.getText().toString() + "\n");
                journal.setJournalDate(calendarView.getSelectedDate().getDate().toString());

                presenter.saveJournal(journal);
            }
        });

        heartBtn = view.findViewById(R.id.heart_btn);
        heartBtn.setOnClickListener(presenter.getShineListener());

        happyBtn = view.findViewById(R.id.happy_btn);
        happyBtn.setOnClickListener(presenter.getShineListener());

        sadBtn = view.findViewById(R.id.sad_btn);
        sadBtn.setOnClickListener(presenter.getShineListener());

        angryBtn = view.findViewById(R.id.angry_btn);
        angryBtn.setOnClickListener(presenter.getShineListener());


        calendarView.setSelectedDate(CalendarDay.today());
        presenter.onDateSelected(CalendarDay.today());

        presenter.getMarkedList("heart");
        presenter.getMarkedList("happy");
        presenter.getMarkedList("sad");
        presenter.getMarkedList("angry");
        return view;
    }

    @Override
    public ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }

    @Override
    public JournalDao getJournalDaoInstance() {
        JournalDao journalDao = dbApp.getDaoSession().getJournalDao();
        return journalDao;
    }

    @Override
    public MarkedDaysDao getMarkedDaysDao() {
        MarkedDaysDao markedDaysDao = dbApp.getDaoSession().getMarkedDaysDao();
        return markedDaysDao;
    }

    @Override
    public String getCalendarDate() {
        return calendarView.getSelectedDate().getDate().toString();
    }

    @Override
    public void loadPhotos(final ArrayList<String> photos) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                photoContainerGv.setAdapter(new ImageAdapter(getContext(), photos));
                hideLoader();
            }
        });
    }

    @Override
    public void viewPhoto(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri imageUri = FileProvider.getUriForFile(
                getContext(),
                getContext().getApplicationContext()
                        .getPackageName() + ".provider", new File(path));
        intent.setDataAndType(imageUri, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void setJournalDay(String day) {
        dayLabelTv.setText(day);
    }

    @Override
    public void setJournalText(String journal) {
        journalEdtx.setText(journal);
    }

    @Override
    public void showSaveButton() {
        saveBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSaveButton() {
        saveBtn.setVisibility(View.GONE);
    }

    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideKeyboard() {
        journalEdtx.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void appendJournalTime(String time) {
        journalEdtx.append(time);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                journalEdtx.setSelection(journalEdtx.length());
            }
        }, 20);
    }

    @Override
    public boolean isStorageGranted() {
        return ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.permission_title);
        builder.setMessage(R.string.permission_message);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Later", null);
        builder.show();
    }

    @Override
    public void reactHeart() {
        heartBtn.setChecked(true);
    }

    @Override
    public void reactHappy() {
        happyBtn.setChecked(true);
    }

    @Override
    public void reactSad() {
        sadBtn.setChecked(true);
    }

    @Override
    public void reactAngry() {
        angryBtn.setChecked(true);
    }

    @Override
    public void setDayMood(View view) {
        ((ShineButton) view).setChecked(true);
    }

    @Override
    public void deactivateReacts() {
        heartBtn.setChecked(false);
        happyBtn.setChecked(false);
        sadBtn.setChecked(false);
        angryBtn.setChecked(false);
    }

    @Override
    public void addDecorator(int drawable, ArrayList<String> markedDays) {
        calendarView.addDecorator(new EventDecorator(getContext().getResources().getDrawable(drawable), markedDays));
    }
}
