package com.example.rezepte;

import android.content.ContentValues;

import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class AddingActivity extends AppCompatActivity {
    private Button addButton;
    private EditText editName;
    private EditText editZutat;
    private EditText editKommentar;

    private Uri currentRezeptUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEntry();
            }
        });

        Intent intent = getIntent();
        currentRezeptUri = intent.getData();

        String [] projection = {
                Contract.RezeptEntry._ID,
                Contract.RezeptEntry.COLUMN_NAME,
                Contract.RezeptEntry.COLUMN_ZUTATEN,
                Contract.RezeptEntry.COLUMN_KOMMENTAR,
        };



        if(currentRezeptUri == null){
            setTitle("Rezept hinzufügen");
        } else {
            Cursor cursor = getContentResolver().query(currentRezeptUri,projection,null,null,null);

            setTitle("Rezept bearbeiten");

            Button addButton = findViewById(R.id.addButton);
            addButton.setText("UPDATE");

            Button deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteRezept();
                }
            });




                // Find the columns of pet attributes that we're interested in
                int nameColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_NAME);
                int zutatColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_ZUTATEN);
                int kommentarColumnIndex = cursor.getColumnIndex(Contract.RezeptEntry.COLUMN_KOMMENTAR);
                cursor.move(nameColumnIndex);

                // Extract out the value from the Cursor for the given column index
                String name = cursor.getString(nameColumnIndex);
                String zutaten = cursor.getString(zutatColumnIndex);
                String kommentar = cursor.getString(kommentarColumnIndex);
                editName = (EditText) findViewById(R.id.edit_name);
            editKommentar = (EditText) findViewById(R.id.edit_kommentar);
            editZutat = (EditText) findViewById(R.id.edit_zutaten);

                editName.setText(name);
                editZutat.setText(zutaten);
                editKommentar.setText(kommentar);


        }


    }



    /**
     * Perform the deletion of the pet in the database.
     */

    private void deleteRezept() {
        // Only perform the delete if this is an existing pet.
        if (currentRezeptUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(currentRezeptUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    private void saveEntry(){

        Uri mCurrentRezeptUri = getIntent().getData();



        editName = (EditText) findViewById(R.id.edit_name);
        editKommentar = (EditText) findViewById(R.id.edit_kommentar);
        editZutat = (EditText) findViewById(R.id.edit_zutaten);



        String nameString = editName.getText().toString().trim();
        String zutatenString = editZutat.getText().toString();
        String kommentarString = editKommentar.getText().toString().trim();


        ContentValues cv = new ContentValues();
        cv.put(Contract.RezeptEntry.COLUMN_NAME, nameString);
        cv.put(Contract.RezeptEntry.COLUMN_ZUTATEN, zutatenString);
        cv.put(Contract.RezeptEntry.COLUMN_KOMMENTAR, kommentarString);

        //long newRowId = db.insert(Contract.RezeptEntry.TABLE_NAME,null, cv);
        if(mCurrentRezeptUri == null) {
            Uri newUri = getContentResolver().insert(Contract.RezeptEntry.CONTENT_URI, cv);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }}
        else {
            int rowsAffected = getContentResolver().update(mCurrentRezeptUri, cv, null, null);
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


        editName.getText().clear();
        editZutat.getText().clear();
        editKommentar.getText().clear();


            Intent i = new Intent(AddingActivity.this, MainActivity.class);
            startActivity(i);




    }
}
