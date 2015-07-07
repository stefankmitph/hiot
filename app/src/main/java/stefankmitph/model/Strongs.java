package stefankmitph.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "strongs")
public class Strongs {
    @DatabaseField
    private String id;

    @DatabaseField
    private String tag;

    public Strongs() {
        // banana
    }

    public String getTag() {
        return tag;
    }
}
