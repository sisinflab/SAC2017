package it.poliba.sisinflab.simlib;

import it.poliba.sisinflab.simlib.datamodel.Node;
import java.math.BigDecimal;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Utilities algorithms for arrays and maps operations
 *
 * @author Giorgio Basile
 * @since 1.0
 */
public class Utilities {

    /**
     * Returns the dot product between two vectors
     *
     * @param a first vector
     * @param b second vector
     * @return the dot product
     * @throws IllegalArgumentException if the two vector don't have the same length
     */
    public static float dotProduct(double[] a, double[] b){
        if(a.length != b.length) {
            throw new IllegalArgumentException("Error computing dotProduct in Utilities.dotProduct: arrays should have the same length");
        }
        float sp = 0;
        for (int i = 0; i < a.length; i++) {
            sp += a[i] * b[i];
        }

        return sp;
    }

    /**
     * Returns a vector norm
     *
     * @param v vector
     * @return the vector norm
     */
    public static double norm(double[] v) {
        double norm = 0;
        for (double c : v) {
            norm += Math.pow(c, 2);
        }
        norm = Math.sqrt(norm);
        return norm;
    }

    /**
     * Returns the sum of two vectors
     *
     * @param a first vector
     * @param b second vector
     * @return the sum
     * @throws IllegalArgumentException if the two vector don't have the same length
     */
    public static double[] sum(double[] a, double[] b){
        if(a.length != b.length){
            throw new IllegalArgumentException("Error computing sum in Utilities.sum: arrays should have the same length");
        }
        double[] sum = new double[a.length];
        for(int i = 0; i < a.length; i++){
            sum[i] = a[i] + b[i];
        }
        return sum;
    }

    /**
     * Returns the mean between two vectors
     *
     * @param a first vector
     * @param b second vector
     * @return the mean
     * @throws IllegalArgumentException if the two vector don't have the same length
     */
    public static double[] mean(double[] a, double[] b){
        if(a.length != b.length){
            throw new IllegalArgumentException("Error computing mean in Utilities.mean: arrays should have the same length");
        }
        double[] sum = new double[a.length];
        for(int i = 0; i < a.length; i++){
            sum[i] = (a[i] + b[i]) / 2;
        }
        return sum;
    }

    /**
     * Returns the cosine similarity value between two weights vectors
     *
     * @param weight1 first vector
     * @param weight2 second vector
     * @return similarity value between the two vectors
     */
    public static double cosineSimilarity(double weight1[], double weight2[]){
        return (Utilities.dotProduct(weight1, weight2) / (Utilities.norm(weight1) * Utilities.norm(weight2)));
    }


    public static <T, R> Map<T, R> sortByValues(Map<T, R> map) {
        List list = new LinkedList<>(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, Map.Entry.comparingByValue().reversed());

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap<T, R> sortedHashMap = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry<T, R> entry = (Map.Entry<T, R>) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static <T, Integer> void incrementMapCount(Map<T, java.lang.Integer> map, T object, int value) {
        if(!map.containsKey(object)){
            map.put(object, value);
        }else{
            map.put(object, map.get(object) + value);
        }
    }

    public static <T, Integer> void decrementMapCount(Map<T, java.lang.Integer> map, T object, int value) {
        if(!map.containsKey(object)){
            map.put(object, value);
        }else{
            map.put(object, map.get(object) - value);
        }
    }

    public static String uriEncoding(String plainString) {
        try {
            URL url = new URL(plainString);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return uri.toString();
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printTopN(Map<Node, Double> mapRank, int n){
        System.out.println("TOP " + n);

        int i = 0;
        Iterator<Map.Entry<Node, Double>> iter = mapRank.entrySet().iterator();
        while(iter.hasNext() && i < n){
            Map.Entry<Node, Double> e = iter.next();
            System.out.println((i + 1) + ". " + e.getKey().getId() + " " + e.getValue());
            i++;
        }
    }
    
      //arithmetic mean of list of double values
    public static float meanArithmetic(LinkedList<Float> a){
        int n = a.size();
        float sum=(float) 0.0;
        for(int i = 0; i < a.size(); i++){
            sum = sum + a.get(i);
        }
        return sum/n;
    }
    
    //geometric mean of list of double values
     public static double meanGeometric(LinkedList<Double> a){
        int n = a.size();
        //double prod=1.0;
        BigDecimal prod = new BigDecimal("1.0");
        for(int i = 0; i < a.size(); i++){
            prod = prod.multiply(new BigDecimal(a.get(i)));
            //prod = prod *a.get(i);
        }
        /*if(Math.pow(prod, 1.0/n)==Double.POSITIVE_INFINITY || Math.pow(prod, 1.0/n)==Double.NEGATIVE_INFINITY)
        {
            System.out.print("Value infinity"); 
        }*/  
        //return Math.pow(prod, 1.0/n);
        BigDecimal exp = new BigDecimal(1.0/n);
        BigDecimal valuebigdecimal = RootCalculus.nthRoot(n, prod);
        //BigDecimal z = BigFunctions.exp( BigFunctions.ln(x, SCALE).multiply(y),SCALE );
        //return prod.pow(exp);
        return valuebigdecimal.doubleValue();
    }

}
