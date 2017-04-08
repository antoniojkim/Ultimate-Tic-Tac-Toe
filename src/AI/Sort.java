/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package AI;

import java.util.List;

/**

@author Antonio
*/
public class Sort {
    
    /*
    public static void main (String[] args){
    String[] array = {"8", "6", "2", "10", "4"};
    //        int length = Random.randomint(5, 10);
    //        for (int a = 0; a<array.length; a++){
    //            array[a] = Random.randomString(length);
    //            System.out.print(array[a]+" ");
    //        }
    long start = System.nanoTime();
    quicksort(array);
    long end = System.nanoTime();
    System.out.println("\n");
    for (int a = 0; a<array.length; a++){
    System.out.print(array[a]+" ");
    }
    System.out.println("\n\nTook "+((end-start)/(100000.0))+" Milliseconds to sort");
    }
    /*
    */
    
    
    public static void quicksort(double[] values, List<int[]> possible){
        quicksort(values, possible, true);
    }
    public static void quicksort(double[] values, List<int[]> possible, boolean ascending){
        quicksort(values, 0, values.length-1, possible, ascending);
    }
    private static void quicksort(double[] array, int first, int last, List<int[]> possible, boolean ascending){
        if (first >= last){
            return;
        }
        double pivot = array[first];
        int pos = last;
        if (ascending){
            for (int a = last; a>first; a--){
                if (array[a] < pivot || (array[a] == pivot && Math.random() > 0)){
                    swap(array, possible, pos, a);
                    pos--;
                }
            }
        }
        else{
            for (int a = last; a>first; a--){
                if (array[a] > pivot || (array[a] == pivot && Math.random() > 0)){
                    swap(array, possible, pos, a);
                    pos--;
                }
            }
        }
        swap(array, possible, first, pos);
        quicksort(array, first, pos-1, possible, ascending);
        quicksort(array, pos+1, last, possible, ascending);
    }
    
    private static void swap(double[] array, List<int[]> possible, int pos1, int pos2){
        double temp = array[pos1];
        array[pos1] = array[pos2];
        array[pos2] = temp;
        
        int[] tempArray = possible.get(pos1);
        possible.set(pos1, possible.get(pos2));
        possible.set(pos2, tempArray);
    }
}
