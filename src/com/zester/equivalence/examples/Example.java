package com.zester.equivalence.examples;

import com.zester.equivalence.Equivalence;

/**
 *
 * @author Angelo
 */
public class Example {
    
    public static void main (String a[]){
        Equivalence equivalence = new Equivalence();
        String [] alphabet = equivalence.getStringDebuggedAsArray("a,b,c");
        String [][] automaton1 = equivalence.getTransitionTableAsMatrix(alphabet, 4);
        String [][] automaton2 = equivalence.getTransitionTableAsMatrix(alphabet, 3);
        
       equivalence.generateEquivalenceTable(automaton1, automaton2, alphabet, 4);
       
       equivalence.isEquivalent("q0,q1", "q2");
    }
}
