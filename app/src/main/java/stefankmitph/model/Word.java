package stefankmitph.model;

/**
 * Created by KumpitschS on 14.04.2015.
 */
public class Word {

    private String book;
    private int chapter;
    private int verse;
    private int word_nr;
    private String word;
    private String functional;
    private String strongs;
    private String lemma;
    private String concordance;

    public Word(String book,
                int chapter,
                int verse,
                int word_nr,
                String word,
                String functional,
                String strongs,
                String lemma,
                String concordance) {

        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.word_nr = word_nr;
        this.word = word;
        this.functional = functional;
        this.strongs = strongs;
        this.lemma = lemma;
        this.concordance = concordance;
    }

    public String getLemma() {
        return lemma;
    }

    public String getStrongs() {
        return strongs;
    }

    public String getFunctional() {
        return functional;
    }

    public String getWord() {
        return word;
    }

    public int getWord_nr() {
        return word_nr;
    }

    public int getVerse() {
        return verse;
    }

    public int getChapter() {
        return chapter;
    }

    public String getBook() {
        return book;
    }

    public String getConcordance() {
        return concordance;
    }
}
