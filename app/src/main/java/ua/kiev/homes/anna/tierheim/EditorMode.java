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
import android.graphics.Matrix;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class EditorMode extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * Request Code for camera
     */
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * Request code for gallery image
     */
    private static final int PICK_IMAGE_REQUEST = 2;

    /**
     * a path where the picture is saved on the phone
     */
    private String mCurrentPhotoPath;

    /**
     * Image for the pet
     */
    private ImageView mPetImageView;

    /**
     * a {@link Bitmap} Bitmap for the picture
     */
    private Bitmap bitmap;

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
                //save pet when "Speichern" button is clicked
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
                        if (bitmap == null) {
                            mPetImageView.setImageResource(R.drawable.ic_dog_default);
                        } else {
                            mPetImageView.setImageBitmap(bitmap);
                        }
                    } else if (selection.equals(getString(R.string.cat_spinner))) {
                        mPetType = Tier.TierItem.TYPE_CAT;
                        //set default image for cat
                        if (bitmap == null) {
                            mPetImageView.setImageResource(R.drawable.ic_cat_default);
                        } else {
                            mPetImageView.setImageBitmap(bitmap);
                        }
                    } else {
                        mPetType = Tier.TierItem.TYPE_PARROT;
                        //set default image for parrot
                        if (bitmap == null) {
                            mPetImageView.setImageResource(R.drawable.ic_parrot_default);
                        } else {
                            mPetImageView.setImageBitmap(bitmap);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPetType = Tier.TierItem.TYPE_DOG;
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
     *
     * @param menu
     * @return true or false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor_mode, menu);
        //if we are in editor mode for the first time
        if (petUri == null) {
            //set invisible all except for 'Tier Löschen' menu item
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i).getTitle().equals(getString(R.string.delete_item))) {
                    menu.getItem(i).setVisible(false);
                    break;
                }
            }
        }
        return true;
    }

    /**
     * Depending on the item selected do different actions
     *
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

            case R.id.galeryPicture:
                chosePictureFromGalery();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chosePictureFromGalery() {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * Start camera Intent
     */
    private void changeProfilePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ua.kiev.homes.anna.tierheim.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Create imageFile
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            //set the width and height
            bitmap = getResizedBitmap(bitmap, mPetImageView.getWidth(), mPetImageView.getHeight());
            mPetImageView.setImageBitmap(bitmap);

        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                //the bitmap from the gallery which is normally bigger than the size of the view
                Bitmap bigSizedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = getResizedBitmap(bigSizedBitmap, mPetImageView.getWidth(), mPetImageView.getHeight());
                // Log.d(TAG, String.valueOf(bitmap));
                mPetImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param bm A {@link Bitmap} bitmap
     * @param newWidth the desired width of the picture
     * @param newHeight the desired height of the bitmap
     * @return a {@link Bitmap} resized bitmap
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
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

        if (bitmap == null) {
            if (mPetType == Tier.TierItem.TYPE_DOG) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dog_default);

            } else if (mPetType == Tier.TierItem.TYPE_CAT) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cat_default);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_parrot_default);
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] image = bos.toByteArray();

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();
        int weight = 0;
        try {
            weight = Integer.parseInt(weightString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.empty_weight), Toast.LENGTH_SHORT).show();
        }

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
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

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
        mPetImageView.setImageBitmap(null);
    }
}
