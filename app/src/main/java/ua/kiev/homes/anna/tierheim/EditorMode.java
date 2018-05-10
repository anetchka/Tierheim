package ua.kiev.homes.anna.tierheim;

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
import android.widget.EditText;
import android.widget.Spinner;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class EditorMode extends AppCompatActivity {

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

        setUpGenderSpinner();

        setUpPetTypeSpinner();
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
                    if (selection.equals("dog")) {
                        mPetType = Tier.TierItem.TYPE_DOG;
                    } else if (selection.equals("female")) {
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
                    if (selection.equals("dog")) {
                        mGender = Tier.TierItem.GENDER_MALE;
                    } else if (selection.equals("female")) {
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
                savePet();
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
}
