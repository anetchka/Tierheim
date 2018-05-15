package ua.kiev.homes.anna.tierheim;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class EditorMode extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * Image for the pet
     */
    private ImageView mPetImageView;

    private Uri petUri;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * Integer for a Spinner Gender Choice
     */
    private int mGender;

    /**
     * Integer for pet Type choice in a spinner
     */
    private int mPetType;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    /**
     * Spinner for Pet Type
     */
    private Spinner mPetTypeSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_mode);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mBreedEditText = (EditText) findViewById(R.id.breedEditText);
        mWeightEditText = (EditText) findViewById(R.id.weightEditText);
        mGenderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        mPetTypeSpinner = (Spinner) findViewById(R.id.petTypeSpinner);
        mPetImageView = (ImageView) findViewById(R.id.defaultPetImageView);
        Button savePet = (Button) findViewById(R.id.save_pet_button);
        savePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePet();
                finish();
            }
        });

        setUpGenderSpinner();

        setUpPetTypeSpinner();
        Intent intent = getIntent();
        //get uri from the intent
        petUri = intent.getData();
        if (petUri == null) {
            this.setTitle(getString(R.string.save_item));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
        } else {
            this.setTitle(getString(R.string.edit_pet));
            //initiate the Loader
            getLoaderManager().initLoader(0, null, this);
        }
    }

    //Set Up Spinner to choose pet Type
    private void setUpPetTypeSpinner() {
        // Creating adapter for spinner
        ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.array_pet_type, android.R.layout.simple_spinner_dropdown_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mPetTypeSpinner.setAdapter(dataAdapter);
        mPetTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.dog_spinner))) {
                        mPetType = Tier.TierItem.TYPE_DOG;
                        //set default image for dog
                        mPetImageView.setImageResource(R.drawable.ic_dog_default);
                    } else if (selection.equals(getString(R.string.cat_spinner))) {
                        mPetType = Tier.TierItem.TYPE_CAT;
                        //set default image for cat
                        mPetImageView.setImageResource(R.drawable.ic_cat_default);
                    } else {
                        mPetType = Tier.TierItem.TYPE_PARROT;
                        //set default image for parrot
                        mPetImageView.setImageResource(R.drawable.ic_parrot_default);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPetType= Tier.TierItem.TYPE_DOG;
            }
        });
    }

    //Set Up Spinner to choose Gender of a pet
    private void setUpGenderSpinner() {
        // Creating adapter for spinner
        ArrayAdapter dataAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_dropdown_item);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mGenderSpinner.setAdapter(dataAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.male_spinner))) {
                        mGender = Tier.TierItem.GENDER_MALE;
                    } else if (selection.equals(R.string.female_spinner)) {
                        mGender = Tier.TierItem.GENDER_FEMALE;
                    } else {
                        mGender = Tier.TierItem.GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPetType = Tier.TierItem.TYPE_DOG;
            }
        });
    }

    /**
     * Inflate Menu into the layout
     * @param menu
     * @return true or false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor_mode, menu);
        return true;
    }

    /**
     * Depending on the item selected do different actions
     * @param item the selected item from the spinner
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        switch (selectedItem) {
            case R.id.delete:
                deletePet();
                finish();
                return true;
            case R.id.changePicture:
                changeProfilePicture();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Start camera Intent
     */
    private void changeProfilePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);
        }
    }


    private void deletePet() {
        getContentResolver().delete(petUri, null, null);
    }

    /**
     * A method to save a pet
     */
    private void savePet() {
        ContentValues values = new ContentValues();
        //Convert image to bitmap
        Bitmap bitmap;
        if (mPetType == Tier.TierItem.TYPE_DOG) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dog_default);
        } else if (mPetType == Tier.TierItem.TYPE_CAT) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cat_default);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_parrot_default);
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] image =bos.toByteArray();

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight = Integer.parseInt(weightString);
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        values.put(Tier.TierItem.COLUMN_PET_NAME, nameString);
        values.put(Tier.TierItem.COLUMN_PET_BREED, breedString);
        values.put(Tier.TierItem.COLUMN_PET_GENDER, mGender);
        values.put(Tier.TierItem.COLUMN_PET_WEIGHT, weight);
        values.put(Tier.TierItem.COLUMN_PET_TYPE, mPetType);
        values.put(Tier.TierItem.COLUMN_PICTURE, image);

        //if insert pet
        if (petUri == null) {
            Uri uriForInsertedPet = getContentResolver().insert(Tier.TierItem.CONTENT_URI, values);
        } else {
            int rowsUpdated = -1;
            rowsUpdated = getContentResolver().update(petUri, values, null, null);
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
                Tier.TierItem.COLUMN_PICTURE};
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
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

            //populate the values into the TextViews
            mNameEditText.setText(petName);
            mBreedEditText.setText(petBreed);
            mWeightEditText.setText(String.valueOf(petWeight));
            mPetImageView.setImageBitmap(bitmap);
            switch (petGender) {
                case 0:
                    mGenderSpinner.setSelection(0);
                    break;
                case 1:
                    mGenderSpinner.setSelection(1);
                    break;
                case 2:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
            }
            switch (petType) {
                case 0:
                    mPetTypeSpinner.setSelection(0);
                    break;
                case 1:
                    mPetTypeSpinner.setSelection(1);
                    break;
                case 2:
                    mPetTypeSpinner.setSelection(2);
                    break;
                default:
                    mPetTypeSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mBreedEditText.setText("");
        mWeightEditText.setText("");
        mGenderSpinner.setSelection(0);
        mPetTypeSpinner.setSelection(0);
    }
}
