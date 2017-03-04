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
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Mode extends JPanel{
    
    static int x = p.convertX(230), y = p.convertY(250),width = p.convertX(700), height = p.convertY(120);
    static boolean singlepressed = false, multipressed = false, input = false, menupressed = false, singleplayer = false;
//    static Multiplayer m = new Multiplayer();
    static Board board = new Board();
    
    public void run(){
        Main.w.setResizable(false);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                
            }
            
            @Override
            public void mousePressed(MouseEvent me) {
                int mx = me.getX();
                int my = me.getY();
                if (mx>x && mx<(x+width) && my>y && my<(y+height)){
                    singlepressed = true;
                    repaint();
                }
                else if(mx>x && mx<(x+width) && my>(y+height+p.convertY(20)) && my<(y+2*height+p.convertY(20))){
                    multipressed = true;
                    repaint();
                }
                else if(mx>(x+width-p.convertX(130)) && mx<(x+2*width-p.convertX(480)) && my>(y+2*height+p.convertY(160)) && my<(y+p.convertY(130)+3*height)){
                    menupressed = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
                if(singlepressed || multipressed){
                    if (singlepressed){
                        singleplayer = true;
                        board.game.computers.reset();
//                        progress p = new progress();
//                        p.setVisible(true);
                    }
                    else{
                        singleplayer = false;
                    }
                    multipressed = false;
                    singlepressed = false;
                    repaint();
                    Main.w.Switch(MainMenu.mode, board);
                    board.run();
                }
                else if(menupressed){
                    menupressed = false;
                    repaint();
                    Main.w.remove(MainMenu.mode);
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
                if(keyCode == KeyEvent.VK_BACK_QUOTE){
                    singleplayer = true;
                    MainMenu.mode.setVisible(false);
                    Main.w.remove(MainMenu.mode);
                    Main.w.add(board, BorderLayout.CENTER);
                    board.setVisible(true);
                    board.run();
                }
                else if (keyCode == KeyEvent.VK_BACK_SPACE){
                    Main.w.Switch(MainMenu.mode, Main.menu);
                    Main.menu.run();
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
        Graphics2D g2 = (Graphics2D) g;
        if(!input){
            g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(70)));
            g.drawString("Select a Mode:", p.convertX(50), p.convertY(120));
            g2.setStroke(new BasicStroke(p.getFontSize(3)));
            g.setColor(Color.BLACK);
            if (singlepressed){
                g.setColor(Color.GRAY);
                g.fillRect (x+p.convertX(2), y+p.convertY(2), width-p.convertX(2), height-p.convertY(2));
                g.setColor(Color.BLACK);
            }
            g.drawRect (x, y, width, height);
            if (multipressed){
                g.setColor(Color.GRAY);
                g.fillRect (x+p.convertX(2), y+p.convertY(2)+height+p.convertY(50), width-p.convertX(2), height-p.convertY(2));
                g.setColor(Color.BLACK);
            }
            if (menupressed){
                g.setColor(Color.GRAY);
                g.fillRect (x+width-p.convertX(128), y+p.convertY(165)+2*height,  width-p.convertX(352), height-p.convertY(36));
                g.setColor(Color.BLACK);
            }
            g.drawRect (x+width-p.convertX(130), y+p.convertY(165)+2*height, width-p.convertX(350), height-p.convertY(35));
            g.drawRect (x, y+height+p.convertY(50), width, height);
            g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(70)));
            g.drawString("Single Player", x+p.convertX(188), y+p.convertY(80));
            g.drawString("Multiplayer", x+p.convertX(198), y+height+p.convertY(130));
            g.setFont(new Font("Calibri", Font.PLAIN, p.getFontSize(40)));
            g.drawString("Back to Main Menu", x+width-p.convertX(110), y+2*height+p.convertY(215));
        }
    }
    
    private class progress extends JFrame{
        public progress(){
            super("Section in Progress");
            setSize(515, 150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setResizable(false);
            Text text = new Text();
            add(text);
        }
    }
    private class Text extends JPanel{
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setFont(new Font("Calibri", Font.BOLD, 25));
            g.drawString("This section is still under development.", 50, 50);
            g.drawString("Try again another time", 50, 100);
        }
        
    }
    
}
