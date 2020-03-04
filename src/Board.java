import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements MouseListener, ActionListener {
    public int[][] board;       //1 for White, 0 for Black, -1 for Empty
    public int size;
    public int turn;            //1 for White, 0 for Black
    private int x, y;
    public int b_Score, w_Score;
    private boolean mouseclicked = false;
    public int[][] possMovesArray;  //0 for not possible, 1 for possible
    public JLabel bScore;
    public JLabel wScore;
    public JButton easy;
    public JButton hard;
    public JButton restart;

    Board(int s) {
        size = s;
        board = new int[size][size];
        turn = 0;
        possMovesArray = new int [size][size];
        b_Score = 0;
        w_Score = 0;
        bScore = new JLabel(b_Score+"");
        wScore = new JLabel(w_Score+"");
        easy = new JButton("Easy");
        hard = new JButton("Hard");
        restart = new JButton("Restart");
        addMouseListener(this);
    }

    Board(Board b) {
        size = b.size;
        board = new int[size][size];
        possMovesArray = new int [size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = b.board[i][j];
                possMovesArray[i][j] = b.possMovesArray[i][j];
            }
        }
        turn = b.turn;
        mouseclicked = b.mouseclicked;
        x = b.x;
        y = b.y;
        b_Score = b.b_Score;
        w_Score = b.w_Score;
        bScore = b.bScore;
        wScore = b.wScore;
        easy = b.easy;
        hard = b.hard;
        restart = b.restart;
        addMouseListener(this);
    }

    public void initBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = -1;
            }
        }
        board[3][3] = 1;
        board[3][4] = 0;
        board[4][3] = 0;
        board[4][4] = 1;
    }

    public void drawBoard() {
        JFrame frame = new Board(this);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(150, 550, 100, 50)));
