package trainings.home.com.testinguriimageloading;

import org.greenrobot.eventbus.EventBus;
import static trainings.home.com.testinguriimageloading.DatabaseHelper.CREATE_RESULTS_TABLE;
import static trainings.home.com.testinguriimageloading.DatabaseHelper.DATABASE_FILE_NAME;
import static trainings.home.com.testinguriimageloading.DatabaseHelper.DATABASE_VERSION;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ImagesLocalDatabase imagesLocalDatabase;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper databaseHelper = DatabaseHelper.newInstance(this, CREATE_RESULTS_TABLE, DATABASE_FILE_NAME,
                DATABASE_VERSION);
        imagesLocalDatabase = new ImagesLocalDatabase(databaseHelper);
    }

    DataBaseHandler getDb() {
        return imagesLocalDatabase;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            
            imagesLocalDatabase.destroyDatabase();

            if (imagesLocalDatabase.hasDatabaseTable()) {
                EventBus.getDefault().post(new NoDataBaseEmptyEvent());
            }             
            
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}