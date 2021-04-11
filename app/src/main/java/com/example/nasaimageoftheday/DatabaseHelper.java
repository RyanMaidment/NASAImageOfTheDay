package com.example.nasaimageoftheday;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "imgoftheday";

    // Table Names
    private static final String DB_TABLE = "table_image";
    // column names
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    private static final String KEY_DATE = "image_date";
    private static final String KEY_EXPLANATION = "image_explanation";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + " ( " +KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " BLOB," +
            KEY_DATE + " TEXT," +
            KEY_EXPLANATION + " TEXT)";

    /**
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate executes an SQL statement
     * to create a table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating table
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    /**
     * onUpgrade will drop database table and create
     * a new one if the database has been upgraded.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // create new table
        onCreate(db);
    }

    /**
     * viewPhoto returns a cursor that will loop through
     * rows in the database.
     * @return
     */
    public Cursor viewPhoto() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    /**
     * deleteRow deletes the row at the rowId
     * it takes in from the parameter.s
     * @param rowId
     */
    public void deleteRow(int rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query ="DELETE FROM "+DB_TABLE+" WHERE "+KEY_ID+"="+rowId;
        db.execSQL(query);

    }


}