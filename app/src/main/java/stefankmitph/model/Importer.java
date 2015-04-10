package stefankmitph.model;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;

/**
 * Created by KumpitschS on 07.04.2015.
 */
public class Importer {
    private CSV csv;

    public Importer()
    {
        csv = CSV.separator('\t').create();
    }

    public void read(InputStream inputStream) {
        final String patternString = "(\\w+)\\s(\\d+)\\:(\\d+)";
        csv.read(inputStream, new CSVReadProc() {
            @Override
            public void procRow(int i, String... strings) {

                    String position = strings[0];
                    Pattern pattern = Pattern.compile(patternString);

                    Matcher matcher = pattern.matcher(position);

                    String book, chapter, verse;
                    while (matcher.find()) {
                        book = matcher.group(1);
                        chapter = matcher.group(2);
                        verse = matcher.group(3);
                    }
                }
        });
        }
}
