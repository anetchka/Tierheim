package ua.kiev.homes.anna.tierheim.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Tier {

    //For Uri content authority part
    public static final String CONTENT_AUTHORITY = "ua.kiev.homes.anna.tierheim";

    //Consists of "content://ua.kiev.homes.anna.tierheim"
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //table name
    public static final String PATH_PETS = "tiere";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private Tier() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class TierItem implements BaseColumns {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        //the complete Uri with the table name at the end: content://ua.kiev.homes.anna.tierheim/tiere
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        /** Name of database table for pets */
        public final static String TABLE_NAME = "tiere";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PET_NAME ="name";

        /**
         * Breed of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PET_BREED = "breed";

        /**
         * Picture of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PICTURE = "picture";

        /**
         * A flag to know if the picture is chosen from the spinner or the gallery
         */
        public final static String COLUMN_DEFAULT_PICTURE = "defaultPicture";

        /**
         * Gender of the pet.
         *
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PET_GENDER = "gender";

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

        /**
         * Weight of the pet.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PET_WEIGHT = "weight";

        /**
         * Possible values for the gender of the pet.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

        public static boolean isValidtType(int type) {
            if (type == TYPE_DOG || type == TYPE_CAT || type == TYPE_PARROT) {
                return true;
            }
            return false;
        }
    }

}
