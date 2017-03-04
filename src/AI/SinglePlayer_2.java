package AI;


import Main.Board;
import Main.p;
import java.util.ArrayList;
import java.util.List;


public class SinglePlayer_2{
    
    //static int[] value = new int[possible.size()];
    static int player = 0;
    //static Thread planAhead, planAhead2;
    
    
    /*
    - Monte Carlo Tree Search Algorithm
    - A* Algorithm
    - Dijkstra's Algorithm
    - Breadth First Search Algorithm
    - Depth First Search Algorithm
    - Heuristic Search Algorithm
    - Bidirectional Search Algorithm
    */
    
    boolean showProcess = true;
    public void move(int player){
        long start = System.currentTimeMillis();
        this.player = player;
        if (Board.game.path.size() >= 3){
            //planAhead.interrupt();
            //planAhead2.interrupt();
            System.out.println("Move Number:  "+Board.game.path.size());
            int[] array = getMove(player);
            Board.game.LargeBox = array[0];
            setValues(array[1]);
        }
        else if (Board.game.path.size() < 3){
            setValues (MainAI.random());
        }
        long time = System.currentTimeMillis()-start;
        System.out.println("Took "+time+" Milliseconds to process\n");
        if(time < 750){
            p.delay(750-time);
        }
    }
    
