package Main;


import AI.Set;
import AI.SinglePlayer;
import static Main.Mode.board;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


public class Game{
    
    /*
    Created by Antonio Kim
    Date last Modified: January 18th 2016
    */
    
    final public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
    public static int player = 1, LargeBox = -1, lastmove = 2, playerwon = 0;
    public static int x = p.convertX(35), y = p.convertY(20), width = p.convertX(720), space = p.convertY(20), divisions = 9;
    public  final static int proportion = (width/3 - 2*(width/3/space))/3; //72
    public  final static int[][] ranges = {{0,2},{3,5},{6,8}};
    public  static long start, end;
    public  static boolean hover = false;
    public  static int hoverX = 0, hoverY = 0, hoverA = 0, hoverB = 0, mx = 0, my = 0;
    public  static String[] players = {"Player 1","Player 2"};
    public  static String text = players[0]+"'s turn";
    public   static boolean newpressed = false, menupressed = false, newgame = true, undo = false, running = false, helppressed = false, disable = false;
    
//    LinkedList<int[]>grids = new LinkedList<>();
    
    public  static int[][] grid = new int[divisions][divisions];
    public   static int[] LargeFilled = new int[divisions];
    
//    public   static OldSinglePlayer computers = new OldSinglePlayer();
    public   static SinglePlayer computers = new SinglePlayer();
    
    public static Set path = new Set();
    
