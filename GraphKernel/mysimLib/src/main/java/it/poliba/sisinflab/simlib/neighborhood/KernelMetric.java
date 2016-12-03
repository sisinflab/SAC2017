package it.poliba.sisinflab.simlib.neighborhood;


import it.poliba.sisinflab.simlib.datamodel.Graph;
import it.poliba.sisinflab.simlib.datamodel.Node;
import it.poliba.sisinflab.simlib.neighborhood.pathbaseditem.EntityPath;
import it.poliba.sisinflab.simlib.neighborhood.pathbaseditem.ReachablePaths;
import java.util.HashSet;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by Corrado on 15/04/2016.
 */
public abstract class KernelMetric {

    protected Graph graph;

    public KernelMetric(Graph graph){
        this.graph = graph;
    }

    public KernelMetric() {

    }

    public abstract double computeKernel(Node item1, Node item2);
    public abstract Map<Node, Double> computeKernelRank(String itemID);
    
    public abstract Map<String, Double> computeKernelRankMod(String itemID);
    
    public abstract Map<Node, Double> computeFeatureMap(Node n);

    public abstract double computeKernel(String idItem1, String idItem2);
    //public abstract Map<Node, Double> computeKernelRank(String nodeId);
    
    
    public abstract LinkedHashMap<Integer,Float> computeSingleVector3(Node item);
    //public abstract LinkedHashMap<EntityPath,Double> computeSingleVector3(Node item);

    
    //add after for reccomendations
    public abstract double[] computeSingleVector(Node item);
    public abstract double computeKernelModify(Node item1,double[] weight1, Node item2);
    public abstract double computeKernelModify(Node item1,ReachablePaths reachablepaths1, Node item2);
    public abstract double[] computeSingleVector(Node item,ReachablePaths reachablepaths1,ReachablePaths reachablepaths2,HashSet<EntityPath> allsubpaths);
    
    public abstract Map<Node, Double> computeKernelRank2(String itemID);
    public abstract void computeKernelRankTOT(Set<String> items);   
}
