/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package AI;

import Main.Board;
import Main.p;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*
* @author Antonio's Laptop
*/
public class MainAI{
    
    
    public static void move(List<Set> moves, Set path, int Largebox, int smallbox, int numtimes){
        Set set = new Set(path);
        set.add(Largebox, smallbox);
        if (numtimes>0){
            int result = check(set.grid);
            if (result != 0){
                moves.add(set);
            }
            else{
                List<Integer> possible = new ArrayList<>();
                for (int a = 0; a<Board.game.grid[smallbox].length; a++){
                    if (Board.game.grid[smallbox][a] == 0){
                        possible.add(a);
                    }
                }
                if (possible.isEmpty()){
                    for (int a = 0; a<9; a++){
                        for (int b = 0; b<9; b++){
                            move(moves, set, a, b, numtimes-1);
                        }
                    }
                }
                else{
                    for (int a = 0; a<possible.size(); a++){
                        move(moves, set, smallbox, possible.get(a), numtimes-1);
                    }
                }
            }
        }
        else{
            moves.add(set);
        }
    }
    
    public static int getHighest(int[] value){
        int highest = 0;
        for (int a = 0; a<value.length; a++){
            if (value[a] > highest){
                highest = value[a];
            }
        }
        return highest;
    }
    public static int getHighestat(int[] value){
        int highest = 0;
        List<Integer> highestat = new ArrayList<>();
        for (int a = 0; a<value.length; a++){
            if (value[a] > highest){
                highest = value[a];
                highestat.clear();
                highestat.add(a);
            }
            else if (value[a] == highest){
                highestat.add(a);
            }
        }
        if (highestat.size() > 0){
            return highestat.get(p.randomint(0, highestat.size()-1));
        }
        return -1;
    }
    
    static int[][][] planAhead = {
        /*Move == 0*/{{1, 2}, {3, 6}, {4, 8}},
        /*Move == 1*/{{0, 2}, {4, 7}},
        /*Move == 2*/{{0, 1}, {5, 8}, {4, 6}},
        /*Move == 3*/{{0, 6}, {4, 5}},
        /*Move == 4*/{{0, 8}, {1, 7}, {2, 6}, {3, 5}},
        /*Move == 5*/{{2, 8}, {3, 4}},
        /*Move == 6*/{{0, 3}, {2, 4}, {7, 8}},
        /*Move == 7*/{{1, 4}, {6, 8}},
        /*Move == 8*/{{0, 4}, {2, 5}, {6, 7}}
    };
    static int[][][] setup = {
        /*Move == 0*/{{1, 2}, {3, 6}},
        /*Move == 1*/{{0, 2}},
        /*Move == 2*/{{0, 1}, {5, 8}},
        /*Move == 3*/{{0, 6}},
        /*Move == 4*/{{0, 8}, {1, 7}, {2, 6}, {3, 5}},
        /*Move == 5*/{{2, 8}},
        /*Move == 6*/{{0, 3}, {7, 8}},
        /*Move == 7*/{{6, 8}},
        /*Move == 8*/{{2, 5}, {6, 7}}
    };
//    static int[][][] fork = {
//        /*Move == 0*/{{1, 3}, {1, 6}, {2, 3}, {2, 6}, {1, 6}},
//        /*Move == 1*/{{0, 2}},
//        /*Move == 2*/{{0, 1}, {5, 8}},
//        /*Move == 3*/{{0, 6}},
//        /*Move == 4*/{{0, 8}, {1, 7}, {2, 6}, {3, 5}},
//        /*Move == 5*/{{2, 8}},
//        /*Move == 6*/{{0, 3}, {7, 8}},
//        /*Move == 7*/{{6, 8}},
//        /*Move == 8*/{{2, 5}, {6, 7}}
//    };
    public static int[] players = {0, 2, 1};
    public static int canOpponentTake(int[]square, int player){
        for (int a = 0; a<9; a++){
            square[a] = player;
            if (filled(square, player)){
                square[a] = 0;
                return a;
            }
            square[a] = 0;
        }
        return -1;
    }
    public static boolean canOpponentWin(int[][]square, int largebox, int player){
        for (int a = 0; a<9; a++){
            square[largebox][a] = player;
            if (check(square) == player){
                square[largebox][a] = 0;
                return true;
            }
            square[largebox][a] = 0;
        }
        return false;
    }
    public static boolean canWin(int[][] totalgrid, int largebox, int smallbox, int player){
        totalgrid[largebox][smallbox] = player;
        if (filled(totalgrid[largebox], player) && check(totalgrid) == player){
            return true;
        }
        totalgrid[largebox][smallbox] = 0;
        return false;
    }
    public static boolean canTake(int[][] totalgrid, int largebox, int player){
        for (int a = 0; a<totalgrid[largebox].length; a++){
            if (totalgrid[largebox][a] == 0){
                totalgrid[largebox][a] = player;
                if (filled(totalgrid[largebox], player)){
                    totalgrid[largebox][a] = 0;
                    return true;
                }
                totalgrid[largebox][a] = 0;
            }
        }
        return false;
    }
    
