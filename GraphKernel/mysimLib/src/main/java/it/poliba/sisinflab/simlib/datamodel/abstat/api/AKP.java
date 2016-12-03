package it.poliba.sisinflab.simlib.datamodel.abstat.api;


import java.util.Map;

/**
 * Created by giorgio on 12/05/16.
 */
public class AKP {

    private static final String TYPE = "type";
    private static final String VALUE = "value";
    private static final String DATATYPE = "datatype";

    private Map<String, String> akp;
    private Map<String, String> subj;
    private Map<String, String> pred;
    private Map<String, String> obj;
    private Map<String, String> occurrences;

    public String getAkpValue(){
        return akp.get(VALUE);
    }

    public String getSubjValue(){
        return subj.get(VALUE);
    }

    public String getPredValue(){
        return pred.get(VALUE);
    }

    public String getObjValue(){
        return obj.get(VALUE);
    }

    public String getOccurrencesValue(){
        return occurrences.get(VALUE);
    }

}
