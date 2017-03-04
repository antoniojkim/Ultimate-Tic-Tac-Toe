package AI;


import Main.Board;
import Main.p;
import java.util.ArrayList;
import java.util.List;


public class SinglePlayer{
    
    /*
    public static void main (String[] args){
    
    }
    /*
    */
    
    //static int[] value = new int[possible.size()];
    static int player = 0;
    static int[] other = {0, 2, 1};
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
        List<int[]> possible = new ArrayList<>();
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
                if (Board.game.path.Largefilled[a] == 0){
                    for (int b = 0; b<9; b++){
                        if ( Board.game.grid[a][b] == 0){
                            int[] array = {a, b};
                            if (MainAI.canWin(Board.game.path.grid, array[0], array[1], player)){
                                return array;
                            }
                            possible.add(array);
                        }
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
        for (int times = 0; times<2; times++){
            for (int a = 0; a<possible.size(); a++){
                Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
                List<int[]> nextpossible = new ArrayList<>();
                if (set.nextLarge != -1 && set.Largefilled[set.nextLarge] == 0){
                    for (int b = 0; b<9; b++){
                        if (set.grid[set.nextLarge][b] == 0){
                            int[] array = {set.nextLarge, b};
                            if (times == 1 || !MainAI.canWin(set.grid, array[0], array[1], other[player])){
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
                        if (set.Largefilled[b] == 0){
                            for (int c = 0; c<9; c++){
                                if (set.grid[b][c] == 0){
                                    int[] array = {b, c};
                                    if (times == 1 || !MainAI.canWin(set.grid, array[0], array[1], other[player])){
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
                }
                if (!nextpossible.isEmpty()){
                    viable.add(possible.get(a));
                    nextPossible.add(nextpossible);
                }
            }
            if (!viable.isEmpty()){
                break;
            }
        }
        if (viable.size() == 1){
            int[] array = {viable.get(0)[0], viable.get(0)[1]};
            return array;
        }
        else if (viable.isEmpty()){
            System.out.println("Error Here");
            return MainAI.randomArray();
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
                    Set set = new Set(Board.game.path, viable.get(a)[0], viable.get(a)[1]);
                    for (int b = 0; b<nextPossible.get(a).size(); b++){
                        Set newset = new Set(set, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                        if (!largeSquareWin.contains(newset.nextLarge) && newset.nextLarge != -1){
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
        for (int a = 1; a<primaryAnalysis.length; a++){
            if (primaryAnalysis[a] < primaryAnalysis[a-1] ||
                    (primaryAnalysis[a] == primaryAnalysis[a-1] && Math.random() > 0.5)){
                double primary = primaryAnalysis[a-1];
                primaryAnalysis[a-1] = primaryAnalysis[a];
                primaryAnalysis[a] = primary;
                int[] viable_Place = viable.get(a-1);
                viable.set(a-1, viable.get(a));
                viable.set(a, viable_Place);
                List<int[]> next_Place = nextPossible.get(a-1);
                nextPossible.set(a-1, nextPossible.get(a));
                nextPossible.set(a, next_Place);
                a = 0;
            }
        }
        if (showProcess){
            System.out.print("Primary Analysis:   {");
            for (int a = 0; a<primaryAnalysis.length; a++){
                if (a != 0){
                    System.out.print(" "+primaryAnalysis[a]);
                }
                else{
                    System.out.print(primaryAnalysis[a]);
                }
            }
            System.out.println("}");
            System.out.print("Viable:             {");
            for (int a = 0; a<viable.size(); a++){
                if (a != 0){
                    System.out.print("   "+viable.get(a)[1]);
                }
                else{
                    System.out.print(viable.get(a)[1]);
                }
            }
            System.out.println("}");
        }
        double[] depthSearch = new double[viable.size()];
        for (int a = 0; a<viable.size(); a++){ //Math.min(viable.size(), 15)
            depthSearch[a] = primaryAnalysis[a];
            Set depth1 = new Set(Board.game.path, viable.get(a)[0], viable.get(a)[1]);
            if (depth1.nextLarge == -1){
                depthSearch[a] -= Board.game.path.numEmpty()/2.0;
            }
            if (depth1.numFill[player-1] > Board.game.path.numFill[player-1]){
                depthSearch[a] += Board.game.path.numEmpty()/2.0;
            }
            List<List<int[]>> depth3Possible = new ArrayList<>();
            for (int b = 0; b<nextPossible.get(a).size(); b++){
                Set depth2 = new Set(depth1, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                if (depth2.winner != 0){
                    if (depth2.winner == 1){
                        depthSearch[a] -= Board.game.path.size();
                    }
                    if (depth2.winner == 3){
                        depthSearch[a] += Board.game.path.size()/2.0;
                    }
                    depth3Possible.add(new ArrayList<>());
                }
                else{
                    if (depth2.numFill[other[player]-1] > depth1.numFill[other[player]-1]){
                        depthSearch[a] -= Board.game.path.size()/3.0;
                    }
                    if (depth2.nextLarge == -1){
                        depthSearch[a] += Board.game.path.size()/5.0;
                    }
                    depth3Possible.add(depth2.getNextPossible());
                    depthSearch[a] += depth3(depth2, depth3Possible.get(b), viable.size()*nextPossible.size(), player);
                }
            }
            double[] depth1OpponentAnalysis = primaryAnalysis(depth1, nextPossible.get(a), depth3Possible, other[player]);
            for (int b = 0; b<depth1OpponentAnalysis.length; b++){
                depthSearch[a] -= depth1OpponentAnalysis[b]/2.0;
            }
        }
        if (showProcess){
            System.out.print("Final Depth Search Values:   {");
            for (int a = 0; a<depthSearch.length; a++){
                if (a != 0){
                    System.out.print(" "+p.round(depthSearch[a], 2));
                }
                else{
                    System.out.print(p.round(depthSearch[a], 2));
                }
            }
            System.out.println("}");
        }
        for (int a = 1; a<depthSearch.length; a++){
            if (depthSearch[a] > depthSearch[a-1] ||
                    (depthSearch[a] == depthSearch[a-1] && Math.random() > 0.5)){
                double depth_hold = depthSearch[a-1];
                depthSearch[a-1] = depthSearch[a];
                depthSearch[a] = depth_hold;
                int[] viable_Place = viable.get(a-1);
                viable.set(a-1, viable.get(a));
                viable.set(a, viable_Place);
                a = 0;
            }
        }
        
        return viable.get(0);
    }
    
    private double[] primaryAnalysis(Set board, List<int[]> viable, List<List<int[]>> nextPossible, int player){
        double[] values = new double[viable.size()];
        List<Thread> threads = new ArrayList<>();
        //Analyse Small Square
        if (board.size() < 15){
            // Is the box that I am to play in, empty?
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
            // If I choose this square as my final decision, is the box in which the opponent must play, empty?
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
        // If this were a regular game of tic tac toe, is making this move good in terms of regular tic Tac Toe Heuristics?
        for (int a = 0; a<viable.size(); a++){
            if (viable.get(a)[1] == 4){
                values[a]--;
            }
            else if(viable.get(a)[1]%2 == 0){
                values[a]++;
            }
            if (viable.get(a)[0] == viable.get(a)[1]){
                values[a]++;
            }
        }
        // Can I take this square?
        for (int a = 0; a<viable.size(); a++){
            Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
            if (set.numFill[player-1] > board.numFill[player-1]){
                values[a]++;
                // Is the large square that I am taking, the center square
                if (viable.get(a)[0] == 4){
                    values[a]++;
                }
                // Is it worth taking this square?
                if (set.numFilled > 1){
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
        // Can I block my opponent from taking this square in the future?
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
        // Can I create or block a fork in the grid I must play in?
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
        // If I play in this square, am I setting myself up to be able to take this square in the future?
        for (int a = 0; a<viable.size(); a++){
            Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
            for (int b = 0; b<MainAI.setup[viable.get(a)[1]].length; b++){
                if ((set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][0]] != other[player] &&
                        set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][1]] != other[player]) &&
                        (set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][0]] == player ||
                        set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][1]] == player)){
                    values[a]++;
                }
            }
        }
        // If I play in this square, can the opponent take the large square that I am sending them to?
        for (int a = 0; a<viable.size(); a++){
            for (int b = 0; b<nextPossible.get(a).size(); b++){
                Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
                set.add(nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                if (set.numFill[other[player]] > board.numFill[other[player]]){
                    values[a]-=2;
                }
            }
        }
        // If I play in this square, am I giving my opponent the freedom to go anywhere?
        for (int a = 0; a<viable.size(); a++){
            Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
            if (set.nextLarge == -1){
                values[a]--;
            }
        }
        
        //Analyse Large Squares
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
        //        for (int a = 0; a<threads.size(); a++){
        //            threads.get(a).start();
        //        }
        //        for (int a = 0; a<threads.size(); a++){
        //            try{
        //                threads.get(a).join();
        //            }catch(InterruptedException e){}
        //        }
        //        double lowest = values[0];
        //        for (int a = 1; a<values.length; a++){
        //            if (values[a] < lowest){
        //                lowest = values[a];
        //            }
        //        }
        //        for (int a = 0; a<values.length; a++){
        //            values[a] -= lowest-1;
        //        }
        return values;
    }
    
    private double depth3(Set depth2, List<int[]> possible, int numBranches, int player){
        double value = 0;
        List<depth4> nextDepth = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<List<int[]>> depth4Possible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set depth3 = new Set(depth2, possible.get(a)[0], possible.get(a)[1]);
            if (depth3.winner != 0){
                if (depth3.winner == 2){
                    value += Board.game.path.size();
                }
                if (depth3.winner == 3){
                    value += Board.game.path.size()/2.0;
                }
                depth4Possible.add(new ArrayList<>());
            }
            else{
                if (depth3.numFill[player-1] > depth2.numFill[player-1]){
                    value += Board.game.path.size()/4.0;
                }
                if (depth3.nextLarge == -1){
                    value -= Board.game.path.size()/5.0;
                }
                depth4Possible.add(depth3.getNextPossible());
                if (Board.game.path.numFilled > 0){
                    nextDepth.add(new depth4(depth3, depth4Possible.get(a), numBranches*possible.size(), player));
                }
            }
        }
        double[] depth3OpponentAnalysis = primaryAnalysis(depth2, possible, depth4Possible, player);
        for (int b = 0; b<depth3OpponentAnalysis.length; b++){
            value += depth3OpponentAnalysis[b]/3.0;
        }
        for (int a = 0; a<nextDepth.size(); a++){
            threads.add(new Thread(nextDepth.get(a)));
            threads.get(a).start();
        }
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
                value += nextDepth.get(a).getValue();
            }catch(InterruptedException e){};
        }
        return value;
    }
    private class depth4 implements Runnable{
        
        private double value = 0;
        private Set depth3;
        private List<int[]> possible;
        private int numBranches, player;
        
        public depth4(Set depth3, List<int[]> possible, int numBranches, int player) {
            this.depth3 = depth3;
            this.possible = possible;
            this.numBranches = numBranches;
            this.player = player;
        }
        @Override
        public void run() {
            List<List<int[]>> depth5Possible = new ArrayList<>();
            for (int a = 0; a<possible.size(); a++){
                Set depth4 = new Set(depth3, possible.get(a)[0], possible.get(a)[1]);
                if (depth4.winner != 0){
                    if (depth4.winner == 1){
                        value -= Board.game.path.size();
                    }
                    if (depth4.winner == 3){
                        value += Board.game.path.size()/3.0;
                    }
                    depth5Possible.add(new ArrayList<>());
                }
                else{
                    if (depth4.numFill[other[player]-1] > depth4.numFill[other[player]-1]){
                        value += Board.game.path.size()/5.0;
                    }
                    depth5Possible.add(depth4.getNextPossible());
                    //                if (Board.game.path.numEmpty() < 45){
                    value += depth5(depth4, depth5Possible.get(a), numBranches*possible.size(), player);
                    //
                }
            }
            double[] depth4OpponentAnalysis = primaryAnalysis(depth3, possible, depth5Possible, other[player]);
            for (int b = 0; b<depth4OpponentAnalysis.length; b++){
                value -= depth4OpponentAnalysis[b]/4.0;
            }
        }
        
        public double getValue(){
            return value;
        }
    }
    private double depth5(Set depth4, List<int[]> possible, int numBranches, int player){
        double value = 0;
        List<depth6> nextDepth = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<List<int[]>> depth6Possible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set depth5 = new Set(depth4, possible.get(a)[0], possible.get(a)[1]);
            if (depth5.winner != 0){
                if (depth5.winner == 2){
                    value += Board.game.path.size();
                }
                if (depth5.winner == 3){
                    value += Board.game.path.size()/4.0;
                }
                depth6Possible.add(new ArrayList<>());
            }
            else{
                if (depth5.numFill[player-1] > depth5.numFill[player-1]){
                    value += Board.game.path.size()/6.0;
                }
                depth6Possible.add(depth5.getNextPossible());
                if (Board.game.path.numFilled > 3 && Board.game.path.nextLarge != -1){
                    nextDepth.add(new depth6(depth5, depth6Possible.get(a), numBranches*possible.size(), player));
                }
            }
        }
        double[] depth5OpponentAnalysis = primaryAnalysis(depth4, possible, depth6Possible, other[player]);
        for (int b = 0; b<depth5OpponentAnalysis.length; b++){
            value += depth5OpponentAnalysis[b]/5.0;
        }
        for (int a = 0; a<nextDepth.size(); a++){
            threads.add(new Thread(nextDepth.get(a)));
            threads.get(a).start();
        }
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
                value += nextDepth.get(a).getValue();
            }catch(InterruptedException e){};
        }
        return value;
    }
    private class depth6 implements Runnable{
        
        private double value = 0;
        private Set depth5;
        private List<int[]> possible;
        private int numBranches, player;
        
        public depth6(Set depth5, List<int[]> possible, int numBranches, int player) {
            this.depth5 = depth5;
            this.possible = possible;
            this.numBranches = numBranches;
            this.player = player;
        }
        @Override
        public void run() {
            List<List<int[]>> depth7Possible = new ArrayList<>();
            for (int a = 0; a<possible.size(); a++){
                Set depth6 = new Set(depth5, possible.get(a)[0], possible.get(a)[1]);
                if (depth6.winner != 0){
                    if (depth6.winner == 1){
                        value -= Board.game.path.size();
                    }
                    if (depth6.winner == 3){
                        value += Board.game.path.size()/4.0;
                    }
                    depth7Possible.add(new ArrayList<>());
                }
                else{
                    if (depth6.numFill[other[player]-1] > depth6.numFill[other[player]-1]){
                        value += Board.game.path.size()/7.0;
                    }
                    depth7Possible.add(depth6.getNextPossible());
                    //                if (Board.game.path.numEmpty() < 45){
                    value += depth7(depth6, depth7Possible.get(a), numBranches*possible.size(), player);
                    //                }
                }
            }
            double[] depth6OpponentAnalysis = primaryAnalysis(depth5, possible, depth7Possible, other[player]);
            for (int b = 0; b<depth6OpponentAnalysis.length; b++){
                value -= depth6OpponentAnalysis[b]/6.0;
            }
        }
        
        public double getValue(){
            return value;
        }
    }
    private double depth7(Set depth6, List<int[]> possible, int numBranches, int player){
        double value = 0;
        List<List<int[]>> depth8Possible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set depth7 = new Set(depth6, possible.get(a)[0], possible.get(a)[1]);
            if (depth7.winner != 0){
                if (depth7.winner == 2){
                    value += Board.game.path.size();
                }
                if (depth7.winner == 3){
                    value += Board.game.path.size()/5.0;
                }
                depth8Possible.add(new ArrayList<>());
            }
            else{
                if (depth7.numFill[player-1] > depth7.numFill[player-1]){
                    value += Board.game.path.size()/8.0;
                }
                depth8Possible.add(depth7.getNextPossible());
            }
        }
        double[] depth7OpponentAnalysis = primaryAnalysis(depth6, possible, depth8Possible, other[player]);
        for (int b = 0; b<depth7OpponentAnalysis.length; b++){
            value += depth7OpponentAnalysis[b]/7.0;
        }
        return value;
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
