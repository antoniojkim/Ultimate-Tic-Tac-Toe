/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package AI;

import Main.p;
import java.util.List;

/**

@author Antonio
*/
public class NeuralNetwork {
    
    /*
    public static void main (String[] args){
        double extraFactor = 1;
        
        double weight1 = 2;
        double z1 = 0.25;
        double bias1 = -0.5;
        System.out.println("Weight:  "+weight1+"   z:  "+z1+"   Bias:  "+bias1+"\n");
        System.out.println(1/(1+Math.exp(-1*weight1*z1-bias1))*extraFactor);
        
        double weight2 = weight1;
        double z2 = -0.5;
        double bias2 = bias1;
        System.out.println("\n\nWeight:  "+weight2+"   z:  "+z2+"   Bias:  "+bias2+"\n");
        System.out.println(1/(1+Math.exp(-1*weight2*z2-bias2))*extraFactor);
        
        double weight3 = weight1;
        double z3 = 1;
        double bias3 = bias1;
        System.out.println("\n\nWeight:  "+weight3+"   z:  "+z3+"   Bias:  "+bias3+"\n");
        System.out.println(1/(1+Math.exp(-1*weight3*z3-bias3))*extraFactor);
    }
    /*
    */
    
    public static int[] process(Set board, List<int[]> possible, int player){
        double[] values = new double[possible.size()];
        HiddenLayers[] layer1s = new HiddenLayers[possible.size()];
        Thread[] threads = new Thread[possible.size()];
        for (int a = 0; a<possible.size(); a++){
            layer1s[a] = new HiddenLayers(board, possible.get(a), player);
            threads[a] = new Thread(layer1s[a]);
        }
        for (int a = 0; a<threads.length; a++){
            threads[a].start();
        }
        for (int a = 0; a<threads.length; a++){
            try{
                threads[a].join();
            }catch(InterruptedException e){}
        }
        for (int a = 0; a<values.length; a++){
            values[a] = layer1s[a].getScore();
        }
        Sort.quicksort(values, possible);
        System.out.print("Values:  ");
        for (int a = 0; a<values.length; a++){
            System.out.print(p.round(values[a], 5)+"  ");
        }
        System.out.print("\nMove:    ");
        for (int a = 0; a<possible.size(); a++){
            System.out.print("["+possible.get(a)[0]+", "+possible.get(a)[1]+"]   ");
        }
        System.out.println("");
        return possible.get(0);
    }
    
    public static double[] getNeuralValues(Set board, List<int[]> possible, int player){
        double[] values = new double[possible.size()];
        HiddenLayers[] layer1s = new HiddenLayers[possible.size()];
        for (int a = 0; a<possible.size(); a++){
            layer1s[a] = new HiddenLayers(board, possible.get(a), player);
            layer1s[a].run();
            values[a] = layer1s[a].getScore();
        }
        return values;
    }
    
    private static class HiddenLayers implements Runnable{
        
        private final Set set;
        private final Set newset;
        private final int[] move;
        private double score = 0;
        private final int player;
        private final int[] other = {0, 2, 1};
        
        public HiddenLayers(Set set, int[] move, int player){
            this.set = set;
            this.move = move;
            this.player = player;
            newset = new Set(set, move[0], move[1]);
        }
        
        @Override
        public void run() {
            double[] firstLayerOutput =
            {canTakeSquare(2.75, -2), canBlockOpponent(2, -1.5), isSettingUp(1.75, -2),
                isCornerMove(1, -1.5), isGivingFreedom(2.85, -2), isGivingOpportunityToTake(5, -2)};
            for (int a = 0; a<firstLayerOutput.length; a++){
                score += firstLayerOutput[a];//*(firstLayerOutput.length-a)/3;
            }
//            List<int[]> nextPossible = newset.getNextPossible();
        }
        
