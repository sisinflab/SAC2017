
package it.poliba.sisinflab.simlib.neighborhood.pathbaseditem;

import it.poliba.sisinflab.simlib.Utilities;
import it.poliba.sisinflab.simlib.exceptions.InvalidGraphException;
import it.poliba.sisinflab.simlib.input.StatementFileReader;
import it.poliba.sisinflab.simlib.datamodel.Graph;
import it.poliba.sisinflab.simlib.datamodel.GraphFactory;
import it.poliba.sisinflab.simlib.datamodel.Node;
import it.poliba.sisinflab.simlib.neighborhood.KernelMetric;
import it.poliba.sisinflab.simlib.neighborhood.NeighborGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Corrado on 15/04/2016.
 */
public class NeighborhoodPathKernelMetric extends KernelMetric {

    private int distance;

    public static final String MEAN_ARITHMETIC = "mean_arithmetic";
    public static final String MEAN_GEOMETRIC = "mean_geometric";

    public static List<String> itemdone = new ArrayList<>();
    public static int i = 0;

    public NeighborhoodPathKernelMetric(Graph graph, int distance) {
        super(graph);
        this.distance = distance;
    }

    public NeighborhoodPathKernelMetric(Graph graph, int distance, String fileType) throws InvalidGraphException, IOException {
        super(graph);
        if (distance < 1) {
            throw new IllegalArgumentException("distance must be at least 1");
        }
        this.distance = distance;

    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public double computeKernel(Node item1, Node item2) {

        NeighborGraph ng1 = new NeighborGraph(item1, distance);
        NeighborGraph ng2 = new NeighborGraph(item2, distance);

        ReachablePaths reachablepaths1 = new ReachablePaths(ng1);
        ReachablePaths reachablepaths2 = new ReachablePaths(ng2);

        double[] weight1 = null;
                //computeSingleVector(item1, reachablepaths1, reachablepaths2);
        double[] weight2 = null;
                //computeSingleVector(item2, reachablepaths2, reachablepaths1);

        //System.out.println("DotProduct weights: "+Utilities.dotProduct2(weight1, weight2));
        System.out.println("Norm 1 weights: " + Utilities.norm(weight1));
        System.out.println("Norm 2 weights: " + Utilities.norm(weight2));

        String[] name1 = item1.getId().split("/");
        int n1 = name1.length;
        String[] name2 = item2.getId().split("/");
        int n2 = name2.length;
        System.out.println("normalized Kernel(" + name1[n1 - 1] + "," + name2[n2 - 1] + ") = " + Utilities.cosineSimilarity(weight1, weight2));
        System.out.println();
        System.out.println();
        return Utilities.dotProduct(weight1, weight2);
    }

    @Override
    public Map<Node, Double> computeFeatureMap(Node n) {
        return null;
    }

    @Override
    public void computeKernelRankTOT(Set<String> items) {

        //items.parallelStream().forEachOrdered(i->ranktot.put(i,computeKernelRank2(i,ranktot)));
    }

    @Override
    public double computeKernel(String idItem1, String idItem2) {
        Node item1 = graph.getItems().get(idItem1);
        Node item2 = graph.getItems().get(idItem2);
        return computeKernel(item1, item2);
    }

    @Override
    public Map<Node, Double> computeKernelRank2(String itemID) {
            Node item1 = graph.getNode(itemID);
        i++;
        if (item1 != null) {
            NeighborGraph ng1 = new NeighborGraph(item1, distance);
            ReachablePaths reachablepaths1 = new ReachablePaths(ng1);
            PathWeightFeature pathweightfeature = new PathWeightFeature(distance, reachablepaths1); 
            
            //modifica con parallel stream
            Map<Node, Double> rank
                    = graph.getItems().entrySet().parallelStream()
                    .filter(e -> !e.getValue().equals(item1)).filter(e -> !itemdone.contains(e.getValue().getId()))
                    .collect(Collectors.toMap(
                            e -> e.getValue(),
                            e -> computeKernelModify2(pathweightfeature, new PathWeightFeature(distance,new ReachablePaths(new NeighborGraph(e.getValue(), distance))))
                    ));
            //salvo solo l'id item esaminato non il rank associato
            itemdone.add(itemID);
            //ranktot.put(itemID, rank);
            //System.out.println(item1.getId());++
            System.out.print(i); System.out.print("-"); System.out.print(itemdone.size()); System.out.print("-"); System.out.println(rank.size()); 
            return Utilities.sortByValues(rank);
        } else {
            return null;
        }

    }
    
    @SuppressWarnings("empty-statement")
     private static double compareVectorandDot(LinkedHashMap<Integer,Float> weightvectors1,LinkedHashMap<Integer,Float> weightvectors2)
   {
        LinkedHashMap<Integer,Float> wv1 = new LinkedHashMap<>();
        LinkedHashMap<Integer,Float> wv2 = new LinkedHashMap<>();       
        weightvectors1.entrySet().stream().forEach(
                e-> {
                        wv1.put(e.getKey(), (float)0.0);
                        wv2.put(e.getKey(), (float)0.0);
                        }
        );
                        
        weightvectors2.entrySet().stream().forEach(
                e-> {
                        wv1.put(e.getKey(), (float)0.0);
                        wv2.put(e.getKey(), (float)0.0);
                        }
        );
        
        weightvectors1.entrySet().parallelStream().forEach(
                e-> wv1.put(e.getKey(), e.getValue()));
        weightvectors2.entrySet().parallelStream().forEach(
                e-> wv2.put(e.getKey(), e.getValue()));
        
        double[] weight1 = wv1.values().parallelStream().mapToDouble(e -> e).toArray();
        double[] weight2 = wv2.values().parallelStream().mapToDouble(e -> e).toArray();
        
        return Utilities.cosineSimilarity(weight1, weight2);
  }

    @Override
    public Map<String, Double> computeKernelRankMod(String itemID) {
        Node item1 = graph.getNode(itemID);
        i++;
        if (item1 != null) {
            //NeighborGraph ng1 = new NeighborGraph(item1, distance);
            LinkedHashMap<Integer,Float> weightvectors = computeSingleVector3(item1);
            //ReachablePaths reachablepaths1 = new ReachablePaths(ng1);
            Map<String, Double> rank =
                graph.getItems().entrySet().parallelStream()
                        //.filter(e -> !e.getKey().equals(wv.getKey())).filter(e->!totracc3.containsKey(e.getKey()))
                        .filter(e -> !e.getValue().equals(item1)).filter(e -> !itemdone.contains(e.getValue().getId()))
                        .collect(Collectors.toMap(
                                e -> e.getKey(),
                                e -> compareVectorandDot(weightvectors, computeSingleVector3(graph.getNode(e.getKey())))
                                //e -> Utilities.cosineSimilarity(wv.getValue(), e.getValue())
                        ));
            //salvo solo l'id item esaminato non il rank associato
            itemdone.add(itemID);
            //ranktot.put(itemID, rank);
            //System.out.println(item1.getId());++
            System.out.print(i); System.out.print("-"); System.out.print(itemdone.size()); System.out.print("-"); System.out.println(rank.size()); 
            return Utilities.sortByValues(rank);
        } else {
            return null;
        }

    }

    @Override
    public double computeKernelModify(Node item1, ReachablePaths reachablepaths1, Node item2) {

        if(item2.getId().equals("http://dbpedia.org/resource/Ong_Bak_3"))
        {
            System.out.println("FOUND");
        }
        
        NeighborGraph ng2 = new NeighborGraph(item2, distance);
        ReachablePaths reachablepaths2 = new ReachablePaths(ng2);
        
        HashSet<EntityPath> allsubpaths = new HashSet<>();
        reachablepaths1.getEntitysubpaths().forEach(e -> allsubpaths.add(e));
        reachablepaths2.getEntitysubpaths().forEach(e -> allsubpaths.add(e));

        double[] weight1 = computeSingleVector(item1, reachablepaths1, reachablepaths2,allsubpaths);
        double[] weight2 = computeSingleVector(item2, reachablepaths2, reachablepaths1,allsubpaths);

        String[] name1 = item1.getId().split("/");
        int n1 = name1.length;
        String[] name2 = item2.getId().split("/");
        int n2 = name2.length;

        //System.out.println("normalized Kernel(" + name1[n1-1] + "," + name2[n2-1] +") = "+ Utilities.cosineSimilarity(weight1,weight2));
        return Utilities.cosineSimilarity(weight1, weight2);

    }
    
    public double computeKernelModify2(PathWeightFeature pathweightfeature1 ,PathWeightFeature pathweightfeature2) {
        
        HashSet<EntityPath> allsubpaths = new HashSet<>();
        pathweightfeature1.getListSubPaths().forEach(e -> allsubpaths.add(e));
        pathweightfeature2.getListSubPaths().forEach(e -> allsubpaths.add(e));

        double[] weight1 = computeSingleVector2(pathweightfeature1,allsubpaths);
        double[] weight2 = computeSingleVector2(pathweightfeature2,allsubpaths);

        /*String[] name1 = item1.getId().split("/");
        int n1 = name1.length;
        String[] name2 = item2.getId().split("/");
        int n2 = name2.length;*/

        //System.out.println("normalized Kernel(" + name1[n1-1] + "," + name2[n2-1] +") = "+ Utilities.cosineSimilarity(weight1,weight2));
        return Utilities.cosineSimilarity(weight1, weight2);

    }

    @Override
    public double[] computeSingleVector(Node item, ReachablePaths reachablepaths1, ReachablePaths reachablepaths2,HashSet<EntityPath> allsubpaths) {

        //System.out.println("\033[31m                            ...PATH-BASED ITEM NEIGHBORHOOD MAPPPING...ITEM: "+item.getId());
        PathWeightFeature pathweightfeature = new PathWeightFeature(distance, reachablepaths1);
        //LinkedList<EntityPath> subpaths1 = reachablepaths1.getEntitysubpaths();
        //LinkedList<EntityPath> subpaths2 = reachablepaths2.getEntitysubpaths();
        //cut part
        /*HashSet<EntityPath> allsubpaths = new HashSet<>();
        reachablepaths1.getEntitysubpaths().forEach(e -> allsubpaths.add(e));
        reachablepaths2.getEntitysubpaths().forEach(e -> allsubpaths.add(e));*/

        pathweightfeature.calculateWeight(MEAN_ARITHMETIC, allsubpaths);
        double[] weight = pathweightfeature.getWeights().values().parallelStream().mapToDouble(e -> e).toArray();

        return weight;

    }
    
    
    public double[] computeSingleVector2(PathWeightFeature pathweightfeature,HashSet<EntityPath> allsubpaths) {
        
        pathweightfeature.calculateWeight(MEAN_ARITHMETIC, allsubpaths);
        double[] weight = pathweightfeature.getWeights().values().parallelStream().mapToDouble(e -> e).toArray();
        return weight;

    }
    

    @Override
    public LinkedHashMap<Integer,Float> computeSingleVector3(Node item) {
    //public LinkedHashMap<EntityPath,Double> computeSingleVector3(Node item) {  
        
        NeighborGraph ng = new NeighborGraph(item, distance);
        ReachablePaths reachablepaths = new ReachablePaths(ng);
        PathWeightFeature pathweightfeature = new PathWeightFeature(distance, reachablepaths);
        
        pathweightfeature.calculateWeight3(MEAN_ARITHMETIC);
        //double[] weight = pathweightfeature.getWeights().values().parallelStream().mapToDouble(e -> e).toArray();
        
        LinkedHashMap<Integer,Float> newweights = new LinkedHashMap<>();
        
        pathweightfeature.getWeights().entrySet().stream().forEachOrdered(e-> newweights.put(e.getKey().hashCode(),e.getValue()));
        
        
        //return pathweightfeature.getWeights();
        return newweights;
    }


    @Override
    public double[] computeSingleVector(Node item) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double computeKernelModify(Node item1, double[] weight1, Node item2) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<Node, Double> computeKernelRank(String itemID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
