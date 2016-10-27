package trainings.home.com.testinguriimageloading;

import static trainings.home.com.testinguriimageloading.DatabaseHelper.KEY_IMAGE_PATH;
import static trainings.home.com.testinguriimageloading.DatabaseHelper.KEY_IMAGE_TITLE;
import static trainings.home.com.testinguriimageloading.DatabaseHelper.KEY_IMAGE_URI;
import static trainings.home.com.testinguriimageloading.DatabaseHelper.SEARCH_RESULTS_DB_TABLE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

public class ImagesLocalDatabase implements DataBaseHandler {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public ImagesLocalDatabase(final DatabaseHelper DbHelper) {
        this.dbHelper = DbHelper;
    }

    private void openDb() {
        if (sqLiteDatabase != null) {
            if (!sqLiteDatabase.isOpen()) {
                sqLiteDatabase = dbHelper.getWritableDatabase();
            }
        } else {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
    }

    private void closeDb() {
        if (sqLiteDatabase.isOpen()) {
            dbHelper.close();
        }
    }

    @Override
    public void addResults(final ImageContent imageContent) {
        openDb();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_TITLE, imageContent.imageTitle);
        values.put(KEY_IMAGE_PATH, imageContent.imagePath);
        values.put(KEY_IMAGE_URI, imageContent.imageUri);

        sqLiteDatabase.insertOrThrow(SEARCH_RESULTS_DB_TABLE, null, values);

        closeDb();
    }

    @Override
    public List<ImageContent> getAllResults() {
        Cursor cursor;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            KEY_IMAGE_TITLE, //
            KEY_IMAGE_PATH,  //
            KEY_IMAGE_URI
        };

        String path;
        String title;
        String uri;

        List<ImageContent> savedResult = new ArrayList<>();

        openDb();

        cursor = sqLiteDatabase.query(   //
                SEARCH_RESULTS_DB_TABLE, // The table to query
                projection,              // The columns to return
                null,                    // The columns for the WHERE clause
                null,                    // The values for the WHERE clause
                null,                    // don't group the rows
                null,                    // don't filter by row groups
                null);                   // The sort order

        if (cursor != null) {

            // boolean moveToFirst(): moves the cursor to the first row in the result set, returns false if the result
            // set is empty.
            if (cursor.moveToFirst()) {
                do {
                    title = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_TITLE));
                    path = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
                    uri = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URI));

                    savedResult.add(new ImageContent(title, path, uri));
                } while (cursor.moveToNext());
            }

            cursor.close();
        } else {
            savedResult = Collections.emptyList();
        }

        closeDb();

        return savedResult;
    }

    void destroyDatabase() {
        openDb();
        sqLiteDatabase.delete(SEARCH_RESULTS_DB_TABLE, null, null);
    }

    boolean hasDatabaseTable() {
        if (sqLiteDatabase != null) {
            if (!sqLiteDatabase.isOpen()) {
                sqLiteDatabase = dbHelper.getWritableDatabase();
            }
        } else {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }

        Cursor cursor = sqLiteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + SEARCH_RESULTS_DB_TABLE + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }

            cursor.close();
        }

        return false;
    }
}
