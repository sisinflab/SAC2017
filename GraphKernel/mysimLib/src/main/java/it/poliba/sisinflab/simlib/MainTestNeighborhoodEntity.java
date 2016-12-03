/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.poliba.sisinflab.simlib;

import static it.poliba.sisinflab.simlib.MainTestJacc.FEATURED_PATHS;
import static it.poliba.sisinflab.simlib.MainTestNeighborhoodPath.FEATURED_PATHS;
import static it.poliba.sisinflab.simlib.MainTestNeighborhoodPath.ITEMS;
import static it.poliba.sisinflab.simlib.MainTestNeighborhoodPath.METADATA;
import it.poliba.sisinflab.simlib.datamodel.Graph;
import it.poliba.sisinflab.simlib.datamodel.GraphFactory;
import it.poliba.sisinflab.simlib.datamodel.Node;
import it.poliba.sisinflab.simlib.datamodel.Path;
import it.poliba.sisinflab.simlib.exceptions.InvalidGraphException;
import it.poliba.sisinflab.simlib.input.StatementFileReader;
import it.poliba.sisinflab.simlib.neighborhood.KernelMetric;
import it.poliba.sisinflab.simlib.neighborhood.entitybaseditem.NeighborhoodEntityKernelMetric;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 *
 * @author Corrado on 05/04/2016.
 */
public class MainTestNeighborhoodEntity {
    
    //public static final String METADATA = "data/movies/metadata.txt";
    //public static final String METADATA = "data/movies/graph_dbpedia_film_ABSTATfeatures10OP_utf8.txt";
    public static final String METADATA = "data/movies/graph_dbpedia_film_ABSTATfeatures15OPnoonto_utf8.txt";
    //public static final String METADATA = "data/movies/graph_dbpedia_film_ABSTATfeatures15OPwithoutrepet_utf8.txt";
    //public static final String METADATA = "data/movies/graph_dbpedia_film_cleaningIGfeatures15withoutprop_utf8.txt";
    //public static final String METADATA = "data/movies/graph_dbpedia_film_cleaningIGfeatures10withoutonto_utf8.txt";
    //public static final String METADATA = "data/movies/graph_dbpedia_film_cleaningIGfeatures15withoutrepet_utf8.txt"; 
    //public static final String METADATA = "data/movies/graph_dbpedia_film_ABSTATfeatures5OP+DTP_utf8.txt";
    //public static final String METADATA = "data/movies/graph_dbpedia_film_ABSTATfeatures10OP+DTPnoonto_utf8.txt";
    
    public static final String METADATA2 = "data/graph_wikidata.txt";
    public static final String ITEMS2 = "data/mapping_wikidata.txt";
    public static final String ITEMS = "data/movies/mapping_completo";
    public static final String FEATURED_PATHS = "data/movies/featuredPaths.txt";

