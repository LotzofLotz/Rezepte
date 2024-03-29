package com.example.rezepte;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RezeptCursorAdapter extends CursorAdapter {
    /**
     * Constructs a new {@link RezeptCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public RezeptCursorAdapter(Context context, Cursor c) {
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

        TextView name = view.findViewById(R.id.name);
        TextView zutaten = view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_NAME);
        int zutatColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_ZUTATEN);

        String rezeptName = cursor.getString(nameColumnIndex);
        String zutatenString = cursor.getString(zutatColumnIndex);

        name.setText(rezeptName);
        zutaten.setText(zutatenString);

    }
}
