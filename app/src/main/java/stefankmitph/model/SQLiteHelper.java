package stefankmitph.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import stefankmitph.kint.R;

/**
 * Created by KumpitschS on 08.04.2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private Context context;

    public SQLiteHelper(Context context) {
        super(context,
                context.getResources().getString(R.string.db_name),
                null,
                Integer.parseInt(context.getResources().getString(R.string.version)));
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String sql : context.getResources().getStringArray(R.array.create)) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