    public static void main(String[] args) {
        try {
            int distance = 3;

            File graphFile = new File(METADATA);
            File itemsFile = new File(ITEMS);
            
            List<String> itemsIDs = readItems(itemsFile);
            Graph graph = GraphFactory.create(graphFile, itemsIDs, GraphFactory.TRIPLE_GRAPH);
            HashSet<Path> featuredPaths = new HashSet<>(readPaths(new File(FEATURED_PATHS), graph));
            
            System.out.println();

            long startTime = System.currentTimeMillis();
  
            KernelMetric neighborhoodentityKernel = new NeighborhoodEntityKernelMetric(graph, distance, StatementFileReader.TAB_TRIPLES);
            //System.out.println("Kernel(486,486) = "+ neighborhoodentityKernel.computeKernel(StatementFileReader.ITEM + "486", StatementFileReader.ITEM + "486"));
            //System.out.println("Kernel(486,3174) = "+ neighborhoodentityKernel.computeKernel(StatementFileReader.ITEM + "486", StatementFileReader.ITEM + "3174"));
            //System.out.println("Kernel(486,1575) = "+ neighborhoodentityKernel.computeKernel(StatementFileReader.ITEM + "486", StatementFileReader.ITEM + "1575"));
            //System.out.println("Kernel(486,3330) = "+ neighborhoodentityKernel.computeKernel(StatementFileReader.ITEM + "486", StatementFileReader.ITEM + "3330"));
           
            /*System.out.println("Kernel(Saw_3D,Saw_IV) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_3D","http://dbpedia.org/resource/Saw_IV"));
            System.out.println("Kernel(Saw_3D,Saw_II) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_3D","http://dbpedia.org/resource/Saw_II"));
            System.out.println("Kernel(Saw_3D,Saw_VI) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_3D", "http://dbpedia.org/resource/Saw_VI"));
            System.out.println("Kernel(Saw_3D,Major_League_(film)) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_3D","http://dbpedia.org/resource/Major_League_(film)"));
           
            System.out.println("Kernel(The Sting,The Program) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/The_Sting","http://dbpedia.org/resource/The_Program"));
           
            System.out.println("Kernel(Dumbo,Bambi) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Dumbo","http://dbpedia.org/resource/Bambi"));
            */
            
            /*System.out.println("Kernel(Apple Inc.,Steve jobs) = "+ neighborhoodentityKernel.computeKernel("http://www.wikidata.org/entity/Q312","http://www.wikidata.org/entity/Q19837"));
            System.out.println("Kernel(Steve_Wozniak,Steve jobs) = "+ neighborhoodentityKernel.computeKernel("http://www.wikidata.org/entity/Q483382","http://www.wikidata.org/entity/Q19837"));
            System.out.println("Kernel(Apple Inc.,Ford Motor Company) = "+ neighborhoodentityKernel.computeKernel("http://www.wikidata.org/entity/Q312", "http://www.wikidata.org/entity/Q44294"));
           
            System.out.println("Kernel(Facebook,Mark Zuckerberg) = "+ neighborhoodentityKernel.computeKernel("http://www.wikidata.org/entity/Q380","http://www.wikidata.org/entity/Q36215"));
            System.out.println("Kernel(Facebook,South Park) = "+ neighborhoodentityKernel.computeKernel("http://www.wikidata.org/entity/Q380", "http://www.wikidata.org/entity/Q16538"));
           
            System.out.println("Kernel(Angelina Jolie,Brad Pitt) = "+ neighborhoodentityKernel.computeKernel("http://www.wikidata.org/entity/Q13909","http://www.wikidata.org/entity/Q35332"));
           */
            
            int j=0;
            List<String> raccomandations = new ArrayList<String>();
            Map<String,double[]> weightvectors = new HashMap<>();
            Map<String,double[]> weightvectors2 = new HashMap<>();
            //MODO1
            /*for(String s:itemsIDs)
            {
                j++;
                Node n = graph.getItem(s);
                if(n!=null)
                {
                weightvectors.put(s, neighborhoodentityKernel.computeSingleVector(n));
                System.out.println(j);
                }
            }*/
            
            //controllare MODO2
            itemsIDs.parallelStream().filter(p->graph.getItem(p)!=null).forEach(
                    (String s)->
                    {
                        System.out.println(s);
                        weightvectors.put(s, neighborhoodentityKernel.computeSingleVector(graph.getNode(s)));
                    }
                            );
                    
                    
            
                  /*  .forEachOrdered(
                    s->{
                    if(graph.getItem(s)!=null)
                    {
                    weightvectors.put(s, neighborhoodentityKernel.computeSingleVector(graph.getNode(s)));
                            }
                    }
            );*/
                    
            
            
            int z=0;
            Map<String,Map<String,Double>> totracc = new HashMap<>();
            for(Entry<String, double[]> wv:weightvectors.entrySet())
            {
            z++;
            Map<String, Double> rank =
                weightvectors.entrySet().parallelStream()
                        .filter(e -> !e.getKey().equals(wv.getKey())).filter(e->!totracc.containsKey(e.getKey()))
                        .collect(Collectors.toMap(
                                e -> e.getKey(),
                                e -> Utilities.cosineSimilarity(wv.getValue(), e.getValue())
                        ));
            
            Map<String, Double> orderedrank = Utilities.sortByValues(rank);
            totracc.put(wv.getKey(), orderedrank);
            
            if(orderedrank!=null)
            {
                orderedrank.entrySet().parallelStream().filter(p->!p.getValue().equals(0.0)).forEachOrdered(r->raccomandations.add(wv.getKey()+"\t"+r.getKey()+"\t"+r.getValue()));
            }
            System.out.println(z);
            
            }
            
            
            
           /* Map<Node, Double> totmap = new HashMap<>();
            List<String> raccomandations = new ArrayList<String>();
            int i=0;
            for(String s:itemsIDs)
            {
                i++;
                Map<Node, Double> map = neighborhoodentityKernel.computeKernelRank(s);
                //totmap.putAll(map.entrySet().parallelStream().filter(p->!p.equals(0.0)));
                if(map!=null)
                {
                map.entrySet().parallelStream().filter(p->!p.getValue().equals(0.0)).forEachOrdered(r->raccomandations.add(s+"\t"+r.getKey().getId()+"\t"+r.getValue()));
                }                
                //Map<Node, Double> map = neighborhoodpathKernel.computeKernelRank("http://dbpedia.org/resource/Saw_II");
                System.out.println(i);
            }*/
            createFileRaccomandation(raccomandations);
            
            
            
            List<String> ranking = new ArrayList<>();
            
            /*int n = 20;
            System.out.println("TOP " + n);

            int i = 0;
            Iterator<Map.Entry<Node, Double>> iter = map.entrySet().iterator();
            while(iter.hasNext() && i < n){
                Map.Entry<Node, Double> e = iter.next();
                String[] name = e.getKey().getId().split("/");
                int l = name.length;
                System.out.println((i + 1) + ". " + name[l-1] + " " + e.getValue());
                ranking.add((i + 1) + ". " + name[l-1] + " " + e.getValue());
                i++;
            }*/  
            /*ranking.add(0,"Kernel(Saw_II,Saw_IV) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_II","http://dbpedia.org/resource/Saw_IV"));
            ranking.add(1,"Kernel(Saw_II,Saw_3D) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_II","http://dbpedia.org/resource/Saw_3D"));
            ranking.add(2,"Kernel(Saw_II,Saw_VI) = "+ neighborhoodentityKernel.computeKernel("http://dbpedia.org/resource/Saw_II", "http://dbpedia.org/resource/Saw_VI"));
            ranking.add(3,"\n");*/
            
            //java.nio.file.Path file2 = Paths.get("data/40GraphEntityRankingDist2.txt");
            //Files.write(file2, ranking, Charset.forName("UTF-8"));
            
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("Execution time item: " + elapsedTime / 1000D);
      
        }catch (IOException | InvalidGraphException e){
            e.printStackTrace();
        }
    }
    
       private static Set<Path> readPaths(File inputFile, Graph graph) throws IOException {
        return Files.lines(Paths.get(inputFile.getPath()), Charset.defaultCharset())
                .map(l -> graph.createPath(l.split("--")))
                .collect(toSet());
    }

    private static List<String> readItems(File inputFile) throws IOException {
        return Files.lines(Paths.get(inputFile.getPath()), Charset.forName("UTF-8"))
                .map(l -> l.split("\t"))
                .filter(f -> f[1].equals("movie"))
                .map(f -> f[2])
                .collect(toList());
    }
    
    private static void createFileRaccomandation(List<String> raccomandations) throws IOException
    {
          //java.nio.file.Path file2 = Paths.get("data/ABSTAT10OPEntity3distrecommendations.txt");
            java.nio.file.Path file2 = Paths.get("data/NOONTOABOP15Entity3distrecommendations.txt");       
          Files.write(file2, raccomandations, Charset.forName("UTF-8"));

    }
    
}
