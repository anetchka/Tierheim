package ua.kiev.homes.anna.tierheim;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private TierCursorAdapter tierAdapter;

    //counter for the chosen items from the ListView
    private int checkedCount;

    //a list for ids of chosen items
    private ArrayList<Long> item_list = new ArrayList<Long>();

    //List view where the pets are populated
    private  ListView petListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up a listener for the add fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorMode.class);
                startActivity(intent);
            }
        });


        // Find the ListView which will be populated with the pet data
        petListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        //Contact Content Resolver
        tierAdapter = new TierCursorAdapter(this, null);
        petListView.setAdapter(tierAdapter);

        //Set up item Click Listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorMode.class);
                //set uri for the chosen pet and set it in intent data
                intent.setData(ContentUris.withAppendedId(Tier.TierItem.CONTENT_URI, id));
                startActivity(intent);
            }
        });
        //set up a mode for multiple items to be chosen from the listView
        petListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        petListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                checkedCount = petListView.getCheckedItemCount();
                //if more than one item selected set the appropriate title
                if (checkedCount > 1) {
                    mode.setTitle(checkedCount + " items selected");
                } else {
                    mode.setTitle(checkedCount + " item selected");
                }
                mode.setTitle(checkedCount + " item selected");
                if(checked) {
                    item_list.add(id);
                } else {
                    item_list.remove(id);
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_editor_mode, menu);
                //set invisible all except for 'Tier Löschen' menu item
                for (int i = 0; i < menu.size(); i++) {
                    if (menu.getItem(i).getTitle().equals(getString(R.string.delete_item)) ) {
                        continue;
                    }
                    menu.getItem(i).setVisible(false);
                }
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                final int deleteSize = item_list.size();
                int itemId = item.getItemId();
                if(itemId == R.id.delete) {
                    for (Long id : item_list) {
                        String whereDeleteID = Tier.TierItem._ID + "=" + id;
                        int result = getContentResolver().delete(Tier.TierItem.CONTENT_URI, whereDeleteID, null);
                        if (result == -1) {
                            Log.i("From MainActivity", "Delete failed for id " + id);
                        }
                    }

                }
                checkedCount = 0;
                item_list.clear();
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                return;
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Tier.TierItem._ID,
                Tier.TierItem.COLUMN_PET_NAME,
                Tier.TierItem.COLUMN_PET_BREED,
                Tier.TierItem.COLUMN_PICTURE};
        return new CursorLoader(this, Tier.TierItem.CONTENT_URI, projection, null, null, null);
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
