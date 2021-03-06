package ua.kiev.homes.anna.tierheim.forWorker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ua.kiev.homes.anna.tierheim.R;
import ua.kiev.homes.anna.tierheim.database.Tier;

public class TierCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link TierCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public TierCursorAdapter(Context context, Cursor c) {

        super(context, c, 0 /* flags */);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTV = (TextView) view.findViewById(R.id.petNameTVInListItem);
        TextView petBreed = (TextView) view.findViewById(R.id.breedTVInListItem);
        //retrieve image from database
        ImageView petPictureIV = (ImageView) view.findViewById(R.id.imageIdInListItem);
        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(Tier.TierItem.COLUMN_PICTURE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        String petName = cursor.getString(cursor.getColumnIndexOrThrow(Tier.TierItem.COLUMN_PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndexOrThrow(Tier.TierItem.COLUMN_PET_BREED));;

        nameTV.setText(petName);
        petBreed.setText(breed);
        petPictureIV.setImageBitmap(bitmap);
    }
}
