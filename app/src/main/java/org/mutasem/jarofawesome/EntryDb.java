package org.mutasem.jarofawesome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mutasemdmour on 6/4/16.
 */
public class EntryDb {
    public static SQLiteDatabase entryDb;
    public static EntryDbHelper entryDbHelper;

    public EntryDb(Context applicationContext) {
        entryDbHelper = new EntryDbHelper(applicationContext);
        entryDb = entryDbHelper.getWritableDatabase();
    }

    public void dropTable(Context applicationContext){
        //entryDb.execSQL(EntryDbContract.Entry.SQL_DROP_TABLE);
        entryDbHelper = new EntryDbHelper(applicationContext);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void insertRow(String content, String title){
        ContentValues values = new ContentValues();
        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT,content);
        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_TITLE,title);
//        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_SHOWN, 0);
        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_DATE_ADDED, getDateTime());
        entryDb.insert(
                EntryDbContract.Entry.TABLE_NAME,
                EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT,
                values
        );
    }

    public boolean hasAtLeast(int num){
        String[] columns = {EntryDbContract.Entry._ID};
        Cursor cursor = entryDb.query(
                true,
                EntryDbContract.Entry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
                String.valueOf(num),
                null
        );
        if (cursor.getCount() == num) {
            return true;
        }
        return false;
    }

    public void updateRow(Entry entry){
        ContentValues values = new ContentValues();
        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT,entry.getContent());
        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_TITLE,entry.getTitle());
        values.put(EntryDbContract.Entry.COLUMN_NAME_ENTRY_DATE_ADDED, getDateTime());

        String selection = EntryDbContract.Entry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(entry.get_id())};
        Log.d("updateRow","lol");
        int count = entryDb.update(
                EntryDbContract.Entry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if (count == 0){
            Log.d("updateRow","fuck");
        }
    }

    public Entry getRandom(){
        String[] columns = {
                EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT,
                EntryDbContract.Entry.COLUMN_NAME_ENTRY_TITLE,
                EntryDbContract.Entry._ID};
//        String selection = EntryDbContract.Entry.COLUMN_NAME_ENTRY_SHOWN + " LIKE ?";
//        String[] selectionArgs = { String.valueOf(0)};
        Cursor cursor = entryDb.query(
                true,
                EntryDbContract.Entry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                "RANDOM()",
                "1",
                null
        );
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            long _id = cursor.getLong(cursor.getColumnIndex(EntryDbContract.Entry._ID));
            String content = cursor.getString(cursor.getColumnIndex(EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT));
            String title = cursor.getString(cursor.getColumnIndex(EntryDbContract.Entry.COLUMN_NAME_ENTRY_TITLE));
            Entry entry = new Entry(_id, content, title);
            return entry;
        } else {
            return null;
        }
    }

//    public Entry getRow(Long _id){
//        String[] columns = {EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT, EntryDbContract.Entry._ID};
//        String selection = EntryDbContract.Entry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
//        Log.d("getRow id",String.valueOf(_id));
//        String[] selectionArgs = {String.valueOf(_id)};
//        Cursor cursor = entryDb.query(
//                true,
//                EntryDbContract.Entry.TABLE_NAME,
//                columns,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        if (cursor.getCount() != 0) {
//            cursor.moveToFirst();
//            String content = cursor.getString(cursor.getColumnIndex(EntryDbContract.Entry.COLUMN_NAME_ENTRY_CONTENT));
//            Entry entry = new Entry(_id, content);
//            Log.d("getRow","fuck " + content);
//            return entry;
//        } else {
//            Log.d("getRow","fuck me");
//            return null;
//        }
//    }

    public Cursor getAll(String[] columns){
        Cursor cursor = entryDb.query(
                true,
                EntryDbContract.Entry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null, //order
                null, //limit
                null
        );
        return cursor;
    }

    public void deleteRow(Entry entry){
        String selection = EntryDbContract.Entry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(entry.get_id())};
        entryDb.delete(
                EntryDbContract.Entry.TABLE_NAME,
                selection,
                selectionArgs
        );
    }


    //don't yet
    public void deleteAll(){
        entryDb.delete(
                EntryDbContract.Entry.TABLE_NAME,
                null,
                null
        );    }
}