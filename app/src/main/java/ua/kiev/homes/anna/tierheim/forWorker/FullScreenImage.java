package ua.kiev.homes.anna.tierheim.forWorker;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import ua.kiev.homes.anna.tierheim.database.Tier;
import ua.kiev.homes.anna.tierheim.R;

public class FullScreenImage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * A flag for a default picture
     */
    private boolean isDefaultPicture = true;

    /**
     * Image for the pet
     */
    private ImageView mPetImageView;

    /**
     * a {@link Uri} Uri for a pet
     */
    private Uri petUri;

    /**
     * a {@link Bitmap} Bitmap for the picture
     */
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        mPetImageView = (ImageView) findViewById(R.id.full_screen_ImageView);

        Intent intent = getIntent();
        //get uri from the intent
        petUri = intent.getData();
        if (petUri != null) {
            this.setTitle(getString(R.string.edit_pet));
            //initiate the Loader
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                Tier.TierItem._ID,
                Tier.TierItem.COLUMN_PET_NAME,
                Tier.TierItem.COLUMN_PET_BREED,
                Tier.TierItem.COLUMN_PET_GENDER,
                Tier.TierItem.COLUMN_PET_WEIGHT,
                Tier.TierItem.COLUMN_PET_TYPE,
                Tier.TierItem.COLUMN_PICTURE,
                Tier.TierItem.COLUMN_DEFAULT_PICTURE};
        return new CursorLoader(this, ContentUris.withAppendedId(Tier.TierItem.CONTENT_URI, ContentUris.parseId(petUri)), projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            //get Image from Database
            byte[] image = data.getBlob(data.getColumnIndex(Tier.TierItem.COLUMN_PICTURE));
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            int defaultAsInt = data.getInt(data.getColumnIndexOrThrow(Tier.TierItem.COLUMN_DEFAULT_PICTURE));
            if (defaultAsInt == 0) {
                isDefaultPicture = false;
            } else {
                isDefaultPicture = true;
            }
            mPetImageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPetImageView.setImageBitmap(null);
    }
}
