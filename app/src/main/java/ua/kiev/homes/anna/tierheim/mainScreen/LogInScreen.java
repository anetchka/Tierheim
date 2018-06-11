package ua.kiev.homes.anna.tierheim.mainScreen;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ua.kiev.homes.anna.tierheim.R;
import ua.kiev.homes.anna.tierheim.database.Tier;
import ua.kiev.homes.anna.tierheim.forUser.UserRegistration;
import ua.kiev.homes.anna.tierheim.forWorker.MainActivity;
import ua.kiev.homes.anna.tierheim.userDatabase.User;

public class LogInScreen extends AppCompatActivity {

    public static final String USERNAME = "Admin";
    public static final String PASSWORD = "Tierheim.de";

    public static final String USER_NAME_KEY = "ua.kiev.homes.anna.tierheim.loginscreen.USER_NAME";
    public static final String USER_PASSWORD_KEY = "ua.kiev.homes.anna.tierheim.loginscreen.USER_PASSWORD";
    public static final String UNIQUE_ID = "ua.kiev.homes.anna.tierheim.loginscreen.uniqueID";

    private EditText usernameET;
    private EditText passwordET;
    private TextView invalidInput;
    private int userCounter = 0;
    private int workerCounter = 0;

    private String username;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_screen);

        Button logIn = (Button) findViewById(R.id.logInButton);
        Button signUp = (Button) findViewById(R.id.signUpButton);
        usernameET = (EditText) findViewById(R.id.usernameEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);
        invalidInput = (TextView) findViewById(R.id.invalidIput);
        invalidInput.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        final String extra = intent.getStringExtra(StartScreen.EXTRA_PERSON);

        //for existing users and workers
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extra.equals(StartScreen.USER_VALUE)) {
                    checkUserLogIns();
                } else {
                    checkWorkerLogIns();
                }
            }
        });

        //for the new users and workers
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extra.equals(StartScreen.USER_VALUE)) {
                    signUpUser();
                } else {
             //       signUpWorker();
                }
            }
        });


    }

    //when a new user wants to register
    private void signUpUser() {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            userCounter++;
            String[] projection = new String[] {User.OneUser.COLUMN_USER_NAME};
            String selection = User.OneUser.COLUMN_USER_NAME + "=?";
            String[] selectionArgs = new String[] {username};
            Cursor cursor = getContentResolver().query(User.OneUser.CONTENT_URI, projection, selection, selectionArgs, null);
            if (cursor.getCount() == 0) {
                Intent intent = new Intent(LogInScreen.this, UserRegistration.class);
                intent.putExtra(USER_NAME_KEY, usernameET.getText().toString().trim());
                intent.putExtra(USER_PASSWORD_KEY, passwordET.getText().toString().trim());
                intent.setData(ContentUris.withAppendedId(User.OneUser.CONTENT_URI, userCounter));
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Dieser Usaername ist schon besetzt", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Bitte Formular ausfüllen", Toast.LENGTH_LONG).show();
        }

    }

    //when an existing user wants to sign in
    private void checkUserLogIns() {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
        String[] projection = new String[] {User.OneUser.COLUMN_USER_NAME, User.OneUser.COLUMN_PASSWORD, User.OneUser._ID};
        String selection = User.OneUser.COLUMN_USER_NAME + "=?";
        String[] selectionArgs = new String[] {username};
        Cursor cursor = getContentResolver().query(User.OneUser.CONTENT_URI, projection, selection, selectionArgs, null);
        cursor.moveToFirst();
        if (cursor != null) {
            String savedUname = cursor.getString(cursor.getColumnIndex(User.OneUser.COLUMN_USER_NAME));
            String savedPword = cursor.getString(cursor.getColumnIndex(User.OneUser.COLUMN_PASSWORD));
            String uniqueID = cursor.getString(cursor.getColumnIndex(User.OneUser._ID));
            if (savedUname.equals(username) && savedPword.equals(password)) {
                Intent intent = new Intent(LogInScreen.this, UserRegistration.class);
                intent.putExtra(UNIQUE_ID ,uniqueID);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), " Username oder Kennwort ist falsch", Toast.LENGTH_LONG).show();
            }
        }
    }

    //when an existing worker wants to sign in
    private void checkWorkerLogIns() {
        if (usernameET.getText().toString().equals(USERNAME) && passwordET.getText().toString().equals(PASSWORD)) {
            Intent intent = new Intent(LogInScreen.this, MainActivity.class);
            startActivity(intent);
        } else {
            invalidInput.setVisibility(View.VISIBLE);
        }
    }
}
