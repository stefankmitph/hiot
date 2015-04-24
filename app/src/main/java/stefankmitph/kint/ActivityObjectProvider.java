package stefankmitph.kint;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import java.util.List;

import stefankmitph.model.Word;

/**
 * Created by KumpitschS on 14.04.2015.
 */
public interface ActivityObjectProvider {
    public SQLiteDatabase getDatabase();

    List<Word> getWords(int verse);

    Typeface getTypeface();

    Bundle getPreferences();
}
