package ua.kiev.homes.anna.tierheim;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import ua.kiev.homes.anna.tierheim.database.Tier;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TierCursorAdapter tierAdapter;

    //counter for the chosen items from the ListView
    private int checkedCount;

    //a HashMap for ids and positions of chosen items
    private HashMap<Long, Integer> itemMap = new HashMap<Long, Integer>();

    //List view where the pets are populated
    private ListView petListView;

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
                if (checked) {
                    //add into hashmap and change the background color to gray
                    itemMap.put(id, position);
                    petListView.getChildAt(itemMap.get(id)).setBackgroundColor(getResources().getColor(R.color.colorItemSelected));
                } else {
                    //remove from hashmap and change the background color to white
                    petListView.getChildAt(itemMap.get(id)).setBackgroundColor(getResources().getColor(R.color.colorItemSelected));
                    itemMap.remove(id);

                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_editor_mode, menu);
                //set invisible all except for 'Tier Löschen' menu item
                for (int i = 0; i < menu.size(); i++) {
                    if (menu.getItem(i).getTitle().equals(getString(R.string.delete_item))) {
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
            public boolean onActionItemClicked(final ActionMode mode, final MenuItem item) {
                //if the user chooses "Tier Löschen" from menu item
                if (item.getItemId() == R.id.delete) {

                    //create Alert Dialog
                    AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);

                    adb.setTitle("Items löschen?");

                    adb.setIcon(android.R.drawable.ic_dialog_alert);

                    //if "OK" button is clicked, delete the items
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for (Long id : itemMap.keySet()) {
                                if (petListView.getChildAt(itemMap.get(id)) == null) {
                                    continue;
                                }
                                String whereDeleteID = Tier.TierItem._ID + "=" + id;
                                petListView.getChildAt(itemMap.get(id)).setBackgroundColor(getResources().getColor(R.color.colorItemNotSelected));
                                int result = getContentResolver().delete(Tier.TierItem.CONTENT_URI, whereDeleteID, null);
                                if (result == -1) {
                                    Log.i("From MainActivity", "Delete failed for id " + id);
                                }
                            }
                            onDestroyActionMode(mode);
                            itemMap.clear();
                            checkedCount = 0;
                            mode.finish();
                        }
                    });

                    //if "Cancel" button is clicked
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onDestroyActionMode(mode);
                            itemMap.clear();
                            checkedCount = 0;
                            mode.finish();

                        }
                    });
                    adb.show();
                }
                return true;

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                if (itemMap.isEmpty()) {
                    return;
                }
                for (Long id : itemMap.keySet()) {
                    //set the color back to unselected
                    if (petListView.getChildAt(itemMap.get(id)) == null) {
                        continue;
                    }
                    petListView.getChildAt(itemMap.get(id)).setBackgroundColor(getResources().getColor(R.color.colorItemNotSelected));

                }
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
