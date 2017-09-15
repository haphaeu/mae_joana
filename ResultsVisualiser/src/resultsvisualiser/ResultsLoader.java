/*
 DEPRECATED CLASS

 To be deleted soon...

 Much better implemented in ResultsMatrix.

 */
package resultsvisualiser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author rarossi
 */
@Deprecated
public class ResultsLoader {
    public HashMap<String, double[]> table;
    public HashMap<String, Integer> seastateCounter;
    public HashMap<Double, double[]> periodsPerHs;
    public HashSet<Integer> directions;
    public String[] setHs;
    public String[] setTp;
    public String[] setDirections;
    String[] variables;
    String fname;
    
    ResultsLoader(File file) {
        this(file.getPath());
    }
    ResultsLoader (String nm) {
        this();
        fname = nm;
        readFile(fname);
        createNameSets();
    }
    ResultsLoader () {
        table = new HashMap<String, double[]>();
        seastateCounter = new HashMap<String, Integer>();
        periodsPerHs = new HashMap<Double, double[]>();
        directions = new HashSet<Integer>();
    }
    final void createNameSets() {
        setDirections = new String[directions.size()];
        int i = 0;
        System.out.println("Names of Directions:");
        for (int wd: directions) {
            setDirections[i++] = Integer.toString(wd);
            System.out.print("   " + setDirections[i-1]);
        }
        System.out.println("");
        
        HashSet<Double> periods = new HashSet<Double>();
        setHs = new String[periodsPerHs.keySet().size()];
        i = 0;
        System.out.println("Names of Hs:");
        for (double hs: periodsPerHs.keySet()) {
            setHs[i++] = Double.toString(hs);
            System.out.print("   " + setHs[i-1]);
            for (double tp: periodsPerHs.get(hs)) {
                periods.add(tp);
            }
        }
        System.out.println("");
        
        setTp = new String[periods.size()];
        i = 0;
        System.out.println("Names of Tp:");
        for (double tp: periods) {
            setTp[i++] = Double.toString(tp);
            System.out.print("   " + setTp[i-1]);
        }
        System.out.println("");
    }
    final void readFile(String fname) {
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(fname));
            try {
                StringBuilder sb = new StringBuilder();
                
                // First line is the header with the names of variables
                // Need to remove first 3 columns with Hs, Tp and direction
                String line = br.readLine();
                String[] vars_tmp = line.split("\t");
                List<String> tmp = new ArrayList(Arrays.asList(vars_tmp));  
                tmp.remove(2); // direction
                tmp.remove(1); // Tp
                tmp.remove(0); // hs
                //variables = (String[]) tmp.toArray();
                variables = tmp.toArray(new String[0]);
                // get number of results
                int num = variables.length ;
                
                System.out.println("Init'd variables of size " + num);
                for (String var: variables) {
                    System.out.println(var);
                }
                
                // Read results line by line
                String[] tmp2;
                String tag;
                double[] vals = new double[num];
                line = br.readLine();
                while (line != null) {
                    tmp2 = line.split("\t");
                    tag = createKey(tmp2[0], tmp2[1], tmp2[2]);
                    System.out.println("Entry: " + tag);
                    for (int i=0; i < num; i++) {
                        vals[i] = Double.parseDouble(tmp2[i + 3]);
                        //table[i].add(x)
                    }
                    table.put(tag, vals);
                    
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Problem reading file" + fname);
        }
    }
    
    String createKey(String sHs, String sTp, String sDirection) {
        int seed;
        HashSet<Double> tps;
        double hs = Double.parseDouble(sHs);
        double tp = Double.parseDouble(sTp);
        Integer wd = (int) Double.parseDouble(sDirection);
        String key = "Hs" + sHs + "Tp" + sTp + "wd" + sDirection;
        
        // update array with all directions
        if (!directions.contains(wd)) {
            directions.add(wd);
        }
        
        // update array with a list of Tps per Hs
        if (!periodsPerHs.containsKey(hs)) {
            periodsPerHs.put(hs, new double[] {tp});
        } else {
            tps = new HashSet<Double>();
            for (double x: periodsPerHs.get(hs)) {
                tps.add(x);
            }
            tps.add(tp);
            double[] dTps = new double[tps.size()];
            int i = 0;
            for (double x: tps) {
                dTps[i++] = x;
            }
            periodsPerHs.replace(hs, dTps);
        }
        
        if (seastateCounter.containsKey(key)) {
            seed = seastateCounter.get(key) + 1;
            seastateCounter.replace(key, seed);
        } else {
            seed = 1;
            seastateCounter.put(key, seed);
        }
        return key + "seed" + seed;
    }
    
    void printTable() {
        for(String key: table.keySet()) {
            System.out.print("Key: " + key);
            for(double x: table.get(key)) {
                System.out.print(" " + x);
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) {
        // testing of class
        ResultsLoader loader = new ResultsLoader("results.txt");
    }
}
