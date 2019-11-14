package com.example.rezepte;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity  {

   private FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage2();
            }
        });

        ListView listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                     Intent intent = new Intent(MainActivity.this, AddingActivity.class);
                     Uri currentRezeptUri = ContentUris.withAppendedId(Contract.RezeptEntry.CONTENT_URI, id);
                     intent.setData(currentRezeptUri);
                     startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }







    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
      // DbHelper mDbHelper = new DbHelper(this);

        // Create and/or open a database to read from it
      // SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
       // Cursor cursor = db.rawQuery("SELECT * FROM " + Contract.RezeptEntry.TABLE_NAME, null);

        String [] projection = {
                Contract.RezeptEntry._ID,
        Contract.RezeptEntry.COLUMN_NAME,
        Contract.RezeptEntry.COLUMN_ZUTATEN,
        Contract.RezeptEntry.COLUMN_KOMMENTAR,
        };

       /*  Cursor cursor = db.query(
                Contract.RezeptEntry.TABLE_NAME,
                projection,
                null,
            null,
            null,
            null,
                null);*/

       Cursor cursor = getContentResolver().query(
              Contract.RezeptEntry.CONTENT_URI,projection, null,null,null);

        //TextView displayView = (TextView) findViewById(R.id.text_view_rezept);

        ListView listView =  findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        RezeptCursorAdapter adapter = new RezeptCursorAdapter(this, cursor);

        /*try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).

            displayView.setText("Number of rows in database table: " + cursor.getCount());
            displayView.append( "\n" + Contract.RezeptEntry._ID + " - " +
            Contract.RezeptEntry.COLUMN_NAME + " - " +
                    Contract.RezeptEntry.COLUMN_ZUTATEN + " - "
                    + Contract.RezeptEntry.COLUMN_KOMMENTAR);

            int idColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_NAME);
            int zutatColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_ZUTATEN);
            int kommentarColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_KOMMENTAR);

            while(cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentZutat = cursor.getString(zutatColumnIndex);
                String currentKommentar = cursor.getString(kommentarColumnIndex);

                displayView.append(("\n" + currentID + " - " + currentName + " - " + currentZutat) + " - " + currentKommentar);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }*/

        listView.setAdapter(adapter);
    }

    public void openPage2() {
        Intent i = new Intent(MainActivity.this, AddingActivity.class);
        startActivity(i);

    }


}
