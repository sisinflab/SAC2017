/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.poliba.sisinflab.simlib.neighborhood.pathbaseditem;

import org.apache.commons.collections.HashBag;
import it.poliba.sisinflab.simlib.Utilities;
import it.poliba.sisinflab.simlib.datamodel.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;


/**
 *
 * @author Corrado on 05/04/2016.
 */
public class PathWeightFeature {
    
   //item del neighborhood graph
    protected Node item;   
    //profondità massima graph
    protected Integer hop;
    //lista sub-paths di ogni path percorribile dall'item i 
    protected LinkedList<EntityPath> listsubpaths = new LinkedList<>();
    //insieme dei paths percorribili dall'item i
    protected HashSet<EntityPath> setcompletepaths = new HashSet<>();
    //paths and weights
    protected LinkedHashMap<EntityPath,Float> pathsweights = new LinkedHashMap<>();
    
    //protected HashMap<EntityPath,LinkedList<Double>> listentityweight = new HashMap<>();
    
        
    public PathWeightFeature(int distance,ReachablePaths rc){
        
        this.setcompletepaths = (HashSet<EntityPath>) rc.getEntitypaths().clone();
        this.listsubpaths = (LinkedList<EntityPath>) rc.getEntitysubpaths().clone();
        this.hop = distance;
        this.item = rc.getItem();     
    }
    
    public Node getItem() {
        return item;
    }

    public void setItem(Node item) {
        this.item = item;
    }
    
    public Integer getHop()
    {
        return hop;
    }
    
    public void setHop(Integer hop)
    {
        this.hop = hop;
    }
    
    public LinkedHashMap<EntityPath,Float> getWeights()
    {
        return pathsweights;
    }
    
    public void setWeights(LinkedHashMap<EntityPath,Float> pathsweights)
    {
        this.pathsweights = pathsweights;
    }
    
    public LinkedList<EntityPath> getListSubPaths()
    {
        return listsubpaths;
    }
    
    public void setListSubpaths(LinkedList<EntityPath> listsubpaths)
    {
        this.listsubpaths = listsubpaths;
    }
   
    //calcola il vettore risultante dei pesi delle feature
    public void calculateWeight(String typemean,HashSet<EntityPath> allsubpaths)
    {     
        HashMap<EntityPath,LinkedList<Double>> listentityweight = new HashMap<>();
        pathsweights.clear();
        //allsubpaths.parallelStream().forEach(e-> pathsweights.put(e, 0.0));
        HashBag hsbag = new HashBag();
        hsbag.addAll(listsubpaths);
        
        
        //setcompletepaths.parallelStream().forEach(e-> compute(hsbag,e) );
        
       for(EntityPath mainpath : setcompletepaths) {          
            LinkedList<EntityPath> subpath = mainpath.getSubPathList();
            int occ=0;
            for(EntityPath p : subpath)
            {   //is faster HashBag then Collections.frequency() and then stream().filter().count()
                long startTime3 = System.currentTimeMillis();
                occ =  hsbag.getCount(p);
                long stopTime3 = System.currentTimeMillis();
                long elapsedTime3 = stopTime3 - startTime3;
                //calculate single weight
                double totweightpath = calculatesingleWeight(mainpath,p,occ);
                if(listentityweight.containsKey(p))
                {
                    listentityweight.get(p).add(totweightpath);
                }else
                {
                    LinkedList<Double> list = new LinkedList<>();
                    list.add(totweightpath);
                    listentityweight.put(p, list);
                }
            }                
        }
        
         //System.out.println("Creating vector weights...");
        // createVectorWeights(listentityweight,typemean);
    }
    
    //MODIFICA3
    //calcola il vettore risultante dei pesi delle feature
    public void calculateWeight3(String typemean)
    {     
        
        HashMap<EntityPath,LinkedList<Float>> listentityweight = new HashMap<>();

        //allsubpaths.forEach(e-> pathsweights.put(e, 0.0));
        listsubpaths.forEach(e-> pathsweights.put(e, (float)0.0));
        
        HashBag hsbag = new HashBag();
        hsbag.addAll(listsubpaths);
        for(EntityPath mainpath : setcompletepaths) {          
            LinkedList<EntityPath> subpath = mainpath.getSubPathList();
            int occ=0;
            for(EntityPath p : subpath)
            {   //is faster HashBag then Collections.frequency() and then stream().filter().count()
                long startTime3 = System.currentTimeMillis();
                occ =  hsbag.getCount(p);
                long stopTime3 = System.currentTimeMillis();
                long elapsedTime3 = stopTime3 - startTime3;
                //calculate single weight
                //double totweightpath = calculatesingleWeight(mainpath,p,occ);
                float totweightpath = calculatesingleWeightF(mainpath,p,occ);
                
                if(listentityweight.containsKey(p))
                {
                    listentityweight.get(p).add(totweightpath);
                }else
                {
                    LinkedList<Float> list = new LinkedList<>();
                    list.add(totweightpath);
                    listentityweight.put(p, list);
                }
            }                
        }
        
        listentityweight.entrySet().parallelStream().forEach(epw-> pathsweights.put(epw.getKey(),epw.getValue().get(0)));
         //System.out.println("Creating vector weights...");
         
         //createVectorWeights(listentityweight,typemean);
        
        

    }
  

    public Double calculatesingleWeight(EntityPath mainpath,EntityPath p,int occ)
    {
        double value=0.0;
        double discount = 0;
        
        discount = Math.abs(mainpath.getPathList().size()-1) - Math.abs(p.getPathList().size()-1);                
        value = (double)occ/discount;

        return value;
    }
   
    
    public Float calculatesingleWeightF(EntityPath mainpath,EntityPath p,int occ)
    {
        float value=(float) 0.0;
        float discount = 0;
        
        discount = Math.abs(mainpath.getPathList().size()-1) - Math.abs(p.getPathList().size()-1);                
        value = (float)occ/discount;

        return value;
    }
    
    
    //controllo se il sub-path ha più di una occorrenza e fare la media aritmetica o geometrica dei pesi calcolati   
    public void createVectorWeights(HashMap<EntityPath,LinkedList<Float>> listentityweight,String typemean)
    {
        Set<Entry<EntityPath,LinkedList<Float>>> lew = listentityweight.entrySet();
        
        //listentityweight.entrySet().parallelStream().forEach(epw-> pathsweights.put(epw.getKey(),Utilities.meanArithmetic(epw.getValue())));
        listentityweight.entrySet().parallelStream().forEach(epw-> pathsweights.put(epw.getKey(),epw.getValue().get(0)));
        
        
        /*for(Entry<EntityPath, LinkedList<Double>> epw : lew)
        {
              // if(typemean.equals(NeighborhoodPathKernelMetric.MEAN_ARITHMETIC))
              // {//inserisco la media aritmetica
                   pathsweights.put(epw.getKey(),Utilities.meanArithmetic(epw.getValue()));
              // }else if(typemean.equals(NeighborhoodPathKernelMetric.MEAN_GEOMETRIC))
               //{//inserisco la media geometrcia
               //    pathsweights.put(epw.getKey(), Utilities.meanGeometric(epw.getValue()));
               //}
        }*/
    }
       
}