    public int[] getMove(int player){
        List<Thread> threads = new ArrayList<>();
        List<int[]> possible = new ArrayList<>();
        List<List<randomTree>> games = new ArrayList<>();
        //Find all the possible moves
        if (Board.game.path.nextLarge != -1 && Board.game.path.Largefilled[Board.game.path.nextLarge] == 0){
            for (int a = 0; a<9; a++){
                if (Board.game.path.grid[Board.game.path.nextLarge][a] == 0){
                    int[] array = {Board.game.path.nextLarge, a};
                    if (MainAI.canWin(Board.game.path.grid, array[0], array[1], player)){
                        return array;
                    }
                    possible.add(array);
                }
            }
        }
        else{
            for (int a = 0; a<9; a++){
                for (int b = 0; b<9; b++){
                    if (Board.game.path.Largefilled[a] == 0 && Board.game.grid[a][b] == 0){
                        int[] array = {a, b};
                        if (MainAI.canWin(Board.game.path.grid, array[0], array[1], player)){
                            return array;
                        }
                        possible.add(array);
                    }
                }
            }
        }
        if (possible.size() == 1){
            int[] array = {possible.get(0)[0], possible.get(0)[1]};
            return array;
        }
        List<int[]> viable = new ArrayList<>();
        List<List<int[]>> nextPossible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
            List<int[]> nextpossible = new ArrayList<>();
            if (set.nextLarge != -1 && set.Largefilled[set.nextLarge] == 0){
                for (int b = 0; b<9; b++){
                    if (set.grid[set.nextLarge][b] == 0){
                        int[] array = {set.nextLarge, b};
                        if (!MainAI.canWin(set.grid, array[0], array[1], other[player])
                                || (a == possible.size()-1 && viable.isEmpty())){
                            nextpossible.add(array);
                        }
                        else{
                            nextpossible.clear();
                            break;
                        }
                    }
                }
            }
            else{
                for (int b = 0; b<9; b++){
                    for (int c = 0; c<9; c++){
                        if (set.Largefilled[b] == 0 && set.grid[b][c] == 0){
                            int[] array = {b, c};
                            if (!MainAI.canWin(set.grid, array[0], array[1], other[player])
                                    || (a == possible.size()-1 && viable.isEmpty())){
                                nextpossible.add(array);
                            }
                            else{
                                nextpossible.clear();
                                b = 9;
                                break;
                            }
                        }
                    }
                }
            }
            if (!nextpossible.isEmpty()){
                viable.add(possible.get(a));
                nextPossible.add(nextpossible);
            }
        }
        if (viable.size() == 1){
            int[] array = {viable.get(0)[0], viable.get(0)[1]};
            return array;
        }
        if (viable.isEmpty()){
            if (possible.isEmpty()){
                System.out.println("Error here");
            }
            else{
                System.out.println("No Viable Moves");
                double[] values = new double[possible.size()];
                double[] deepSearch = deepSearch(possible);
                double[] incValues = primaryAnalysis(Board.game.path, possible, player);
                for (int a = 0; a<values.length; a++){
                    values[a] = deepSearch[a]*incValues[a];
                }
                for (int a = 1; a<possible.size(); a++){
                    if (values[a] > values[a-1]){
                        int[] firstPossible = possible.get(a);
                        possible.set(a, possible.get(a-1));
                        possible.set(a-1, firstPossible);
                        double firstValue = values[a];
                        values[a] = values[a-1];
                        values[a-1] = firstValue;
                        a = Math.max(a-2, 0);
                    }
                }
                int[] array = {possible.get(0)[0], possible.get(0)[1]};
                return array;
            }
        }
        if (Board.game.path.numFill[player-1] >= 2){
            //If there is a way to force my opponent into giving me the large square, do it
            List<Integer> largeSquareWin = new ArrayList<>();
            for (int a = 0; a<MainAI.planAhead.length; a++){
                for (int b = 0; b<MainAI.planAhead[a].length; b++){
                    //If I have two squares in a row:
                    if (Board.game.path.Largefilled[MainAI.planAhead[a][b][0]] == player &&
                            Board.game.path.Largefilled[MainAI.planAhead[a][b][1]] == player){
                        largeSquareWin.add(a);
                    }
                }
            }
            if (!largeSquareWin.isEmpty()){
                for (int a = 0; a<viable.size(); a++){
                    boolean force = true;
                    for (int b = 0; b<nextPossible.get(a).size(); b++){
                        Set set = new Set(Board.game.path, viable.get(a)[0], viable.get(a)[1]);
                        set.add(nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                        if (!largeSquareWin.contains(set.nextLarge) && set.nextLarge != -1){
                            force = false;
                            break;
                        }
                    }
                    if (force){
                        return viable.get(a);
                    }
                }
                if (showProcess){ System.out.print("Force Analysis Completed:     "); }
            }
        }
        double[] primaryAnalysis = primaryAnalysis(Board.game.path, viable, nextPossible, player);
        if (showProcess) {System.out.println("Completed Primary Analysis");}
        double[] monteCarlo = new double[viable.size()];
        for (int a = 0; a<monteCarlo.length; a++){
            monteCarlo[a] = 0;
        }
        for (int a = 0; a<viable.size(); a++){
            games.add(new ArrayList<>());
        }
        for (int a = 0; a<viable.size() && a<viable.size(); a++){
            Set set = new Set(Board.game.path, viable.get(a)[0], viable.get(a)[1]);
            final int A = a;
            for (int b = 0; b<nextPossible.get(a).size(); b++){
                Set newset = new Set(set, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                Thread thread = new Thread(()->{
                    int numEmpty = Board.game.path.numEmpty();
                    int numThread;
                    if (numEmpty > 70){
                        numThread = 35000/(viable.size()*nextPossible.get(A).size());
                    }
                    else if (numEmpty > 45){
                        numThread = 50000/(viable.size()*nextPossible.get(A).size());
                    }
                    else if (numEmpty > 25){
                        numThread = 65000/(viable.size()*nextPossible.get(A).size());
                    }
                    else if (numEmpty > 15){
                        numThread = 80000/(viable.size()*nextPossible.get(A).size());
                    }
                    else{
                        numThread = 95000/(viable.size()*nextPossible.get(A).size());
                    }
                    for (int c = 0; c<numThread; c++){
                        randomTree game = new randomTree(newset);
                        game.run();
                        games.get(A).add(game);
                    }
                });
                thread.start();
                threads.add(thread);
            }
        }
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
            }catch(InterruptedException e){}
        }
        double[] incValue = analyse(games, viable, player);
        for (int a = 0; a<incValue.length; a++){
            monteCarlo[a] += incValue[a];
        }
        if (showProcess) {
            System.out.println("Completed Monte Carlo Algorithm");
            int numEmpty = Board.game.path.numEmpty();
            if (numEmpty > 65){
                System.out.println("20000 Random Games Traversed Through");
            }
            else if (numEmpty > 45){
                System.out.println("30000 Random Games Traversed Through");
            }
            else if (numEmpty > 25){
                System.out.println("40000 Random Games Traversed Through");
            }
            else if (numEmpty > 15){
                System.out.println("50000 Random Games Traversed Through");
            }
            else{
                System.out.println("60000 Random Games Traversed Through");
            }
        }
        double[] values = new double[viable.size()];
        if (showProcess) {System.out.println("Beginning Deep Search");}
        double[] deepSearch = deepSearch(viable);
        if (showProcess) {
            int numEmpty = Board.game.path.numEmpty(), depth = 0;
            if (numEmpty > 45){  depth = 5; }
            else if (numEmpty > 35){    depth = 6;  }
            else if (numEmpty > 25){    depth = 7;  }
            else if (numEmpty > 15){    depth = 8;  }
            else{  depth = 9; }
            System.out.println("Completed Deep Search. Searched a depth of "+depth);
        }
        for (int a = 0; a<values.length; a++){
            values[a] = deepSearch[a];
        }
        for (int a = 0; a<values.length; a++){
            values[a] = p.round(primaryAnalysis[a]*monteCarlo[a]*values[a], 5);
        }
        for (int a = 1; a<values.length; a++){
            if (values[a] > values[a-1]){
                int[] firstViable = viable.get(a);
                viable.set(a, viable.get(a-1));
                viable.set(a-1, firstViable);
                double firstValue = values[a];
                values[a] = values[a-1];
                values[a-1] = firstValue;
                double firstPrimary = primaryAnalysis[a];
                primaryAnalysis[a] = primaryAnalysis[a-1];
                primaryAnalysis[a-1] = firstPrimary;
                double firstSearch = deepSearch[a];
                deepSearch[a] = deepSearch[a-1];
                deepSearch[a-1] = firstSearch;
                a = Math.max(a-2, 0);
            }
        }
        if (showProcess) {
            System.out.print("Primary Analysis:  ");
            p.print(primaryAnalysis);
            System.out.print("Deep Search:  ");
            p.print(deepSearch(possible));
            System.out.print("Final Values:  ");
            p.print(values);
            System.out.print(viable.get(0)[1]);
            for (int a = 1; a<viable.size(); a++){
                System.out.print(", "+viable.get(a)[1]);
            }
            System.out.println("");
        }
        if (!viable.isEmpty()){
            int[] array = {viable.get(0)[0], viable.get(0)[1]};
            return array;
        }
        return MainAI.randomArray();
    }
    
    
    private double[] primaryAnalysis(Set board, List<int[]> viable, int player){
        List<List<int[]>> nextPossible = new ArrayList<>();
        for (int a = 0; a<viable.size(); a++){
            Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
            nextPossible.add(MainAI.getPossible(set));
        }
        return primaryAnalysis(board, viable, nextPossible, player);
    }
    private double[] primaryAnalysis(Set board, List<int[]> viable, List<List<int[]>> nextPossible, int player){
        double[] values = new double[viable.size()];
        List<Thread> threads = new ArrayList<>();
        //Analyse Small Square
        if (board.size() < 15){
            // Is the box that I am to play in, empty?
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    if (board.nextLarge != -1){
                        boolean empty = true;
                        for (int b = 0; b<9; b++){
                            if (board.grid[board.nextLarge][b] != 0){
                                empty = false;
                            }
                        }
                        if (empty){
                            values[board.nextLarge]+=2;
                        }
                    }
                }
            }));
            // If I choose this square as my final decision, is the box in which the opponent must play, empty?
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int a = 0; a<viable.size(); a++){
                        Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                        if (set.nextLarge != -1){
                            boolean empty = true;
                            for (int b = 0; b<9; b++){
                                if (set.grid[viable.get(a)[1]][b] != 0){
                                    empty = false;
                                }
                            }
                            if (empty){
                                values[a]++;
                            }
                        }
                    }
                }
            }));
        }
        // If this were a regular game of tic tac toe, is making this move good in terms of regular tic Tac Toe Heuristics?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                    if (set.numFill[player-1] > board.numFill[player-1]){
                        if (set.numFill[other[player]] > board.numFill[other[player]]){
                            values[a]+=1;
                        }
                        values[a]+=6;
                        continue;
                    }
                    if (set.numFill[other[player]] > board.numFill[other[player]]){
                        values[a]+=5;
                        continue;
                    }
                    else if(viable.get(a)[1] != 4 && viable.get(a)[1]%2 == 0){
                        values[a]++;
                    }
                    if (viable.get(a)[0] == viable.get(a)[1]){
                        values[a]++;
                    }
                }
            }
        }));
        // Can I take this square?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                    if (set.numFill[player-1] > board.numFill[player-1]){
                        values[a]+=2;
                        // Is the large square that I am taking, the center square
                        if (viable.get(a)[0] == 4){
                            values[a]++;
                        }
                        // Is it worth taking this square?
                        if (set.numFilled > 0){
                            int numSetups = 0;
                            for (int b = 0; b<MainAI.planAhead[viable.get(a)[0]].length; b++){
                                // Is there a square taken by the other player in the way of a setup?
                                if (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][0]] != other[player] &&
                                        set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][1]] != other[player]){
                                    // Will taking this square lead to a potential win? In other words, can I create a setup that can possibly lead to a win
                                    for (int c = 0; c<MainAI.planAhead[viable.get(a)[0]][b].length; c++){
                                        if (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][c]] == player){
                                            values[a]++;
                                            numSetups++;
                                        }
                                    }
                                }
                                else if (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][0]] == other[player] ||
                                        set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][1]] == other[player]){
                                    // Will taking this square prevent a potential loss?
                                    if (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][0]] == other[player] &&
                                            set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][1]] == other[player]){
                                        values[a]+=2;
                                    }
                                    // There is a square taken by the other player that negates the potential that taking this square could have
                                    else if ((set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][0]] == player &&
                                            set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][1]] == other[player]) ||
                                            (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][0]] == other[player] &&
                                            set.Largefilled[MainAI.planAhead[viable.get(a)[0]][b][1]] == player)){
                                        values[a]--;
                                    }
                                }
                            }
                            // Can I create a fork in the large grid?
                            if (numSetups > 1){
                                values[a]+=numSetups;
                            }
                        }
                        //This is the first square being taken
                        else{
                            values[a]++;
                        }
                    }
                }
            }
        }));
        // Can I block my opponent from taking this square in the future?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                    for (int b = 0; b<MainAI.planAhead[viable.get(a)[1]].length; b++){
                        if (set.grid[viable.get(a)[0]][MainAI.planAhead[viable.get(a)[1]][b][0]] == other[player] &&
                                set.grid[viable.get(a)[0]][MainAI.planAhead[viable.get(a)[1]][b][1]] == other[player]){
                            values[a]++;
                            // Is it worth stopping them from potentially taking this square in the future?
                            if (set.numFill[other[player]-1] > 0){
                                for (int c = 0; c<MainAI.planAhead[viable.get(a)[0]].length; c++){
                                    // If they take this square in the future, can they create a potential possibility of winning
                                    if (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][c][0]] != player &&
                                            set.Largefilled[MainAI.planAhead[viable.get(a)[0]][c][1]] != player &&
                                            (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][c][0]] == other[player] ||
                                            set.Largefilled[MainAI.planAhead[viable.get(a)[0]][c][1]] == other[player])){
                                        values[a]++;
                                        // If they take this square in the future, can they win?
                                        if (set.Largefilled[MainAI.planAhead[viable.get(a)[0]][c][0]] == other[player] &&
                                                set.Largefilled[MainAI.planAhead[viable.get(a)[0]][c][1]] == other[player]){
                                            values[a]+=2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }));
        // Can I create or block a fork in the grid I must play in?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    int numSetup1 = 0, numSetup2 = 0;
                    Set set = new Set(Board.game.path, viable.get(a)[0], viable.get(a)[1]);
                    if (set.numFill[player-1] == Board.game.path.numFill[player-1]){
                        for (int b = 0; b<MainAI.planAhead[viable.get(a)[1]].length; b++){
                            if (set.grid[viable.get(a)[0]][MainAI.planAhead[viable.get(a)[1]][b][0]] != other[player] &&
                                    set.grid[viable.get(a)[0]][MainAI.planAhead[viable.get(a)[1]][b][1]]  != other[player]){
                                if (set.grid[viable.get(a)[0]][MainAI.planAhead[viable.get(a)[1]][b][0]] == player ||
                                        set.grid[viable.get(a)[0]][MainAI.planAhead[viable.get(a)[1]][b][1]] == player){
                                    numSetup2++;
                                }
                            }
                            else{
                                numSetup1++;
                            }
                        }
                        //I can create a fork in the grid that I must play in if I make this move
                        if (numSetup2 > 1){
                            values[a] += numSetup2;
                        }
                        //I can block a fork in the grid that I must play in if I make this move
                        if (numSetup1 > 1){
                            values[a]++;
                        }
                    }
                }
            }
        }));
        // If I play in this square, am I setting myself up to be able to take this square in the future?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                    for (int b = 0; b<MainAI.setup[viable.get(a)[1]].length; b++){
                        if (set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][0]] != other[player] ||
                                set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][1]] != other[player]){
                            if(set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][0]] == player ||
                                    set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][1]] == player){
                                values[a]+=2;
                            }
                            else{
                                values[a]++;
                            }
                        }
                    }
                }
            }
        }));
        // If I play in this square, can the opponent take the large square that I am sending them to?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    for (int b = 0; b<nextPossible.get(a).size(); b++){
                        Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                        set.add(nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                        if (set.numFill[other[player]] > board.numFill[other[player]]){
                            values[a]-=2;
                            //Does it matter if they take it?
                            for (int c = 0; c<MainAI.planAhead[nextPossible.get(a).get(b)[0]].length; c++){
                                if (set.Largefilled[MainAI.planAhead[nextPossible.get(a).get(b)[0]][c][0]] == other[player] ||
                                        set.Largefilled[MainAI.planAhead[nextPossible.get(a).get(b)[0]][c][1]] == other[player]){
                                    values[a]-=2;
                                }
                            }
                        }
                    }
                }
            }
        }));
        // If I play in this square, am I giving my opponent the freedom to go anywhere?
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                for (int a = 0; a<viable.size(); a++){
                    Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                    if (set.nextLarge == -1){
                        values[a]--;
                        for (int b = 0; b<nextPossible.get(a).size(); b++){
                            set.add(nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                            if (set.numFill[other[player]] > board.numFill[other[player]]){
                                //Does it matter if they take it?
                                for (int c = 0; c<MainAI.planAhead[nextPossible.get(a).get(b)[0]].length; c++){
                                    if (set.Largefilled[MainAI.planAhead[nextPossible.get(a).get(b)[0]][c][0]] == other[player] ||
                                            set.Largefilled[MainAI.planAhead[nextPossible.get(a).get(b)[0]][c][1]] == other[player]){
                                        values[a]--;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }));
        
        //Analyse Large Squares
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                List<Integer> largeSquareWin = new ArrayList<>();
                List<Integer> largeSquareBlock = new ArrayList<>();
                for (int a = 0; a<MainAI.planAhead.length; a++){
                    for (int b = 0; b<MainAI.planAhead[a].length; b++){
                        //If I have two squares in a row:
                        if (board.Largefilled[MainAI.planAhead[a][b][0]] == player &&
                                board.Largefilled[MainAI.planAhead[a][b][1]] == player){
                            largeSquareWin.add(a);
                        }
                        //If the opponent has two squares in a row:
                        else if (board.Largefilled[MainAI.planAhead[a][b][0]] == other[player] &&
                                board.Largefilled[MainAI.planAhead[a][b][1]] == other[player]){
                            largeSquareBlock.add(a);
                        }
                    }
                }
                //There exists some large square k such that if I take k, I win
                if (!largeSquareWin.isEmpty()){
                    for (int a = 0; a<viable.size(); a++){
                        // Making this move gives the opponent a chance to block my win
                        if (largeSquareWin.contains(viable.get(a)[1])){
                            values[a] -= 2;
                        }
                    }
                    //If there is a way to force my opponent into giving me the large square, do it
                }
                //There exists some large square k such that if the opponent takes k, I lose
                else if (!largeSquareBlock.isEmpty()){
                    for (int a = 0; a<viable.size(); a++){
                        // Making this move gives the opponent a chance to create a win
                        if (largeSquareBlock.contains(viable.get(a)[1])){
                            values[a] -= 3;
                        }
                    }
                }
                //At this point in the game, there is no setup such that by taking some large square k, one of us can win
                else{
                    //I don't want to send my opponent to a place where he can take a large square so that
                    //     there exists some other large square k such that if the opponent takes k, I lose
                    for (int a = 0; a<viable.size(); a++){
                        for (int c = 0; c<MainAI.planAhead[viable.get(a)[1]].length; c++){
                            for (int b = 0; b<nextPossible.get(a).size(); b++){
                                Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                                set.add(nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                                // If I make this move, then in the next move, the opponent can take a square
                                if (set.numFill[other[player]-1] > set.numFill[other[player]-1]){
                                    // If I make this move, then in the next move, the opponent can take a square that has potential to lead to a loss
                                    if (set.Largefilled[MainAI.planAhead[viable.get(a)[1]][c][0]] == other[player] ||
                                            set.Largefilled[MainAI.planAhead[viable.get(a)[1]][c][1]] == other[player]){
                                        values[a]-=3;
                                    }
                                    else{
                                        values[a]--;
                                    }
                                }
                            }
                        }
                    }
                    // If I go here, in the next move, will the opponent have an opportunity to
                }
            }
        }));
        for (int a = 0; a<threads.size(); a++){
            threads.get(a).start();
        }
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
            }catch(InterruptedException e){}
        }
        double lowest = values[0];
        for (int a = 1; a<values.length; a++){
            if (values[a] < lowest){
                lowest = values[a];
            }
        }
        return values;
        //        List<Integer> primary = new ArrayList<>();
        //        for (int a = 0; a<values.length; a++){
        //            primary.add((int)(values[a]-lowest+1));
        //        }
        //        return primary;
    }
    
    public double[] deepSearch (List<int[]> possible){
        List<Thread> threads = new ArrayList<>();
        double[] values = new double[possible.size()];
        for (int a = 0; a<possible.size(); a++){
            values[a] = 0;
            Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
            List<int[]> nextPossible = MainAI.getPossible(set);
            final int A = a;
            for (int b = 0; b<nextPossible.size(); b++){
                Set newset = new Set(set, nextPossible.get(b)[0], nextPossible.get(b)[1]);
                Thread thread = new Thread(()->{
                    int numEmpty = Board.game.path.numEmpty();
                    if (numEmpty > 75){
                        bruteForceSearch(newset, 1, values, A);
                    }
                    else if (numEmpty > 65){
                        bruteForceSearch(newset, 2, values, A);
                    }
                    else if (numEmpty > 55){
                        bruteForceSearch(newset, 3, values, A);
                    }
                    else if (numEmpty > 45){
                        bruteForceSearch(newset, 4, values, A);
                    }
                    else if (numEmpty > 35){
                        bruteForceSearch(newset, 5, values, A);
                    }
                    else if (numEmpty > 25){
                        bruteForceSearch(newset, 6, values, A);
                    }
                    else if (numEmpty > 15){
                        bruteForceSearch(newset, 7, values, A);
                    }
                    else{
                        bruteForceSearch(newset, 8, values, A);
                    }
                });
                thread.start();
                threads.add(thread);
            }
        }
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
            }catch(InterruptedException e){}
        }
        double lowest = values[0];
        for (int a = 1; a<values.length; a++){
            if (values[a] < lowest){
                lowest = values[a];
            }
        }
        for (int a = 0; a<values.length; a++){
            values[a] -= lowest-1;
        }
        return values;
    }
    
    int[] bruteForceOutcomes = {0, 250, -250, 50};
    public void bruteForceSearch (Set set, final int depth, double[] values, final int parent){
        int player = set.size()%2+1;
        List<int[]> possible = MainAI.getPossible(set);
        int[] Largegrid = set.Largefilled;
        for (int a = 0; a<possible.size(); a++){
            if (Largegrid[possible.get(a)[1]] == 0){
                Set newset = new Set(set, possible.get(a)[0], possible.get(a)[1]);
                if (set.numFill[this.player] < newset.numFill[this.player]){
                    values[parent] += Math.floor(depth/3.0);
                }
                else if (set.numFill[other[this.player]] < newset.numFill[other[this.player]]){
                    values[parent] -= Math.floor(depth/3.0);
                }
                if (newset.nextLarge == -1 && player == 2){
                    values[parent] -= Math.floor(depth/3.0);
                }
                if (newset.winner != 0){
                    if (newset.winner == 1){
                        values[parent] -= depth;
                    }
                    else{
                        values[parent] += depth;
                    }
                }
                else if (depth > 0){
                    bruteForceSearch(newset, depth-1, values, parent);
                }
            }
        }
    }
    
    int[] other = {0, 2, 1};
    public double[] analyse(List<List<randomTree>> games, List<int[]> possible, int player){
        List<Integer> values = new ArrayList<>();
        long start = System.currentTimeMillis();
        int[][] grid = Board.game.path.grid;
        int[] Largefilled = Board.game.path.Largefilled;
        int promisingPoints = 0;
        int unPromisingPoints = 0;
        int neutralPoints = 0;
        //int numPromising = 0;
        //int numUnpromising = 0;
        //int numNeutral = 0;
        //        List<List<Set>> promising = new ArrayList<>();
        //        List<List<Set>> neutral = new ArrayList<>();
        //        List<List<Set>> unpromising = new ArrayList<>();
        //        int[] closestWin = new int[possible.size()];
        //        int[] closestTie = new int[possible.size()];
        //        int[] closestLoss = new int[possible.size()];
        //        int[] closestWinFrequency = new int[possible.size()];
        //        int[] closestTieFrequency  = new int[possible.size()];
        //        int[] closestLossFrequency  = new int[possible.size()];
        double[] averageSquaresTaken1 = new double[possible.size()];
        double[] averageSquaresTaken2 = new double[possible.size()];
        double[] averageSquaresTaken3 = new double[possible.size()];
        double[] averageLargeOptionsGiven1 = new double[possible.size()];
        double[] averageLargeOptionsGiven2 = new double[possible.size()];
        int boardSize = Board.game.path.size();
        for (int a = 0; a<games.size(); a++){
            //            promising.add(new ArrayList<>());
            //            neutral.add(new ArrayList<>());
            //            unpromising.add(new ArrayList<>());
            promisingPoints = 0;
            unPromisingPoints = 0;
            neutralPoints = 0;
            //numPromising = 0;
            //numUnpromising = 0;
            //numNeutral = 0;
            for (int b = 0; b<games.get(a).size(); b++){
                try{
                    if (games.get(a).get(b).winner == player){
                        promisingPoints += 81/(games.get(a).get(b).set.size()-boardSize);
                        //numPromising++;
                        //                        promising.get(a).add(games.get(a).get(b).set);
                        //                        if ((games.get(a).get(b).set.size()-Board.game.path.size()) < closestWin[a] || closestWin[a] == 0){
                        //                            closestWin[a] = games.get(a).get(b).set.size()-Board.game.path.size();
                        //                            closestWinFrequency[a] = 1;
                        //                        }
                        //                        else if ((games.get(a).get(b).set.size()-Board.game.path.size()) == closestWin[a]){
                        //                            closestWinFrequency[a]++;
                        //                        }
                    }
                    else if (games.get(a).get(b).winner == 3){
                        neutralPoints += 50/(games.get(a).get(b).set.size()-boardSize);
                        //numNeutral++;
                        //                        neutral.get(a).add(games.get(a).get(b).set);
                        //                        if ((games.get(a).get(b).set.size()-Board.game.path.size()) < closestTie[a] || closestTie[a] == 0){
                        //                            closestTie[a] = games.get(a).get(b).set.size()-Board.game.path.size();
                        //                            closestTieFrequency[a] = 1;
                        //                        }
                        //                        else if ((games.get(a).get(b).set.size()-Board.game.path.size()) == closestTie[a]){
                        //                            closestTieFrequency[a]++;
                        //                        }
                    }
                    else if (games.get(a).get(b).winner == other[player]){
                        unPromisingPoints += 81/(games.get(a).get(b).set.size()-boardSize);
                        //numUnpromising++;
                        //                        unpromising.get(a).add(games.get(a).get(b).set);
                        //                        if ((games.get(a).get(b).set.size()-Board.game.path.size()) < closestLoss[a] || closestLoss[a] == 0){
                        //                            closestLoss[a] = games.get(a).get(b).set.size()-Board.game.path.size();
                        //                            closestLossFrequency[a] = 1;
                        //                        }
                        //                        else if ((games.get(a).get(b).set.size()-Board.game.path.size()) == closestLoss[a]){
                        //                            closestLossFrequency[a]++;
                        //                        }
                    }
                    averageSquaresTaken1[a] += games.get(a).get(b).set.numFill[0];
                    averageSquaresTaken2[a] += games.get(a).get(b).set.numFill[1];
                    averageSquaresTaken3[a] += games.get(a).get(b).set.numFill[2];
                    averageLargeOptionsGiven1[a] += games.get(a).get(b).givingLargeOptions[1];
                    averageLargeOptionsGiven2[a] += games.get(a).get(b).givingLargeOptions[2];
                }catch (NullPointerException e){}
            }
            int points = 0;
            points = promisingPoints+neutralPoints-unPromisingPoints;
            //if (numPromising > 0){
            //    points -= promisingPoints/numPromising;
            //System.out.print(promisingPoints/numPromising+"       ");
            //}
            //if (numUnpromising > 0){
            //    points += unPromisingPoints/numUnpromising;
            //System.out.print(unPromisingPoints/numUnpromising+"       ");
            //}
            //if (numNeutral > 0){
            //    points -= 3*neutralPoints/numNeutral/4;
            //System.out.print(3*neutralPoints/numNeutral/4+"       ");
            //}
            //            points -= promisingPoints;
            //            points += unPromisingPoints;
            //            points -= neutralPoints;
            //System.out.println("");
            values.add(points);
            averageSquaresTaken1[a] = averageSquaresTaken1[a]/games.get(a).size();
            averageSquaresTaken2[a] = averageSquaresTaken2[a]/games.get(a).size();
            averageSquaresTaken3[a] = averageSquaresTaken3[a]/games.get(a).size();
            averageLargeOptionsGiven1[a] = averageLargeOptionsGiven1[a]/games.get(a).size();
            averageLargeOptionsGiven2[a] = averageLargeOptionsGiven2[a]/games.get(a).size();
        }
        int average = 0, lowest = 0;
        for (int a = 0; a<values.size(); a++){
            double payoff = 0;
            double cost = 0 ;
            //            if (!promising.get(a).isEmpty() && !unpromising.get(a).isEmpty()
            //                    && closestWin[a] != 0 && closestLoss[a] != 0
            //                    && closestWinFrequency[a] != 0 && closestLossFrequency[a] != 0){
            payoff =
                    //                        promising.get(a).size()*1.0*Math.min(closestWinFrequency[a], 1)/closestWin[a]
                    //                        *averageSquaresTaken2[a]/averageLargeOptionsGiven1[a]
                    //                        +neutral.get(a).size()*1.0*closestTieFrequency[a]/closestTie[a]/2*averageSquaresTaken3[a]
                    //                        -unpromising.get(a).size()*1.0*closestLossFrequency[a]/closestLoss[a]*
                    averageSquaresTaken1[a]/averageLargeOptionsGiven2[a]
                    //                                                                                                                                                                                       //(81*(promising.get(a).size()+neutral.get(a).size()/2)/unpromising.get(a).size())
                    //                                                                                                                                                                                       //+(81/(closestLoss[a]*closestLossFrequency[a]/(1.0*closestWin[a]*closestWinFrequency[a])))
                    //                                                                                                                                                                                       //+(closestTieFrequency[a]*closestTie[a]/1.5)
                    ;
            cost =
                    //                        unpromising.get(a).size()*1.0*Math.min(closestLossFrequency[a], 1)/closestLoss[a]
                    //                        *averageSquaresTaken1[a]/averageLargeOptionsGiven2[a]
                    //                        +neutral.get(a).size()*1.0*closestTieFrequency[a]/closestTie[a]/2*averageSquaresTaken3[a]
                    //                        -promising.get(a).size()*1.0*closestWinFrequency[a]/closestWin[a]*averageSquaresTaken2[a]*
                    averageSquaresTaken2[a]/averageLargeOptionsGiven1[a]
                    //                                                                                                                                                                                       //(81*(unpromising.get(a).size()+neutral.get(a).size()/2)/promising.get(a).size())
                    //                                                                                                                                                                                       //+(81/(closestWin[a]*closestWinFrequency[a]/(1.0*closestLoss[a]*closestLossFrequency[a])))
                    //                                                                                                                                                                                       //+(closestTieFrequency[a]*closestTie[a]/1.5)
                    ;
            values.set(a, (int)(values.get(a)+(cost-payoff)));
            //            }
            //            else if (promising.get(a).isEmpty() || closestWin[a] == 0 || closestWinFrequency[a] == 0){
            //                values.set(a, values.get(a)+Math.min(-1*closestLossFrequency[a]*unpromising.get(a).size(), -5000));
            //            }
            //            else if (unpromising.get(a).isEmpty() || closestLoss[a] == 0 || closestLossFrequency[a] == 0){
            //                values.set(a, values.get(a)+Math.max(closestWin[a]*promising.get(a).size(), 3000));
            //            }
            if (values.get(a) < lowest){
                lowest = values.get(a);
            }
        }
        for (int a = 0; a<values.size(); a++){
            values.set(a, values.get(a)-lowest+1);
            //            average += values.get(a);
        }
        //        average /= values.size();
        //        for (int a = 0; a<values.size(); a++){
        //            if (Largefilled[possible.get(a)[1]] != 0){
        //                values.set(a, values.get(a)-average);
        //            }
        //            if (possible.get(a)[1] == 4){
        //                values.set(a, values.get(a)-average/3);
        //            }
        //            grid[possible.get(a)[0]][possible.get(a)[1]] = player;
        //            if (!MainAI.filled(grid[possible.get(a)[0]], player)){
        //                grid[possible.get(a)[0]][possible.get(a)[1]] = 0;
        //                if (!MainAI.canTake(grid, possible.get(a)[1], other[player])){
        //                    if (possible.get(a)[0] == possible.get(a)[1]){
        //                        values.set(a, values.get(a)+average/5);
        //                    }
        //                }
        //                else{
        //                    values.set(a, values.get(a)-average);
        //                }
        //            }
        //            else{
        //                values.set(a, values.get(a)+average);
        //            }
        //            grid[possible.get(a)[0]][possible.get(a)[1]] = 0;
        //        }
        
        //if (showProcess) {
        //    p.print(values);
        //    System.out.println("Took "+(System.currentTimeMillis()-start)+" Milliseconds to complete Priliminary Analysis");}
        double[] analysis = new double[values.size()];
        for (int a = 0; a<analysis.length; a++){
            analysis[a] = values.get(a)/10000.0;
        }
        return analysis;
    }
    
    public void setValues(int move){
        int[]values = {0,2,1};
        Board.game.path.add(Board.game.LargeBox, move);
        Board.game.grid[Board.game.LargeBox][move] = player;
        Board.game.lastmove = player;
        Board.game.check(Board.game.LargeBox, 2);
        if (Board.game.LargeFilled[move] != 0){
            Board.game.LargeBox = -1;
        }
        else{
            Board.game.LargeBox = move;
        }
        if (Board.game.playerwon == 1 || Board.game.playerwon == 2){
            Board.game.text = (Board.game.players[Board.game.playerwon-1]+" Wins!!!");
        }
        else if (Board.game.playerwon == 3){
            Board.game.text = ("Cats Game!!!");
        }
        else{
            Board.game.player = values[player];
            Board.game.text = (Board.game.players[Board.game.player-1]+"'s turn");
        }
    }
    
    public void reset(){
        player = 0;
    }
    
    
    
    public void old(){
        /*
        for (int a = 1; a<viable.size(); a++){
        if (values.get(a) > values.get(a-1)){
        int[] firstPossible = viable.get(a);
        int firstValue = values.get(a);
        viable.set(a, viable.get(a-1));
        values.set(a, values.get(a-1));
        viable.set(a-1, firstPossible);
        values.set(a-1, firstValue);
        a = 0;
        }
        }
        long start = System.currentTimeMillis();
        if (showProcess) {System.out.print("Most Promising Moves Found: {");}
        List<int[]> promising = new ArrayList<>();
        for (int a = 0; a<viable.size() && a<3; a++){
        promising.add(viable.get(a));
        if (showProcess) {System.out.print(viable.get(a)[1]+",  ");}
        }
        if (showProcess) {System.out.println("}");}
        values.clear();
        values.addAll(deepSearch(promising));
        for (int a = 1; a<promising.size(); a++){
        if (values.get(a) > values.get(a-1)){
        int[] firstPromising = promising.get(a);
        int firstValue = values.get(a);
        promising.set(a, promising.get(a-1));
        values.set(a, values.get(a-1));
        promising.set(a-1, firstPromising);
        values.set(a-1, firstValue);
        a = 0;
        }
        }
        for (int a = 1; a<promising.size(); a++){
        if (values.get(a) > values.get(0)){
        values.remove(a);
        promising.remove(a);
        a = 0;
        }
        }
        if (showProcess) {System.out.println("Took "+(System.currentTimeMillis()-start)+" Milliseconds to complete Secondary Deep Analysis");}
        if (promising.size() == 1){
        Board.game.LargeBox = promising.get(0)[0];
        return promising.get(0)[1];
        }
        if (promising.size() > 1){
        for (int a = 0; a<viable.size(); a++){
        int index = promising.indexOf(viable.get(a));
        if (index != -1){
        Board.game.LargeBox = promising.get(index)[0];
        return promising.get(index)[1];
        }
        }
        }public List<Integer> deepSearch (List<int[]> possible){
        List<Thread> threads = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
        values.add(0);
        Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
        List<int[]> nextPossible = MainAI.getPossible(set);
        final int A = a;
        for (int b = 0; b<nextPossible.size(); b++){
        Set newset = new Set(set, nextPossible.get(b)[0], nextPossible.get(b)[1]);
        if (newset.winner == 1 && possible.size() > 1){
        possible.remove(a);
        a--;
        break;
        }
        else{
        Thread thread = new Thread(()->{
        bruteForceSearch(set, 3, values, A);
        });
        thread.start();
        threads.add(thread);
        }
        }
        }
        for (int a = 0; a<threads.size(); a++){
        try{
        threads.get(a).join();
        }catch(InterruptedException e){}
        }
        return values;
        }
        
        int[] bruteForceOutcomes = {0, 250, -250, 50};
        public void bruteForceSearch (Set set, final int depth, List<Integer> values, final int parent){
        List<int[]> possible = MainAI.getPossible(set);
        int[] Largegrid = set.Largefilled;
        for (int a = 0; a<possible.size(); a++){
        if (Largegrid[possible.get(a)[1]] == 0){
        Set newset = new Set(set, possible.get(a)[0], possible.get(a)[1]);
        if (newset.winner != 0 || depth == 0){
        values.set(parent, values.get(parent)+bruteForceOutcomes[newset.winner]);
        newset.updateHeuristic();
        values.set(parent, values.get(parent)+newset.heuristicValue);
        }
        else{
        bruteForceSearch(newset, depth-1, values, parent);
        }
        }
        }
        }
        
        */
    }
}
