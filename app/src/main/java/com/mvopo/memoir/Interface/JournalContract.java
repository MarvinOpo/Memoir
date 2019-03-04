package com.mvopo.memoir.Interface;

import android.content.ContentResolver;
import android.view.View;
import android.widget.GridView;

import com.mvopo.memoir.Helper.EventDecorator;
import com.mvopo.memoir.Model.Journal;
import com.mvopo.memoir.Model.JournalDao;
import com.mvopo.memoir.Model.MarkedDays;
import com.mvopo.memoir.Model.MarkedDaysDao;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

public class JournalContract {

    public interface journalView{
        ContentResolver getContentResolver();
        JournalDao getJournalDaoInstance();
        MarkedDaysDao getMarkedDaysDao();

        String getCalendarDate();

        void loadPhotos(ArrayList<String> photos);
        void viewPhoto(String path);

        void setJournalDay(String day);
        void setJournalText(String journal);

        void showSaveButton();
        void hideSaveButton();

        void showLoader();
        void hideLoader();

        void hideKeyboard();

        void appendJournalTime(String time);

        boolean isStorageGranted();
        void  showPermissionDialog();

        void reactHeart();
        void reactHappy();
        void reactSad();
        void reactAngry();

        void setDayMood(View view);
        void deactivateReacts();

        void addDecorator(int drawable, ArrayList<String> markedDays);
    }

    public interface journalAction{
        ShineButton.OnClickListener getShineListener();
        void getMarkedList(String mood);

        void onDateSelected(CalendarDay date);
        void onImageClick(int position);
        void onJournalFocusChange(boolean hasFocus);

        void populatePhotos(long startDate, long endDate);

        void readJournal(String date);
        void saveJournal(Journal journal);
        void insertJournal(Journal journal);
        void updateJournal(Journal journal);

        void readMood(String date);
        void saveMood(String mood);
        void insertMood(MarkedDays markedDays);
        void updateMood(MarkedDays markedDays);

        String getMoodString(int viewID);
        int getMoodResource(String mood);

        void adjustGridHeight(GridView gridView);
    }
}
