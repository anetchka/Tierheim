package ua.kiev.homes.anna.tierheim.userDatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ua.kiev.homes.anna.tierheim.userDatabase.User;

public class UserProvider extends ContentProvider {
    private static final int ALL_USERS = 0;

    private static final int ONE_USER = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //between user und user id URI differentiate
    static {
        sUriMatcher.addURI(User.CONTENT_AUTHORITY, User.PATH_USER, ALL_USERS);
        sUriMatcher.addURI(User.CONTENT_AUTHORITY, User.PATH_USER + "/#", ONE_USER);
    }

    //DbHeleper
    private UserDB dbHelper;

    /** Tag for the log messages */
    public static final String LOG_TAG = UserProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        dbHelper = new UserDB(getContext());
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
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Cursor to be returned
        Cursor cursor;
        //We need only to read from the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //compare numbers
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL_USERS:
                cursor = db.query(User.OneUser.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ONE_USER:
                selection = User.OneUser._ID + "=?";
                //get ID from the URI
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(User.OneUser.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query with the uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
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
        if (sUriMatcher.match(uri) == ALL_USERS) {
            // Check that the name is not null
            /*
            String name = values.getAsString(User.OneUser.COLUMN_PET_NAME);
            if (name.isEmpty() || name == null) {
                Toast.makeText(getContext(), "Tier braucht einen Namen", Toast.LENGTH_LONG).show();
                return null;
            }
            //Check weight
            Integer weight = values.getAsInteger(User.OneUser.COLUMN_PET_WEIGHT);
            if (weight == null && weight < 0) {
                Toast.makeText(getContext(), "Das Gewicht muss >= 0 sein", Toast.LENGTH_LONG).show();
                return null;
            }
            //Check for pet type
            Integer type = values.getAsInteger(User.OneUser.COLUMN_PET_TYPE);
            if (type == null || !User.OneUser.isValidtType(type)) {
                Toast.makeText(getContext(), "Geben Sie den Typ vom Tier", Toast.LENGTH_LONG).show();
                return null;
            }
            //Check for gender
            Integer gender = values.getAsInteger(User.OneUser.COLUMN_PET_GENDER);
            if (gender == null || !User.OneUser.isValidGender(gender)) {
                Toast.makeText(getContext(), "Geben Sie das Geschlecht an", Toast.LENGTH_LONG).show();
                return null;
            }
            */
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //check if the insertion was successful
            Long insertedID = db.insertOrThrow(User.OneUser.TABLE_NAME, null, values);
            if (insertedID != -1) {
                //notify all listeners that data has been changed
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return ContentUris.withAppendedId(uri, insertedID);
        } else {
            throw new IllegalArgumentException("Cannot insert with the uri" + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numberDeletedRows = -1;
        //get writable DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        //if URI for all users
        if (match == ALL_USERS) {
            numberDeletedRows = db.delete(User.OneUser.TABLE_NAME, selection, selectionArgs);
            //if uri is for one user only
        } else if (match == ONE_USER) {
            selection = User.OneUser._ID + "=?";
            //get ID of the user
            selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
            numberDeletedRows = db.delete(User.OneUser.TABLE_NAME, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        //if deletion was successful then notify content resolver
        if (numberDeletedRows != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberDeletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return -1;
        }
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
     /*
        if (values.containsKey(User.OneUser.COLUMN_PET_NAME)) {
            String name = values.getAsString(User.OneUser.COLUMN_PET_NAME);
            if (name == null || name.isEmpty()) {
                Toast.makeText(getContext(), "Tier braucht einen Namen", Toast.LENGTH_LONG).show();
                return -1;
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(User.OneUser.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(User.OneUser.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                Toast.makeText(getContext(), "Das Gewicht muss >= 0 sein", Toast.LENGTH_LONG).show();
                return -1;
            }
        }

        if (values.containsKey(User.OneUser.COLUMN_PET_TYPE)) {
            Integer type = values.getAsInteger(User.OneUser.COLUMN_PET_TYPE);
            if (type == null && !User.OneUser.isValidGender(type)) {
                Toast.makeText(getContext(), "Geben Sie das Geschlecht an", Toast.LENGTH_LONG).show();
                return -1;
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(User.OneUser.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(User.OneUser.COLUMN_PET_GENDER);
            if (gender == null || !User.OneUser.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }
*/
        int numberUpdatedRows = -1;
        //get writable DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        //if URI for all users
        if (match == ALL_USERS) {
            numberUpdatedRows = db.update(User.OneUser.TABLE_NAME, values, selection, selectionArgs);
            //if uri is for one user only
        } else if (match == ONE_USER) {
            selection = User.OneUser._ID + "=?";
            //get ID of the pet
            selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
            numberUpdatedRows = db.update(User.OneUser.TABLE_NAME, values, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        //if deletion was successful then notify content resolver
        if (numberUpdatedRows != -1) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberUpdatedRows;
    }
}
