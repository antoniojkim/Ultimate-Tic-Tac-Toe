package Main;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class p {
    
    public static void main (String[] args){
    }
    
    public static  Color defaultcolor = UIManager.getColor ( "Panel.background" );
    public static  Border border = new LineBorder(Color.BLACK, 1);
    public final static String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public final static double infinitesimal = 0.000000000000001;
    
    
    //Math methods
    public static double round(double num, int place){
        double rounded = Math.round(num*Math.pow(10, place))/Math.pow(10, place);
        return rounded;
    }
    public static double random(double low, double high){
        return (high-low)*Math.random()+low;
    }
    public static int randomint(int low, int high){
        return (int)((high-low+1)*Math.random()+low);
    }
    
    
    
    //JComponents
    public static Border border(int thickness){
        Border border = new LineBorder(Color.BLACK, thickness);
        return border;
    }
    public static JLabel label (int xcoordinate, int ycoordinate, String text, int fontsize){
        JLabel label = new JLabel();
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        int width = (int)(font.getStringBounds(text, frc).getWidth());
        int height = (int)(font.getStringBounds(text, frc).getHeight());
        
//        System.out.println(text+":   "+width+", "+height);
        label.setLocation(xcoordinate, ycoordinate);
        label.setSize(width+5, height+5);
        label.setFont(font);
        label.setText(text);
        return label;
    }
    public static void JMessagePane(String message, String header, int type){
        JOptionPane.showMessageDialog (null, message, header, type);
    }
    public static int showConfirmDialog(String message, String header, int mode, int type){
        return JOptionPane.showConfirmDialog(null, message, header, mode, type);
    }
    
    // GUI Shortcuts
    public void addGUIButton(JPanel panel, Graphics2D g, int xcoordinate, int ycoordinate, int width, int height, String text, int fontsize, int mx, int my){
        g.setStroke(new BasicStroke(3));
        g.drawRect (xcoordinate, ycoordinate, width, height);
        if (mx >= xcoordinate && mx <= xcoordinate+width && my>=ycoordinate && my<=ycoordinate+height) {
            g.setColor(Color.GRAY);
            g.fillRect (xcoordinate+2, ycoordinate+2, width-3, height-3);
            g.setColor(Color.BLACK);
            panel.repaint();
        }
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
        int textheight = (int)(font.getStringBounds(text, frc).getHeight());
        g.setFont(font);
        g.drawString(text, xcoordinate+((width-textwidth)/2), ycoordinate+height-(height-textheight));
//        System.out.println("a: "+a+"\n"+(((c)-width)/2)+a);
    }
    public void addGUIButton(JPanel panel, Graphics2D g, int xcoordinate, int ycoordinate, int width, int height,  String text, int fontsize, int textwidth, int textheight, int mx, int my){
        g.setStroke(new BasicStroke(3));
        g.drawRect (xcoordinate, ycoordinate, width, height);
        if (mx >= xcoordinate && mx <= xcoordinate+width && my>=ycoordinate && my<=ycoordinate+height) {
            g.setColor(Color.GRAY);
            g.fillRect (xcoordinate+2, ycoordinate+2, width-3, height-3);
            g.setColor(Color.BLACK);
            panel.repaint();
        }
        Font font = new Font("Calibri", Font.PLAIN, fontsize);
        g.setFont(font);
        g.drawString(text, textwidth, textheight);
//        System.out.println("a: "+a+"\n"+(((c)-width)/2)+a);
    }
    
    //sorting algorithms
    public static boolean sort(int[]order){
        for (int a = 0; a<order.length; a++){
            for (int b = a; b<order.length; b++){
                if (order[a]>order[b]){
                    int switch1 = order[a];
                    order[a] = order[b];
                    order[b] = switch1;
                    return sort(order);
                }
            }
        }
        return true;
    }
    public static boolean sort(int[][]order){
        for (int a = 0; a<order.length; a++){
            for (int b = 0; b<order[a].length; b++){
                if (b<order[a].length-1){
                    if (order[a][b]>order[a][b+1]){
                        int switch1 = order[a][b];
                        order[a][b] = order[a][b+1];
                        order[a][b+1] = switch1;
                        return sort(order);
                    }
                }
                else if (a<order.length-1){
                    if (order[a][b]>order[a+1][0]){
                        int switch1 = order[a][b];
                        order[a][b] = order[a+1][0];
                        order[a+1][0] = switch1;
                        return sort(order);
                    }
                }
            }
        }
        return true;
    }
    public static String[] sort (String[] array){
//        print(array);
        if (array.length == 1){
            return array;
        }
        else{
            int index = (int)Math.floor((array.length)/2.0);
            LinkedList<String> new1 = new LinkedList<>(Arrays.asList(sort(Arrays.copyOf(array, index))));
            LinkedList<String> new2 = new LinkedList<>(Arrays.asList(sort(Arrays.copyOfRange(array, index, array.length))));
            String[] merge = new String[array.length];
            int x = 0, y = 0;
            for (int a = 0; a<merge.length; a++){
                if (new1.peek()==null){
                    while(new2.peek()!=null){
                        merge[a] = new2.pollFirst();
                        a++;
                    }
                    break;
                }
                else if (new2.peek()==null){
                    while(new1.peek()!=null){
                        merge[a] = new1.pollFirst();
                        a++;
                    }
                    break;
                }
                else{
                    if (new1.peek().compareTo(new2.peek())>0){
                        merge[a] = new2.pollFirst();
                    }
                    else if (new1.peek().compareTo(new2.peek())<0){
                        merge[a] = new1.pollFirst();
                    }
                    else if(new1.peek().compareTo(new2.peek())==0){
                        merge[a] = new2.pollFirst();
                        a++;
                        merge[a] = new1.pollFirst();
                    }
                }
            }
            return merge;
            /*
            Merge Sort Tester
            
            int rsize = (int)((10-3+1)*Math.random()+3);
            String[] array = new String[rsize];
            for (int a = 0; a<array.length; a++){
            int strsize = (int)((10-3+1)*Math.random()+3);
            String str = "";
            for (int b = 0; b<strsize; b++){
            str += letters[(int)(letters.length*Math.random())];
            }
            array[a] = str;
            }
            print(array);
            array = sort(array);
            print(array);
            */
        }
    }
    public static Object[] sort (Object[] array){
//        print(array);
        if (array.length == 1){
            return array;
        }
        else{
            int index = (int)Math.floor((array.length)/2.0);
            LinkedList<Object> new1 = new LinkedList<>(Arrays.asList(sort(Arrays.copyOf(array, index))));
            LinkedList<Object> new2 = new LinkedList<>(Arrays.asList(sort(Arrays.copyOfRange(array, index, array.length))));
            Object[] merge = new Object[array.length];
            int x = 0, y = 0;
            for (int a = 0; a<merge.length; a++){
                if (new1.peek()==null){
                    while(new2.peek()!=null){
                        merge[a] = new2.pollFirst();
                        a++;
                    }
                    break;
                }
                else if (new2.peek()==null){
                    while(new1.peek()!=null){
                        merge[a] = new1.pollFirst();
                        a++;
                    }
                    break;
                }
                else{
                    if (new1.peek().toString().compareTo(new2.peek().toString())>0){
                        merge[a] = new2.pollFirst();
                    }
                    else if (new1.peek().toString().compareTo(new2.peek().toString())<0){
                        merge[a] = new1.pollFirst();
                    }
                    else if(new1.peek().toString().compareTo(new2.peek().toString())==0){
                        merge[a] = new2.pollFirst();
                        a++;
                        merge[a] = new1.pollFirst();
                    }
                }
            }
            return merge;
            /*
            Merge Sort Tester
            
            int rsize = (int)((10-3+1)*Math.random()+3);
            String[] array = new String[rsize];
            for (int a = 0; a<array.length; a++){
            int strsize = (int)((10-3+1)*Math.random()+3);
            String str = "";
            for (int b = 0; b<strsize; b++){
            str += letters[(int)(letters.length*Math.random())];
            }
            array[a] = str;
            }
            print(array);
            array = sort(array);
            print(array);
            */
        }
    }
    public static boolean sort(String[][]array){
        for (int a = 0; a<array.length; a++){
            for (int b = 0; b<array[a].length; b++){
                if ((b+1)<array[a].length){
                    if (array[a][b].compareTo(array[a][b+1])>0){
                        String switch1 = array[a][b];
                        array[a][b] = array[a][b+1];
                        array[a][b+1] = switch1;
                        return sort(array);
                    }
                }
                else if((a+1)<array.length){
                    if (array[a][b].compareTo(array[a+1][0])>0){
                        String switch1 = array[a][b];
                        array[a][b] = array[a+1][0];
                        array[a+1][0] = switch1;
                        return sort(array);
                    }
                }
            }
        }
        return true;
    }
    public static boolean sort(List<Integer> list){
        for (int a = 0; a<list.size(); a++){
            for (int b = a+1; b<list.size(); b++){
                if (list.get(b) > list.get(a)){
                    int switch1 = list.get(b);
                    list.set(b, list.get(a));
                    list.set(a, switch1);
                    return sort(list);
                }
            }
        }
        return true;
    }
    public static boolean sort(List<Integer> list, List<Integer> order){
        for (int a = 0; a<list.size(); a++){
            for (int b = a+1; b<list.size(); b++){
                if (list.get(b) > list.get(a)){
                    int switch1 = list.get(b);
                    list.set(b, list.get(a));
                    list.set(a, switch1);
                    int switch2 =  order.get(b);
                    order.set(b, order.get(a));
                    order.set(a, switch2);
                    return sort(list, order);
                }
            }
        }
        return true;
    }
    public static void randomSort(List<Integer> list, List<Integer> order){
        List<Integer> listcopy = new ArrayList<>();
        listcopy.addAll(list);
        List<Integer> ordercopy = new ArrayList<>();
        ordercopy.addAll(order);
        list.clear();
        order.clear();
        List<Integer> picked = new ArrayList<>();
        while (picked.size() < listcopy.size()){
            int r = p.randomint(0, listcopy.size()-1);
            if (!picked.contains(r)){
                picked.add(r);
                list.add(listcopy.get(r));
                order.add(ordercopy.get(r));
            }
        }
        sort(list, order);
    }
    
    
    //Delay
    public static void delay (double time){
        try{
            Thread.sleep((long)Math.floor(time), (int)((time-Math.floor(time))*1000000));
//            System.out.println("Delayed "+(long)Math.floor(time)+"Milliseconds and "+(int)((time-Math.floor(time))*100000)+" Nanoseconds");
        }catch(InterruptedException e){}
    }
    
    //Buffered Input
    public static BufferedImage loadImage(String path){
        BufferedImage image = null;
        try {
//            image = ImageIO.read(getClass().getResource(path));
            image = ImageIO.read(new FileInputStream(path));
        } catch (IOException e) {
        }
        return image;
    }
    public static String getString(){
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        String string = "";
        try {
            string =  br.readLine();
        } catch (IOException ex) {        }
        return string;
    }
    public static int getInt(){
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        try{
            return Integer.parseInt(br.readLine());
        }catch (NumberFormatException | IOException e){        }
        return -1;
    }
    public static double getDouble(){
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        try{
            return Double.parseDouble(br.readLine());
        }catch (NumberFormatException | IOException e){        }
        return -1;
    }
    public static long getLong(){
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        try{
            return Long.parseLong(br.readLine());
        }catch (NumberFormatException | IOException e){        }
        return -1L;
    }
    public static float getFloat(){
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        try{
            return Float.parseFloat(br.readLine());
        }catch (NumberFormatException | IOException e){        }
        return -1f;
    }
    public static PrintWriter printwriter(String path){
        try {
            return new PrintWriter (new FileWriter (path));
        } catch (IOException ex) {        }
        return null;
    }
    public static BufferedReader filereader(String path){
        
        //example path:
        
        try {
            return new BufferedReader (new FileReader (path));
        } catch (FileNotFoundException ex) {        }
        return null;
    }
    
    //Print Arrays
    public static void print(String[] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print(array[a]);
            if ((a+1)<array.length){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void print(int[] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print(array[a]);
            if ((a+1)<array.length){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void print(double[] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print(array[a]);
            if ((a+1)<array.length){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void print(long[] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print(array[a]);
            if ((a+1)<array.length){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void print(float[] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print(array[a]);
            if ((a+1)<array.length){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void print(Object[] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print(array[a]);
            if ((a+1)<array.length){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    public static void print(String[][] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print("{");
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]);
                if ((b+1)<array[a].length){
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
        System.out.println("}");
    }
    public static void print(int[][] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print("{");
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]);
                if ((b+1)<array[a].length){
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
        System.out.println("}");
    }
    public static void print(double[][] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print("{");
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]);
                if ((b+1)<array[a].length){
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
        System.out.println("}");
    }
    public static void print(long[][] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print("{");
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]);
                if ((b+1)<array[a].length){
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
        System.out.println("}");
    }
    public static void print(float[][] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print("{");
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]);
                if ((b+1)<array[a].length){
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
        System.out.println("}");
    }
    public static void print(Object[][] array){
        System.out.print("{");
        for (int a = 0; a<array.length; a++){
            System.out.print("{");
            for (int b = 0; b<array[a].length; b++){
                System.out.print(array[a][b]);
                if ((b+1)<array[a].length){
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
        System.out.println("}");
    }
    public static void print(List<Integer> list){
        System.out.print("{");
        for (int a = 0; a<list.size(); a++){
            System.out.print(list.get(a));
            if ((a+1)<list.size()){
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    
    //String functions
    public static int stringWidth(String string, Font f){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = f;
        int width = (int)(font.getStringBounds(string, frc).getWidth());
        return width;
    }
    public static int stringHeight(String string, Font f){
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        Font font = f;
        int height = (int)(font.getStringBounds(string, frc).getHeight());
        return height;
    }
    
    //ToolKit
    public static double getScreenWidth(){
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }
    public static double getScreenHeight(){
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }
    public static int getFontSize(double old){
        return (int)(Main.w.getWindowDiagonal()/(1442.2205101855957/old));
    }
    public static int convertX(double old){
        return (int)(Main.w.getWidth()/(1200.0/old));
    }
    public static int convertY(double old){
        return (int)(Main.w.getHeight()/(800.0/old));
    }
}
