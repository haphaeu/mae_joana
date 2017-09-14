package resultsvisualiser; 
        
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/*
 * ResultsMatrix
 * *************
 * 
 * Reads the results file from the repeated lowering control module, typically
 * called "results.txt". 
 * 
 * Converts the data into a double[][] matrix with all the rows. 
 * 
 * Implements getters that work with the ResultsVisualiser application.
 * 
 * Private members:
 *   double[][] m               : matrix with all data, m[3+numVars][numRows]
 *   String[] variables         : names of the variables
 *   int numRows, numVars       : number od data rows and number of variables
 *   private String fileContents: string with the contents of results.txt
 *
 * Public getters:
 *   String[] getVars()
 *   int      getNumVars()
 *   int      getRows()
 *   double[] getSample(String var, double hs, double tp, double wd) 
 *   double[] getSample(ind idxVar, double hs, double tp, double wd)
 *   Double[] getHsSet()
 *   Double[] getTpSet()
 *   Double[] getTpSet(double hs)
 *   Double[] getWDSet()
 *   String[] getHsSetasString()
 *   String[] getTpSetasString()
 *   String[] getTpSetasString(double hs)
 *   String[] getWDSetasString
 *
 * Public setters:
 *    void loadResults(File f)
 *    void loadResults(String fname)
 *
 * Main implemented to perform some tests and benchmarking.
 *
 *
 * @author: Rafael Rossi
 * @date: 13/09/2017
 */
public class ResultsMatrix {

    private double [][] m;
    private String [] variables;
    private int numRows, numVars;
    private String fileContents;
    
    ResultsMatrix() {}
    ResultsMatrix(File file) {
        loadResults(file);
        
    }
    ResultsMatrix(String fname) {
        loadResults(fname);
    }
    /*
     * Compare two double for equality.
     */
    private boolean equal(double x, double y) {
        return Math.abs(x-y) < 1e-6;
    }
    
