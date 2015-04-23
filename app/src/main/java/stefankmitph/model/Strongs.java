package stefankmitph.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "strongs")
public class Strongs {
    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField
    private int nr;

    @DatabaseField
    private String text;

    public Strongs() {
        // banana
    }

    public String getText() {
        return text;
    }
}
