package ua.kiev.homes.anna.tierheim.userDatabase;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class User {

    //For Uri content authority part

    public static final String CONTENT_AUTHORITY = "ua.kiev.homes.anna.userProvider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //table name
    public static final String PATH_USER = "users";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private User() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single user.
     */
    public static final class OneUser implements BaseColumns {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of users.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single user.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        //the complete Uri with the table name at the end: content://ua.kiev.homes.anna.userProviders/users

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);

        /** Name of database table for pets */
        public final static String TABLE_NAME = "users";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * User'S log in name.
         *
         * Type: TEXT
         */
        public final static String COLUMN_USER_NAME ="username";

        /**
         * Age of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_AGE ="age";

        /**
         * Surname of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_SECOND_NAME ="surname";

        /**
         * Name of the user.
         *
         * Type: TEXT
         */
        public final static String COLUMN_FIRST_NAME ="name";

        /**
         * User's password
         *
         * Type: TEXT
         */
        public final static String COLUMN_PASSWORD = "password";

        /**
         * User's E-Mail
         *
         * Type: TEXT
         */
        public final static String COLUMN_EMAIL = "email";

        /**
         * User's Adress
         */
        public final static String COLUMN_ADDRESS = "address";


        /**
         * Gender of the pet.
         *
         * The only possible values are {@link #TYPE_DOG}, {@link #TYPE_CAT},
         * or {@link #TYPE_PARROT}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PET_TYPE = "type";

        /**
         * Possible values for the gender of the pet.
         */
        public static final int TYPE_DOG = 0;
        public static final int TYPE_CAT = 1;
        public static final int TYPE_PARROT = 2;


        public static boolean isValidtType(int type) {
            if (type == TYPE_DOG || type == TYPE_CAT || type == TYPE_PARROT) {
                return true;
            }
            return false;
        }
    }
}