//        JButton easy = new JButton("Easy");
//        JButton hard = new JButton("Hard");
//        JButton restart = new JButton("Restart");
        easy = new JButton("Easy");
        hard = new JButton("Hard");
        restart = new JButton("Restart");
        easy.addActionListener(this);
        hard.addActionListener(this);
        restart.addActionListener(this);
        panel.add(easy);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(hard);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(restart);
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel,BoxLayout.X_AXIS));
        scoresPanel.setBorder(new EmptyBorder(new Insets(520, 215, 40, 150)));
        bScore.setText(b_Score+"");
        JLabel black = new JLabel("Black");
        wScore.setText(w_Score+"");
        JLabel white = new JLabel("White");
        scoresPanel.add(bScore);
        scoresPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        scoresPanel.add(black);
        scoresPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        scoresPanel.add(wScore);
        scoresPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        scoresPanel.add(white);
        JPanel title = new JPanel();
        JLabel othello = new JLabel("OTHELLO");
        title.add(othello);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add (panel,BorderLayout.NORTH);
        frame.getContentPane().add (scoresPanel,BorderLayout.SOUTH);
        //frame.getContentPane().add (title,BorderLayout.NORTH);
        frame.setVisible(true);
        frame.repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        String bgColor = new String("0x006600");
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

        bScore.setText(b_Score+"");
        wScore.setText(w_Score+"");

        calculatePossibleMoves();

        for (int i = 0, bx = 100; i < size && bx <= 500; i++, bx += 50) {
            for (int j = 0, by = 100; j < size && by <= 500; j++, by += 50) {
                if (board[i][j] == 1) {
                    g.setColor(Color.WHITE);
                    g.fillOval(bx + 5, by + 5, 40, 40);
                } else if (board[i][j] == 0) {
                    g.setColor(Color.BLACK);
                    g.fillOval(bx + 5, by + 5, 40, 40);
                }
                if (possMovesArray[i][j] == 1){
                    g.setColor(Color.GRAY);
                    g.drawArc(bx + 5, by + 5, 40, 40, 0, 360);
                }
            }
        }

        if (mouseclicked) {
            int hundredx = x / 100;
            hundredx *= 100;
            int hundredy = y / 100;
            hundredy *= 100;
            int tensx = x % 100;
            int tensy = y % 100;
            if (tensx > 50) {
                hundredx += 50;
            }
            if (tensy > 50) {
                hundredy += 50;
            }
            if (turn == 0) {             //DRAW BLACK CIRCLES
                g.setColor(Color.black);
                g.fillOval(hundredx + 5, hundredy + 5, 40, 40);
            } else {                       //DRAW WHITE CIRCLES
                g.setColor(Color.WHITE);
                g.fillOval(hundredx + 5, hundredy + 5, 40, 40);
            }
            mouseclicked = false;
        }
    }

    public void calculatePossibleMoves(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                possMovesArray[i][j] = 0;
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j]==0){
                    //  UP DOWN LEFT RIGHT
                    if (i-1 > 0)
                    if (board[i-1][j] == 1) {                 //down
                        for (int k = i - 2; k > 0; k--) {    //x dec, y same
                            if (board[k][j] == 0) break;
                            if (board[k][j] == -1) {
                                possMovesArray[k][j] = 1;
                                break;
                            }
                        }
                    }
                    if (i+1 < 8)
                    if (board[i+1][j] == 1) {                 //up
                        for (int k = i + 2; k < 8; k++) {    //x inc, y same
                            if (board[k][j] == 0) break;
                            if (board[k][j] == -1) {
                                possMovesArray[k][j] = 1;
                                break;
                            }
                        }
                    }
                    if (j-1 > 0)
                    if (board[i][j-1] == 1) {                 //left
                        for (int k = j - 2; k > 0; k--) {    //x same, y dec
                            if (board[i][k] == 0) break;
                            if (board[i][k] == -1) {
                                possMovesArray[i][k] = 1;
                                break;
                            }
                        }
                    }
                    if (j+1 < 8)
                    if (board[i][j+1] == 1) {                 //right
                        for (int k = j + 2; k < 8; k++) {    //x same, y inc
                            if (board[i][k] == 0) break;
                            if (board[i][k] == -1) {
                                possMovesArray[i][k] = 1;
                                break;
                            }
                        }
                    }
                    //  DIAGONALS
                    if (i+1 < 8 && j+1 < 8)
                    if (board[i+1][j+1] == 1) {
                        outerloop1:
                        for (int m = i + 2;m < 8 ;m++) {    //down right
                            for (int k = j + 2; k < 8; k++) {   //x inc, y inc
                                if (board[m][k] == 0) break outerloop1;
                                if (board[m][k] == -1) {
                                    possMovesArray[m][k] = 1;
                                    break outerloop1;
                                }
                            }
                        }
                    }
                    if (i-1 > 0 && j+1 < 8)
                    if (board[i-1][j+1] == 1) {
                        outerloop2:
                        for (int m = i - 2;m > 0 ;m--) {    //up right
                            for (int k = j + 2; k < 8; k++) {   //x dec, y inc
                                if (board[m][k] == 0) break outerloop2;
                                if (board[m][k] == -1) {
                                    possMovesArray[m][k] = 1;
                                    break outerloop2;
                                }
                            }
                        }
                    }
                    if (i+1 < 8 && j-1 > 0)
                    if (board[i+1][j-1] == 1) {
                        outerloop3:
                        for (int m = i + 2;m < 8 ;m++) {    //down left
                            for (int k = j - 2; k > 0; k--) {   //x inc, y dec
                                if (board[m][k] == 0) break outerloop3;
                                if (board[m][k] == -1) {
                                    possMovesArray[m][k] = 1;
                                    break outerloop3;
                                }
                            }
                        }
                    }
                    if (i-1 > 0 && j-1 > 0)
                    if (board[i-1][j-1] == 1) {
                        outerloop4:
                        for (int m = i - 2;m > 0 ;m--) {    //up left
                            for (int k = j - 2; k > 0; k--) {   //x dec, y dec
                                if (board[m][k] == 0) break outerloop4;
                                if (board[m][k] == -1) {
                                    possMovesArray[m][k] = 1;
                                    break outerloop4;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void restartGame(){
        initBoard();
        b_Score = 0;
        w_Score = 0;
        drawBoard();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        System.out.println("Mouse Pressed at X: " + x + " - Y: " + y);
        for (int i = 0, bx = 100; i < size && bx <= 500; i++, bx += 50) {   //check that mouse is clicked in hint
            for (int j = 0, by = 100; j < size && by <= 500; j++, by += 50) {
                if ((x>=bx && x<=bx+50) && (y>=by && y<=by+50)) {
                    if (possMovesArray[i][j] == 1) {
                        board[i][j] = 0;
                        mouseclicked = true;
                        repaint();
                    }
                }
            }
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals("Easy")) {

        }
        else if (action.equals("Hard")){

        }
        else if (action.equals("Restart")) {
            System.out.println("Pressed");
            restartGame();
            repaint();
        }
    }
}