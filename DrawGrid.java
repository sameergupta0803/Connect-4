import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class DrawGrid {
    private JFrame frame;

    public DrawGrid() {
        frame = new JFrame("DrawGrid");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new MultiDraw(frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... argv) {
        new DrawGrid();
    }

    public static class MultiDraw extends JPanel  implements MouseListener {
        int startX = 10;
        int startY = 10;
        int cellSize = 60;
        int turn = 2;
        int rows = 6;
        int cols = 7;
        boolean winner=false;
        String ccolor = "";

        Color[][] grid = new Color[rows][cols];
        public MultiDraw(Dimension dimension) {
            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            //1. initialize array here
            int x = 0;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    Color c;
                    if(x%2==0){
                        grid[row][col] = Color.white; 
                    }else{
                        grid[row][col] = Color.white;
                    }
                    x++;

                }

            }
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            Dimension d = getSize();
            g2.setColor(new Color(0, 0, 255));
            g2.fillRect(0,0,d.width,d.height);
            startX = 0;
            startY = 0;

            //2) draw grid here
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {

                    g2.setColor(grid[row][col]);
                    g2.fillRect(startX,startY,cellSize,cellSize);
                    g2.setColor(Color.black);
                    g2.drawRect(startX,startY,cellSize,cellSize);
                    startX += cellSize;
                }
                startY += cellSize;
                startX = 0;
            }

            g2.setColor(new Color(255, 255, 255));
            if(winner==false){
                if(turn%2==0)
                    g2.drawString("Red's Turn",450,20);
                else
                    g2.drawString("Yellow's Turn",450,20);
            }else{
                g2.drawString("WINNER - "+ ccolor,450,20);
            }

        }

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if(winner==false){
                if(x<(cellSize*grid[0].length) && y<(cellSize*grid.length)){
                    int clickedRow = y/cellSize;
                    int clickedCol = x/cellSize;

                    clickedRow = dropP(clickedCol);

                    if(clickedRow!=-1){

                        if(turn%2==0){
                            grid[clickedRow][clickedCol]= Color.red;
                            ccolor =  "RED";
                        } else{
                            grid[clickedRow][clickedCol]= Color.yellow;
                            ccolor =  "Yellow";
                        }
                        turn++;
                        if(checkForWinner(clickedCol,clickedRow, grid[clickedRow][clickedCol])){
                            winner=true;

                        }
                    }
                }
                repaint();
            }
        }

        public int dropP(int cc){
            int cr = grid.length-1;

            while(cr>=0){ 

                if(grid[cr][cc].equals(Color.white)){
                    return cr;
                }
                cr--;
            }

            return -1;

        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {
            reset();
        }

        public void mouseClicked(MouseEvent e) {

        }
        
        //Marvel

        public boolean checkForWinner(int cc, int cr, Color c) {
            int count;

            // Check horizontal (left to right)
            count = 1;
            int x = cc - 1;
            while (x >= 0 && grid[cr][x].equals(c)) {
                count++;
                x--;
            }
            x = cc + 1;
            while (x < grid[0].length && grid[cr][x].equals(c)) {
                count++;
                x++;
            }
            if (count >= 4) return true;

            // Check vertical (top to bottom)
            count = 1;
            int y = cr - 1;
            while (y >= 0 && grid[y][cc].equals(c)) {
                count++;
                y--;
            }
            y = cr + 1;
            while (y < grid.length && grid[y][cc].equals(c)) {
                count++;
                y++;
            }
            if (count >= 4) return true;

            // Check diagonal (top-left to bottom-right)
            count = 1;
            x = cc - 1;
            y = cr - 1;
            while (x >= 0 && y >= 0 && grid[y][x].equals(c)) {
                count++;
                x--;
                y--;
            }
            x = cc + 1;
            y = cr + 1;
            while (x < grid[0].length && y < grid.length && grid[y][x].equals(c)) {
                count++;
                x++;
                y++;
            }
            if (count >= 4) return true;

            // Check diagonal (bottom-left to top-right)
            count = 1;
            x = cc - 1;
            y = cr + 1;
            while (x >= 0 && y < grid.length && grid[y][x].equals(c)) {
                count++;
                x--;
                y++;
            }
            x = cc + 1;
            y = cr - 1;
            while (x < grid[0].length && y >= 0 && grid[y][x].equals(c)) {
                count++;
                x++;
                y--;
            }
            if (count >= 4) return true;

            return false;
        }

        public void reset(){
            winner=false;
            turn=2;
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    grid[row][col] = Color.white; 

                }
            }
        }

    }//end of class
}