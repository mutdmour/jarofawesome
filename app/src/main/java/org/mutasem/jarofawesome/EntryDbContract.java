package org.mutasem.jarofawesome;

import android.provider.BaseColumns;

import java.sql.Time;

/**
 * Created by mutasemdmour on 6/4/16.
 */
public final class EntryDbContract {
    public EntryDbContract(){}

    public static abstract class Entry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_CONTENT = "entryContent";
        public static final String COLUMN_NAME_ENTRY_TITLE = "entryTitle";
        public static final String COLUMN_NAME_ENTRY_DATE_ADDED = "dateAdded";
//        public static final String COLUMN_NAME_ENTRY_SHOWN = "shown";

        public static final String TEXT_TYPE = " TEXT";
//        public static final String INT_TYPE = " INT";
        public static final String TIMESTAMP_TYPE = " DATETIME";
        public static final String COMMA_SEP = ",";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Entry.TABLE_NAME + " ("
                + Entry._ID + " INTEGER PRIMARY KEY,"
                + Entry.COLUMN_NAME_ENTRY_TITLE + TEXT_TYPE
                        + COMMA_SEP
                + Entry.COLUMN_NAME_ENTRY_CONTENT + TEXT_TYPE
                        + COMMA_SEP
                + Entry.COLUMN_NAME_ENTRY_DATE_ADDED + TIMESTAMP_TYPE
//                        + COMMA_SEP
//                        + Entry.COLUMN_NAME_ENTRY_SHOWN + INT_TYPE
                + " )";
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;

//        public static final String SQL_SIZE_ENTRIES =
//                "SELECT SUM( LENGTH( " + Entry.TABLE_NAME + " ) ) FROM "+ Entry.TABLE_NAME;
    }

}