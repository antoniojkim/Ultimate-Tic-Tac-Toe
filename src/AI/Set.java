/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package AI;

import Main.p;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
*
* @author Antonio's Laptop
*/
public class Set {
    
    public List<Integer> Large = new ArrayList<>();
    public List<Integer> Small = new ArrayList<>();
    
    public int nextLarge = 0;
    public int numFilled = 0;
    public int[] Largefilled = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    public int[] numFill = {0, 0, 0};
    public int[][] grid = new int[9][9];
    int score = 0;
    int winner = 0;
    
    public Set(){
        
    }
    public Set(Set oldset){
        copy(oldset);
    }
    public Set(Set oldset, int large, int small){
        copy(oldset);
        add(large, small);
    }
    
    public void add(int large, int small){
        if (winner == 0){
            this.Large.add(large);
            this.Small.add(small);
            grid[large][small] = 2-(size()%2);
            int result = MainAI.filled(grid[large]);
            if (result != 0){
                numFilled++;
                Largefilled[large] = result;
                numFill[result-1]++;
                if (numFill[0] > 2 || numFill[1] > 2 || numFill[2] > 2){
                    winner = MainAI.filled(Largefilled);
                }
            }
            if (Largefilled[small] != 0){
                nextLarge = -1;
            }
            else{
                nextLarge = small;
            }
        }
    }
    public void copy(Set oldset){
        Large.clear();
        Small.clear();
        Large.addAll(oldset.Large);
        Small.addAll(oldset.Small);
        numFilled = oldset.numFilled;
        for (int a = 0; a<Largefilled.length; a++){
            Largefilled[a] = oldset.Largefilled[a];
        }
        for (int a = 0; a<grid.length; a++){
            for (int b = 0; b<grid[a].length; b++){
                grid[a][b] = oldset.grid[a][b];
            }
        }
        winner = oldset.winner;
        numFill[0] = oldset.numFill[0];
        numFill[1] = oldset.numFill[1];
        numFill[2] = oldset.numFill[2];
        nextLarge = oldset.nextLarge;
    }
    public void remove(){
        this.Large.remove(Large.size()-1);
        this.Small.remove(Small.size()-1);
    }
    public int size(){
        return this.Large.size();
    }
    public int numEmpty(){
        int numEmpty = 81;
        for (int a = 0; a<grid.length; a++){
            if (Largefilled[a] != 0){
                numEmpty -= 9;
            }
            else{
                for (int b = 0; b<grid[a].length; b++){
                    if (grid[a][b] != 0){
                        numEmpty--;
                    }
                }
            }
        }
        return numEmpty;
    }
    public int numNonEmpty(){
        int numEmpty = 0;
        for (int a = 0; a<grid.length; a++){
            if (Largefilled[a] != 0){
                numEmpty += 9;
            }
            else{
                for (int b = 0; b<grid[a].length; b++){
                    if (grid[a][b] != 0){
                        numEmpty++;
                    }
                }
            }
        }
        return numEmpty;
    }
    public Set getSubset(int start, int end){
        Set set = new Set();
        for (int a = start; a<end; a++){
            set.add(this.Large.get(a), this.Small.get(a));
        }
        return set;
    }
    public boolean contains (Set set){
        if (set.size() != size()){
            return false;
        }
        else{
            for (int a = set.size()-1; a>=0; a--){
                if (set.Small.get(a).equals(Small.get(a)) || !set.Large.get(a).equals(Large.get(a))){
                    return false;
                }
            }
            return true;
        }
    }
    public String getString(){
        String string = "Set:  ";
        for (int a = 0; a<Large.size(); a++){
            if (a != 0){
                string += ", ";
            }
            string += "["+Large.get(a)+", "+Small.get(a)+"]";
        }
        return string;
    }
    public void print(){
        System.out.println(getString());
    }
//    public int[][] toArray(){
//        int[][] array = new int [9][9];
//        for (int a = 0; a<Large.size(); a++){
//            array[Large.get(a)][Small.get(a)] = (a%2)+1;
//        }
////        for (int a = 0; a<array.length; a++){
////            Largefilled[a] = MainAI.filled(array[a]);
////        }
//return array;
//    }
    public int getLastSmall(){
        return this.Small.get(this.Small.size()-1);
    }
    public int getLastLarge(){
        return this.Large.get(this.Large.size()-1);
    }
    public List<int[]> getNextPossible(){
        List<int[]> possible = new ArrayList<>();
        if (nextLarge != -1 && Largefilled[nextLarge] == 0){
            for (int a = 0; a<9; a++){
                if (grid[nextLarge][a] == 0){
                    int[] array = {nextLarge, a};
                    possible.add(array);
                }
            }
        }
        else{
            for (int a = 0; a<9; a++){
                if (Largefilled[a] == 0){
                    for (int b = 0; b<9; b++){
                        if (grid[a][b] == 0){
                            int[] array = {a, b};
                            possible.add(array);
                        }
                    }
                }
            }
        }
        return possible;
    }
    public void addRandom(){
        List<int[]> possible = getNextPossible();
        if (!possible.isEmpty()){
            int[] move = possible.get(p.randomint(0, possible.size()-1));
            add(move[0], move[1]);
        }
    }
}
