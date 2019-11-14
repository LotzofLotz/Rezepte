package com.example.rezepte;


import androidx.appcompat.app.AppCompatActivity;



import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;


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


        String [] projection = {
                Contract.RezeptEntry._ID,
        Contract.RezeptEntry.COLUMN_NAME,
        Contract.RezeptEntry.COLUMN_ZUTATEN,
        Contract.RezeptEntry.COLUMN_KOMMENTAR,
        };



       Cursor cursor = getContentResolver().query(
              Contract.RezeptEntry.CONTENT_URI,projection, null,null,null);


        ListView listView =  findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        RezeptCursorAdapter adapter = new RezeptCursorAdapter(this, cursor);



        listView.setAdapter(adapter);
    }

    public void openPage2() {
        Intent i = new Intent(MainActivity.this, AddingActivity.class);
        startActivity(i);

    }


}
