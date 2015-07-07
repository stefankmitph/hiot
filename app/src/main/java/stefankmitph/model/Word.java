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
    private String book;

    @DatabaseField
    private int chapter;

    @DatabaseField
    private int verse;

    @DatabaseField
    private int wordnr;

    @DatabaseField
    private String word;

    @DatabaseField
    private String translit;

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

    public String getTranslit() {
        return translit;
    }

    public int getWordnr() {
        return wordnr;
    }

    public String getConcordance() {
        if(concordance != null)
            return concordance;
        return "";
    }

    public int getVerse() {
        return verse;
    }

    public String getLemma() {
        return lemma;
    }
}
