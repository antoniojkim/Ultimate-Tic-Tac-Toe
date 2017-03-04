
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;


public class Multiplayer extends JPanel{
    
    /*
    Created by Antonio Kim
    Date last Modified: January 18th 2016
    */
    
//    static JTextArea text, results = new JTextArea();
//    public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
//    static int player = 1, LargeBox = -1, lastmove = 2, playerwon = 0;
//    final static int x = 35, y = 20, width = 720, space = 20, divisions = 9;
//    final static int proportion = (width/3 - 2*(width/3/space))/3; //72
//    final static int[] gridX = { x+(width/3/space), x+(width/3/space)+proportion, x+(width/3/space)+proportion+proportion, x+(width/3)+(width/3/space),
//        x+(width/3)+(width/3/space)+proportion, x+(width/3)+(width/3/space)+proportion+proportion, x+(width/3)+(width/3)+(width/3/space), x+(width/3)+(width/3)+(width/3/space)+proportion,
//        x+(width/3)+(width/3)+(width/3/space)+proportion+proportion };
//    final static int[] gridY = { y+(width/3/space), y+(width/3/space)+proportion, y+(width/3/space)+proportion+proportion, y+(width/3)+(width/3/space),
//        y+(width/3)+(width/3/space)+proportion, y+(width/3)+(width/3/space)+proportion+proportion, y+(width/3)+(width/3)+(width/3/space), y+(width/3)+(width/3)+(width/3/space)+proportion,
//        y+(width/3)+(width/3)+(width/3/space)+proportion+proportion };
//    final static int[][] ranges = {{0,2},{3,5},{6,8}};
//    static long start, end;
//    static boolean hover = false;
//    static int hoverX = 0, hoverY = 0, hoverA = 0, hoverB = 0;
//    String[] players = {"",""};
//    static boolean newpressed = false, menupressed = false, newgame = true, running = false;
//    
//    static int[][] grid = new int[divisions][divisions];
//    static int[][] moves = new int[divisions][divisions];
//    static int[] LargeFilled = new int[divisions];
//    
//    public void run(){
//        clear();
//        setLayout(null);
//        add(results);       results.setLocation(x+width+50, 50);      results.setSize(350, 300);      results.setFont(new Font("Calibri", Font.PLAIN, 50));       results.setBackground(defaultcolor);
//        results.setLineWrap(true);   results.setWrapStyleWord(true);     results.setEditable(false);        results.setVisible(true);
//        results.setText("Welcome to Ultimate Tic Tac Toe!!\n\n"+players[0]+"'s turn");
//        
//        addMouseListener(new MouseListener() {
//            
//            @Override
//            public void mouseClicked(MouseEvent me) {                }
//            @Override
//            public void mousePressed(MouseEvent me) {
//                setFocusable(true);
//                requestFocusInWindow();
//                int mx = me.getX();
//                int my = me.getY();
//                //System.out.println(mx+", "+my);
//                if (mx>1155 && mx<1155+30 && my>10 && my<10+45){
//                    newgame = false;
//                    Main.w.Switch(Mode.m, MainMenu.i);
//                    MainMenu.i.run();
//                }
//                if (playerwon == 0){
//                    for (int d = 0; d<3; d++){
//                        for (int c = 0; c<3; c++){
//                            for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
//                                for (int b = ranges[c][0]; b<=ranges[c][1];b++){
//                                    
//                                    if(mx>gridX[3*(a%3)+b%3] && mx<(gridX[3*(a%3)+b%3]+proportion) && my>gridY[3*(a/3)+b/3] && my<(gridY[3*(a/3)+b/3]+proportion)){
////                                        System.out.println(a+""+b);
//                                        if( LargeBox == a || LargeBox == -1){
//                                            if (grid[a][b] == 0){
//                                                if (LargeFilled[b] != 0){
//                                                    LargeBox = -1;
//                                                }
//                                                else{
//                                                    LargeBox = b;
//                                                }
//                                                lastmove = player;
//                                                grid[a][b] = player;
//                                                check(a, player);
//                                                if (playerwon ==1 || playerwon == 2){
//                                                    results.setText("Welcome to Ultimate Tic Tac Toe!!\n\n"+players[playerwon-1]+" Wins!!!");
//                                                }
//                                                else if (playerwon == 3){
//                                                    results.setText("Welcome to Ultimate Tic Tac Toe!!\n\nCats Game!!!");
//                                                }
//                                                else if (player == 1){
//                                                    player = 2;
//                                                    results.setText("Welcome to Ultimate Tic Tac Toe!!\n\n"+players[player-1]+"'s turn");
//                                                }
//                                                else if (player == 2){
//                                                    player = 1;
//                                                    results.setText("Welcome to Ultimate Tic Tac Toe!!\n\n"+players[player-1]+"'s turn");
//                                                }
//                                                repaint();
////                                            textPrint();
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//                if (mx>800 && mx<800+370 && my>575 && my<575+75){
//                    newpressed = true;
//                    repaint();
//                }
//                else if (mx>800 && mx<800+370 && my>575+95 && my<575+75+95){
//                    menupressed = true;
//                    repaint();
//                }
//            }
//            @Override
//            public void mouseReleased(MouseEvent me) {
//                if (newpressed){
//                    newpressed = false;
//                    repaint();
//                    if(playerwon == 0){
//                        int result = JOptionPane.showConfirmDialog (null, "Are you sure you want to reset the game?", "Select an option", JOptionPane.YES_NO_CANCEL_OPTION);
//                        if (result == JOptionPane.OK_OPTION || result == JOptionPane.YES_OPTION){
//                            player = 1;
//                            LargeBox = -1;
//                            lastmove = 2;
//                            results.setText("Welcome to Ultimate Tic Tac Toe!!\n\n"+players[0]+"'s turn");
//                            clear();
//                            repaint();
//                        }
//                    }
//                    else{
//                        clear();
//                        Main.w.Switch(Mode.m, MainMenu.mode);
//                        MainMenu.mode.run();
//                    }
//                }
//                else if (menupressed){
//                    menupressed = false;
//                    repaint();
//                    Main.w.remove(Mode.m);
//                    Main.w.add(Main.menu, BorderLayout.CENTER);
//                    Main.menu.setVisible(true);
//                }
//            }
//            @Override
//            public void mouseEntered(MouseEvent me) {            }
//            @Override
//            public void mouseExited(MouseEvent me) {                }
//            
//        });
//        addMouseMotionListener(new MouseMotionListener() {
//            
//            @Override
//            public void mouseDragged(MouseEvent me) {
//                
//            }
//            @Override
//            public void mouseMoved(MouseEvent me) {
//                int mx = me.getX();
//                int my = me.getY();
//                hover = HoverCoordinates(mx, my);
//                repaint();
//            }
//        });
//        addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent ke) {}
//            
//            @Override
//            public void keyPressed(KeyEvent ke) {
//                int keyCode = ke.getKeyCode();
//                if(keyCode == KeyEvent.VK_BACK_SPACE){
//                    newgame = false;
//                    Main.w.remove(Mode.m);
//                    Main.w.add(Main.menu, BorderLayout.CENTER);
//                    Main.menu.setVisible(true);
//                }
//                else if (keyCode == KeyEvent.VK_ADD && playerwon == 0){
//                    randomPlay();
//                    repaint();
//                }
//                else if(keyCode == KeyEvent.VK_BACK_QUOTE && playerwon != 0){
//                    clear();
//                    Main.menu.setVisible(false);
//                    Main.w.remove(Mode.m);
//                    Main.w.add(MainMenu.mode, BorderLayout.CENTER);
//                    MainMenu.mode.setVisible(true);
//                    MainMenu.mode.run();
//                }
//            }
//            @Override
//            public void keyReleased(KeyEvent ke) {
//                int keyCode = ke.getKeyCode();
//                
//            }
//            
//        });
//        // randomPlay();
////        while(true){
//        setFocusable(true);
//        requestFocusInWindow();
////                try {
////                    Thread.sleep(99);
////                } catch (InterruptedException ex) {
////                    Logger.getLogger(Multiplayer.class.getName()).log(Level.SEVERE, null, ex);
////                }
////        }
//    }
//    
//    public void clear(){
//        if(!newgame){
//            players[0] = "";
//            players[1] = "";
//        }
//        newgame = true;
//        playerwon = 0;
//        player = 1;
//        LargeBox = -1;
//        lastmove = 2;
//        for (int a = 0; a<9; a++){
//            for (int b = 0; b<9; b++){
//                grid[a][b] = 0;
//            }
//            LargeFilled[a] = 0;
//        }
//    }
//    
//    public void check(int a, int player){
//        newgame = false;
//        if((grid[a][0] ==player && grid[a][1] ==player && grid[a][2] ==player) || (grid[a][3] ==player && grid[a][4] ==player && grid[a][5] ==player) || (grid[a][6] ==player && grid[a][7] ==player && grid[a][8] ==player) ||
//                (grid[a][0] ==player && grid[a][3] ==player && grid[a][6] ==player) || (grid[a][1] ==player && grid[a][4] ==player && grid[a][7] ==player) || (grid[a][2] ==player && grid[a][5] ==player && grid[a][8] ==player) ||
//                (grid[a][0] ==player && grid[a][4] ==player && grid[a][8] ==player) || (grid[a][2] ==player && grid[a][4] ==player && grid[a][6] ==player)){
//            LargeFilled[a] = player;
//            if (a == LargeBox){
//                LargeBox = -1;
//            }
////            for (int b = 0;b< grid[a].length;b++){
////                if (grid[a][b] != 1 && grid[a][b] != 2){
////                    grid[a][b] = 3;
////                }
////            }
//        }
//        else if (grid[a][0] != 0 && grid[a][1] != 0 && grid[a][2] != 0 && grid[a][3] != 0 && grid[a][4] != 0 && grid[a][5] != 0 && grid[a][6] != 0 && grid[a][7] != 0 && grid[a][8] != 0 && LargeFilled[a] == 0){
//            LargeFilled[a] = 3;
//        }
//        checkWin(player);
//    }
//    
//    public void checkWin(int player){
//        if(LargeFilled[0] != 0 && LargeFilled[1] != 0 && LargeFilled[2] != 0 && LargeFilled[3] != 0 && LargeFilled[4] != 0 && LargeFilled[5] != 0 && LargeFilled[6] != 0 && LargeFilled[7] != 0 && LargeFilled[8] != 0 && playerwon == 0){
////            System.out.println("Cats Game");
//            results.setText("Welcome to Ultimate Tic Tac Toe!!\n\nCats Game!!!");
//            playerwon = 3;
//        }
//        else if((LargeFilled[0] ==player && LargeFilled[1] ==player && LargeFilled[2] ==player) || (LargeFilled[3] ==player && LargeFilled[4] ==player && LargeFilled[5] ==player) || (LargeFilled[6] ==player && LargeFilled[7] ==player && LargeFilled[8] ==player) ||
//                (LargeFilled[0] ==player && LargeFilled[3] ==player && LargeFilled[6] ==player) || (LargeFilled[1] ==player && LargeFilled[4] ==player && LargeFilled[7] ==player) || (LargeFilled[2] ==player && LargeFilled[5] ==player && LargeFilled[8] ==player) ||
//                (LargeFilled[0] ==player && LargeFilled[4] ==player && LargeFilled[8] ==player) || (LargeFilled[2] ==player && LargeFilled[4] ==player && LargeFilled[6] ==player)){
//            playerwon = player;
//            results.setText("Welcome to Ultimate Tic Tac Toe!!\n\n"+players[playerwon-1]+" Wins!!!");
//        }
//        
//    }
//    
//    public void randomPlay (){
////                    System.out.println("Randomizing");
//        while(playerwon == 0){
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
////                            repaint();
////                            Thread.sleep(100);
////                        } catch (InterruptedException ex) {
////                            Logger.getLogger(Multiplayer.class.getName()).log(Level.SEVERE, null, ex);
////                        }
//                        break;
//                    }
//                }
//            }
////                setFocusable(true);
////                requestFocusInWindow();
////                repaint();
//////                System.out.println("repainted");
//        }
//    }
//    
////    public void textPrint(){
////        for (int d = 0; d<3; d++){
////            for (int c = 0; c<3; c++){
////                for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
////                    for (int b = ranges[c][0]; b<=ranges[c][1];b++){
////                        System.out.print(grid[a][b]+" ");
////                    }
////                }
////                System.out.println("");
////            }
////        }
////        System.out.println("\n\n");
////    }
//    
//    
//    
//    @Override
//    public void paintComponent(Graphics g){
//        super.paintComponent(g);
//        Graphics2D g2 = (Graphics2D) g;
//        g.setColor(Color.BLACK);
//        DrawGrid(g2);
//        DrawValues(g2);
//        DrawLargeBox(g2);
//        g.setColor(Color.BLACK);
//        g2.setStroke(new BasicStroke(1));
//        g.drawRect (1155, 10, 30, 45);
//        g2.setStroke(new BasicStroke(3));
//        if (newpressed){
//            g.setColor(Color.GRAY);
//            g.fillRect (800+2, 575+2, 370-2, 75-2);
//            g.setColor(Color.BLACK);
//        }
//        g.drawRect (800, 575, 370, 75);
//        if (menupressed){
//            g.setColor(Color.GRAY);
//            g.fillRect (800+2, 575+2+75+20, 370-2, 75-2);
//            g.setColor(Color.BLACK);
//        }
//        g.drawRect (800, 575+75+20, 370, 75);
//        g.setFont(new Font("Calibri", Font.PLAIN, 45));
//        if(playerwon == 0){
//            g.drawString("Reset", x+893, y+605);
//        }
//        else {g.drawString("Play Again", x+853, y+605);}
//        g.drawString("Back to Main Menu", x+775, y+685+15);
//        g.setFont(new Font("Calibri", Font.PLAIN, 50));
//        g.drawString("?", 1160, 50);
//        DrawHover(g2);
//    }
//    public void DrawGrid(Graphics2D g){
//        g.setStroke(new BasicStroke(1));
//        for (int a = 0; a<3 ; a++){
//            for (int b = 0; b<3 ; b++){
//                for (int c = 1; c<=2 ; c++){
//                    g.drawLine(x+(width/3/space)+(a*width/3), y+(b*width/3)+(width/3/space)+(c*proportion), x+(width/3)-(width/3/space)+(a*width/3), y+(b*width/3)+(width/3/space)+(c*proportion));
//                    g.drawLine(x+(b*width/3)+(width/3/space)+(c*proportion), y+(width/3/space)+(a*width/3), x+(b*width/3)+(width/3/space)+(c*proportion), y+(width/3)-(width/3/space)+(a*width/3));
//                    if (a == 2 && b ==2){
//                        g.setStroke(new BasicStroke(3));
//                        g.drawLine(x, (c*width/3)+y, x+width,(c*width/3)+y);
//                        g.drawLine( (c*width/3)+x,y,  (c*width/3)+x, y+width);
//                        g.setStroke(new BasicStroke(1));
//                    }
//                }
//            }
//        }
//    }
//    
//    public void DrawValues(Graphics2D g){
//        for (int d = 0; d<3; d++){
//            for (int c = 0; c<3; c++){
//                for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
//                    for (int b = ranges[c][0]; b<=ranges[c][1];b++){
////                        g.setFont(new Font("Calibri", Font.PLAIN, 50));
////                        g.drawString (a+""+b,gridX[3*(a%3)+b%3]+15, gridY[3*(a/3)+b/3]+50);
////                        System.out.println(a+""+b);
//                        if(playerwon == 1 || playerwon == 2){
//                            if (playerwon == 1){g.setColor(new Color(0, 255, 0, 45));}
//                            else if (playerwon == 2){g.setColor(new Color(0, 128, 225, 45));}
//                            g.fillRect (gridX[3*(a%3)+b%3], gridY[3*(a/3)+b/3], 73, 73);
//                        }
//                        if (grid[a][b] == 1 || grid[a][b] == 2){
//                            String string = "";
//                            int adjustX = 15, adjustY = 59;
//                            if (grid[a][b] == 1){
//                                if (LargeFilled[a]!=0 || playerwon !=0){
//                                    g.setColor(new Color(0, 255, 0, 75));
//                                }
//                                else{
//                                    g.setColor(Color.GREEN);
//                                }
//                                string = "X";
//                            }
//                            else if (grid[a][b] == 2){
//                                if (LargeFilled[a]!=0 || playerwon !=0){
//                                    g.setColor(new Color(0, 128, 225, 75));
//                                }
//                                else{
//                                    g.setColor(Color.BLUE);
//                                }
//                                string = "O";      adjustX -= 4;
//                            }
//                            g.setFont(new Font("Calibri", Font.BOLD, 80));
//                            g.drawString (string,gridX[3*(a%3)+b%3]+adjustX, gridY[3*(a/3)+b/3]+adjustY);
////                            g.fillRect (gridX[3*(a%3)+b%3], gridY[3*(a/3)+b/3], 73, 73);
//                        }
//                        else if((LargeBox == a || LargeBox == -1) && (LargeFilled[a] ==0) && playerwon == 0){
//                            if (lastmove == 2){g.setColor(new Color(0, 255, 0, 45));}
//                            else if (lastmove == 1){g.setColor(new Color(0, 128, 225, 45));}
//                            g.fillRect (gridX[3*(a%3)+b%3], gridY[3*(a/3)+b/3], 73, 73);
//                        }
//                    }
//                }
//            }
//        }
//    }
//    public void DrawLargeBox(Graphics2D g){
//        for (int a = 0; a<divisions; a++){
//            if (LargeFilled[a] == 1 || LargeFilled[a] == 2){
//                String string = "";
//                int adjustX = 32, adjustY = 200;
//                if (LargeFilled[a]  == 1){
////                    if (playerwon == 2){
////                        g.setColor(new Color(0, 255, 0, 99));
////                    }
////                    else{
//                    g.setColor(Color.GREEN);
////                    }
//                    string = "X";
//                }
//                else if (LargeFilled[a]  == 2){
////                    if (playerwon == 1){
////                        g.setColor(new Color(0, 0, 225, 99));
////                    }
////                    else{
//                    g.setColor(Color.BLUE);
////                    }
//                    string = "O";
//                    adjustX -= 19;
//                }
//                g.setFont(new Font("Calibri", Font.BOLD, 280));
//                g.drawString (string,gridX[3*(a%3)]+adjustX, gridY[3*(a/3)]+adjustY);
//            }
//        }
//    }
//    public void DrawHover(Graphics2D g){
////        System.out.println("hovering");
//        if (hover && grid[hoverA][hoverB] == 0 && (LargeBox == hoverA || LargeBox == -1) && LargeFilled[hoverA] == 0){
//            String string = "";
//            int adjustX = 15, adjustY = 59;
//            if (player == 1){g.setColor(Color.GREEN);       string = "X";       }
//            else if (player == 2){g.setColor(Color.BLUE);       string = "O";      adjustX -= 4; }
//            g.setFont(new Font("Calibri", Font.BOLD, 80));
//            g.drawString (string,hoverX+adjustX, hoverY+adjustY);
//        }
//    }
//    
//    public boolean HoverCoordinates(int mx, int my){
//        for (int d = 0; d<3; d++){
//            for (int c = 0; c<3; c++){
//                for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
//                    for (int b = ranges[c][0]; b<=ranges[c][1];b++){
//                        if(mx>gridX[3*(a%3)+b%3] && mx<(gridX[3*(a%3)+b%3]+proportion) && my>gridY[3*(a/3)+b/3] && my<(gridY[3*(a/3)+b/3]+proportion) ){
//                            //&& hoverX != gridX[3*(a%3)+b%3] && hoverY != gridY[3*(a/3)+b/3]
//                            hoverX = gridX[3*(a%3)+b%3];
//                            hoverY = gridY[3*(a/3)+b/3];
//                            hoverA = a;
//                            hoverB = b;
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }
    
}
