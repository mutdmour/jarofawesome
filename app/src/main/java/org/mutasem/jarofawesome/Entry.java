package org.mutasem.jarofawesome;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mutasemdmour on 6/8/16.
 */
public class Entry implements Parcelable{
    public static long _id;
    public static String content;
    public static String title;

    public Entry(long _id, String content, String title){
        this._id = _id;
        this.title = title;
        this.content = content;
    }

    private Entry(Parcel in){
        this._id = in.readLong();
        String[] strings = new String[2];
        in.readStringArray(strings);
        this.content = strings[0];
        this.title = strings[1];
    }

    public static final Parcelable.Creator<Entry> CREATOR
            = new Parcelable.Creator<Entry>(){
        public Entry createFromParcel(Parcel in){
            return new Entry(in);
        }
        public Entry[] newArray(int size){
            return new Entry[size];
        }
    };

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeLong(this._id);
        String[] strings = {this.content, this.title};
        out.writeStringArray(strings);
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public long get_id(){
        return this._id;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

}
