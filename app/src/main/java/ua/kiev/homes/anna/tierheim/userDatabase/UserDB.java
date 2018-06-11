package ua.kiev.homes.anna.tierheim.userDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class UserDB extends SQLiteOpenHelper {
    /** Name of the database file */
    private static final String DB_NAME = "users.db";

    private static Context myContext;

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DB_VERSION = 1;

    public UserDB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + User.OneUser.TABLE_NAME + " ("
                + User.OneUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + User.OneUser.COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE, "
                + User.OneUser.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + User.OneUser.COLUMN_FIRST_NAME + " TEXT NOT NULL,  "
                + User.OneUser.COLUMN_SECOND_NAME + " TEXT NOT NULL, "
                + User.OneUser.COLUMN_AGE + " INTEGER NOT NULL, "
                + User.OneUser.COLUMN_ADDRESS + " TEXT, "
                + User.OneUser.COLUMN_PET_TYPE + " INTEGER, "
                + User.OneUser.COLUMN_EMAIL + " TEXT NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User.OneUser.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        Cursor cursor = db.rawQuery("select * from " + User.OneUser.TABLE_NAME + ";", null);
        String[] columns = cursor.getColumnNames();
        String str = cursor.toString();
        int i = cursor.getCount();
    }
}
