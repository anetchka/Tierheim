package ua.kiev.homes.anna.tierheim.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Tier {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private Tier() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class TierItem implements BaseColumns {

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
        public final static String COLUMN_PET_Type = "type";

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

        public static boolean isValidtTyPE(int type) {
            if (type == TYPE_DOG || type == TYPE_CAT || type == TYPE_PARROT) {
                return true;
            }
            return false;
        }
    }

}
