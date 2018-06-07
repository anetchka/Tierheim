package ua.kiev.homes.anna.tierheim.forUser;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;


import ua.kiev.homes.anna.tierheim.R;
import ua.kiev.homes.anna.tierheim.database.Tier;
import ua.kiev.homes.anna.tierheim.forWorker.TierCursorAdapter;

public class UserMainScreen extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TierCursorAdapter tierAdapter;

    //List view where the pets are populated
    private ListView petListView;

    private int mPetType;
    private String extra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        extra = intent.getStringExtra(UserRegistration.PREFERRED_PET_TYPE);
        mPetType = Integer.valueOf(extra);

        // Find the ListView which will be populated with the pet data
        petListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        //Contact Content Resolver
        tierAdapter = new TierCursorAdapter(this, null);
        petListView.setAdapter(tierAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
            String selection = Tier.TierItem.COLUMN_PET_TYPE + " = ?";
            String []selectionArgs = new String[] {extra} ;
        String[] projection = {
                Tier.TierItem._ID,
                Tier.TierItem.COLUMN_PET_NAME,
                Tier.TierItem.COLUMN_PET_BREED,
                Tier.TierItem.COLUMN_PICTURE};
        return new CursorLoader(this, Tier.TierItem.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tierAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tierAdapter.swapCursor(null);
    }
}
