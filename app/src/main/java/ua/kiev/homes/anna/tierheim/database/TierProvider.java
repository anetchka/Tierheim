package ua.kiev.homes.anna.tierheim.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TierProvider extends ContentProvider {

    private static final int ALL_PETS = 0;

    private static final int ONE_PET = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //between pets und pets id URI differentiate
    static {
        sUriMatcher.addURI(Tier.CONTENT_AUTHORITY, Tier.PATH_PETS, ALL_PETS);
        sUriMatcher.addURI(Tier.CONTENT_AUTHORITY, Tier.PATH_PETS + "/#", ONE_PET);
    }

    //DbHeleper
    private TierheimDB dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new TierheimDB(getContext());
        return true;
    }

    /**
     * Perform select from the given URI
     *
     * @param uri           URI
     * @param projection    Column Names
     * @param selection     Where Clause
     * @param selectionArgs Arguments for Where Clause
     * @param sortOrder     SortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //Cursor to be returned
        Cursor cursor;
        //We need only to read from the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //compare numbers
        int match = sUriMatcher.match(uri);
        switch (match) {

            case ALL_PETS:
                cursor = db.query(Tier.TierItem.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case ONE_PET:
                selection = Tier.TierItem._ID + "=?";
                //get ID from the URI
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(Tier.TierItem.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query with the uri" + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
