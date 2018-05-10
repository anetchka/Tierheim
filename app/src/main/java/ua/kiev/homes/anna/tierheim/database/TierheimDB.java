package ua.kiev.homes.anna.tierheim.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TierheimDB extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DB_NAME = "tierheim.db";

    private static Context myContext;

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DB_VERSION = 1;

    public TierheimDB(Context context)
    {

        super(context, DB_NAME, null, DB_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + Tier.TierItem.TABLE_NAME + " ("
                + Tier.TierItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Tier.TierItem.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + Tier.TierItem.COLUMN_PET_TYPE + " INTEGER NOT NULL, "
                + Tier.TierItem.COLUMN_PICTURE + " BLOB,  "
                + Tier.TierItem.COLUMN_PET_BREED + " TEXT, "
                + Tier.TierItem.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + Tier.TierItem.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        Cursor cursor = db.rawQuery("select * from " + Tier.TierItem.TABLE_NAME + ";", null);
        String[] columns = cursor.getColumnNames();
        String str = cursor.toString();
        int i = cursor.getCount();
    }
}
