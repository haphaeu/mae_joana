//package raf.classes.geometry;

import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * 
 * @author raf
 *
 * Test the scope of variables with different kinds of variables:
 * 
 *   . instance variables
 *   
 *   . static - class variable, accessible from all instances
 *   
 *   . final - instance constant. to be initialised at declaration or in constructor
 *   
 *   . static final - class variable, constant
 */
public class VariablesScope
{
    public static void main (String[] args)
    {
		System.out.println("TestScope.b = " + TestScope.b);
		
        TestScope ca = new TestScope(8);
        TestScope cb = new TestScope(9);
        ca.a = 2;
        ca.b = 3;
        // ca.c = 4; // wrong - variable is final
        // ca.d = 5; // wrong - variable is final
        System.out.println("ca.a = " + ca.a);
        System.out.println("cb.a = " + cb.a);
        System.out.println("ca.b = " + ca.b);
        System.out.println("cb.b = " + cb.b);
        System.out.println("ca.d = " + ca.d);
        System.out.println("cb.d = " + cb.d);
		System.out.println("TestScope.b = " + TestScope.b);
        
    }
}

 class TestScope {
    
     // instance variable, non-static field
    int a=1; 
    
    // class variable, static field
    static int b=1; 
    
    // class variable, constant immutable
    final static int c = 4; 
    
    // instance variable, constant - must be initialised before the constructor finished
    final int d; 
    
    TestScope(int tmp) {
        d = tmp;
    }
}

