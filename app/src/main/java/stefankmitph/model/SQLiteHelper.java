package stefankmitph.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import stefankmitph.kint.R;

/**
 * Created by KumpitschS on 08.04.2015.
 */
public class SQLiteHelper extends OrmLiteSqliteOpenHelper {

    private Context context;

    public SQLiteHelper(Context context) {
        super(context,
                context.getResources().getString(R.string.db_name),
                null,
                Integer.parseInt(context.getResources().getString(R.string.version)));
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        for(String sql : context.getResources().getStringArray(R.array.create)) {
            database.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private Dao<Word, Integer> wordDao;
    public Dao<Word, Integer> getWordDao() {
        if(wordDao == null) {
            try {
                wordDao = getDao(Word.class);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return wordDao;
    }
}
