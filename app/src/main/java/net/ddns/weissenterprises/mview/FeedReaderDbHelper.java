package net.ddns.weissenterprises.mview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_MONTH + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DAY + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORY + " INTEGER," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_VAL + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CF_DB.db";

    public FeedReaderDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
