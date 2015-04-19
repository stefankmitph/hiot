package stefankmitph.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by stefankmitph on 19.04.2015.
 */
public class Concordance {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String book_name;

    @DatabaseField
    private int chapter_nr;

    @DatabaseField
    private int verse_nr;

    @DatabaseField
    private int strongs;

    @DatabaseField
    private String text;

    public Concordance() {}
}
