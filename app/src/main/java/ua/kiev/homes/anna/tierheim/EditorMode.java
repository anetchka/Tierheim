package ua.kiev.homes.anna.tierheim;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Spinner;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class EditorMode extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

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

        getLoaderManager().initLoader(0, null, this);
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
                    if (selection.equals("Dog")) {
                        mPetType = Tier.TierItem.TYPE_DOG;
                    } else if (selection.equals("Cat")) {
                        mPetType = Tier.TierItem.TYPE_CAT;
                    } else {
                        mPetType = Tier.TierItem.TYPE_PARROT;
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
                    if (selection.equals("Male")) {
                        mGender = Tier.TierItem.GENDER_MALE;
                    } else if (selection.equals("Female")) {
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
            case R.id.save:
               // savePet();
               // finish();
                return true;
            case R.id.delete:
                //deletePet();
                return true;
            case R.id.changePicture:
                //changeProfilePicture();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A method to save a pet
     */
    private void savePet() {
        ContentValues values = new ContentValues();
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
        //values.put(Tier.TierItem.COLUMN_PICTURE, "");

        Uri uriForInsertedPet = getContentResolver().insert(Tier.TierItem.CONTENT_URI, values);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
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

            //populate the values into the TextViews
            mNameEditText.setText(petName);
            mBreedEditText.setText(petBreed);
            mWeightEditText.setText(String.valueOf(petWeight));
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
