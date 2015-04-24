package stefankmitph.model;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PosParser {

    enum VerbExtra {
        M,
        C,
        T,
        A,
        ATT,
        AP,
        IRR,
        NONE
    }

    enum Mood {
        I,
        S,
        O,
        M,
        N,
        P,
        R,
        NONE
    }

    enum Voice {
        A,
        M,
        P,
        E,
        D,
        O,
        N,
        Q,
        X,
        NONE
    }

    enum Tense {
        P,
        I,
        F,
        TWO_F,
        A,
        TWO_A,
        R,
        TWO_R,
        L,
        TWO_L,
        NONE, X
    }

    enum Gender {
        M,F,N,NONE
    }

    enum Number {
        S, NONE, P
    }

    enum Case {
        NONE,
        N,V,G,D,A
    }
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

    private static VerbExtra getVerbExtra(String verbExtra) {
        switch(verbExtra) {
            case "-M"  : return VerbExtra.M;
            case "-C"  : return VerbExtra.C;
            case "-T"  : return VerbExtra.T;
            case "-A"  : return VerbExtra.A;
            case "-ATT": return VerbExtra.ATT;
            case "-AP" : return VerbExtra.AP;
            case "-IRR": return VerbExtra.IRR;
        }
        return VerbExtra.NONE;
    }

    private static Mood getMoodEnum(String mood) {
        switch(mood) {
            case "I": return Mood.I;
            case "S": return Mood.S;
            case "O": return Mood.O;
            case "M": return Mood.M;
            case "N": return Mood.N;
            case "P": return Mood.P;
            case "R": return Mood.R;
        }
        return Mood.NONE;
    }

    private static Voice getVoiceEnum(String voice) {
        switch(voice) {
            case "A": return Voice.A;
            case "M": return Voice.M;
            case "P": return Voice.P;
            case "E": return Voice.E;
            case "D": return Voice.D;
            case "O": return Voice.O;
            case "N": return Voice.N;
            case "Q": return Voice.Q;
            case "X": return Voice.X;
        }
        return Voice.NONE;
    }

    private static Tense getTenseEnum(String tense) {
        switch(tense) {
            case "P": return Tense.P;
            case "I": return Tense.I;
            case "F": return Tense.F;
            case "2F": return Tense.TWO_F;
            case "A": return Tense.A;
            case "2A": return Tense.TWO_A;
            case "R": return Tense.R;
            case "2R": return Tense.TWO_R;
            case "L": return Tense.L;
            case "2L": return Tense.TWO_L;
            case "X": return Tense.X;
        }
        return Tense.NONE;
    }

    private static Gender getGenderEnum(String gender) {
        switch(gender) {
            case "M": return Gender.M;
            case "F": return Gender.F;
            case "N": return Gender.N;
        }
        return Gender.NONE;
    }

    private static Number getNumberEnum(String number) {
        switch(number) {
            case "P": return Number.P;
            case "S": return Number.S;
        }
        return Number.NONE;
    }

    private static Case getCaseEnum(String _case) {
        switch(_case) {
            case "N":
                return Case.N;
            case "D":
                return Case.D;
            case "V":
                return Case.V;
            case "A":
                return Case.A;
            case "G":
                return Case.G;
        }
        return Case.NONE;
    }

    private static Pos getPosEnum(String pos) {
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

        Set<Map.Entry<String, Pos>> entries = mapPos.entrySet();
        for(Map.Entry<String, Pos> entry : entries) {
            if(entry.getKey().trim().equals(pos))
                return entry.getValue();
        }
        return Pos.NONE;
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

        String result = "";
        Pos pos = Pos.NONE;
        String leftString = pattern;

        Set<Map.Entry<String, String>> entries = mapPos.entrySet();
        for(Map.Entry<String, String> entry : entries) {
            if(pattern.startsWith(entry.getKey().trim()))
            {
                result = entry.getValue();
                pos = getPosEnum(entry.getKey().trim());
                leftString = pattern.replaceAll(entry.getKey().trim(), "");
                break;
            }
        }

        if(result == "")
            return "";

        HashMap<Case, String> mapCase = new HashMap<>();
        mapCase.put(Case.N, "nominative");
        mapCase.put(Case.V, "vocative");
        mapCase.put(Case.G, "genetive");
        mapCase.put(Case.D, "dative");
        mapCase.put(Case.A, "accusative");

        HashMap<Number, String> mapNumber = new HashMap<>();
        mapNumber.put(Number.S, "singular");
        mapNumber.put(Number.P, "plural");

        HashMap<Gender, String> mapGender = new HashMap<>();
        mapGender.put(Gender.M, "masculine");
        mapGender.put(Gender.F, "feminine");
        mapGender.put(Gender.N, "neuter");

        HashMap<Tense, String> mapTense = new HashMap<>();
        mapTense.put(Tense.P  ,"present");
        mapTense.put(Tense.I  ,"imperfect");
        mapTense.put(Tense.F  ,"future");
        mapTense.put(Tense.TWO_F ,"second future");
        mapTense.put(Tense.A  ,"aorist");
        mapTense.put(Tense.TWO_A ,"second aorist");
        mapTense.put(Tense.R  ,"perfect");
        mapTense.put(Tense.TWO_R ,"second perfect");
        mapTense.put(Tense.L  ,"pluperfect");
        mapTense.put(Tense.TWO_L ,"second pluperfect");
        mapTense.put(Tense.X  ,"no tense stated");

        HashMap<Voice, String> mapVoice = new HashMap<>();
        mapVoice.put(Voice.A, "active");
        mapVoice.put(Voice.M, "middle");
        mapVoice.put(Voice.P, "passive");
        mapVoice.put(Voice.E, "middle or passive");
        mapVoice.put(Voice.D, "middle deponent");
        mapVoice.put(Voice.O, "passive deponent");
        mapVoice.put(Voice.N, "middle or passive deponent");
        mapVoice.put(Voice.Q, "impersonal active");
        mapVoice.put(Voice.X, "no voice");

        HashMap<Mood, String> mapMood = new HashMap<>();
        mapMood.put(Mood.I, "indicative");
        mapMood.put(Mood.S, "subjunctive");
        mapMood.put(Mood.O, "optative");
        mapMood.put(Mood.M, "imperative");
        mapMood.put(Mood.N, "infinitive");
        mapMood.put(Mood.P, "participle");
        mapMood.put(Mood.R, "imperative participle");

        HashMap<Integer, String> mapPerson = new HashMap<>();
        mapPerson.put(1, "first person");
        mapPerson.put(2, "second person");
        mapPerson.put(3, "third person");

        HashMap<VerbExtra, String> mapVerbExtra = new HashMap<>();
        mapVerbExtra.put(VerbExtra.M  ,"middle significance");
        mapVerbExtra.put(VerbExtra.C  ,"contracted form");
        mapVerbExtra.put(VerbExtra.T  ,"transitive");
        mapVerbExtra.put(VerbExtra.A  ,"aeolic");
        mapVerbExtra.put(VerbExtra.ATT,"attic");
        mapVerbExtra.put(VerbExtra.AP ,"apocopated form");
        mapVerbExtra.put(VerbExtra.IRR,"irregular or impure form");


        switch(pos) {
            // pos case number gender [suffix]
            case N:
            case A:
            case T:
                String c = leftString.substring(0, 1);
                Case _case = getCaseEnum(c);

                if(_case == Case.NONE)
                    return result;
                result += " " + mapCase.get(_case);

                leftString = leftString.substring(1, leftString.length());

                String n = leftString.substring(0,1);
                Number number = getNumberEnum(n);

                if(number == Number.NONE)
                    return result;
                result += " " + mapNumber.get(number);

                leftString = leftString.substring(1, leftString.length());

                String g = leftString.substring(0,1);

                Gender gender = getGenderEnum(g);

                if(gender == Gender.NONE)
                    return result;
                result += " " + mapGender.get(gender);

                return result;
            /*
              V- tense voice I person number [verb-extra]
              V- tense voice S person number [verb-extra]
              V- tense voice O person number [verb-extra]
              V- tense voice M person number [verb-extra]
              V- tense voice N
              V- tense voice P case number gender [verb-extra]
              V- tense voice R case number gender [verb-extra]
             */
            case V:
                Tense tense = Tense.NONE;
                String firstTry = leftString.substring(0,1);
                if(getTenseEnum(firstTry) == Tense.NONE) {
                    String secondTry = leftString.substring(0,2);
                    tense = getTenseEnum(secondTry);
                    leftString = leftString.substring(2, leftString.length());
                } else {
                    tense = getTenseEnum(firstTry);
                    leftString = leftString.substring(1, leftString.length());
                }

                if(tense == Tense.NONE)
                    return result;

                result += " " + mapTense.get(tense);

                String v = leftString.substring(0,1);
                Voice voice = getVoiceEnum(v);

                if(voice == Voice.NONE)
                    return result;

                result += " " + mapVoice.get(voice);
                leftString = leftString.substring(1, leftString.length());

                String m = leftString.substring(0,1);
                Mood mood = getMoodEnum(m);

                if(leftString.length() > 2)
                    leftString = leftString.substring(2, leftString.length());

                switch(mood) {
                    case I:
                    case S:
                    case O:
                    case M:
                        result += " " + mapMood.get(mood);

                        String p = leftString.substring(0,1);
                        int person = Integer.parseInt(p);

                        result += " " + mapPerson.get(person);

                        leftString = leftString.substring(1, leftString.length());

                        n = leftString.substring(0, 1);
                        number = getNumberEnum(n);

                        if(number == Number.NONE)
                            return result;

                        result += " " + mapNumber.get(number);

                        if(leftString.length() > 1)
                            leftString = leftString.substring(1, leftString.length());
                            if(leftString.trim().length() > 0) {
                                String ve = leftString;
                                VerbExtra verbExtra = getVerbExtra(ve);
                                if(verbExtra != VerbExtra.NONE)
                                    result += " " + mapVerbExtra.get(verbExtra);;
                        }
                        else {
                            leftString = leftString.substring(1,1);
                        }



                        return result;
                    case N:
                        result += " " + mapMood.get(mood);
                        break;
                    case P:
                    case R:
                        result += " " + mapMood.get(mood);

                        c = leftString.substring(0, 1);
                        _case = getCaseEnum(c);
                        if(_case == Case.NONE)
                            return result;

                        result += " " + mapCase.get(_case);

                        leftString = leftString.substring(1, leftString.length());

                        n = leftString.substring(0,1);
                        number = getNumberEnum(n);

                        if(number == Number.NONE)
                            return result;
                        result += " " + mapNumber.get(number);

                        leftString = leftString.substring(1, leftString.length());

                        g = leftString.substring(0,1);

                        gender = getGenderEnum(g);

                        if(gender == Gender.NONE)
                            return result;
                        result += " " + mapGender.get(gender);

                        leftString = leftString.substring(1, leftString.length());
                        if(leftString.trim().length() > 0) {
                            String ve = leftString;
                            VerbExtra verbExtra = getVerbExtra(ve);
                            if(verbExtra == VerbExtra.NONE)
                                return result;

                            result += " " + mapVerbExtra.get(verbExtra);
                        }

                        return result;
                }

                return result;
        }


        return result;
    }
}
