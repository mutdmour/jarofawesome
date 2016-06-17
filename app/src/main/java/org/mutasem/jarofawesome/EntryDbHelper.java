package org.mutasem.jarofawesome;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by mutasemdmour on 6/4/16.
 */
public class EntryDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Entry.db";
    public EntryDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EntryDbContract.Entry.SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        //add methods to keep data
//        Log.d("sup","damn");
        if (oldVersion < newVersion){
            //copy old data into a new table then drop old one
//            db.execSQL(EntryDbContract.Entry.SQL_DROP_TABLE);
//            db.execSQL(EntryDbContract.Entry.SQL_CREATE_ENTRIES);
        }
    }

}