    /*
     * Reads the file with results
     */
    private void readFile(File file) {
        try {
            Scanner sc = new Scanner(file);
            fileContents = sc.useDelimiter("\\Z").next();
            sc.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    /*
     * Converts file contents into matrix
     */
    private void createMatrix() {
        if (fileContents == null) {
            return;
        }
        String[] lines = fileContents.split("\n");
        String[] header = lines[0].replace("\n", "").replace("\r", "").split("\t");
        
        numRows = lines.length - 1;       // not count header
        numVars = header.length - 3;      // not count Hs, Tp, wd
        
        //System.out.println("Matrix has " + numVars + " variables and " + numRows + " rows.");
        
        
        variables = new String[numVars];
        for(int i=0; i < numVars; i++) {
            variables[i] = header[3+i];
        }
        
        m = new double[3+numVars][numRows];
        String[] line;
        for(int j=0; j < numRows; j++) {
            line = lines[j+1].split("\t");
            for(int i=0; i < 3+numVars; i++) {
                m[i][j] = Double.parseDouble(line[i]);
            }
        }
    }
    
    /*********************************************************************************************
     * Getters for private members
     */
    public String[] getVars() {
        return variables;
    }
    public int getRows() {
        return numRows;
    }
    public int getNumVars() {
        return numVars;
    }
    /*
     * Returns a sample with the number of seeds for the input variable and seastate
     */
    public double[] getSample(int idxVar, double hs, double tp, double wd) {
        int maxSize = 200;
        double[] sample = new double[maxSize];
        int sample_size = 0;
        for (int rw=0; rw < numRows; rw++) {
            if (equal(m[0][rw], hs) && equal(m[1][rw], tp) && equal(m[2][rw], wd)) {
                sample[sample_size++] = m[3+idxVar][rw];
                if (sample_size == maxSize) {
                    maxSize += 50;
                    sample = Arrays.copyOf(sample, maxSize);
                }
            }
        }
        if (sample_size == 0) {
            return null;
        }
        // Now remove unused allocated space and return only the valid sample entries
        double[] ret_sample = new double[sample_size];
        for (int i=0; i < sample_size; i++) {
            ret_sample[i] = sample[i];
        }
        Arrays.sort(ret_sample);
        return ret_sample;
    }
    
    /*
     * Returns a sample with the number of seeds for the input variable index and seastate
     */
    public double[] getSample(String var, double hs, double tp, double wd) {
        int idxVar = -1;
        for (int i=0; i < variables.length; i++) {
            if (var.equals(variables[i])) {
                idxVar = i;
                break;
            }
        }
        if (idxVar == -1) {
            return new double[] {0};
        } else {
            return getSample(idxVar, hs, tp, wd);
        }
    }
    
    private Double[] getSet(int idxSet) {
        Double[] all = new Double[numRows];
        for (int i=0; i < numRows; i++) {
            all[i] = m[idxSet][i];
        }
        Set<Double> mySet = new HashSet<Double>(Arrays.asList(all));
        Double[] ret = new Double[mySet.size()]; 
        ret = mySet.toArray(ret);
        return ret;
    }
    public Double[] getHsSet() {
        return getSet(0);
    }
    public Double[] getTpSet() {
        return getSet(1);
    }
    public Double[] getWDSet() {
        return getSet(2);
    }
    public Double[] getTpSet(double hs) {
        Double[] allTp = new Double[numRows];
        for (int i=0; i < numRows; i++) {
            if (equal(hs, m[0][i])) {
                allTp[i] = m[1][i];
            } else {
                allTp[i] = 0.0;
            }
        }
        Set<Double> mySet = new HashSet<Double>(Arrays.asList(allTp));
        mySet.remove(0.0);
        Double[] retTp = new Double[mySet.size()]; 
        retTp = mySet.toArray(retTp);
        return retTp; 
    }
    private String[] getAsString(int idxSet) {
        Double[] set = getSet(idxSet);
        String[] asString = new String[set.length];
        for (int i=0; i < set.length; i++)  {
            asString[i] = set[i].toString();
        }
        return asString;
    }
    public String[] getHsSetasString() {
        return getAsString(0);
    }
    public String[] getTpSetasString() {
        return getAsString(1);
    }
    public String[] getWDSetasString() {
        return getAsString(2);
    }
    public String[] getTpSetasString(double hs) {
        Double[] set = getTpSet(hs);
        String[] asString = new String[set.length];
        for (int i=0; i < set.length; i++)  {
            asString[i] = set[i].toString();
        }
        return asString;
    }
    /*********************************************************************************************
     * Setters, sort of
     */
    public final void loadResults(String fname) {
        File file = new File(fname);
        loadResults(file);
    }
    public final void loadResults(File file) {
        readFile(file);
        createMatrix();
    }
    
    /**************************************************************************************************
     * From here onwards, some testing functions
     */
    void print() {
        System.out.println("Variables:");
        for(int i=0; i < numVars; i++) {
            System.out.println(variables[i]);
        }
        for(int j=0; j < numRows; j++) {
            for(int i=0; i < 3+numVars; i++) {
                System.out.print("   " + m[i][j]);
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        ResultsMatrix test = new ResultsMatrix();
        test.loadResults("test.txt");
        
        test.print();
        
        System.out.println("Sample is:");
        double[] sample = test.getSample("var1", 2.5, 8.0, 180);
        for (double x: sample) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nHs set is:");
        for (Double x: test.getHsSet()) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nTp set is:");
        for (Double x: test.getTpSet()) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nTp set is to Hs 3:");
        for (Double x: test.getTpSet(3)) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nDirections are:");
        for (Double x: test.getWDSet()) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nHs set as string is:");
        for (String x: test.getHsSetasString()) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nTp set as string is:");
        for (String x: test.getTpSetasString()) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nTp set as string is to Hs 3:");
        for (String x: test.getTpSetasString(3)) {
            System.out.print("   " + x);
        }
        
        System.out.println("\nDirections as string are:");
        for (String x: test.getWDSetasString()) {
            System.out.print("   " + x);
        }
    }
}
