package Main;


import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame{
    
    int width = (int)(p.getScreenWidth()*3.0/4.0), height = (int)(p.getScreenHeight()*8.0/9.0);
    
    public Window(){
        super("Ultimate Tic Tac Toe v2.62 by Antonio Kim");
        setLayout(new BorderLayout());
        
        setSize(width, height);   //used to be 1200, 800
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                try{
//                    Board.game.computers.save();
//                }catch(NullPointerException e){}
                System.exit(0);
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                if(height!=Main.w.getHeight()){
                    setSize((int)(Main.w.getHeight()*1.5), Main.w.getHeight());
                }
                else if (width!=Main.w.getWidth()){
                    setSize(Main.w.getWidth(), (int)(Main.w.getWidth()*0.666666666666666666));
                }
                width = Main.w.getWidth();
                height = Main.w.getHeight();
                setLocationRelativeTo(null);
                Mode.board.repaint();
            }
            
            @Override
            public void componentMoved(ComponentEvent ce) {            }
            
            @Override
            public void componentShown(ComponentEvent ce) {            }
            
            @Override
            public void componentHidden(ComponentEvent ce) {            }
        });
        setResizable(false);
    }
    
    public void Switch(JPanel panel1, JPanel panel2){
        if (panel2.equals(Mode.board)){
            Main.w.setResizable(true);
            Main.w.setSize((int)(p.getScreenWidth()/1.322314049586777), (int)(p.getScreenHeight()*9.0/10.0));
        }else{
            Main.w.setResizable(false);
            setSize(width, height);
        }
        panel1.setVisible(false);
        remove(panel1);
        add(panel2, BorderLayout.CENTER);
        panel2.setVisible(true);
        panel2.setFocusable(true);
        panel2.requestFocusInWindow();
    }
    
    public double getWindowDiagonal(){
        return Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
    }
}
