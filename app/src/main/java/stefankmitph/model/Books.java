package stefankmitph.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by KumpitschS on 06.07.2015.
 */
@DatabaseTable(tableName = "books")
public class Books {
    @DatabaseField
    private String name;
}