    public void run(){
        clear();
        if (Mode.singleplayer){
            players[1] = "Computer";
        }
        else{
            players[1] = "Player 2";
        }
        Mode.board.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {                }
            @Override
            public void mousePressed(MouseEvent me) {
                Mode.board.setFocusable(true);
                Mode.board.requestFocusInWindow();
                mx = me.getX();
                my = me.getY();
                if (!disable && playerwon == 0 && (!Mode.singleplayer || player == 1)){
                    for (int d = 0; d<3; d++){
                        for (int c = 0; c<3; c++){
                            for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
                                for (int b = ranges[c][0]; b<=ranges[c][1];b++){
                                    if(mx>Board.gridX[3*(a%3)+b%3] && mx<(Board.gridX[3*(a%3)+b%3]+proportion) && my>Board.gridY[3*(a/3)+b/3] && my<(Board.gridY[3*(a/3)+b/3]+proportion)){
                                        if (path.Largefilled[a] == 0 && (a == LargeBox || LargeBox == -1) && grid[a][b] == 0){
                                            playermove(a, b);
                                            path.add(a, b);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (mx>(int)(Main.w.getWidth()*(800.0/1210)) && mx<(int)(Main.w.getWidth()*(1170.0/1210)) && my>(int)(Main.w.getHeight()*(575.0/1210))  && my<(int)(Main.w.getHeight()*(650.0/1210))){
                    newpressed = true;
                }
                else if (mx>(int)(Main.w.getWidth()*(800.0/1210)) && mx<(int)(Main.w.getWidth()*(1170.0/1210))  && my>(int)(Main.w.getHeight()*(670.0/1210)) && my<(int)(Main.w.getHeight()*(745.0/1210))){
                    menupressed = true;
                }
                else if(!disable && mx>(int)(Main.w.getWidth()*(800.0/1210)) && mx<(int)(Main.w.getWidth()*(1170.0/1210))  && my>(int)(Main.w.getHeight()*(480.0/1210)) && my<(int)(Main.w.getHeight()*(555.0/1210))){
                    undo = true;
                }
                Mode.board.update();
            }
            @Override
            public void mouseReleased(MouseEvent me) {
                if (mx>(int)(Main.w.getWidth()*(1155.0/1210)) && mx<(int)(Main.w.getWidth()*(1185.0/1210)) && my>(int)(Main.w.getHeight()*(10.0/810)) && my<(int)(Main.w.getHeight()*(55.0/810))){
                    newgame = false;
                    helppressed = false;
                    Main.w.Switch(Mode.board, MainMenu.i);
                    MainMenu.i.run();
                }
                if (mx>(int)(Main.w.getWidth()*(800.0/1210)) && mx<(int)(Main.w.getWidth()*(1170.0/1210)) && my>(int)(Main.w.getHeight()*(575.0/1210))  && my<(int)(Main.w.getHeight()*(650.0/1210))){
                    newpressed = true;
                }
                else if (mx>(int)(Main.w.getWidth()*(800.0/1210)) && mx<(int)(Main.w.getWidth()*(1170.0/1210))  && my>(int)(Main.w.getHeight()*(670.0/1210)) && my<(int)(Main.w.getHeight()*(745.0/1210))){
                    menupressed = true;
                }
                else if(!disable && mx>(int)(Main.w.getWidth()*(800.0/1210)) && mx<(int)(Main.w.getWidth()*(1170.0/1210))  && my>(int)(Main.w.getHeight()*(480.0/1210)) && my<(int)(Main.w.getHeight()*(555.0/1210))){
                    undo = true;
                }
                mx = 0;
                my = 0;
                if (newpressed){
                    Mode.board.update();
                    newpressed = false;
                    if(playerwon == 0){
                        int result = JOptionPane.showConfirmDialog (null, "Are you sure you want to reset the game?", "Select an option", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION || result == JOptionPane.YES_OPTION){
                            computers.reset();
                            player = 1;
                            LargeBox = -1;
                            lastmove = 2;
                            text = (players[0]+"'s turn");
                            clear();
                            Main.w.Switch(Mode.board, MainMenu.mode);
                            MainMenu.mode.run();
                        }
                    }
                    else{
                        clear();
                        Main.w.Switch(Mode.board, MainMenu.mode);
                        MainMenu.mode.run();
                    }
                    path = new Set();
                }
                else if (menupressed){
                    menupressed = false;
                    Main.w.Switch(Mode.board, Main.menu);
                }
                else if(undo){
                    board.game.computers.reset();
                    undo = false;
                    int[] playernum = {0, 2, 1};
                    if (path.size()>0){
                        check(path.Large.get(path.size()-1), player);
                        if (playerwon == 0){
                            path.remove();
                        }
                        else{
                            for (int a = 0; a<playernum[player]; a++){
                                path.remove();
                            }
                        }
                        grid = path.grid;
                        lastmove = player;
                        player = playernum [player];
                        if (path.size()>0){
                            LargeBox = path.Small.get(path.Small.size()-1);
                            if (Mode.singleplayer){
                                check(path.Large.get(path.size()-1), player);
                                path.remove();
                                grid = path.grid;
                                lastmove = player;
                                player = playernum [player];
                                if (path.size()>0){
                                    LargeBox = path.Small.get(path.Small.size()-1);
                                }
                                else{
                                    LargeBox = -1;
                                    player = 1;
                                    lastmove = 2;
                                }
                            }
                        }
                        else{
                            LargeBox = -1;
                            lastmove = 2;
                            player = 1;
                        }
                    }
                    text = (players[player-1]+"'s turn");
                }
                else{
                    if (Mode.singleplayer && player == 2){
                        computers.move(2);
                    }
                }
                Mode.board.update();
            }
            @Override
            public void mouseEntered(MouseEvent me) {
                Mode.board.setFocusable(true);
                Mode.board.requestFocusInWindow();
            }
            @Override
            public void mouseExited(MouseEvent me) {                }
            
        });
        Mode.board.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {}
            
            @Override
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                if(keyCode == KeyEvent.VK_BACK_SPACE){
                    newgame = false;
                    Main.w.Switch(Mode.board, Main.menu);
                }
                else if (keyCode == KeyEvent.VK_ADD && playerwon == 0){
                    randomPlay();
                }
                else if(keyCode == KeyEvent.VK_BACK_QUOTE && playerwon != 0){
                    clear();
                    Main.w.Switch(Mode.board, MainMenu.mode);
                    MainMenu.mode.run();
                }
                Mode.board.update();
            }
            @Override
            public void keyReleased(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                
            }
            
        });
        Mode.board.setFocusable(true);
        Mode.board.requestFocusInWindow();
    }
    
    public void clear(){
        if(!newgame){
            players[0] = "Player 1";
            players[1] = "Player 2";
        }
        newgame = true;
        playerwon = 0;
        player = 1;
        LargeBox = -1;
        lastmove = 2;
        for (int a = 0; a<9; a++){
            for (int b = 0; b<9; b++){
                grid[a][b] = 0;
            }
            LargeFilled[a] = 0;
        }
        text = (players[player-1]+"'s turn");
        Mode.board.update();
    }
    
    
    public void playermove(int a, int b){
        newgame = false;
        if( LargeBox == a || LargeBox == -1){
            if (grid[a][b] == 0){
                if (LargeFilled[b] != 0){
                    LargeBox = -1;
                }
                else{
                    LargeBox = b;
                }
                lastmove = player;
                grid[a][b] = player;
                check(a, player);
                if (playerwon ==1 || playerwon == 2){
                    text = (players[playerwon-1]+" Wins!!!");
                }
                else if (playerwon == 3){
                    text = ("Cats Game!!!");
                }
                else if (player == 1){
                    player = 2;
                    text = (players[player-1]+"'s turn");
                }
                else if (player == 2){
                    player = 1;
                    text = (players[player-1]+"'s turn");
                }
                Mode.board.update();
            }
        }
    }
    
    public void check(int a, int player){
        newgame = false;
        if(checkfilled(grid[a], player)){
            LargeFilled[a] = player;
            if (a == LargeBox){
                LargeBox = -1;
            }
        }
        else if (checkcats(grid[a]) && LargeFilled[a] == 0){
            LargeFilled[a] = 3;
        }
        else{
            LargeFilled[a] = 0;
        }
        checkWin(player);
    }
    
    public void checkWin(int player){
        if(checkfilled(LargeFilled, player)){
            playerwon = player;
            text = (players[playerwon-1]+" Wins!!!");
        }
        else if(checkcats(LargeFilled) && playerwon == 0){
//            System.out.println("Cats Game");
text = ("Cats Game!!!");
playerwon = 3;
        }
        else{
            playerwon = 0;
        }
    }
    
    public boolean checkfilled(int[]array, int player){
        for (int a = 0; a<3; a++){
            if (array[3*a]==player && array[3*a+1]==player && array[3*a+2]==player){
                return true;
            }
            else if (array[a]==player && array[a+3]==player && array[a+6]==player){
                return true;
            }
            else if(a>=1 && array[2*a-4*(a-1)]==player && array[4*a-4*(a-1)]==player && array[6*a-4*(a-1)]==player){
                return true;
            }
        }
        return false;
    }
    public boolean checkcats(int[]array){
        for (int a = 0; a<array.length; a++){
            if (array[a]==0){
                return false;
            }
        }
        return true;
    }
    
    public void randomPlay (){
//                    System.out.println("Randomizing");
while(playerwon == 0){
//            computers.move(player);
Mode.board.update();
//            int a = LargeBox;
//            if (a == -1){ a = (int)(9*Math.random());}
//            if( LargeFilled[a] == 0){
//                while(playerwon == 0){
//                    int b = (int)(9*Math.random());
//                    if (grid[a][b] == 0){
//                        if (LargeFilled[b] != 0){
//                            LargeBox = -1;
//                        }
//                        else{
//                            LargeBox = b;
//                        }
//                        lastmove = player;
//                        grid[a][b] = player;
//                        check(a, player);
//                        if (player == 1){
//                            player = 2;
//                        }
//                        else if (player == 2){
//                            player = 1;
//                        }
////                        try {
////
////                            Thread.sleep(100);
////                        } catch (InterruptedException ex) {
////                            Logger.getLogger(Multiplayer.class.getName()).log(Level.SEVERE, null, ex);
////                        }
//                        break;
//                    }
//                }
//        }
//                setFocusable(true);
//                requestFocusInWindow();
//
////                System.out.println("repainted");
}
    }
    
    public void textPrint(int[][]grid){
        for (int d = 0; d<3; d++){
            for (int c = 0; c<3; c++){
                for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
                    for (int b = ranges[c][0]; b<=ranges[c][1];b++){
                        System.out.print(grid[a][b]+" ");
                    }
                }
                System.out.println("");
            }
        }
        System.out.println("\n\n");
    }
    
}
