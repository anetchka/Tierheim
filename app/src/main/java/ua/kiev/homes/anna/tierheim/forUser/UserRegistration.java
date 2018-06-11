package ua.kiev.homes.anna.tierheim.forUser;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ua.kiev.homes.anna.tierheim.R;
import ua.kiev.homes.anna.tierheim.database.Tier;
import ua.kiev.homes.anna.tierheim.mainScreen.LogInScreen;
import ua.kiev.homes.anna.tierheim.userDatabase.User;

public class UserRegistration extends AppCompatActivity {
    private EditText nameET;
    private EditText surnameET;
    private EditText ageET;
    private EditText adressET;
    private EditText emailET;
    private String username;
    private String password;

    public static final String PREFERRED_PET_TYPE = "ua.kiev.homes.anna.tierheim.userregistration.PREFERRED_PET_TYPE";

    private Spinner mPetTypeSpinner;
    /**
     * Integer for pet Type choice in a spinner
     */
    private int mPetType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data);

        Intent intent = getIntent();
        username = intent.getStringExtra(LogInScreen.USER_NAME_KEY);
        password = intent.getStringExtra(LogInScreen.USER_PASSWORD_KEY);
        Button saveProfil = (Button) findViewById(R.id.save_user_button);
        nameET = (EditText) findViewById(R.id.nameEditText);
        surnameET = (EditText) findViewById(R.id.surnameEditText);
        ageET = (EditText) findViewById(R.id.ageEditText);
        adressET = (EditText) findViewById(R.id.adressEditText);
        emailET = (EditText) findViewById(R.id.emailAdressEditText);
        mPetTypeSpinner = (Spinner) findViewById(R.id.desiredPetTypeSpinner);
        setUpPetTypeSpinner();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserToDB();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveUserToDB() {
        ContentValues values = new ContentValues();
        values.put(User.OneUser.COLUMN_FIRST_NAME, nameET.getText().toString().trim());
        values.put(User.OneUser.COLUMN_SECOND_NAME, surnameET.getText().toString().trim());
        values.put(User.OneUser.COLUMN_AGE, Integer.parseInt(ageET.getText().toString().trim()));
        values.put(User.OneUser.COLUMN_ADDRESS, adressET.getText().toString().trim());
        values.put(User.OneUser.COLUMN_EMAIL, emailET.getText().toString().trim());
        values.put(User.OneUser.COLUMN_USER_NAME, username);
        values.put(User.OneUser.COLUMN_PASSWORD, password);
        values.put(User.OneUser.COLUMN_PET_TYPE, mPetType);
        Uri insertedUserUri = getContentResolver().insert(User.OneUser.CONTENT_URI, values);
        if (insertedUserUri != null) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(getApplication(), "Profil wurde erfolgreich eingefügt", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserRegistration.this, UserMainScreen.class);
            intent.putExtra(PREFERRED_PET_TYPE, String.valueOf(mPetType));
            startActivity(intent);
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
                    } else if (selection.equals(getString(R.string.cat_spinner))) {
                        mPetType = Tier.TierItem.TYPE_CAT;
                    } else {
                        mPetType = Tier.TierItem.TYPE_PARROT;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mPetType = Tier.TierItem.TYPE_DOG;
            }
        });
    }
}
