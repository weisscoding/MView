package net.ddns.weissenterprises.mview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class FeedReaderContract {
 // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private
    private FeedReaderContract() {}

/* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "cf_data";
        public static final String COLUMN_NAME_MONTH = "month";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_VAL = "val";
        public static final String COLUMN_NAME_TIMESTAMP = "time";
        public static final String COLUMN_NAME_CATEGORY = "cat";
}
}