        //First Hidden Layer
        private double canTakeSquare(final double weight, final double bias){
            double z = 0;
            if (newset.numFill[player-1] > set.numFill[player-1]){
                if (newset.numFill[player-1] == 1){ //First Large Square Taken
                    z = 2;
                }
                else{
                    z = 1;
                }
            }
            return 2*(activationFunction(weight, z, bias)+wantToTakeSquare(1.5, -2));
        }
        private double wantToTakeSquare(final double weight, final double bias){
            double z = 0;
            for (int a = 0; a<setup[move[0]].length; a++){
                if (set.Largefilled[setup[move[0]][a][0]] == player ||
                        set.Largefilled[setup[move[0]][a][1]] == player){
                    z++;
                }
                if (set.Largefilled[setup[move[0]][a][0]] == other[player] ||
                        set.Largefilled[setup[move[0]][a][1]] == other[player]){
                    z--;
                }
            }
            return activationFunction(weight, Math.max(z, 0), bias);
        }
        private double canBlockOpponent(final double weight, final double bias){
            double z = 0;
            for (int a = 0; a<setup[move[1]].length; a++){
                if (set.grid[move[0]][setup[move[1]][a][0]] == other[player] &&
                        set.grid[move[0]][setup[move[1]][a][1]] == other[player]){
                    z++;
                    break;
                }
            }
            return 2*(activationFunction(weight, z, bias)+wantToBlockSquare(4, -1));
        }
        private double wantToBlockSquare(final double weight, final double bias){
            double z = 0;
            for (int a = 0; a<setup[move[0]].length; a++){
                if (set.Largefilled[setup[move[0]][a][0]] == other[player] &&
                        set.Largefilled[setup[move[0]][a][1]] == other[player]){
                    z++;
                }
            }
            return activationFunction(weight, z, bias);
        }
        private double isCornerMove(final double weight, final double bias){
            double z = 0;
            if (move[0] == move[1]){
                z = 2;
            }
            else if (move[1] != 4 && move[1]%2 == 0){
                z = 1;
            }
            return activationFunction(weight, z, bias)*20/set.size();
        }
        private double isSettingUp(final double weight, final double bias){
            double z = 0;
            for (int a = 0; a<setup[move[1]].length; a++){
                if (set.grid[move[0]][setup[move[1]][a][0]] == player ||
                        set.grid[move[0]][setup[move[1]][a][1]] == player){
                    z++;
                }
                if (set.grid[move[0]][setup[move[1]][a][0]] == other[player] ||
                        set.grid[move[0]][setup[move[1]][a][1]] == other[player]){
                    z--;
                }
            }
            return activationFunction(weight, Math.max(z, 0), bias)*20/set.size();
        }
        private double isGivingFreedom(final double weight, final double bias){
            double z = 0;
            if (newset.nextLarge == -1){
                z = 1;
            }
            return -1*activationFunction(weight, z, bias)*20/set.size();
        }
        private double isGivingOpportunityToTake(final double weight, final double bias){
            double z = 0;
            for (int a = 0; a<setup.length; a++){
                if (a != 4){
                    for (int b = 0; b<setup[a].length; b++){
                        if (newset.grid[move[1]][setup[a][b][0]] == other[player] &
                                newset.grid[move[1]][setup[a][b][1]] == other[player]){
                            z = 1;
                            break;
                        }
                    }
                }
            }
            return -1*activationFunction(weight, z, bias);
        }
        
        //Second Hidden Layer:
        
        public double getScore(){
            return score;
        }
        
    }
    
    public static double activationFunction(double weight, double z, double bias){
        //Sigmoid/Logistic Function
        return 1/(1+Math.exp(-1*weight*z-bias));
    }
    
    private static double costFunction(double[] weights, double[] biases){
        return -1;
    }
    
    private static int[][][] setup = {
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
    
    
    
    /*
    private double[] primaryAnalysis(Set board, List<int[]> viable, List<List<int[]>> nextPossible, int player){
    double[] values = new double[viable.size()];
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
    for (int a = 0; a<viable.size(); a++){
    // If this were a regular game of tic tac toe, is making this move good in terms of regular tic Tac Toe Heuristics?
    if (viable.get(a)[1] == 4){
    values[a]--;
    }
    else if(viable.get(a)[1]%2 == 0){
    values[a]++;
    }
    if (viable.get(a)[0] == viable.get(a)[1]){
    values[a]++;
    }
    Set set = new Set(board, viable.get(a)[0], viable.get(a)[1]);
    // If I play in this square, am I giving my opponent the freedom to go anywhere?
    if (set.nextLarge == -1){
    values[a] -= 2;
    }
    // Can I take this square?
    if (set.numFill[player-1] > board.numFill[player-1]){
    values[a] += 2;
    // Is the large square that I am taking, the center square
    if (viable.get(a)[0] == 4){
    values[a] += 0.5;
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
    values[a] += numSetups;
    }
    }
    //This is the first square being taken
    else{
    values[a]++;
    }
    }
    // Can I block my opponent from taking this square in the future?
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
    // Can I create or block a fork in the grid I must play in?
    int numSetup1 = 0, numSetup2 = 0;
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
    // If I play in this square, am I setting myself up to be able to take this square in the future?
    for (int b = 0; b<MainAI.setup[viable.get(a)[1]].length; b++){
    if ((set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][0]] != other[player] &&
    set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][1]] != other[player]) &&
    (set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][0]] == player ||
    set.grid[viable.get(a)[0]][MainAI.setup[viable.get(a)[1]][b][1]] == player)){
    values[a]++;
    }
    }
    // If I play in this square, can the opponent take the large square that I am sending them to?
    for (int b = 0; b<nextPossible.get(a).size(); b++){
    Set newset = new Set(set, nextPossible.get(a).get(b)[0], nextPossible.get(a).get(b)[1]);
    if (newset.numFill[other[player]] > set.numFill[other[player]]){
    values[a] -= 3;
    }
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
    /*
    */
    
    
}
