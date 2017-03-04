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


public class MainMenu extends JPanel {
    
//    static Game g = new Game();
    static Instructions i = new Instructions();
    static Mode mode = new Mode();
    int x = Main.w.getWidth()/6, y = Main.w.getHeight()/2, width = (int)(Main.w.getWidth()*2.0/3.0), height = (int)(Main.w.getHeight()*3.0/20.0);
    boolean playpressed = false;
    boolean instpressed = false;
    
    public void run(){
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {            }
            @Override
            public void mousePressed(MouseEvent me) {
                int mx = me.getX();
                int my = me.getY();
                if (mx>x && mx<(x+width) && my>y && my<(y+height)){
                    playpressed = true;
                    repaint();
                }
                else if(mx>x && mx<(x+width) && my>(y+height+20) && my<(y+height+20+height)){
                    instpressed = true;
                    repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent me) {
                if (playpressed){
                    playpressed = false;
                    repaint();
                    if (Board.game.newgame){
                        Main.w.Switch(Main.menu, mode);
                        mode.run();
                    }
                    else if(!Board.game.newgame){
                        Main.w.Switch(Main.menu, Mode.board);
                    }
                }
                else if(instpressed){
                    instpressed = false;
                    repaint();
                    Main.w.Switch(Main.menu, i);
                    i.run();
                }
            }
            @Override
            public void mouseEntered(MouseEvent me) {            }
            @Override
            public void mouseExited(MouseEvent me) {            }
        });
        addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent ke) {
                
            }
            
            @Override
            public void keyPressed(KeyEvent ke) {
                int keyCode = ke.getKeyCode();
                if(keyCode == KeyEvent.VK_BACK_QUOTE){
                    if (Main.developersmode){
                        Main.developersmode = false;
                    }
                    else{
                        Main.developersmode = true;
                    }
                    System.out.println(Main.developersmode);
                }
                else if(keyCode == KeyEvent.VK_ENTER){
                    if (Board.game.newgame){
                        Main.w.Switch(Main.menu, MainMenu.mode);
                        MainMenu.mode.run();
                    }
                    else if(!Board.game.newgame){
                        Main.w.Switch(Main.menu, Mode.board);
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
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
//        System.out.println("running");
        Graphics2D g2 = (Graphics2D) g;
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(90)));
        String str = "Ultimate Tic Tac Toe";
        int stringWidth = p.stringWidth(str, g.getFont());
        g.drawString (str, Main.w.getWidth()/2-stringWidth/2, Main.w.getHeight()/8);
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(30)));
        g.drawString ("by Antonio Kim", (int)(Main.w.getWidth()/2-stringWidth/2.1), (int)(Main.w.getHeight()*7.0/40.0));
        g2.setStroke(new BasicStroke(p.getFontSize(3)));
        g.setColor(Color.BLACK);
        if (playpressed){
            g.setColor(Color.GRAY);
            g.fillRect (x+2, y+2, width-2, height-2);
            g.setColor(Color.BLACK);
        }
        g.drawRect (x, y, width, height);
        if (instpressed){
            g.setColor(Color.GRAY);
            g.fillRect (x+2, y+2+height+20, width-2, height-2);
            g.setColor(Color.BLACK);
        }
        g.drawRect (x, y+height+20, width, height);
        g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(70)));
        str = "Play Game";
        stringWidth = p.stringWidth(str, g.getFont());
        g.drawString(str, Main.w.getWidth()/2-stringWidth/2, y+Main.w.getHeight()/10);
        str = "Instructions";
        stringWidth = p.stringWidth(str, g.getFont());
        g.drawString("Instructions", Main.w.getWidth()/2-stringWidth/2, y+height+Main.w.getHeight()/9);
    }
}
