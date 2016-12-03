package it.poliba.sisinflab.simlib.datamodel.abstat;

import it.poliba.sisinflab.simlib.datamodel.Arrow;
import it.poliba.sisinflab.simlib.datamodel.Node;
import it.poliba.sisinflab.simlib.datamodel.abstat.api.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by giorgio on 12/05/16.
 */
public class AbstatNode extends Node {

    private int occurrence = 0;

    public AbstatNode(String id, AbstatGraph graphRef) {
        super(id, graphRef);
    }

    @Override
    public HashMap<Arrow, HashSet<Node>> getArrows(){
        if(arrows.size() == 0) {
            HashMap<Arrow, HashSet<Node>> nodeArrows = new HashMap<>();
            AbstatGraph abstatGraph = (AbstatGraph) graphRef;
            List<AKP> akpListOut = abstatGraph.getAkps(abstatGraph.getGraphURI(), id, null, null, null, String.valueOf(Integer.MAX_VALUE), null);
            List<AKP> akpListIn = abstatGraph.getAkps(abstatGraph.getGraphURI(), null, null, id, null, String.valueOf(Integer.MAX_VALUE), null);

            for(AKP akp : akpListOut){
                String pred = akp.getPredValue();
                String obj = akp.getObjValue();

                abstatGraph.createNode(pred);
                abstatGraph.createNode(obj);

                AbstatNode property = (AbstatNode) abstatGraph.getNode(akp.getPredValue());
                AbstatNode object = (AbstatNode) abstatGraph.getNode(akp.getObjValue());

                AbstatArrow abstatArrow = new AbstatArrow(property, Arrow.DIR_OUT);
                if(!nodeArrows.containsKey(abstatArrow)){
                    nodeArrows.put(abstatArrow, new HashSet<>());
                }
                nodeArrows.get(abstatArrow).add(object);
            }


            for(AKP akp : akpListIn){
                String pred = akp.getPredValue();
                String subj = akp.getSubjValue();

                abstatGraph.createNode(pred);
                abstatGraph.createNode(subj);

                AbstatNode property = (AbstatNode) abstatGraph.getNode(akp.getPredValue());
                AbstatNode subject = (AbstatNode) abstatGraph.getNode(akp.getSubjValue());

                AbstatArrow abstatArrow = new AbstatArrow(property, Arrow.DIR_IN);
                if(!nodeArrows.containsKey(abstatArrow)){
                    nodeArrows.put(abstatArrow, new HashSet<>());
                }
                nodeArrows.get(abstatArrow).add(subject);
            }
            arrows = nodeArrows;
        }
        return arrows;

    }

}
