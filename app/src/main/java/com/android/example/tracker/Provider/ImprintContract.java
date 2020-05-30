package com.android.example.tracker.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ImprintContract {

    // The authority, which is how your code knows which Content Provider to access
     static final String AUTHORITY = "com.android.example.tracker";

    // The base content URI = "content://" + <authority>
     static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "places" directory
     static final String PATH_IMPRINTS = "imprints";

    public static final class ImprintEntry implements BaseColumns {

         // TaskEntry content URI = base content URI + path
     public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMPRINTS).build();

        public static final String TABLE_NAME = "imprints";
        public static final String COLUMN_IMPRINT = "imprint";
         public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
