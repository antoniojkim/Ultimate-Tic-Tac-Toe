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
    
    Ideas:
    
    -   Try to store the branches so they do not need to be computed again
    -   Create b rudimentary neural network. Have multiple layers, each judging the board from different angles:
    -       One layer looks at legal moves
    -       One layer looks for large squares that it may want to capture, regardless of whether or not it is possible in the near future.
    -       One layer looks for large squares that the other player may want to capture, regardless of whether or not it is possible in the near future.
    -       One layer can keep track of the opponents most recent moves and try to figure out what it is attempting to accomplish.
    
    Once each layer agrees on what b good move is based on its own criterias for what is "good," the move is made.
    
    */
    
    boolean showProcess = false;
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
        // Find all the possible moves
        List<int[]> possible = Board.game.path.getNextPossible();
        // If I can win, take the win
        if (Board.game.path.numFill[player-1] > 1){
            for (int a = 0; a<possible.size(); a++){
                if (MainAI.canWin(Board.game.path.grid, possible.get(a)[0], possible.get(a)[1], player)){
                    return possible.get(a);
                }
            }
        }
        // If doing this move will make me lose, don't do it
        if (Board.game.path.numFill[other[player]-1] > 1){
            for (int a = 0; a<possible.size(); a++){
                Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
                if (set.nextLarge != -1 && set.Largefilled[set.nextLarge] == 0){
                    for (int b = 0; b<9; b++){
                        if (set.grid[set.nextLarge][b] == 0){
                            int[] array = {set.nextLarge, b};
                            if (MainAI.canWin(set.grid, array[0], array[1], other[player])){
                                possible.remove(a);
                                a--;
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
                                    if (MainAI.canWin(set.grid, array[0], array[1], other[player])){
                                        possible.remove(a);
                                        a--;
                                        b = 9;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // If no matter what I do, I will lose, See if there is possibility to tie.
            if (possible.isEmpty()){
                possible.addAll(Board.game.path.getNextPossible());
                for (int a = 0; a<possible.size(); a++){
                    Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
                    if (set.winner == 3){
                        return possible.get(a);
                    }
                }
                return possible.get(p.randomint(0, possible.size()-1));
            }
        }
        if (possible.size() == 1){
            return possible.get(0);
        }
        if (possible.isEmpty()){
            possible.addAll(Board.game.path.getNextPossible());
            return possible.get(p.randomint(0, possible.size()-1));
        }
        //return NeuralNetwork.process(Board.game.path, possible, player);
        return getMove(possible, player);
    }
    
    private int[] getMove(List<int[]> possible, int player){
        List<List<int[]>> nextPossible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
            nextPossible.add(set.getNextPossible());
        }
        if (Board.game.path.numFill[player-1] >= 2){
            //If there is b way to force my opponent into giving me the large square, do it
            List<Integer> largeSquareWin = new ArrayList<>();
            for (int a = 0; a<MainAI.planAhead.length; a++){
                for (int b = 0; b<MainAI.planAhead[a].length; b++){
                    //If I have two squares in b row:
                    if (Board.game.path.Largefilled[MainAI.planAhead[a][b][0]] == player &&
                            Board.game.path.Largefilled[MainAI.planAhead[a][b][1]] == player){
                        largeSquareWin.add(a);
                    }
                }
            }
            if (!largeSquareWin.isEmpty()){
                for (int a = 0; a<possible.size(); a++){
                    boolean force = true;
                    Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
                    for (int b = 0; b<nextPossible.get(a).size(); b++){
                        Set newset = new Set(set, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                        if (!largeSquareWin.contains(newset.nextLarge) && newset.nextLarge != -1){
                            force = false;
                            break;
                        }
                    }
                    if (force){
                        return possible.get(a);
                    }
                }
                if (showProcess){ System.out.println("Force Analysis Completed"); }
            }
        }
        double[] rating = new double[possible.size()];
        double[] primaryRating = primary(possible, player);
        for (int a = 0; a<primaryRating.length; a++){
            rating[a] += primaryRating[a]*15/Board.game.path.size();
        }
        double[] depthRating = depth(possible, nextPossible, player);
        for (int a = 0; a<depthRating.length; a++){
            rating[a] += depthRating[a];
        }
        if (Board.game.path.size() > 20 && Board.game.path.nextLarge != -1){
            double[] randomSampleRating = randomSample(possible, nextPossible, player);
            for (int a = 0; a<randomSampleRating.length; a++){
                rating[a] += randomSampleRating[a]/Board.game.path.numEmpty();
            }
        }
        Sort.quicksort(rating, possible);
        if (showProcess){
            System.out.print("Rating:  ");
            for (int a = 0; a<rating.length; a++){
                System.out.print(p.round(rating[a], 3)+"  ");
            }
            System.out.print("\nMove:      ");
            for (int a = 0; a<possible.size(); a++){
                System.out.print(possible.get(a)[1]+"      ");
            }
            System.out.println("\n\nMove Made:   ["+possible.get(0)[0]+", "+possible.get(0)[1]+"]");
        }
        return possible.get(0);
    }
    
    private double[] primary(List<int[]> temp, int player){
        List<int[]> possible = new ArrayList<>();
        for (int a = 0; a<temp.size(); a++){
            possible.add(temp.get(a));
        }
        //double[] primaryAnalysis = primaryAnalysis(Board.game.path, possible, nextPossible, player);
        double[] primaryAnalysis = NeuralNetwork.getNeuralValues(Board.game.path, possible, player);
        for (int a = 0; a<possible.size(); a++){
            double z = 0;
            for (int b = 0; b<MainAI.setup.length; b++){
                for (int c = 0; c<MainAI.setup[b].length; c++){
                    if (Board.game.path.grid[possible.get(a)[1]][MainAI.setup[b][c][0]] == other[player] &&
                            Board.game.path.grid[possible.get(a)[1]][MainAI.setup[b][c][1]] == other[player]){
                        z++;
                    }
                }
            }
            primaryAnalysis[a] -= 3*z;
        }
        if (showProcess){
            System.out.print("Primary:   ");
            for (int a = 0; a<primaryAnalysis.length; a++){
                System.out.print(p.round(primaryAnalysis[a], 3)+"  ");
            }
            System.out.print("\nMove:      ");
            for (int a = 0; a<possible.size(); a++){
                System.out.print(possible.get(a)[1]+"      ");
            }
            System.out.println("");
        }
        return primaryAnalysis;
    }
    
    private double[] depth(List<int[]> temp, List<List<int[]>> nextTemp, int player){
        List<int[]> possible = new ArrayList<>();
        for (int a = 0; a<temp.size(); a++){
            possible.add(temp.get(a));
        }
        List<List<int[]>> nextPossible = new ArrayList<>();
        for (int a = 0; a<nextTemp.size(); a++){
            List<int[]> temp1 = new ArrayList<>();
            for (int b = 0; b<nextTemp.get(a).size(); b++){
                temp1.add(nextTemp.get(a).get(b));
            }
            nextPossible.add(temp1);
        }
        double[] depthSearch = new double[possible.size()];
        for (int a = 0; a<possible.size(); a++){ //Math.min(possible.size(), 15)
            Set depth1 = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
            for (int b = 0; b<nextPossible.get(a).size(); b++){
                Set depth2 = new Set(depth1, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                if (depth2.winner != 0){
                    if (depth2.winner == 1){
                        depthSearch[a] -= Board.game.path.size();
                    }
                    if (depth2.winner == 3){
                        depthSearch[a] += Board.game.path.size()/2.0;
                    }
                }
                else{
                    if (depth2.numFill[other[player]-1] > depth1.numFill[other[player]-1]){
                        depthSearch[a] -= Board.game.path.size()/3.0;
                    }
                    if (depth2.nextLarge == -1){
                        depthSearch[a] += Board.game.path.size()/5.0;
                    }
                    if (depth2.nextLarge != -1){
                        depthSearch[a] += depth3(depth2, depth2.getNextPossible(), possible.size()*nextPossible.size(), player)/(9-depth2.numFilled);
                    }
                    else{
                        depthSearch[a] += depth3(depth2, depth2.getNextPossible(), possible.size()*nextPossible.size(), player);
                    }
                }
            }
            //            double[] depth1OpponentAnalysis = primaryAnalysis(depth1, nextPossible.get(b), depth3Possible, other[player]);
            double[] depth1OpponentAnalysis = NeuralNetwork.getNeuralValues(depth1, nextPossible.get(a), other[player]);
            for (int b = 0; b<depth1OpponentAnalysis.length; b++){
                depthSearch[a] -= depth1OpponentAnalysis[b]/3.0/nextPossible.get(a).size();
            }
        }
        if (showProcess){
            System.out.print("Depth:   ");
            for (int a = 0; a<depthSearch.length; a++){
                System.out.print(p.round(depthSearch[a], 3)+"  ");
            }
            System.out.print("\nMove:      ");
            for (int a = 0; a<possible.size(); a++){
                System.out.print(possible.get(a)[1]+"      ");
            }
            System.out.println("");
        }
        return depthSearch;
    }
    private double depth3(Set depth2, List<int[]> possible, int numBranches, int player){
        double value = 0;
        List<depth4> nextDepth = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        List<List<int[]>> depth4Possible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set depth3 = new Set(depth2, possible.get(a)[0], possible.get(a)[1]);
            if (depth3.winner != 0){
                if (depth3.winner == player){
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
            }
        }
        //        double[] depth3OpponentAnalysis = primaryAnalysis(depth2, possible, depth4Possible, player);
        double[] depth3OpponentAnalysis = NeuralNetwork.getNeuralValues(depth2, possible, player);
        for (int b = 0; b<depth3OpponentAnalysis.length; b++){
            value += depth3OpponentAnalysis[b]/5.0/possible.size();
        }
        boolean[] nextLarge = new boolean[possible.size()];
        for (int a = 0; a<possible.size(); a++){
            Set depth3 = new Set(depth2, possible.get(a)[0], possible.get(a)[1]);
            nextDepth.add(new depth4(depth3, depth4Possible.get(a), numBranches*possible.size(), player));
            threads.add(new Thread(nextDepth.get(a)));
            if (depth3.nextLarge == -1){
                nextLarge[a] = true;
            }
            else{
                nextLarge[a] = false;
            }
        }
        for (int a = 0; a<threads.size(); a++){
            threads.get(a).start();
        }
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
                if (nextLarge[a]){
                    value += nextDepth.get(a).getValue()/(8-depth2.nextLarge);
                }
                else{
                    value += nextDepth.get(a).getValue();
                }
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
            for (int a = 0; a<possible.size(); a++){
                Set depth4 = new Set(depth3, possible.get(a)[0], possible.get(a)[1]);
                if (depth4.winner != 0){
                    if (depth4.winner == other[player]){
                        value -= Board.game.path.size();
                    }
                    if (depth4.winner == 3){
                        value += Board.game.path.size()/3.0;
                    }
                }
                else{
                    if (depth4.numFill[other[player]-1] > depth4.numFill[other[player]-1]){
                        value += Board.game.path.size()/5.0;
                    }
                    if (depth4.nextLarge == -1){
                        value += depth5(depth4, depth4.getNextPossible(), numBranches*possible.size(), player)/(9-depth4.numFilled);
                    }
                    else{
                        value += depth5(depth4, depth4.getNextPossible(), numBranches*possible.size(), player);
                    }
                }
            }
            //            double[] depth4OpponentAnalysis = primaryAnalysis(depth3, possible, depth5Possible, other[player]);
            double[] depth4OpponentAnalysis = NeuralNetwork.getNeuralValues(depth3, possible, other[player]);
            for (int b = 0; b<depth4OpponentAnalysis.length; b++){
                value -= depth4OpponentAnalysis[b]/8.0/possible.size();
            }
        }
        
        public double getValue(){
            return value;
        }
    }
    private double depth5(Set depth4, List<int[]> possible, int numBranches, int player){
        double value = 0;
        //List<List<int[]>> depth6Possible = new ArrayList<>();
        for (int a = 0; a<possible.size(); a++){
            Set depth5 = new Set(depth4, possible.get(a)[0], possible.get(a)[1]);
            if (depth5.winner != 0){
                if (depth5.winner == player){
                    value += Board.game.path.size();
                }
                if (depth5.winner == 3){
                    value += Board.game.path.size()/4.0;
                }
                //depth6Possible.add(new ArrayList<>());
            }
            else{
                if (depth5.numFill[player-1] > depth5.numFill[player-1]){
                    value += Board.game.path.size()/6.0;
                }
                //depth6Possible.add(depth5.getNextPossible());
            }
        }
        //        double[] depth5OpponentAnalysis = primaryAnalysis(depth4, possible, depth6Possible, player);
        double[] depth5OpponentAnalysis = NeuralNetwork.getNeuralValues(depth4, possible, player);
        for (int b = 0; b<depth5OpponentAnalysis.length; b++){
            value += depth5OpponentAnalysis[b]/13.0/possible.size();
        }
//        List<depth6> nextDepth = new ArrayList<>();
//        List<Thread> threads = new ArrayList<>();
//        if (Board.game.path.numFilled > 3 && Board.game.path.nextLarge != -1){
//            for (int a = 0; a<possible.size(); a++){
//                Set depth5 = new Set(depth4, possible.get(a)[0], possible.get(a)[1]);
//                depth6 d6 = new depth6(depth5, depth6Possible.get(a), numBranches*possible.size(), player);
//                nextDepth.add(d6);
//                Thread thread = new Thread(d6);
//                threads.add(thread);
//            }
//        }
//        for (int a = 0; a<threads.size(); a++){
//            threads.get(a).start();
//        }
//        for (int a = 0; a<threads.size(); a++){
//            try{
//                threads.get(a).join();
//                value += nextDepth.get(a).getValue();
//            }catch(InterruptedException e){};
//        }
return value;
    }
    
    private double[] randomSample(List<int[]> temp, List<List<int[]>> nextTemp, int player){
        List<int[]> possible = new ArrayList<>();
        for (int a = 0; a<temp.size(); a++){
            possible.add(temp.get(a));
        }
        List<List<int[]>> nextPossible = new ArrayList<>();
        for (int a = 0; a<nextTemp.size(); a++){
            List<int[]> temp1 = new ArrayList<>();
            for (int b = 0; b<nextTemp.get(a).size(); b++){
                temp1.add(nextTemp.get(a).get(b));
            }
            nextPossible.add(temp1);
        }
        List<randomSample> randomSamples = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        int numSample = 400-3*Board.game.path.numEmpty();
        for (int a = 0; a<possible.size(); a++){
            Set set = new Set(Board.game.path, possible.get(a)[0], possible.get(a)[1]);
            for (int b = 0; b<nextPossible.get(a).size(); b++){
                Set newset = new Set(set, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
                for (int c = 0; c<numSample; c++){
                    randomSample rs = new randomSample(new Set(newset), a);
                    randomSamples.add(rs);
                    threads.add(new Thread(rs));
                }
            }
        }
        for (int a = 0; a<threads.size(); a++){
            threads.get(a).start();
        }
        double[] randomSampleValues = new double[possible.size()];
        for (int a = 0; a<threads.size(); a++){
            try{
                threads.get(a).join();
                int parent = randomSamples.get(a).getParent();
                int winner = randomSamples.get(a).getWinner();
                if (winner == player){
                    randomSampleValues[parent] += NeuralNetwork.activationFunction(2, 2, -0.5);
                }
                else if (winner == other[player]){
                    randomSampleValues[parent] -= NeuralNetwork.activationFunction(2, 0.25, -0.5);
                }
                else if (winner == 3){
                    randomSampleValues[parent] += NeuralNetwork.activationFunction(2, -0.5, -0.5);
                }
            }catch(InterruptedException e){};
        }
        if (showProcess){
            System.out.print("Random:  ");
            for (int a = 0; a<randomSampleValues.length; a++){
                System.out.print(p.round(randomSampleValues[a], 3)+"  ");
            }
            System.out.print("\nMove:      ");
            for (int a = 0; a<possible.size(); a++){
                System.out.print(possible.get(a)[1]+"      ");
            }
            System.out.println("");
        }
        return randomSampleValues;
    }
    private class randomSample implements Runnable{
        
        Set set;
        int parent;
        
        public randomSample(Set set, int parent){
            this.set = set;
            this.parent = parent;
        }
        @Override
        public void run() {
            while (set.winner == 0 && (set.size()-Board.game.path.size()) <= 20){
                set.addRandom();
            }
        }
        public int getParent(){
            return parent;
        }
        public int getWinner(){
            return set.winner;
        }
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
//    private class depth6 implements Runnable{
//
//        private double value = 0;
//        private Set depth5;
//        private List<int[]> possible;
//        private int numBranches, player;
//
//        public depth6(Set depth5, List<int[]> possible, int numBranches, int player) {
//            this.depth5 = depth5;
//            this.possible = possible;
//            this.numBranches = numBranches;
//            this.player = player;
//        }
//        @Override
//        public void run() {
//            List<List<int[]>> depth7Possible = new ArrayList<>();
//            for (int a = 0; a<possible.size(); a++){
//                Set depth6 = new Set(depth5, possible.get(a)[0], possible.get(a)[1]);
//                if (depth6.winner != 0){
//                    if (depth6.winner == other[player]){
//                        value -= Board.game.path.size();
//                    }
//                    if (depth6.winner == 3){
//                        value += Board.game.path.size()/4.0;
//                    }
//                    depth7Possible.add(new ArrayList<>());
//                }
//                else{
//                    if (depth6.numFill[other[player]-1] > depth6.numFill[other[player]-1]){
//                        value += Board.game.path.size()/7.0;
//                    }
//                    depth7Possible.add(depth6.getNextPossible());
//                }
//            }
//            //            double[] depth6OpponentAnalysis = primaryAnalysis(depth5, possible, depth7Possible, other[player]);
//            double[] depth6OpponentAnalysis = NeuralNetwork.getNeuralValues(depth5, possible, other[player]);
//            for (int b = 0; b<depth6OpponentAnalysis.length; b++){
//                value -= depth6OpponentAnalysis[b]/11.0/possible.size();
//            }
////            for (int a = 0; a<possible.size(); a++){
////                Set depth6 = new Set(depth5, possible.get(a)[0], possible.get(a)[1]);
////                value += depth7(depth6, depth7Possible.get(a), numBranches*possible.size(), player);
////            }
//        }
//
//        public double getValue(){
//            return value;
//        }
//    }
//    private double depth7(Set depth6, List<int[]> possible, int numBranches, int player){
//        double value = 0;
//        //List<List<int[]>> depth8Possible = new ArrayList<>();
//        for (int a = 0; a<possible.size(); a++){
//            Set depth7 = new Set(depth6, possible.get(a)[0], possible.get(a)[1]);
//            if (depth7.winner != 0){
//                if (depth7.winner == player){
//                    value += Board.game.path.size();
//                }
//                if (depth7.winner == 3){
//                    value += Board.game.path.size()/5.0;
//                }
//                //depth8Possible.add(new ArrayList<>());
//            }
//            else{
//                if (depth7.numFill[player-1] > depth7.numFill[player-1]){
//                    value += Board.game.path.size()/8.0;
//                }
//                //depth8Possible.add(depth7.getNextPossible());
//            }
//        }
//        //        double[] depth7OpponentAnalysis = primaryAnalysis(depth6, possible, depth8Possible, player);
//        double[] depth7OpponentAnalysis = NeuralNetwork.getNeuralValues(depth6, possible, player);
//        for (int b = 0; b<depth7OpponentAnalysis.length; b++){
//            value += depth7OpponentAnalysis[b]/13.0/possible.size();
//        }
////        List<depth8> nextDepth = new ArrayList<>();
////        List<Thread> threads = new ArrayList<>();
////        //        if (Board.game.path.numFilled > 3 && Board.game.path.nextLarge != -1){
////        for (int a = 0; a<possible.size(); a++){
////            Set depth7 = new Set(depth6, possible.get(a)[0], possible.get(a)[1]);
////            depth8 d8 = new depth8(depth7, depth8Possible.get(a), numBranches*possible.size(), player);
////            nextDepth.add(d8);
////            Thread thread = new Thread(d8);
////            threads.add(thread);
////        }
////        //        }
////        for (int a = 0; a<threads.size(); a++){
////            threads.get(a).start();
////        }
////        for (int a = 0; a<threads.size(); a++){
////            try{
////                threads.get(a).join();
////                value += nextDepth.get(a).getValue();
////            }catch(InterruptedException e){};
////        }
//return value;
//    }
//    private class depth8 implements Runnable{
//
//        private double value = 0;
//        private Set depth7;
//        private List<int[]> possible;
//        private int numBranches, player;
//
//        public depth8(Set depth7, List<int[]> possible, int numBranches, int player) {
//            this.depth7 = depth7;
//            this.possible = possible;
//            this.numBranches = numBranches;
//            this.player = player;
//        }
//        @Override
//        public void run() {
//            List<List<int[]>> depth9Possible = new ArrayList<>();
//            for (int a = 0; a<possible.size(); a++){
//                Set depth8 = new Set(depth7, possible.get(a)[0], possible.get(a)[1]);
//                if (depth8.winner != 0){
//                    if (depth8.winner == other[player]){
//                        value -= Board.game.path.size();
//                    }
//                    if (depth8.winner == 3){
//                        value += Board.game.path.size()/6.0;
//                    }
//                    depth9Possible.add(new ArrayList<>());
//                }
//                else{
//                    if (depth8.numFill[other[player]-1] > depth8.numFill[other[player]-1]){
//                        value += Board.game.path.size()/11.0;
//                    }
//                    depth9Possible.add(depth8.getNextPossible());
//                }
//            }
//            //            double[] depth6OpponentAnalysis = primaryAnalysis(depth5, possible, depth7Possible, other[player]);
//            double[] depth8OpponentAnalysis = NeuralNetwork.getNeuralValues(depth7, possible, other[player]);
//            for (int b = 0; b<depth8OpponentAnalysis.length; b++){
//                value -= depth8OpponentAnalysis[b]/15.0/possible.size();
//            }
//            for (int a = 0; a<possible.size(); a++){
//                Set depth8 = new Set(depth7, possible.get(a)[0], possible.get(a)[1]);
//                value += depth9(depth8, depth9Possible.get(a), numBranches*possible.size(), player);
//            }
//        }
//
//        public double getValue(){
//            return value;
//        }
//    }
//    private double depth9(Set depth8, List<int[]> possible, int numBranches, int player){
//        double value = 0;
//        //List<List<int[]>> depth10Possible = new ArrayList<>();
//        for (int a = 0; a<possible.size(); a++){
//            Set depth9 = new Set(depth8, possible.get(a)[0], possible.get(a)[1]);
//            if (depth9.winner != 0){
//                if (depth9.winner == player){
//                    value += Board.game.path.size();
//                }
//                if (depth9.winner == 3){
//                    value += Board.game.path.size()/7.0;
//                }
//                //depth10Possible.add(new ArrayList<>());
//            }
//            else{
//                if (depth9.numFill[player-1] > depth9.numFill[player-1]){
//                    value += Board.game.path.size()/12.0;
//                }
//                //depth10Possible.add(depth9.getNextPossible());
//            }
//        }
//        //        double[] depth7OpponentAnalysis = primaryAnalysis(depth6, possible, depth8Possible, player);
//        double[] depth9OpponentAnalysis = NeuralNetwork.getNeuralValues(depth8, possible, player);
//        for (int b = 0; b<depth9OpponentAnalysis.length; b++){
//            value += depth9OpponentAnalysis[b]/17.0/possible.size();
//        }
//        return value;
//    }
    
    
    public void old(){
        /*
        for (int b = 1; b<possible.size(); b++){
        if (values.get(b) > values.get(b-1)){
        int[] firstPossible = possible.get(b);
        int firstValue = values.get(b);
        possible.set(b, possible.get(b-1));
        values.set(b, values.get(b-1));
        possible.set(b-1, firstPossible);
        values.set(b-1, firstValue);
        b = 0;
        }
        }
        long start = System.currentTimeMillis();
        if (showProcess) {System.out.print("Most Promising Moves Found: {");}
        List<int[]> promising = new ArrayList<>();
        for (int b = 0; b<possible.size() && b<3; b++){
        promising.add(possible.get(b));
        if (showProcess) {System.out.print(possible.get(b)[1]+",  ");}
        }
        if (showProcess) {System.out.println("}");}
        values.clear();
        values.addAll(deepSearch(promising));
        for (int b = 1; b<promising.size(); b++){
        if (values.get(b) > values.get(b-1)){
        int[] firstPromising = promising.get(b);
        int firstValue = values.get(b);
        promising.set(b, promising.get(b-1));
        values.set(b, values.get(b-1));
        promising.set(b-1, firstPromising);
        values.set(b-1, firstValue);
        b = 0;
        }
        }
        for (int b = 1; b<promising.size(); b++){
        if (values.get(b) > values.get(0)){
        values.remove(b);
        promising.remove(b);
        b = 0;
        }
        }
        if (showProcess) {System.out.println("Took "+(System.currentTimeMillis()-start)+" Milliseconds to complete Secondary Deep Analysis");}
        if (promising.size() == 1){
        Board.game.LargeBox = promising.get(0)[0];
        return promising.get(0)[1];
        }
        if (promising.size() > 1){
        for (int b = 0; b<possible.size(); b++){
        int index = promising.indexOf(possible.get(b));
        if (index != -1){
        Board.game.LargeBox = promising.get(index)[0];
        return promising.get(index)[1];
        }
        }
        }public List<Integer> deepSearch (List<int[]> possible){
        List<Thread> threads = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (int b = 0; b<possible.size(); b++){
        values.add(0);
        Set set = new Set(Board.game.path, possible.get(b)[0], possible.get(b)[1]);
        List<int[]> nextPossible = MainAI.getPossible(set);
        final int A = b;
        for (int c = 0; c<nextPossible.size(); c++){
        Set newset = new Set(set, nextPossible.get(c)[0], nextPossible.get(c)[1]);
        if (newset.winner == 1 && possible.size() > 1){
        possible.remove(b);
        b--;
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
        for (int b = 0; b<threads.size(); b++){
        try{
        threads.get(b).join();
        }catch(InterruptedException e){}
        }
        return values;
        }
        
        int[] bruteForceOutcomes = {0, 250, -250, 50};
        public void bruteForceSearch (Set set, final int depth, List<Integer> values, final int parent){
        List<int[]> possible = MainAI.getPossible(set);
        int[] Largegrid = set.Largefilled;
        for (int b = 0; b<possible.size(); b++){
        if (Largegrid[possible.get(b)[1]] == 0){
        Set newset = new Set(set, possible.get(b)[0], possible.get(b)[1]);
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
