import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DrawGrid {
    private JFrame frame;

    public DrawGrid() {
        frame = new JFrame("Connect Four");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new MultiDraw(new Dimension(600, 360)));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String... args) {
        new DrawGrid();
    }

    public static class MultiDraw extends JPanel implements MouseListener {
        int cellSize = 60;
        int turn = 2;
        int rows = 6;
        int cols = 7;
        boolean winner = false;
        String ccolor = "";

        Color[][] grid = new Color[rows][cols];

        public MultiDraw(Dimension dimension) {
            setPreferredSize(dimension);
            addMouseListener(this);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    grid[row][col] = Color.white;
                }
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Dimension d = getSize();
            g2.setColor(new Color(0, 0, 255));
            g2.fillRect(0, 0, d.width, d.height);

            int startX = 0, startY = 0;

            // Draw grid
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    g2.setColor(grid[row][col]);
                    g2.fillRect(startX, startY, cellSize, cellSize);
                    g2.setColor(Color.black);
                    g2.drawRect(startX, startY, cellSize, cellSize);
                    startX += cellSize;
                }
                startY += cellSize;
                startX = 0;
            }

            // Draw status and Play Again button
            g2.setColor(Color.white);
            int textX = 450;
            int textY = 20;
            int buttonX = 450;
            int buttonY = 40;
            int buttonWidth = 100;
            int buttonHeight = 30;

            if (!winner) {
                g2.drawString((turn % 2 == 0 ? "Red's Turn" : "Yellow's Turn"), textX, textY);
            } else {
                g2.drawString("WINNER - " + ccolor, textX, textY);
                g2.setColor(new Color(200, 0, 0));
                g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
                g2.setColor(Color.white);
                g2.drawRect(buttonX, buttonY, buttonWidth, buttonHeight);
                g2.drawString("Play Again", buttonX + 10, buttonY + 20);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int buttonX = 450;
            int buttonY = 40;
            int buttonWidth = 100;
            int buttonHeight = 30;

            // If winner and "Play Again" button clicked
            if (winner && x >= buttonX && x <= buttonX + buttonWidth && y >= buttonY && y <= buttonY + buttonHeight) {
                reset();
                repaint();
                return;
            }

            if (!winner && x < (cellSize * cols) && y < (cellSize * rows)) {
                int clickedCol = x / cellSize;
                int clickedRow = dropP(clickedCol);

                if (clickedRow != -1) {
                    if (turn % 2 == 0) {
                        grid[clickedRow][clickedCol] = Color.red;
                        ccolor = "Red";
                    } else {
                        grid[clickedRow][clickedCol] = Color.yellow;
                        ccolor = "Yellow";
                    }
                    if (checkForWinner(clickedCol, clickedRow, grid[clickedRow][clickedCol])) {
                        winner = true;
                    } else {
                        turn++;
                    }
                    repaint();
                }
            }
        }

        public int dropP(int cc) {
            int cr = rows - 1;
            while (cr >= 0) {
                if (grid[cr][cc].equals(Color.white)) {
                    return cr;
                }
                cr--;
            }
            return -1;
        }

        public boolean checkForWinner(int cc, int cr, Color c) {
            int count;

            // Horizontal
            count = 1;
            int x = cc - 1;
            while (x >= 0 && grid[cr][x].equals(c)) {
                count++;
                x--;
            }
            x = cc + 1;
            while (x < cols && grid[cr][x].equals(c)) {
                count++;
                x++;
            }
            if (count >= 4) return true;

            // Vertical
            count = 1;
            int y = cr - 1;
            while (y >= 0 && grid[y][cc].equals(c)) {
                count++;
                y--;
            }
            y = cr + 1;
            while (y < rows && grid[y][cc].equals(c)) {
                count++;
                y++;
            }
            if (count >= 4) return true;

            // Diagonal ↘
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
            while (x < cols && y < rows && grid[y][x].equals(c)) {
                count++;
                x++;
                y++;
            }
            if (count >= 4) return true;

            // Diagonal ↗
            count = 1;
            x = cc - 1;
            y = cr + 1;
            while (x >= 0 && y < rows && grid[y][x].equals(c)) {
                count++;
                x--;
                y++;
            }
            x = cc + 1;
            y = cr - 1;
            while (x < cols && y >= 0 && grid[y][x].equals(c)) {
                count++;
                x++;
                y--;
            }
            return count >= 4;
        }

        public void reset() {
            winner = false;
            turn = 2;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    grid[row][col] = Color.white;
                }
            }
        }

        // Unused mouse listener methods
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {}
    }
}
