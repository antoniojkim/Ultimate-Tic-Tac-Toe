///*
//* To change this license header, choose License Headers in Project Properties.
//* To change this template file, choose Tools | Templates
//* and open the template in the editor.
//*/
//package AI;
//
//import Main.Board;
//import Main.p;
//import java.util.ArrayList;
//import java.util.List;
//
///**
//
//@author Antonio
//*/
//public class PatternRegonition{
//    
//    int player;
//    
//    static List<int[]> win1 = new ArrayList<>();
//    static List<int[]> win2 = new ArrayList<>();
//    static List<int[]> tie = new ArrayList<>();
//    static List<Thread> threads = new ArrayList<>();
//    int numNetworks = 3;
//    int[][] values = new int[possible.size()][numNetworks];
//    
//    public void move(int player){
//        long start = System.currentTimeMillis();
//        this.player = player;
//        if (Board.game.path.size() >= 3){
//            //planAhead.interrupt();
//            //planAhead2.interrupt();
//            setValues(getMove(player));
//        }
//        else if (Board.game.path.size() < 3){
//            int move = MainAI.random();
//            setValues (move);
//            if (win1.isEmpty() || win2.isEmpty() || tie.isEmpty()){
//                win1.clear();
//                win2.clear();
//                tie.clear();
//                findIterations(new ArrayList<>());
//            }
//        }
//        long time = System.currentTimeMillis()-start;
//        //System.out.println("Took "+time+" Milliseconds to process\n");
//        if(time < 750){
//            p.delay(750-time);
//        }
//    }
//    
//    public static void findIterations (List<Integer> list){
//        if (list.size() == 9){
//            int[] grid = new int[9];
//            for (int a = 0; a<grid.length; a++){
//                grid[a] = list.get(a);
//            }
//            int result = MainAI.filled(grid);
//            //count++;
//            if (result == 1){
//                win1.add(grid);
//            }
//            else if (result == 2){
//                win2.add(grid);
//            }
//            else if (result == 3){
//                tie.add(grid);
//            }
//        }
//        else{
//            for (int a = 0; a<3; a++){
//                List<Integer> newlist = new ArrayList<>();
//                newlist.addAll(list);
//                newlist.add(a);
//                findIterations(newlist);
//            }
//        }
//    }
//    
//    public int getMove(int player){
//        possible.clear();
//        int[][] grid = Board.game.path.toArray();
//        //Find all the possible moves
//        if (Board.game.path.nextLarge != -1 && Board.game.path.Largefilled[Board.game.path.nextLarge] == 0){
//            for (int a = 0; a<9; a++){
//                if (Board.game.grid[Board.game.path.nextLarge][a] == 0){
//                    int[] array = {Board.game.path.nextLarge, a};
//                    if (MainAI.canWin(grid, array[0], array[1], player)){
//                        Board.game.LargeBox = array[0];
//                        return array[1];
//                    }
//                    possible.add(array);
//                }
//            }
//        }
//        else{
//            for (int a = 0; a<9; a++){
//                for (int b = 0; b<9; b++){
//                    if (Board.game.path.Largefilled[a] == 0 && Board.game.grid[a][b] == 0){
//                        int[] array = {a, b};
//                        if (MainAI.canWin(grid, array[0], array[1], player)){
//                            Board.game.LargeBox = array[0];
//                            return array[1];
//                        }
//                        possible.add(array);
//                    }
//                }
//            }
//        }
//        if (possible.size() > 0){
//            threads.clear();
//            values = new int[possible.size()][numNetworks];
//            for (int a = 0; a<possible.size(); a++){
//                Set set = new Set(Board.game.path);
//                set.add(possible.get(a)[0], possible.get(a)[1]);
//                neuralPatternRecognition(set, a);
//            }
//            for (int a = 0; a<threads.size(); a++){
//                try{
//                    threads.get(a).join();
//                }catch (InterruptedException e){}
//            }
//        }
//        return MainAI.random();
//    }
//    
//    public void neuralPatternRecognition (Set set, int parent){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int[][]grid = set.toArray();
//                int square = set.getLastLarge();
//                int filledResult = set.Largefilled[square];
//                // Checks to see if it can take the square
//                if (filledResult == player){// Can take Square
//                    values[parent][0] = 1;
//                }
//                else{// Cannot Take Square
//                    values[parent][0] = 0;
//                }
//                // checks to see if it is worth attempting to take this square
//                Thread worthAttemptTake = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int[] largeSquares = new int[9];
//                        boolean allLargeEmpty = true;
//                        for (int b = 0; b<largeSquares.length; b++){
//                            largeSquares[b] = set.Largefilled[b];
//                            if (largeSquares[b] != 0){
//                                allLargeEmpty = false;
//                            }
//                        }
//                        largeSquares[set.getLastLarge()] = player;
//                        int numWin = partialMatchGrids(largeSquares, win2);
//                        int numLose = partialMatchGrids(largeSquares, win1);
//                        if (numWin > numLose || allLargeEmpty){// Attempting to take this square is good idea
//                            values[parent][1] = 1;
//                        }
//                        else{// Attempting to take this square is not best idea
//                            values[parent][1] = 0;
//                        }
//                    }
//                });
//                worthAttemptTake.start();
//                threads.add(worthAttemptTake);
//                // Checks to see if simply pattern wise (with no future planning) it is a good move
//                Thread goodPatternMove = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int numWin = partialMatchGrids(grid[square], win2);
//                        int numLose = partialMatchGrids(grid[square], win1);
//                        if (numWin > numLose){// Good Conformity to Pattern
//                            values[parent][1] = 1;
//                        }
//                        else{// Not the best Conformity to Pattern
//                            values[parent][1] = 0;
//                        }
//                    }
//                });
//                goodPatternMove.start();
//                threads.add(goodPatternMove);
//                // checks to see if it is wise to let the opponent make their next move in the proportional square
//                
//                // checks to see if it can block the opponent from taking the square
//            }
//        });
//        thread.start();
//        threads.add(thread);
//    }
//    
//    public int partialMatchGrids(int[]grid, List<int[]> match){
//        int count = 0;
//        for (int a = 0; a<match.size(); a++){
//            for (int b = 0; b<grid.length; b++){
//                if (grid[b] == 0 || grid[b] != match.get(a)[b]){
//                    break;
//                }
//                else if (b == grid.length-1){
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//    
//    public void setValues(int move){
//        int[]values = {0,2,1};
//        Board.game.path.add(Board.game.LargeBox, move);
//        Board.game.grid[Board.game.LargeBox][move] = player;
//        Board.game.lastmove = player;
//        Board.game.check(Board.game.LargeBox, 2);
//        if (Board.game.LargeFilled[move] != 0){
//            Board.game.LargeBox = -1;
//        }
//        else{
//            Board.game.LargeBox = move;
//        }
//        if (Board.game.playerwon == 1 || Board.game.playerwon == 2){
//            Board.game.text = (Board.game.players[Board.game.playerwon-1]+" Wins!!!");
//        }
//        else if (Board.game.playerwon == 3){
//            Board.game.text = ("Cats Game!!!");
//        }
//        else{
//            Board.game.player = values[player];
//            Board.game.text = (Board.game.players[Board.game.player-1]+"'s turn");
//        }
//    }
//    
//    public void reset(){
//        possible.clear();
//        player = 0;
//    }
//    
//}
