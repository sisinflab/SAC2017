package it.poliba.sisinflab.simlib.datamodel.abstat;

import it.poliba.sisinflab.simlib.datamodel.Arrow;
import it.poliba.sisinflab.simlib.datamodel.Graph;
import it.poliba.sisinflab.simlib.datamodel.Node;
import it.poliba.sisinflab.simlib.datamodel.abstat.api.AKP;
import it.poliba.sisinflab.simlib.datamodel.abstat.api.AbstatResponse;
import it.poliba.sisinflab.simlib.datamodel.abstat.api.AbstatService;
import retrofit2.Call;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgio on 12/05/16.
 */
public class AbstatGraph extends Graph {

    private AbstatService abstatService;
    private int statementsCount = -1;
    private String graphURI;

    public AbstatGraph(String graphURI){
        super();
        this.graphURI = graphURI;
        this.abstatService = AbstatService.serviceFactory();
    }

    @Override
    public Node createNode(String id){
        if(!nodes.containsKey(id)){
            nodes.put(id, new AbstatNode(id, this));
        }
        return nodes.get(id);
    }

    @Override
    public Node getNode(String id){
        return createNode(id);
    }

    @Override
    public int getArrowOccurrences(Arrow arrow){
        return getAkps(graphURI, null, arrow.getProperty().getId(), null,
                null, String.valueOf(Integer.MAX_VALUE), null).size();
    }

    @Override
    public int getStatementsCount(){
        if(statementsCount == -1){
            statementsCount = getAkps(graphURI, null, null, null,
                    null, String.valueOf(Integer.MAX_VALUE), null).size();
            // 374157 in dbpedia-2015-10
        }
        return statementsCount;
    }

    public AbstatService getAbstatService() {
        return abstatService;
    }

    public void setAbstatService(AbstatService abstatService) {
        this.abstatService = abstatService;
    }

    public List<AKP> getAkps(String dataset, String subject, String predicate, String object,
                              String ranking, String limit, String format){

        Call<AbstatResponse> abstatResponse =
                getAbstatService().minimalPattern(dataset, subject, predicate, object, ranking, limit, format);

        try {

            AbstatResponse resp = abstatResponse.execute().body();
            List<AKP> akps = resp.listAKPs();
            return akps;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String getGraphURI() {
        return graphURI;
    }

    public void setGraphURI(String graphURI) {
        this.graphURI = graphURI;
    }
}
