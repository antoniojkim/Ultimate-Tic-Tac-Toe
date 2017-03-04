/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package AI;

import Main.Board;
import Main.p;
import java.util.ArrayList;
import java.util.List;

/**
*
* @author Antonio's Laptop
*/
public class randomTree implements Runnable{
    
    protected Set set;
    protected int winner = 0;
    protected int[] givingLargeOptions = {0, 0, 0};
    int[] other = {0, 2, 1};
    
    public randomTree(){
        this.set = new Set();
    }
    public randomTree(Set set){
        this.set = new Set(set);
        winner = set.winner;
    }
    
    @Override
    public void run() {
        while (set.winner == 0){
            addRandom();
            if (set.winner != 0){
                break;
            }
        }
        winner = set.winner;
    }
    
    public void addRandom(){
        int player = (set.size()%2)+1;
        List<int[]> possible = getPossible();
        if (set.nextLarge == -1){
            givingLargeOptions[player]++;
        }
        if (possible.size() > 0){
            int r = p.randomint(0, possible.size()-1);
            set.add(possible.get(r)[0], possible.get(r)[1]);
        }
    }
    
    public List<int[]> getPossible(){
        int player = (set.size()%2)+1;
        List<int[]> possible = new ArrayList<>();
        if (set.nextLarge != -1 && set.Largefilled[set.nextLarge] == 0){
            for (int a = 0; a<9; a++){
                if (set.grid[set.nextLarge][a] == 0){
                    int[] array = {set.nextLarge, a};
                    if (player == 2 && MainAI.canWin(set.grid, array[0], array[1], 2)){
                        possible.clear();
                        possible.add(array);
                        return possible;
                    }
                    else{
                        possible.add(array);
                    }
                }
            }
        }
        else{
            for (int a = 0; a<9; a++){
                for (int b = 0; b<9; b++){
                    if (set.Largefilled[a] == 0 && set.grid[a][b] == 0){
                        int[] array = {a, b};
                        if (player == 2 && MainAI.canWin(set.grid, array[0], array[1], 2)){
                            possible.clear();
                            possible.add(array);
                            return possible;
                        }
                        else{
                            possible.add(array);
                        }
                    }
                }
            }
        }
//        if (Board.game.path.size() > 45 && player == 2){
//            List<int[]> viable = new ArrayList<>();
//            for (int a = 0; a<possible.size(); a++){
//                Set newset = new Set(set, possible.get(a)[0], possible.get(a)[1]);
//                boolean viableCandidate = true;
//                if (newset.nextLarge != -1 && newset.Largefilled[newset.nextLarge] == 0){
//                    for (int b = 0; b<9; b++){
//                        if (newset.grid[newset.nextLarge][b] == 0){
//                            int[] array = {newset.nextLarge, b};
//                            if (MainAI.canWin(newset.grid, array[0], array[1], 1)){
//                                viableCandidate = false;
//                                break;
//                            }
//                        }
//                    }
//                    if (viableCandidate){
//                        viable.add(possible.get(a));
//                    }
//                }
//                else{
//                    for (int b = 0; b<9; b++){
//                        for (int c = 0; c<9; c++){
//                            if (newset.Largefilled[b] == 0 && newset.grid[b][c] == 0){
//                                int[] array = {b, c};
//                                if (MainAI.canWin(newset.grid, array[0], array[1], 1)){
//                                    viableCandidate = false;
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (viableCandidate){
//                        viable.add(possible.get(a));
//                    }
//                }
//            }
//            return viable;
//        }
        return possible;
    }
    
}
