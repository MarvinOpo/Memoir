package com.mvopo.memoir.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "bucket_list_tbl")
public class BucketItem {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String image, title, body;

    private String category, difficulty;
    
    @NotNull
    private int progress;

    @Generated(hash = 2064859495)
    public BucketItem(Long id, @NotNull String image, @NotNull String title,
            @NotNull String body, String category, String difficulty,
            int progress) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.body = body;
        this.category = category;
        this.difficulty = difficulty;
        this.progress = progress;
    }

    @Generated(hash = 98808921)
    public BucketItem() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
