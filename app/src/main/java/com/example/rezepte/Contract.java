package com.example.rezepte;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract  {

    private Contract(){}

    public static final String CONTENT_AUTHORITY = "com.example.rezepte";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REZEPT = "rezepte";

    public static final class RezeptEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REZEPT);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REZEPT;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REZEPT;


        public final static String TABLE_NAME ="rezepte";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_ZUTATEN = "zutaten";
        public final static String COLUMN_KOMMENTAR = "kommentar";

    }
}
