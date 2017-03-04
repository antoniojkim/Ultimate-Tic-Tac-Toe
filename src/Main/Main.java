package Main;


import java.awt.BorderLayout;
import AI.MainAI;



public class Main{
    
    static Window w = new Window();
    static MainMenu menu = new MainMenu();
    public static boolean developersmode = false;
    
    public static void main(String[] args) {
//        Thread thread = new Thread(ai);
//        thread.start();
        w.add(menu, BorderLayout.CENTER);
        w.setVisible(true);
        menu.run();
    }
    
}
