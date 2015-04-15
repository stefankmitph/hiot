package stefankmitph.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

    public AsyncTask<Object, Void, Word[]> load(String book, int chapter) {
        AsyncTask<Object, Void, Word[]> loadTask = new AsyncTask<Object, Void, Word[]>() {

            @Override
            protected Word[] doInBackground(Object... params) {
                return new Word[0];
            }
        };
        return loadTask.execute();
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


            String book_name = cursor.getString(1);
            int chapter_nr = cursor.getInt(2);
            int verse_nr = cursor.getInt(3);
            int word_nr = cursor.getInt(4);
            String word = cursor.getString(5);
            String functional = cursor.getString(6);
            String strongs = cursor.getString(7);
            String lemma = cursor.getString(8);

            String[] split = strongs.split("\\&");
            strongs = split[0];

            Cursor concCursor = database.rawQuery(
                    String.format("select * from concordance where book_name = '%s' and chapter_nr = %d and verse_nr = %d and strongs = '%s'",
                            book, chapter_nr, verse_nr, strongs), null);

            String concordance = "";
            while(concCursor.moveToNext()) {
                concordance = concCursor.getString(5);
            }

            words[idx] = new Word(book_name, chapter_nr, verse_nr, word_nr, word, functional, strongs, lemma, concordance);

        }

        return words;
    }
}
