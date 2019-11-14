package com.example.rezepte;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class RezeptProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = RezeptProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the rezepte table */
    private static final int REZEPTE = 100;

    /** URI matcher code for the content URI for a single rezept in the pets table */
    private static final int REZEPT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_REZEPT, REZEPTE);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_REZEPT + "/#", REZEPT_ID);
    }


    private DbHelper dbHelper ;
    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a PetDbHelper object to gain access to the pets database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        dbHelper = new DbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database  = dbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case REZEPTE:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.

                cursor = database.query(Contract.RezeptEntry.TABLE_NAME,  projection,selection, selectionArgs, null, null, sortOrder);
                break;
            case REZEPT_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = Contract.RezeptEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(Contract.RezeptEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REZEPTE:
                return insertRezept(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertRezept(Uri uri, ContentValues values) {

        String name = Contract.RezeptEntry.COLUMN_NAME;
        if(name==null) {
            throw new IllegalArgumentException("Rezept brauch einen Namen!");
        }
        String zutaten = Contract.RezeptEntry.COLUMN_ZUTATEN;
        if(zutaten==null) {
            throw new IllegalArgumentException("Rezept brauch Zutaten!");
        }


SQLiteDatabase db = dbHelper.getWritableDatabase();
long id = db.insert(Contract.RezeptEntry.TABLE_NAME,null,values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REZEPTE:
                return updateRezept(uri, contentValues, selection, selectionArgs);
            case REZEPT_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = Contract.RezeptEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateRezept(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateRezept(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(Contract.RezeptEntry.COLUMN_NAME)) {
            String name = values.getAsString(Contract.RezeptEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Rezept braucht Namen!");
            }
        }
        if (values.containsKey(Contract.RezeptEntry.COLUMN_ZUTATEN)) {
            String zutaten = values.getAsString(Contract.RezeptEntry.COLUMN_ZUTATEN);
            if (zutaten == null) {
                throw new IllegalArgumentException("Rezept braucht Zutaten");
            }
        }
        /*if (values.containsKey(Contract.RezeptEntry.COLUMN_KOMMENTAR)) {
            String kommentar = values.getAsString(Contract.RezeptEntry.COLUMN_KOMMENTAR);
        }*/
        if (values.size() == 0) {
            return 0;
        }
       SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.update(Contract.RezeptEntry.TABLE_NAME,values,selection,selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REZEPTE:
                // Delete all rows that match the selection and selection args
                return database.delete(Contract.RezeptEntry.TABLE_NAME, selection, selectionArgs);
            case REZEPT_ID:
                // Delete a single row given by the ID in the URI
                selection = Contract.RezeptEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return database.delete(Contract.RezeptEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REZEPTE:
                return Contract.RezeptEntry.CONTENT_LIST_TYPE;
            case REZEPT_ID:
                return Contract.RezeptEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
