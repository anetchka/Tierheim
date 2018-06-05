package ua.kiev.homes.anna.tierheim.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ua.kiev.homes.anna.tierheim.R;
import ua.kiev.homes.anna.tierheim.forUser.UserRegistration;
import ua.kiev.homes.anna.tierheim.forWorker.MainActivity;

public class LogInScreen extends AppCompatActivity {

    public static final String USERNAME = "Admin";
    public static final String PASSWORD = "Tierheim.de";

    public static final String USER_NAME_KEY = "ua.kiev.homes.anna.tierheim.mainScreen.USER_NAME";
    public static final String USER_PASSWORD_KEY = "ua.kiev.homes.anna.tierheim.mainScreen.USER_PASSWORD";

    private EditText usernameET;
    private EditText passwordET;
    private TextView invalidInput;

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

    private void signUpUser() {
        Intent intent = new Intent(LogInScreen.this, UserRegistration.class);
        intent.putExtra(USER_NAME_KEY, usernameET.getText().toString().trim());
        intent.putExtra(USER_PASSWORD_KEY, passwordET.getText().toString().trim());
        startActivity(intent);
    }


    private void checkUserLogIns() {
    }

    private void checkWorkerLogIns() {
        usernameET.getText().toString();
        passwordET.getText().toString();
        if (usernameET.getText().toString().equals(USERNAME) && passwordET.getText().toString().equals(PASSWORD)) {
            Intent intent = new Intent(LogInScreen.this, MainActivity.class);
            startActivity(intent);
        } else {
            invalidInput.setVisibility(View.VISIBLE);
        }
    }
}