    public static int check(int[][] array){
        int[] filled = new int[9];
        for (int b = 0; b<array.length; b++){
            for (int a = 1; a<=2; a++){
                if (filled(array[b], a)){
                    filled[b] = a;
                    break;
                }
            }
        }
        if (filled(filled, 1)){ //player 1 wins
            return 1;
        }
        else if (filled(filled, 2)){ // player 2 wins
            return 2;
        }
        else if (cats(filled)){ // cats game
            return 3;
        }
        return 0;
    }
    
    public static boolean cats(int[] filled){
        for (int a = 0; a<filled.length; a++){
            if (filled[a] == 0){
                return false;
            }
        }
        return true;
    }
    
    public static boolean filled(int[] array, int player){
        for (int a = 0; a<3; a++){
            if (array[3*a]== player && array[3*a+1]== player && array[3*a+2]== player ){
                return true;
            }
            else if (array[a]== player && array[a+3]== player && array[a+6]== player ){
                return true;
            }
            else if(a>=1 && array[2*a-4*(a-1)]== player && array[4*a-4*(a-1)]== player && array[6*a-4*(a-1)]== player){
                return true;
            }
        }
        return false;
    }
    public static int filled(int[] array){
        if (filled(array, 1)){
            return 1;
        }
        else if (filled (array, 2)){
            return 2;
        }
        else if (cats (array)){
            return 3;
        }
        return 0;
    }
    
    public static int getHeuristic(Set set){
        int[][] grid = set.grid;
        int[] Largefilled = set.Largefilled;
        int numlargefilled = set.numFilled;
        int numTaken1 = 0, numTaken2 = 0, numTaken3 = 0;
        int goodTaken1 = 0, goodTaken2 = 0, goodTaken3 = 0;
        int[] goodSetup = {0, 0, 0};
        int[] players = {1, 2, 3};
        for (int a = 0; a<grid.length; a++){
            if (Largefilled[a] == 0){
                if (numlargefilled > 0){
                    Largefilled[a] = 1;
                    if (MainAI.filled(Largefilled) == 1){
                        goodTaken1++;
                    }
                    Largefilled[a] = 2;
                    if (MainAI.filled(Largefilled) == 2){
                        goodTaken2++;
                    }
                    Largefilled[a] = 3;
                    if (MainAI.filled(Largefilled) == 3){
                        goodTaken3++;
                    }
                    Largefilled[a] = 0;
                }
                for (int b = 0; b<grid[a].length; b++){
                    for (int c = 0; c<players.length; c++){
                        grid[a][b] = players[c];
                        if (MainAI.filled(grid[a]) == players[c]){
                            goodSetup[c]++;
                        }
                    }
                    grid[a][b] = 0;
                }
            }
            else{
                if (Largefilled[a] == 1){
                    numTaken1++;
                }
                else if (Largefilled[a] == 2){
                    numTaken2++;
                }
                else if (Largefilled[a] == 3){
                    numTaken3++;
                }
            }
        }
        return (int)(2*goodTaken2+numTaken2-2*(goodTaken1+numTaken1)+goodTaken3+numTaken3
                +2*(goodSetup[1]-goodSetup[0])+goodSetup[2]);
    }
    
    public static int random(){
        if (Board.game.LargeBox == -1){
            while (true){
                int a = p.randomint(0, 8);
                int b = p.randomint (0, 8);
                if (Board.game.grid[a][b] == 0){
                    Board.game.LargeBox = a;
                    return b;
                }
            }
        }
        else{
            while (true){
                int a = p.randomint(0, 8);
                if (Board.game.grid[Board.game.LargeBox][a] == 0){
                    return a;
                }
            }
        }
    }
    public static int[] randomArray(){
        if (Board.game.LargeBox == -1){
            while (true){
                int[] array = {p.randomint(0, 8), p.randomint(0, 8)};
                if (Board.game.grid[array[0]][array[1]] == 0){
                    return array;
                }
            }
        }
        else{
            while (true){
                int a = p.randomint(0, 8);
                int[] array = {Board.game.LargeBox, p.randomint(0, 8)};
                if (Board.game.grid[array[0]][array[1]] == 0){
                    return array;
                }
            }
        }
    }
    
    public static void addRandom(Set set){
        int[][] grid = set.grid;
        int player = (set.size()%2)+1;
        List<int[]> possible = new ArrayList<>();
        if (set.nextLarge != -1 && set.Largefilled[set.nextLarge] == 0){
            for (int a = 0; a<9; a++){
                if (grid[set.nextLarge][a] == 0){
                    int[] array = {set.nextLarge, a};
                    possible.add(array);
                }
            }
        }
        else{
            for (int a = 0; a<9; a++){
                for (int b = 0; b<9; b++){
                    if (set.Largefilled[a] == 0 && grid[a][b] == 0){
                        int[] array = {a, b};
                        possible.add(array);
                    }
                }
            }
        }
        if (possible.size() > 0){
            int r = p.randomint(0, possible.size()-1);
            set.add(possible.get(r)[0], possible.get(r)[1]);
        }
    }
    
    public static List<int[]> getPossible(Set set){
        List<int[]> possible = new ArrayList<>();
        int[][] grid = set.grid;
        if (set.Largefilled[set.getLastSmall()] == 0){
            for (int a = 0; a<9; a++){
                if (grid[set.getLastSmall()][a] == 0){
                    int[] array = {set.getLastSmall(), a};
                    possible.add(array);
                }
            }
        }
        else{
            for (int a = 0; a<9; a++){
                if (set.Largefilled[a] == 0){
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
    
}
