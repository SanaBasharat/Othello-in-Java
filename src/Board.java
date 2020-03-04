import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements MouseListener {
    public int board[][];       //1 for White, 0 for Black, -1 for Empty
    public int size;
    public int turn;            //1 for White, 0 for Black
    private int x,y;
    private boolean mouseclicked = false;

    Board(int s){
        size = s;
        board = new int [size][size];
        turn = 0;
        addMouseListener(this);
    }

    Board(Board b){
        size = b.size;
        board = new int [size][size];
        for (int i = 0;i < size;i++){
            for (int j = 0;j < size;j++){
                board[i][j] = b.board[i][j];
            }
        }
        turn = b.turn;
        addMouseListener(this);
    }

    public void initBoard(){
        for (int i = 0;i < size;i++){
            for (int j = 0;j < size;j++){
                board[i][j] = -1;
                System.out.println(board[i][j]);
            }
        }
        board[3][3] = 1;
        board[3][4] = 0;
        board[4][3] = 0;
        board[4][4] = 1;
    }

    public void drawBoard(){
        JFrame frame = new Board(this);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.repaint();
    }

    public void paint(Graphics g){
        String bgColor= new String("0x006600");
        g.setColor(Color.decode(bgColor));
        g.fillRect(100, 100, 400, 400);
        String borderColor = new String("0xffffff");
        g.setColor(Color.decode(borderColor));
        g.drawLine(100, 100, 100, 500);
        g.drawLine(150, 100, 150, 500);
        g.drawLine(200, 100, 200, 500);
        g.drawLine(250, 100, 250, 500);
        g.drawLine(300, 100, 300, 500);
        g.drawLine(350, 100, 350, 500);
        g.drawLine(400, 100, 400, 500);
        g.drawLine(450, 100, 450, 500);
        g.drawLine(500, 100, 500, 500);

        g.drawLine(100, 100, 500, 100);
        g.drawLine(100, 150, 500, 150);
        g.drawLine(100, 200, 500, 200);
        g.drawLine(100, 250, 500, 250);
        g.drawLine(100, 300, 500, 300);
        g.drawLine(100, 350, 500, 350);
        g.drawLine(100, 400, 500, 400);
        g.drawLine(100, 450, 500, 450);
        g.drawLine(100, 500, 500, 500);

        for (int i = 0, bx=100;i < size && bx <= 500;i++,bx+=50){
            for (int j = 0, by=100;j < size && by <= 500;j++,by+=50){
                if (board[i][j] == 1){
                    g.setColor(Color.BLACK);
                    g.drawArc(bx + 5, by + 5, 40, 40, 0, 360);
                    g.setColor(Color.WHITE);
                    g.fillOval(bx+5, by+5, 40, 40);
                }
                else if (board[i][j] == 0){
                    g.setColor(Color.WHITE);
                    g.drawArc(bx + 5, by + 5, 40, 40, 0, 360);
                    g.setColor(Color.BLACK);
                    g.fillOval(bx+5, by+5, 40, 40);
                }
            }
        }

        if (mouseclicked)
        {
            int hundredx = x/100;
            hundredx*=100;
            int hundredy = y/100;
            hundredy*=100;
            int tensx = x%100;
            int tensy = y%100;
            if (tensx>50){
                hundredx+=50;
            }
            if (tensy>50) {
                hundredy+=50;
            }
            if (turn == 0){             //DRAW BLACK CIRCLES
                g.setColor(Color.WHITE);
                g.drawArc(hundredx + 5, hundredy + 5, 40, 40, 0, 360);
                g.setColor(Color.black);
                g.fillOval(hundredx+5, hundredy+5, 40, 40);
            }
            else{                       //DRAW WHITE CIRCLES
                g.setColor(Color.BLACK);
                g.drawArc(hundredx + 5, hundredy + 5, 40, 40, 0, 360);
                g.setColor(Color.WHITE);
                g.fillOval(hundredx+5, hundredy+5, 40, 40);
            }
            mouseclicked = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        System.out.println("Mouse Pressed at X: " + x + " - Y: " + y);
        if (x > 100 && x <=500){
            if (y > 100 && y <= 500){
                mouseclicked = true;
                repaint();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

//    public static void main(String[] args) {
//        //Schedule a job for the event-dispatching thread:
//
//        //creating and showing this application's GUI.
//
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//
//            public void run() {
//                //drawBoard();
//
//            }
//
//        });
//    }
}
