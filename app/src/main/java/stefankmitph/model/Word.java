package stefankmitph.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by KumpitschS on 14.04.2015.
 */
@DatabaseTable(tableName = "content")
public class Word {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private String book_name;

    @DatabaseField
    private int chapter_nr;

    @DatabaseField
    private int verse_nr;

    @DatabaseField
    private int word_nr;

    @DatabaseField
    private String word;

    @DatabaseField
    private String functional;

    @DatabaseField
    private String strongs;

    @DatabaseField
    private String lemma;

    @DatabaseField
    private String concordance;

    public Word() {}

    public String getStrongs() {
        return strongs;
    }

    public String getWord() {
        return word;
    }

    public String getFunctional() {
        return functional;
    }

    public int getWord_nr() {
        return word_nr;
    }

    public String getConcordance() {
        if(concordance != null)
            return concordance;
        return "";
    }

    public int getVerse_nr() {
        return verse_nr;
    }
}
