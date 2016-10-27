package trainings.home.com.testinguriimageloading;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    static final String SEARCH_RESULTS_DB_TABLE = "SEARCH_RESULTS_TABLE";
    static final int DATABASE_VERSION = 2;

    public static final String KEY_IMAGE_PATH = "_imagePath";
    public static final String KEY_IMAGE_TITLE = "_imageTitle";
    public static final String KEY_IMAGE_URI = "_imageUri";

    static final String DATABASE_FILE_NAME = "images.db";
    static final String CREATE_RESULTS_TABLE = "CREATE TABLE " //
            + SEARCH_RESULTS_DB_TABLE + "("                    //
            + KEY_IMAGE_PATH + " text primary_key, "           //
            + KEY_IMAGE_TITLE + " text null, "                 //
            + KEY_IMAGE_URI + " text null "                    //
            + ")";

    private static DatabaseHelper databaseHelper;

    private static String dbTableName;

    /**
     * Return a singleton instance of DatabaseHelper.
     *
     * @param   context          Context
     * @param   dbTableName      Table name
     * @param   dataBaseName     db Name
     * @param   dataBaseVersion  db Version
     *
     * @return  the instance of DatabaseHelper
     */
    public static synchronized DatabaseHelper newInstance(final Context context, final String dbTableName,
            final String dataBaseName, final int dataBaseVersion) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, dbTableName, dataBaseName, dataBaseVersion);
        }

        return databaseHelper;
    }

    /**
     * Constructor Takes and keeps a reference of the passed context in order to access to the application assets and
     * resources. Private to avoid object creation from outside classes.
     *
     * @param  context          to init super()
     * @param  dataBaseVersion  to init super()
     * @param  dataBaseName     to init super()
     */
    private DatabaseHelper(final Context context, final String dbTableName, final String dataBaseName,
            final int dataBaseVersion) {
        super(context, dataBaseName, null, dataBaseVersion);
        this.dbTableName = dbTableName;
    }

    @Override
    public void onOpen(final SQLiteDatabase db) {
        super.onOpen(db);

        if (db.getVersion() < DATABASE_VERSION) {
            onUpgrade(db, db.getVersion(), DATABASE_VERSION);
        }
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {

        // This method not only insert data , but also used to update or modify already existing data in database using
        // bind arguments
        db.execSQL(dbTableName);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_RESULTS_DB_TABLE);
        onCreate(db);
    }
}
