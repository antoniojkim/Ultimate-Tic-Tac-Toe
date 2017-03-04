package Main;


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
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;


public class Instructions extends JPanel{
    
    JTextArea inst = new JTextArea();
    Color defaultcolor = UIManager.getColor ( "Panel.background" );
    int x = p.convertX(120), y = p.convertY(630), width = p.convertX(420), height = p.convertY(90);
    boolean gamepressed = false, menupressed = false;
    
    public void run(){
        Main.w.setResizable(false);
        setLayout(null);
        add(inst);
        inst.setLocation(p.convertX(60), p.convertY(150));
        inst.setSize(p.convertX(1100), p.convertY(450));
        inst.setBackground(defaultcolor);
        inst.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(30)));
        inst.setEditable(false);
        inst.setWrapStyleWord(true);
        inst.setLineWrap(true);
//            inst.setBorder(new LineBorder(Color.GRAY, 1));
        inst.setText("Similar to the original Tic Tac Toe game, the objective of this game is to complete a vertical, horizontal or diagonal row. "
                + "The twist is that in each of the larger grids, exists a smaller grid that you must win in order to take that square and claim it to be yours. "
                + "Once you have claimed a vertical, horizontal, or diagonal row, you win the game.\n\n"
                + "Like the original game, Ultimate Tic Tac Toe is played with the two players alternating turns. The twist is that the player making the move must make the move "
                + "in the large square that mirrors the other players previous move. To avoid confusion the square that the player is allowed to make a move in will be highlighted. "
                + "If it happens that the square mirroring the other player's previous move is already claimed or unable to be played in, the player making the move can make their move "
                + "in any open position left. Again, all available moves will be highlighted for the player that is making his/her move to see.");
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {                }
            
            @Override
            public void mousePressed(MouseEvent me) {
                int mx = me.getX();
                int my = me.getY();
                if (mx>x && mx<(x+width) && my>y && my<(y+height)){
                    gamepressed = true;
                    repaint();
                }
                else if (mx>(x+width+p.convertX(100)) && mx<(x+2*width+p.convertX(100)) && my>y && my<(y+height)){
                    menupressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
                if(gamepressed){
                    gamepressed = false;
                    repaint();
                    if (Board.game.newgame){
                        Main.w.Switch(MainMenu.i, MainMenu.mode);
                        MainMenu.mode.run();
                    }
                    else if(!Board.game.newgame){
                        Main.w.Switch(MainMenu.i, Mode.board);
                    }
                }
                else if(menupressed){
                    menupressed = false;
                    repaint();
                    Main.w.remove(MainMenu.i);
                    Main.w.add(Main.menu, BorderLayout.CENTER);
                    Main.menu.setVisible(true);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent me) {
            }
            
            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }
            @Override
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                if(keyCode == KeyEvent.VK_BACK_SPACE){
                    Main.w.Switch(MainMenu.i, Main.menu);
                    Main.menu.run();
                }
                else if(keyCode == KeyEvent.VK_ENTER){
                    gamepressed = false;
                    repaint();
                    if (Board.game.newgame){
                        Main.w.Switch(MainMenu.i, MainMenu.mode);
                        MainMenu.mode.run();
                    }
                    else if(!Board.game.newgame){
                        Main.w.Switch(MainMenu.i, Mode.board);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(70)));
        g.drawString("Instructions:", p.convertX(50), p.convertY(100));
        g2.setStroke(new BasicStroke(p.getFontSize(3)));
        g.setColor(Color.BLACK);
        if (gamepressed){
            g.setColor(Color.GRAY);
            g.fillRect (x+p.convertX(2), y+p.convertY(2), width-p.convertX(2), height-p.convertY(2));
            g.setColor(Color.BLACK);
        }
        g.drawRect (x, y, width, height);
        if (menupressed){
            g.setColor(Color.GRAY);
            g.fillRect (x+width+p.convertX(102), y+p.convertY(2), width-p.convertX(2), height-p.convertY(2));
            g.setColor(Color.BLACK);
        }
        g.drawRect (x+width+p.convertX(100), y, width, height);
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(50)));
        g.drawString("Play Game", x+p.convertX(98), y+p.convertY(65));
        g.drawString("Back to Main Menu", x+p.convertX(118)+width, y+p.convertY(65));
    }
    
}
