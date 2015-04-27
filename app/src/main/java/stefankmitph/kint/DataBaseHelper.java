package stefankmitph.kint;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import stefankmitph.model.Strongs;
import stefankmitph.model.Word;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static String DB_PATH = "/data/data/stefankmitph.kint/databases/";

    private static String DB_NAME = "test_db";

    private static final String SP_KEY_DB_VER = "db_ver";

    private static final int DB_VERSION = 6;

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        initialize();
    }

    private void initialize() {
        if (checkDataBase()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(myContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DB_VERSION != dbVersion) {
                File dbFile = myContext.getDatabasePath(DB_NAME);
                if (!dbFile.delete()) {
                    Log.w("stefankmitph.kint", "Unable to update database");
                }
            }
        }
        if (!checkDataBase()) {
            try {
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open("database/" + DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        //openDataBase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }


    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    private Dao<Word, Integer> wordDao;
    public Dao<Word, Integer> getWordDao() {
        if(wordDao == null) {
            try {
                wordDao = getDao(Word.class);
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
            }
        }
        return wordDao;
    }

    private Dao<Strongs, Integer> strongsDao;
    public Dao<Strongs, Integer> getStrongsDao() {
        if(strongsDao == null) {
            try {
                strongsDao = getDao(Strongs.class);
            } catch(java.sql.SQLException ex) {
                ex.printStackTrace();
            }
        }
        return strongsDao;
    }
}