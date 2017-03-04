package Main;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Board extends JPanel{
    
    static JTextArea results = new JTextArea();
    JTextField player1;
    JTextField player2;
    final public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
    static int player = 1, LargeBox = -1, lastmove = 2, playerwon = 0;
    static int x = p.convertX(35), y = p.convertY(20), width = p.convertX(720), space = p.convertX(20), divisions = 9;
    static int proportion = (width/3 - 2*(width/3/space))/3; //72
    static int[] gridX = { x+(width/3/space), x+(width/3/space)+proportion, x+(width/3/space)+proportion+proportion, x+(width/3)+(width/3/space),
        x+(width/3)+(width/3/space)+proportion, x+(width/3)+(width/3/space)+proportion+proportion, x+(width/3)+(width/3)+(width/3/space), x+(width/3)+(width/3)+(width/3/space)+proportion,
        x+(width/3)+(width/3)+(width/3/space)+proportion+proportion };
    static int[] gridY = { y+(width/3/space), y+(width/3/space)+proportion, y+(width/3/space)+proportion+proportion, y+(width/3)+(width/3/space),
        y+(width/3)+(width/3/space)+proportion, y+(width/3)+(width/3/space)+proportion+proportion, y+(width/3)+(width/3)+(width/3/space), y+(width/3)+(width/3)+(width/3/space)+proportion,
        y+(width/3)+(width/3)+(width/3/space)+proportion+proportion};
    final static int[][] ranges = {{0,2},{3,5},{6,8}};
    static long start, end;
    static boolean hover = false;
    static int hoverX = 0, hoverY = 0, hoverA = 0, hoverB = 0, mx = 0, my = 0;
    String[] players = {"",""};
    static boolean newpressed = false, menupressed = false, newgame = true, running = false, added = false;
    
    static int[][] grid = new int[divisions][divisions];
    
    public static Game game = new Game();
    
    public Board(){
        super(true);
    }
    
    public void run() {
        update();
        setLayout(null);
        add(results);       results.setLocation(x+width+p.convertX(50), p.convertY(300));      results.setSize(p.convertX(350), p.convertY(150));      results.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(50)));       results.setBackground(defaultcolor);
        results.setLineWrap(true);   results.setWrapStyleWord(true);     results.setEditable(false);        results.setVisible(true);
        results.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!added){
                    added = true;
                    if (!Mode.singleplayer){
                        player1 = new JTextField(game.players[0]);
                        player2 = new JTextField(game.players[1]);
                        Object[] message = {
                            "Player 1:", player1,
                            "Player 2:", player2
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Enter your name(s)", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION){
                            game.players[0]=player1.getText();
                            game.players[1]=player2.getText();
//                            System.out.println(player1.getText());
//                            System.out.println(player2.getText());
                        }
                    }
                    else{
                        JTextField player1 = new JTextField(game.players[0]);
                        Object[] message = {
                            "Player 1:", player1
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Enter your name", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION){
                            game.players[0]=player1.getText();
                            System.out.println(player1.getText());
                        }
                    }
                    game.text = (game.players[game.player-1]+"'s turn");
                    update();
                }
                else{
                    added = false;
                }
            }
            @Override
            public void focusLost(FocusEvent e) {                   }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent me) {
                
            }
            @Override
            public void mouseMoved(MouseEvent me) {
                if (!Mode.singleplayer || (Mode.singleplayer && player == 1)){
                    int mx = me.getX();
                    int my = me.getY();
                    hover = HoverCoordinates(mx, my);
                    repaint();
                }
            }
        });
        game.run();
    }
    public void update(){
        mx = game.mx;
        my = game.my;
        player = game.player;
        LargeBox = game.LargeBox;
        lastmove = game.lastmove;
        playerwon = game.playerwon;
        newgame = game.newgame;
        grid = game.path.grid;
        if (Mode.singleplayer == true && player == 2 && playerwon == 0){
            results.setText("Computer is thinking...");
        }
        else{
            results.setText(game.text);
        }
        repaint();
    }
    public void undo(){
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        adjust();
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        g.setFont(new Font("Calibri", Font.PLAIN, (int)(Main.w.getWidth()*0.058333333)));
        g.drawString("Ultimate", x+width+(int)(Main.w.getWidth()*0.070833333), (int)(Main.w.getHeight()*0.0875));
        g.drawString("Tic Tac Toe", x+width+(int)(Main.w.getWidth()*0.04166666666), (int)(Main.w.getHeight()*0.1625));
        g.setFont(new Font("Calibri", Font.PLAIN, (int)(Main.w.getWidth()*0.025)));
        g.drawString("by Antonio Kim", x+width+(int)(Main.w.getWidth()*0.1), (int)(Main.w.getHeight()*0.2125));
        DrawGrid(g2);
        DrawValues(g2);
        DrawLargeBox(g2);
        g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(p.getFontSize(3)));
        addButton(g2, (int)(Main.w.getWidth()*0.666666666666), (int)(Main.w.getHeight()*0.6), (int)(Main.w.getWidth()*0.308333333333), (int)(Main.w.getHeight()*0.09375), "Undo", (int)(Main.w.getWidth()*0.0375));
        if(playerwon == 0){
            addButton(g2, (int)(Main.w.getWidth()*0.666666666666), (int)(Main.w.getHeight()*0.71875), (int)(Main.w.getWidth()*0.308333333333), (int)(Main.w.getHeight()*0.09375), "Reset",  (int)(Main.w.getWidth()*0.0375));
        }
        else{
            addButton(g2, (int)(Main.w.getWidth()*0.666666666666), (int)(Main.w.getHeight()*0.71875), (int)(Main.w.getWidth()*0.308333333333), (int)(Main.w.getHeight()*0.09375), "Play Again",  (int)(Main.w.getWidth()*0.0375));
        }
        addButton(g2, (int)(Main.w.getWidth()*0.666666666666), (int)(Main.w.getHeight()*0.8375), (int)(Main.w.getWidth()*0.308333333333), (int)(Main.w.getHeight()*0.09375), "Back to Main Menu", (int)(Main.w.getWidth()*0.0375));
        addButton(g2, (int)(Main.w.getWidth()*0.9625), (int)(Main.w.getHeight()*0.0125), (int)(Main.w.getWidth()*0.025), (int)(Main.w.getHeight()*0.05625), "?", (int)(Main.w.getWidth()*0.04166666666666666), (int)(Main.w.getWidth()*0.966666666666666), (int)(Main.w.getHeight()*0.0625));
        DrawHover(g2);
    }
    public void adjust(){
        x = (int)(Main.w.getWidth()*(35.0/1210));
        y = (int)(Main.w.getHeight()*(20/810));
        width = (int)(Main.w.getWidth()*(720.0/1210));
        space = (int)(Main.w.getWidth()*(20.0/1210));
        proportion = (Mode.board.width/3 - 2*(Mode.board.width/3/Mode.board.space))/3;
        gridX[0] = x+(width/3/space);
        gridX[1] = x+(width/3/space)+proportion;
        gridX[2] = x+(width/3/space)+proportion+proportion;
        gridX[3] = x+(width/3)+(width/3/space);
        gridX[4] = x+(width/3)+(width/3/space)+proportion;
        gridX[5] = x+(width/3)+(width/3/space)+proportion+proportion;
        gridX[6] = x+(width/3)+(width/3)+(width/3/space);
        gridX[7] = x+(width/3)+(width/3)+(width/3/space)+proportion;
        gridX[8] = x+(width/3)+(width/3)+(width/3/space)+proportion+proportion;
        gridY[0] = y+(width/3/space);
        gridY[1] = y+(width/3/space)+proportion;
        gridY[2] = y+(width/3/space)+proportion+proportion;
        gridY[3] = y+(width/3)+(width/3/space);
        gridY[4] = y+(width/3)+(width/3/space)+proportion;
        gridY[5] = y+(width/3)+(width/3/space)+proportion+proportion;
        gridY[6] = y+(width/3)+(width/3)+(width/3/space);
        gridY[7] = y+(width/3)+(width/3)+(width/3/space)+proportion;
        gridY[8] = y+(width/3)+(width/3)+(width/3/space)+proportion+proportion;
        results.setLocation(x+width+(int)(Main.w.getWidth()*(50.0/1210)), (int)(Main.w.getWidth()*(300.0/1210)));
        results.setSize((int)(Main.w.getWidth()*(350.0/1210)), (int)(Main.w.getWidth()*(150.0/1210)));
        results.setFont(new Font("Calibri", Font.PLAIN, (int)(Main.w.getWidth()*(50.0/1210))));
    }
    public void addButton(Graphics2D g, int a, int b, int c, int d, String text, int fontsize){
        g.setStroke(new BasicStroke(p.getFontSize(3)));
        g.drawRect (a, b, c, d);
        if (mx >= a && mx <= a+c && my>=b && my<=b+d) {
            g.setColor(Color.GRAY);
            g.fillRect (a+p.convertX(2), b+p.convertY(2), c-p.convertX(3), d-p.convertY(3));
            g.setColor(Color.BLACK);
            repaint();
            switch(text){
                case "Undo":
                    game.undo = true;
                    break;
                case "Reset":
                    game.newpressed = true;
                    break;
                case "Play Again":
                    game.newpressed = true;
                    break;
                case "Back to Main Menu":
                    game.menupressed = true;
                    break;
                case "?":
                    game.helppressed = true;
                    break;
            }
        }
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        int width = (int)(font.getStringBounds(text, frc).getWidth());
        int height = (int)(font.getStringBounds(text, frc).getHeight());
        g.setFont(font);
        g.drawString(text, a+((c-width)/2), b+d-((d-height)));
//        System.out.println("a: "+a+"\n"+(((c)-width)/2)+a);
    }
    public void addButton(Graphics2D g, int a, int b, int c, int d, String text, int fontsize, int e, int f){
        g.setStroke(new BasicStroke(p.getFontSize(3)));
        g.drawRect (a, b, c, d);
        if (mx >= a && mx <= a+c && my>=b && my<=b+d) {
            g.setColor(Color.GRAY);
            g.fillRect (a+p.convertX(2), b+p.convertY(2), c-p.convertX(3), d-p.convertY(3));
            g.setColor(Color.BLACK);
            repaint();
            switch(text){
                case "Undo":
                    game.undo = true;
                    break;
                case "Reset":
                    game.newpressed = true;
                    break;
                case "Play Again":
                    game.newpressed = true;
                    break;
                case "Back to Main Menu":
                    game.menupressed = true;
                    break;
            }
        }
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        g.setFont(font);
        g.drawString(text, e, f);
//        System.out.println("a: "+a+"\n"+(((c)-width)/2)+a);
    }
    public void DrawGrid(Graphics2D g){
        g.setStroke(new BasicStroke(p.getFontSize(1)));
        for (int a = 0; a<3 ; a++){
            for (int b = 0; b<3 ; b++){
                for (int c = 1; c<=2 ; c++){
                    g.drawLine(x+(width/3/space)+(a*width/3), y+(b*width/3)+(width/3/space)+(c*proportion), x+(width/3)-(width/3/space)+(a*width/3), y+(b*width/3)+(width/3/space)+(c*proportion));
                    g.drawLine(x+(b*width/3)+(width/3/space)+(c*proportion), y+(width/3/space)+(a*width/3), x+(b*width/3)+(width/3/space)+(c*proportion), y+(width/3)-(width/3/space)+(a*width/3));
                    if (a == 2 && b ==2){
                        g.setStroke(new BasicStroke(p.getFontSize(3)));
                        g.drawLine(x, (c*width/3)+y, x+width,(c*width/3)+y);
                        g.drawLine( (c*width/3)+x,y,  (c*width/3)+x, y+width);
                        g.setStroke(new BasicStroke(p.getFontSize(1)));
                    }
                }
            }
        }
    }
    
    public void DrawValues(Graphics2D g){
        for (int d = 0; d<3; d++){
            for (int c = 0; c<3; c++){
                for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
                    for (int b = ranges[c][0]; b<=ranges[c][1];b++){
                        if(playerwon == 1 || playerwon == 2){
                            if (playerwon == 1){g.setColor(new Color(0, 255, 0, 45));}
                            else if (playerwon == 2){g.setColor(new Color(0, 128, 225, 45));}
                            g.fillRect (gridX[3*(a%3)+b%3], gridY[3*(a/3)+b/3], (int)(Main.w.getHeight()*(73.0/800)), (int)(Main.w.getHeight()*(73.0/800)));
                        }
                        if (grid[a][b] == 1 || grid[a][b] == 2){
                            String string = "";
                            int adjustX = (int)(Main.w.getWidth()*(15.0/1210)), adjustY = (int)(Main.w.getHeight()*(59.0/810));
                            if (grid[a][b] == 1){
                                if (game.path.Largefilled[a]!=0 || playerwon !=0){
                                    g.setColor(new Color(0, 255, 0, 75));
                                }
                                else{
                                    g.setColor(Color.GREEN);
                                }
                                string = "X";
                            }
                            else if (grid[a][b] == 2){
                                if (game.path.Largefilled[a]!=0 || playerwon !=0){
                                    g.setColor(new Color(0, 128, 225, 75));
                                }
                                else{
                                    g.setColor(Color.BLUE);
                                }
                                string = "O";      adjustX -= (int)(Main.w.getWidth()*(4.0/1210));
                            }
                            g.setFont(new Font("Calibri", Font.BOLD, (int)(Main.w.getWidth()*0.06666666666666666)));
                            g.drawString (string,gridX[3*(a%3)+b%3]+adjustX, gridY[3*(a/3)+b/3]+adjustY);
                        }
                        else if((LargeBox == a || LargeBox == -1) && (game.path.Largefilled[a] ==0) && playerwon == 0){ //drawing the highlighted move options
                            if (lastmove == 2){g.setColor(new Color(0, 255, 0, 45));}
                            else if (lastmove == 1){g.setColor(new Color(0, 128, 225, 45));}
                            g.fillRect (gridX[3*(a%3)+b%3], gridY[3*(a/3)+b/3], p.convertX(76), p.convertY(76));
                        }
                    }
                }
            }
        }
    }
    public void DrawLargeBox(Graphics2D g){
        for (int a = 0; a<divisions; a++){
            if (game.path.Largefilled[a] == 1 || game.path.Largefilled[a] == 2){
                String string = "";
                int adjustX = (int)(Main.w.getWidth()*(32.0/1210)), adjustY = (int)(Main.w.getHeight()*(200.0/810));
                if (game.path.Largefilled[a]  == 1){
                    g.setColor(Color.GREEN);
                    string = "X";
                }
                else if (game.path.Largefilled[a]  == 2){
                    g.setColor(Color.BLUE);
                    string = "O";
                    adjustX -= (int)(Main.w.getWidth()*(19.0/1210));
                }
                g.setFont(new Font("Calibri", Font.BOLD, (int)(Main.w.getWidth()*0.2333333333333333333)));
                g.drawString (string, gridX[3*(a%3)]+adjustX, gridY[3*(a/3)]+adjustY);
            }
        }
    }
    public void DrawHover(Graphics2D g){
        if (hover && grid[hoverA][hoverB] == 0 && (LargeBox == hoverA || LargeBox == -1) && game.path.Largefilled[hoverA] == 0 && playerwon == 0){
            String string = "";
            int adjustX = (int)(Main.w.getWidth()*(15.0/1210)), adjustY = (int)(Main.w.getHeight()*(59.0/810));
            if (player == 1){g.setColor(Color.GREEN);       string = "X";       }
            else if (player == 2){g.setColor(Color.BLUE);       string = "O";      adjustX -= (int)(Main.w.getWidth()*(4.0/1210)); }
            g.setFont(new Font("Calibri", Font.BOLD, (int)(Main.w.getWidth()*0.06666666666666666)));
            g.drawString (string,hoverX+adjustX, hoverY+adjustY);
        }
    }
    
    public boolean HoverCoordinates(int mx, int my){
        for (int d = 0; d<3; d++){
            for (int c = 0; c<3; c++){
                for (int a = ranges[d][0]; a<=ranges[d][1]; a++){
                    for (int b = ranges[c][0]; b<=ranges[c][1];b++){
                        if(mx>gridX[3*(a%3)+b%3] && mx<(gridX[3*(a%3)+b%3]+proportion) && my>gridY[3*(a/3)+b/3] && my<(gridY[3*(a/3)+b/3]+proportion) ){
                            //&& hoverX != gridX[3*(a%3)+b%3] && hoverY != gridY[3*(a/3)+b/3]
                            hoverX = gridX[3*(a%3)+b%3];
                            hoverY = gridY[3*(a/3)+b/3];
                            hoverA = a;
                            hoverB = b;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
