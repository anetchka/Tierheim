package ua.kiev.homes.anna.tierheim.forUser;

import android.app.LoaderManager;
import android.app.TaskStackBuilder;
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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kiev.homes.anna.tierheim.R;
import ua.kiev.homes.anna.tierheim.database.Tier;
import ua.kiev.homes.anna.tierheim.forWorker.EditorMode;
import ua.kiev.homes.anna.tierheim.forWorker.FullScreenImage;

public class ViewPet extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TextView nameTVRight;
    private TextView breedTVRight;
    private TextView weightTVRight;
    private TextView genderVRight;
    private TextView typeTVRight;
    private Button contactButton;
    private ImageView mPetImageView;
    /**
     * a {@link Bitmap} for the picture
     */
    private Bitmap bitmap;
    /**
     * Boolean for the imageView
     */
    private boolean isImageFitToScreen = true;
    /**
     * a {@link Uri} Uri for a pet
     */
    private Uri petUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_details);

        this.setTitle("Benutzer Details");

        nameTVRight = (TextView) findViewById(R.id.nameTextViewRight);
        breedTVRight = (TextView) findViewById(R.id.breedEditTextRight);
        weightTVRight = (TextView) findViewById(R.id.weightTextViewRight);
        genderVRight = (TextView) findViewById(R.id.genderTextViewRight);
        typeTVRight = (TextView) findViewById(R.id.petTypeSTextViewRight);
        contactButton = (Button) findViewById(R.id.contact_petshelter_button);

        Intent intent = getIntent();
        petUri = intent.getData();

        final Button savePet = (Button) findViewById(R.id.save_pet_button);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactPetshelter();
                finish();
            }
        });
        mPetImageView = (ImageView) findViewById(R.id.defaultPetImageView);
        mPetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageFitToScreen) {
                    isImageFitToScreen = false;
                    Intent intent = new Intent(ViewPet.this, FullScreenImage.class);
                    intent.setData(petUri);
                    startActivity(intent);
                } else {
                    isImageFitToScreen = true;
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void contactPetshelter() {

    }

    //if you add a new column to a table, the value should also be added here
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
            //get values from cursor
            String petName = data.getString(data.getColumnIndex(Tier.TierItem.COLUMN_PET_NAME));
            String petBreed = data.getString(data.getColumnIndex(Tier.TierItem.COLUMN_PET_BREED));
            int petWeight = data.getInt(data.getColumnIndex(Tier.TierItem.COLUMN_PET_WEIGHT));
            int petGender = data.getInt(data.getColumnIndex(Tier.TierItem.COLUMN_PET_GENDER));
            int petType = data.getInt(data.getColumnIndex(Tier.TierItem.COLUMN_PET_TYPE));
            //get Image from Database
            byte[] image = data.getBlob(data.getColumnIndex(Tier.TierItem.COLUMN_PICTURE));
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            //populate the values into the TextViews
            nameTVRight.setText(petName);
            breedTVRight.setText(petBreed);
            weightTVRight.setText(String.valueOf(petWeight));
            mPetImageView.setImageBitmap(bitmap);
            switch (petGender) {
                case 0:
                    genderVRight.setText("Hund");
                    break;
                case 1:
                    genderVRight.setText("Katze");
                    break;
                case 2:
                    genderVRight.setText("Papagei");
                    break;
                default:
                    genderVRight.setText("Hund");
            }
            switch (petType) {
                case 0:
                    typeTVRight.setText("Unbekannt");
                    break;
                case 1:
                    typeTVRight.setText("Männlich");
                    break;
                case 2:
                    typeTVRight.setText("Weiblich");
                    break;
                default:
                    typeTVRight.setText("Unbekannt");
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameTVRight.setText("");
        breedTVRight.setText("");
        weightTVRight.setText("");
        genderVRight.setText(String.valueOf(0));
        typeTVRight.setText(String.valueOf(0));
        mPetImageView.setImageBitmap(null);
    }
}
