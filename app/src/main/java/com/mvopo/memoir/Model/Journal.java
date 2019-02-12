package com.mvopo.memoir.Model;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "journal_tbl")
public class Journal {

    @Id(autoincrement = true)
    private Long id;

    @NonNull
    private String journalDate, journalTxt;

    @Generated(hash = 2137538006)
    public Journal(Long id, @NonNull String journalDate,
            @NonNull String journalTxt) {
        this.id = id;
        this.journalDate = journalDate;
        this.journalTxt = journalTxt;
    }

    @Generated(hash = 1562390721)
    public Journal() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJournalDate() {
        return this.journalDate;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }

    public String getJournalTxt() {
        return this.journalTxt;
    }

    public void setJournalTxt(String journalTxt) {
        this.journalTxt = journalTxt;
    }


}
