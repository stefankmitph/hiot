package stefankmitph.model;

import android.content.Context;

import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;
import java.util.List;

import stefankmitph.kint.DataBaseHelper;

/**
 * Created by stefankmitph on 19.04.2015.
 */
public class DatabaseManager {
    static private DatabaseManager instance;

    static public void init(Context context) {
        if(instance == null){
            instance = new DatabaseManager(context);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private SQLiteHelper helper;
    private DatabaseManager(Context context) {
        helper = new SQLiteHelper(context);
    }

    private SQLiteHelper getHelper() {
        return helper;
    }

    public long getCountOfChapters(String book_name) {
        long count = 0;
        try {
            List<Word> list = getHelper().getWordDao().queryBuilder()
                    .groupByRaw("book_name, chapter_nr")
                    .where().eq("book_name", book_name)
                    .query();

            count = list.size();

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public long getCountOfVerses(String book, int chapter) {
        long count = 0;
        try {
            List<Word> list = getHelper().getWordDao().queryBuilder()
                    .groupByRaw("book_name, chapter_nr, verse_nr")
                    .where().eq("book_name", book).and().eq("chapter_nr", chapter)
                    .query();

            count = list.size();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<Word> getAllWords() {
        List<Word> words = null;
        try {
            PreparedQuery<Word> query = getHelper().getWordDao().queryBuilder().where()
                                                                               .eq("book_name", "John")
                                                                               .and()
                                                                               .eq("chapter_nr", 1).prepare();
            words = getHelper().getWordDao().query(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public List<Word> getChapter(String book, int chapter) {
        List<Word> list = null;
        try {
            PreparedQuery<Word> query = getHelper().getWordDao().queryBuilder()
                    .where()
                    .eq("book_name", book)
                    .and()
                    .eq("chapter_nr", chapter)
                    .prepare();

            list = getHelper().getWordDao().query(query);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Concordance> getConcordanceForChapter(String book, int chapter) {
        List<Concordance> list = null;
        try {
            list = getHelper().getConcordanceDao().queryBuilder()
                    .where()
                    .eq("book_name", book)
                    .and()
                    .eq("chapter_nr", chapter)
                    .query();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
