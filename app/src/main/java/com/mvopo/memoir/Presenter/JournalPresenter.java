package com.mvopo.memoir.Presenter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.mvopo.memoir.Helper.EventDecorator;
import com.mvopo.memoir.Interface.JournalContract;
import com.mvopo.memoir.Model.Journal;
import com.mvopo.memoir.Model.JournalDao;
import com.mvopo.memoir.Model.MarkedDays;
import com.mvopo.memoir.Model.MarkedDaysDao;
import com.mvopo.memoir.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JournalPresenter implements JournalContract.journalAction {

    private JournalContract.journalView journalView;
    private Calendar calendar;

    private ArrayList<String> photos = new ArrayList<>();

    private JournalDao journalDao;
    private Journal journal;

    private MarkedDaysDao markedDaysDao;
    private MarkedDays markedDay;

    public JournalPresenter(JournalContract.journalView journalView) {
        this.journalView = journalView;

        calendar = Calendar.getInstance();
        journalDao = journalView.getJournalDaoInstance();
        markedDaysDao = journalView.getMarkedDaysDao();
    }

    @Override
    public ShineButton.OnClickListener getShineListener() {
        ShineButton.OnClickListener listener = new ShineButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                journalView.deactivateReacts();
                journalView.setDayMood(view);

                String mood = getMoodString(view.getId());
                saveMood(mood);

                ArrayList<String> dates = new ArrayList<>();
                dates.add(journalView.getCalendarDate());

                journalView.addDecorator(getMoodResource(mood), dates);
            }
        };

        return listener;
    }

    @Override
    public void getMarkedList(String mood) {
        ArrayList<String> result = new ArrayList<>();

        String sql = "SELECT date FROM marked_days_tbl WHERE mood=?";

        Cursor c = journalView.getMarkedDaysDao().getDatabase().rawQuery(sql, new String[]{mood});
        try{
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    result.add(c.getString(0));
                    c.moveToNext();
                }
            }

            journalView.addDecorator(getMoodResource(mood), result);
        }catch(Exception e){
            Log.e("JournalPresenter", e.getMessage());
        }

        c.close();
    }

    @Override
    public void onDateSelected(CalendarDay date) {
        journalView.showLoader();

        journalView.deactivateReacts();

        readJournal(date.getDate().toString());
        readMood(date.getDate().toString());

        calendar.set(date.getYear(), (date.getMonth() - 1), date.getDay());

        int day_month = calendar.get(Calendar.DAY_OF_MONTH);
        String day_week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        String journalDate = day_month + " - " + day_week;

        journalView.setJournalDay(journalDate);

        long startDate = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, 1);
        long endDate = calendar.getTimeInMillis();

        populatePhotos(startDate, endDate);
    }

    @Override
    public void onImageClick(int position) {
        if (journalView.isStorageGranted()) {
            String path = photos.get(position);
            if (!path.contains("No Photo")) journalView.viewPhoto(path);

        } else {
            journalView.showPermissionDialog();
        }
    }

    @Override
    public void onJournalFocusChange(boolean hasFocus) {
        if (hasFocus) {
            journalView.showSaveButton();
            String time = DateFormat.format("hh:mm aaa", Calendar.getInstance().getTime()).toString();

            journalView.appendJournalTime("\n" + time + "\n\n");
        } else {
            journalView.hideSaveButton();
        }
    }

    @Override
    public void populatePhotos(final long startDate, final long endDate) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                photos.clear();
                if (journalView.isStorageGranted()) {
                    String imgPath;
                    Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                    String[] projection = {MediaStore.MediaColumns.DATA};

                    Cursor cursor = journalView.getContentResolver().query(uri, projection,
                            MediaStore.Images.Media.DATE_TAKEN + "> ? and " +
                                    MediaStore.Images.Media.DATE_TAKEN + "< ?",
                            new String[]{String.valueOf(startDate), String.valueOf(endDate)}, null);

                    if (cursor != null && cursor.moveToFirst()) {
                        int data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        while (!cursor.isAfterLast()) {
                            imgPath = cursor.getString(data);
                            photos.add(imgPath);

                            cursor.moveToNext();
                        }
                    }
                    cursor.close();
                }

                if (photos.isEmpty()) photos.add("No Photo");

                journalView.loadPhotos(photos);
            }
        });
        thread.start();
    }

    @Override
    public void readJournal(String date) {
        journal = null;
        List<Journal> journalList = journalDao.queryRaw("WHERE journal_date=?", date);

        String journalTxt = "";

        if (!journalList.isEmpty()) {
            journal = journalList.get(0);
            journalTxt = journal.getJournalTxt();
        }

        journalView.setJournalText(journalTxt);
    }

    @Override
    public void saveJournal(Journal journal) {
        if (this.journal == null) {
            insertJournal(journal);
        } else {
            journal.setId(this.journal.getId());
            updateJournal(journal);
        }
    }

    @Override
    public void insertJournal(Journal journal) {
        journalDao.insert(journal);
        journalView.hideKeyboard();
    }

    @Override
    public void updateJournal(Journal journal) {
        journalDao.update(journal);
        journalView.hideKeyboard();
    }

    @Override
    public void readMood(String date) {
        markedDay = null;
        List<MarkedDays> markedDaysList = markedDaysDao.queryRaw("WHERE date=?", date);


        if (!markedDaysList.isEmpty()) {
            markedDay = markedDaysList.get(0);

            switch (markedDay.getMood()) {
                case "heart":
                    journalView.reactHeart();
                    break;
                case "happy":
                    journalView.reactHappy();
                    break;
                case "sad":
                    journalView.reactSad();
                    break;
                case "angry":
                    journalView.reactAngry();
                    break;

            }
        }
    }

    @Override
    public void saveMood(String mood) {
        MarkedDays markedDay = new MarkedDays();
        markedDay.setDate(journalView.getCalendarDate());
        markedDay.setMood(mood);

        if (this.markedDay == null) {
            insertMood(markedDay);
        } else {
            markedDay.setId(this.markedDay.getId());
            updateMood(markedDay);
        }
    }

    @Override
    public void insertMood(MarkedDays markedDays) {
        markedDaysDao.insert(markedDays);
    }

    @Override
    public void updateMood(MarkedDays markedDays) {
        markedDaysDao.update(markedDays);
    }

    @Override
    public String getMoodString(int viewID) {
        String mood = "";

        switch (viewID) {
            case R.id.heart_btn:
                mood = "heart";
                break;
            case R.id.happy_btn:
                mood = "happy";
                break;
            case R.id.sad_btn:
                mood = "sad";
                break;
            case R.id.angry_btn:
                mood = "angry";
                break;
        }

        return mood;
    }

    @Override
    public int getMoodResource(String mood) {
        int resource = 0;

        switch (mood) {
            case "heart":
                resource = R.drawable.heart;
                break;
            case "happy":
                resource = R.drawable.smile;
                break;
            case "sad":
                resource = R.drawable.sad;
                break;
            case "angry":
                resource = R.drawable.angry;
                break;

        }

        return resource;
    }
}
