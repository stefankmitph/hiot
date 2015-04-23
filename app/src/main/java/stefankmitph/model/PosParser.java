package stefankmitph.model;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PosParser {

    enum Pos {
        NONE,
        N,
        A,
        T,
        V,
        P,
        R,
        C,
        D,
        K,
        I,
        X,
        Q,
        F,
        S,
        ADV,
        CONJ,
        COND,
        PRT,
        PREP,
        INJ,
        ARAM,
        HEB,
        N_PRI,
        A_NUI,
        N_LI,
        N_OI,
        PUNCT
    }

    private static Pos getEnum(String pos) {
        HashMap<String, Pos> mapPos = new HashMap<>();
        mapPos.put("N-    ",Pos.N);
        mapPos.put("A-    ",Pos.A);
        mapPos.put("T-    ",Pos.T);
        mapPos.put("V-    ",Pos.V);
        mapPos.put("P-    ",Pos.P);
        mapPos.put("R-    ",Pos.R);
        mapPos.put("C-    ",Pos.C);
        mapPos.put("D-    ",Pos.D);
        mapPos.put("K-    ",Pos.K);
        mapPos.put("I-    ",Pos.I);
        mapPos.put("X-    ",Pos.X);
        mapPos.put("Q-    ",Pos.Q);
        mapPos.put("F-    ",Pos.F);
        mapPos.put("S-    ",Pos.S);
        mapPos.put("ADV   ",Pos.ADV);
        mapPos.put("CONJ  ",Pos.CONJ);
        mapPos.put("COND  ",Pos.COND);
        mapPos.put("PRT   ",Pos.PRT);
        mapPos.put("PREP  ",Pos.PREP);
        mapPos.put("INJ   ",Pos.INJ);
        mapPos.put("ARAM  ",Pos.ARAM);
        mapPos.put("HEB   ",Pos.HEB);
        mapPos.put("N-PRI ",Pos.N_PRI);
        mapPos.put("A-NUI ",Pos.A_NUI);
        mapPos.put("N-LI  ",Pos.N_LI);
        mapPos.put("N-OI  ",Pos.N_OI);
        mapPos.put("PUNCT ", Pos.PUNCT);
    }

    public static String get(String pattern) {
        HashMap<String, String> mapPos = new HashMap<>();
        mapPos.put("N-    ","noun");
        mapPos.put("A-    ","adjective");
        mapPos.put("T-    ","article");
        mapPos.put("V-    ","verb");
        mapPos.put("P-    ","personal pronoun");
        mapPos.put("R-    ","relative pronoun");
        mapPos.put("C-    ","reciprocal pronoun");
        mapPos.put("D-    ","demonstrative pronoun");
        mapPos.put("K-    ","correlative pronoun");
        mapPos.put("I-    ","interrogative pronoun");
        mapPos.put("X-    ","indefinite pronoun");
        mapPos.put("Q-    ","correlative or interrogative pronoun");
        mapPos.put("F-    ","reflexive pronoun");
        mapPos.put("S-    ","possessive pronoun");
        mapPos.put("ADV   ","adverb");
        mapPos.put("CONJ  ","conjunction");
        mapPos.put("COND  ","cond");
        mapPos.put("PRT   ","particle");
        mapPos.put("PREP  ","preposition");
        mapPos.put("INJ   ","interjection");
        mapPos.put("ARAM  ","aramaic");
        mapPos.put("HEB   ","hebrew");
        mapPos.put("N-PRI ","proper noun indeclinable");
        mapPos.put("A-NUI ","numeral indeclinable");
        mapPos.put("N-LI  ","letter indeclinable");
        mapPos.put("N-OI  ","noun other type indeclinable");
        mapPos.put("PUNCT ", "punctuation");

        Pos pos = Pos.NONE;
        String foundPos = "";
        Set<Map.Entry<String, String>> entries = mapPos.entrySet();
        for(Map.Entry<String, String> entry : entries) {
            if(pattern.startsWith(entry.getKey().trim()))
            {
                foundPos = entry.getValue();
            }
        }

        if(foundPos == "")
            return "";



        return "";
    }
}
