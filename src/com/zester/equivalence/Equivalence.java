package com.zester.equivalence;


import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * @version 1.0
 * @author Angelo
 * Conjunto de métodos para realizar equivalencias entre autómatas
 */
public final class Equivalence {

    private ArrayList columnZero;
    private final String originMessage = "Ingresa el estado de origen de la transición No.";
    private final String destinyMessage = "Ingresa el estado de destino de \n";
    private String alphabet;
    private int amountStates1;
    private int amountStates2;
    
    public Equivalence(){
        columnZero = new ArrayList();
    }
    
	/**
     *
     * @param finalState1 estado final del primer autómata
	 * @param finalState2 estado final del segundo autómata
	 * @return si los autómatas son equivalentes (si sus estados son compatibles)
     */
    public boolean isEquivalent(String finalState1, String finalState2){
        boolean compatible = false;
         for (Object element : columnZero) {
                if(!isCompatible((String) element, finalState1, finalState2)){
                    compatible = false;
                    break;
                } else compatible = true;
            }
         return compatible;
    }

    /**
     *
     * @param text el texto a depurar(quitar comas, alfabeto)
     * @return un arreglo de caracteres de la cadena ingresada como parámetro
     * omitiendo comas
     */
    public String[] getStringDebuggedAsArray(String text) {
        int j = 0;
        //count symbols
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ',') {
                j++;
            }
        }
        //add symbols to array
        String[] symbols = new String[j];
        int k = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ',') {
                symbols[k] = text.charAt(i) + "";
                k++;
            }
        }
        return symbols;
    }

    /**
     *
     * @param symbols los simbolos del alfabeto como arreglo (depurado, sin
     * comas)
     * @param amountStates cantidad de estados del automata a generar su tabla
     * de transiciones
     * @return la matriz de tamaño: cant de edos(filas)*simbolos(oolumnas) se
     * tiene que llenar con el origen y destino
     */
    public String[][] getTransitionTableAsMatrix(String[] symbols, int amountStates) {
        int amountSymbols = symbols.length;

        int amountRows = amountStates * amountSymbols;
        String[][] transitionTable = new String[amountRows][3];

        int k = 0;
        for (int j = 0; j < amountStates; j++) {
            for (int i = 0; i < amountSymbols; i++) {
                transitionTable[k][0] = getInputFromMessage(originMessage + (k + 1));
                transitionTable[k][1] = symbols[i];
                transitionTable[k][2] = getInputFromMessage(destinyMessage
                        + transitionTable[k][0]
                        + "  ,       "
                        + transitionTable[k][1]
                        + "  ----   > ");
                k++;
            }
        }

        return transitionTable;
    }

    /**
     *
     * @param message el mensaje a mostrar al usuario
     * @return lo que el usuario introduzca al dialogo
     */
    public String getInputFromMessage(String message) {
        return JOptionPane.showInputDialog(null, message);
    }
    
    /**
     *
     * @param message to show
     */
    public void showMessage(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     *
     * @param automaton1 el primer automata
     * @param automaton2 el segundo automata
     * @param symbols los simbolos del alfabeto
     * @param amountStates la cantidad de estados
     */
    public void generateEquivalenceTable(String[][] automaton1, String[][] automaton2, String[] symbols, int amountStates) {
        //se crea el arreglo con tamaño de [tamaño_del_automata][simbolos_del_alfabeto]
        String[][] equivalenceTable = new String[amountStates][symbols.length];
        int j = 0;
        int l = 0;
        //se agrega el par ordenado (s,s') que contiene los estados iniciales m,m'
        getColumnZero().add(automaton1[j][0] + automaton2[j][0]);
        int lengthState1 = automaton1[j][0].length();
        int lengthState2 = automaton2[j][0].length();
        int i = 0;
        while (i < equivalenceTable.length) {
            //se añade a la tabla cada estado para cada simbolo
            for (int k = 0; k < symbols.length; k++) {
                equivalenceTable[i][k] = automaton1[l][2] + automaton2[j][2];
                j++;
                l++;
            }
            //busca los punteros de los estados nuevos de cada automata
            checkColumnZero(equivalenceTable, symbols, i);
            try {
                l = getNewPointer(i, 0, lengthState1, automaton1);
                j = getNewPointer(i, lengthState1, lengthState1 + lengthState2, automaton2);
            } catch (Exception e) {}
            i++;
            showMessage("Paso "+i+"°\n"+getMatrixAsString(equivalenceTable, symbols));
        }
    }

    /**
     *
     * @param equivalenceTable la tabla de equivalencia a evaluar
     * @param symbols los simbolos del alfabeto(para las columnas)
     * @param index el indice que indica donde ya se ha verificado
     * @see Verifica cada elemento de la tabla de equivalencia a partir del
     * indice
     */
    public void checkColumnZero(String[][] equivalenceTable, String[] symbols, int index) {
        for (int i = 0; i < symbols.length; i++) {
            if (!getColumnZero().contains(equivalenceTable[index][i])) {
                if (equivalenceTable[index][i].equals("") || equivalenceTable[index][i] == null) {
                    break;
                } else {
                    getColumnZero().add(equivalenceTable[index][i]);
                }
            }
        }
    }

    /**
     *
     * @param index el indice donde esta actualmente ubicada la tabla de
     * equivalencia
     * @param start inicio del substring
     * @param end fin del substring
     * @param automaton automata a evaluar
     * @return el puntero donde se encuentra el estado siguiente
     */
    public int getNewPointer(int index, int start, int end, String[][] automaton) {
        return getPointerFromState(getStateFromPair(getColumnZero().get(index + 1) + "", start, end), automaton);
    }

    /**
     *
     * @param pair el par de estados
     * @param start el inicio del substring
     * @param end el final del substring
     * @return el estado a partir del string completo
     */
    public String getStateFromPair(String pair, int start, int end) {
        return pair.substring(start, end);
    }

    /**
     *
     * @param state el estado a buscar en el automata como matriz
     * @param automaton el automata como matriz
     * @return el puntero en donde se encuentra el estado en la matriz
     */
    public int getPointerFromState(String state, String[][] automaton) {
        for (int i = 0; i < automaton.length; i++) 
            if (state.equals(automaton[i][0])) 
                return i;
        return 0;
    }

    /**
     *
     * @param matrix a imprimir
     * @param symbols columnas de la matriz
     * @return la matriz en forma de texto
     */
    public String getMatrixAsString(String[][] matrix, String[] symbols) {
        String tableGenerated = "";
        for (int k = 0; k < matrix.length; k++) {
            for (int m = 0; m < symbols.length; m++) 
                tableGenerated += "|   " + matrix[k][m] + "    |";
            tableGenerated += "\n";
        }
        tableGenerated += "\n\n\n";
        tableGenerated += "Columna Cero:\n" + getColumnZero().toString();
        return tableGenerated;
    }
    
    /**
     *
     * @param state
     * @param finalStates1
     * @param finalStates2
     * @return si es compatible el estado actual (no todo el autómata, solo uno)
     */
    public boolean isCompatible(String state, String finalStates1, String finalStates2) {
        boolean isFinal1 = false, isFinal2 = false;
        String state1 = state.substring(0, 2);
        String state2 = state.substring(2, 4);
        System.out.println("STATE1 " + state1 + " STATE2 " + state2);

        if (finalStates1.length() > 2 || finalStates2.length() > 2) {
            String [] finalState1 = decomposeFinalStates(finalStates1);
            String[] finalState2 = decomposeFinalStates(finalStates2);
            System.out.println("FINAL STATE1 "+Arrays.toString(finalState1)+" FINAL STATE2 "+Arrays.toString(finalState2));
            for (int i = 0; i < finalState1.length; i++) {
                if(state1.equals(finalState1[i])){
                    isFinal1 = true;
                    break;
                } else isFinal1 = false;
            }
            for (int j = 0; j < finalState2.length; j++) {
                if (state2.equals(finalState2[j])) {
                    isFinal2 = true;
                    break;
                } else {
                    isFinal2 = false;
                }
            }
        } else {
            System.out.println("FINAL STATE1 " + finalStates1 + " FINAL STATE2 " + finalStates2);
            if (state1.equals(finalStates1) && state2.equals(finalStates2)) {
                isFinal1 = isFinal2 = true;
            } else if (!state1.equals(finalStates1) && state2.equals(finalStates2)) {
                isFinal1 = false;
                isFinal2 = true;
            } else if (state1.equals(finalStates1) && !state2.equals(finalStates2)) {
                isFinal1 = true;
                isFinal2 = false;
            }
        }
        if (isFinal1 && isFinal2) {
            return true;
        } else if (!isFinal1 && !isFinal2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param finalStates
     * @return un arreglo de estados
     */
    public String[] decomposeFinalStates(String finalStates) {
        int j = 0;
        for (int i = 0; i < finalStates.length(); i++) {
            if (finalStates.charAt(i) == ',') {
                j++;
            }
        }
        String[] states = new String[j + 1];
        for (int i = 0; i < states.length; i++) {
            states[i] = "";
        }
        int i = 0;
        int k = 0;
        while (i < states.length) {
            if(k >= finalStates.length())break;
            if (finalStates.charAt(k) != ',') {
                states[i] += finalStates.charAt(k)+"";
                k++;
            } else if (finalStates.charAt(k) == ','){
                i++;
                k++;
            }
        }
        return states;
    }

    /**
     * @return the columnZero
     */
    public ArrayList getColumnZero() {return columnZero; }

    /**
     * @param columnZero the columnZero to set
     */
    public void setColumnZero(ArrayList columnZero) {this.columnZero = columnZero;}

    /**
     * @return the alphabet
     */
    public String getAlphabet() {
        return alphabet;
    }

    /**
     * @param alphabet the alphabet to set
     */
    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * @return the amountStates1
     */
    public int getAmountStates1() {
        return amountStates1;
    }

    /**
     * @param amountStates1 the amountStates1 to set
     */
    public void setAmountStates1(int amountStates1) {
        this.amountStates1 = amountStates1;
    }

    /**
     * @return the amountStates2
     */
    public int getAmountStates2() {
        return amountStates2;
    }

    /**
     * @param amountStates2 the amountStates2 to set
     */
    public void setAmountStates2(int amountStates2) {
        this.amountStates2 = amountStates2;
    }
}
