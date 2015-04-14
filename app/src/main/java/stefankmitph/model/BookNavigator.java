package stefankmitph.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.UserDictionary;

/**
 * Created by KumpitschS on 14.04.2015.
 */
public class BookNavigator {
    private SQLiteDatabase database;

    public BookNavigator(SQLiteDatabase database) {
        this.database = database;
    }

    public int getVerseCount(String book, int chapter) {
        Cursor cursor = database.rawQuery(
                String.format("select verse_nr from content where book_name = '%s' and chapter_nr = %d " +
                              "group by book_name, chapter_nr", book, chapter), null);
        int count = 0;
        while(cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    public Word[] getVerse(String book, int chapter, int verse) {
        Cursor cursor = database.rawQuery(
                String.format("select * from content where book_name = '%s' and chapter_nr = %d and verse_nr = %d",
                        book,
                        chapter,
                        verse), null);

        Word[] words = new Word[cursor.getCount()];

        while(cursor.moveToNext()) {
            int idx = cursor.getPosition();


            words[idx] = new Word(
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8));
        }

        return words;
    }
}
