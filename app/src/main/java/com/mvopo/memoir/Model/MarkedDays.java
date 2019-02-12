package com.mvopo.memoir.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "marked_days_tbl")
public class MarkedDays {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String date, mood;

    @Generated(hash = 1429279294)
    public MarkedDays(Long id, @NotNull String date, @NotNull String mood) {
        this.id = id;
        this.date = date;
        this.mood = mood;
    }

    @Generated(hash = 639730585)
    public MarkedDays() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMood() {
        return this.mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

}
