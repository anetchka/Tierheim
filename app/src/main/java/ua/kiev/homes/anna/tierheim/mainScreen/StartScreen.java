package ua.kiev.homes.anna.tierheim.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ua.kiev.homes.anna.tierheim.R;

public class StartScreen extends AppCompatActivity {

    public static final String WORKER_VALUE = "worker";
    public static final String USER_VALUE = "user";
    public static final String EXTRA_PERSON = "ua.kiev.homes.anna.tierheim.mainScreen.EXTRA_PERSON";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Button userBtn = (Button) findViewById(R.id.as_User);
        Button workerBtn = (Button) findViewById(R.id.as_worker);

        workerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartScreen.this, LogInScreen.class);
                intent.putExtra(EXTRA_PERSON, WORKER_VALUE);
                startActivity(intent);
            }
        });

       userBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(StartScreen.this, LogInScreen.class);
               intent.putExtra(EXTRA_PERSON, USER_VALUE);
               startActivity(intent);
           }
       });
    }
}
