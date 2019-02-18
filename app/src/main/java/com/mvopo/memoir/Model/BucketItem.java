package com.mvopo.memoir.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "bucket_list_tbl")
public class BucketItem implements Parcelable{

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String title, body;

    private boolean isDone;

    private String image, category, difficulty;

    @Generated(hash = 1788423518)
    public BucketItem(Long id, @NotNull String title, @NotNull String body,
            boolean isDone, String image, String category, String difficulty) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.isDone = isDone;
        this.image = image;
        this.category = category;
        this.difficulty = difficulty;
    }

    @Generated(hash = 98808921)
    public BucketItem() {
    }

    protected BucketItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        title = in.readString();
        body = in.readString();
        isDone = in.readByte() != 0;
        image = in.readString();
        category = in.readString();
        difficulty = in.readString();
    }

    public static final Creator<BucketItem> CREATOR = new Creator<BucketItem>() {
        @Override
        public BucketItem createFromParcel(Parcel in) {
            return new BucketItem(in);
        }

        @Override
        public BucketItem[] newArray(int size) {
            return new BucketItem[size];
        }
    };

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean getIsDone() {
        return this.isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeByte((byte) (isDone ? 1 : 0));
        parcel.writeString(image);
        parcel.writeString(category);
        parcel.writeString(difficulty);
    }
}
