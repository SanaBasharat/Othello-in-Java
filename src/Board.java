import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.TimeUnit;

public class Board extends JFrame implements MouseListener, ActionListener {
    public int[][] board;       //1 for White, 0 for Black, -1 for Empty
    public int size;
    //public int turn;            //1 for White, 0 for Black
    private int x, y;
    public int b_Score, w_Score;
    private boolean mouseclicked = false;
    public int[][] possMovesArray;  //0 for not possible, 1 for possible
    public JLabel bScore;
    public JLabel wScore;
    public JButton restart;
    private AI myAI;
    int difficulty;
    public Boolean isGameOver;
    public Boolean bMovesLeft;
    public Boolean wMovesLeft;

    Board(int s) {
        size = s;
        board = new int[size][size];
        //turn = 0;
        possMovesArray = new int [size][size];
        b_Score = 2;
        w_Score = 2;
        bScore = new JLabel(b_Score+"");
        wScore = new JLabel(w_Score+"");
        restart = new JButton("Restart");
        myAI=new AI();
        difficulty = 1;
        isGameOver = false;
        bMovesLeft = true;
        wMovesLeft = true;
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
        mouseclicked = b.mouseclicked;
        x = b.x;
        y = b.y;
        b_Score = b.b_Score;
        w_Score = b.w_Score;
        bScore = b.bScore;
        wScore = b.wScore;
        restart = b.restart;
        myAI=b.myAI;
        difficulty = b.difficulty;
        isGameOver = b.isGameOver;
        bMovesLeft = b.bMovesLeft;
        wMovesLeft = b.wMovesLeft;
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

        Frame f = new Frame();
        Object[] options = {"Easy","Hard"};
        int n = JOptionPane.showOptionDialog(f,"Choose a difficulty level: ","Othello",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==0) difficulty=1;
        else difficulty=3;
    }

    public void drawBoard() {
        JFrame frame = new Board(this);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setTitle("Othello");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        restart = new JButton("Restart");
        restart.addActionListener(this);
        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel,BoxLayout.X_AXIS));
        scoresPanel.setBorder(new EmptyBorder(new Insets(520, 170, 40, 110)));
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
        scoresPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        scoresPanel.add(restart);
        JPanel title = new JPanel();
        JLabel othello = new JLabel("OTHELLO");
        title.add(othello);
        title.setLayout(new BoxLayout(title,BoxLayout.X_AXIS));
        title.setBorder(new EmptyBorder(new Insets(20,50,500,200)));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add (scoresPanel,BorderLayout.SOUTH);
        frame.getContentPane().add (title,BorderLayout.CENTER);
        if (!isGameOver){
            frame.setVisible(true);
            frame.repaint();
        }
        else{
            //
        }
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

        for (int i = 0, by = 100; i < size && by <= 500; i++, by += 50) {
            for (int j = 0, bx = 100; j < size && bx <= 500; j++, bx += 50) {
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

        if (checkBoardFilled()){
            gameOver();
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
            }             //DRAW BLACK CIRCLES
            g.setColor(Color.black);
            g.fillOval(hundredx + 5, hundredy + 5, 40, 40);
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
                if(myAI.validMove(board,i,j,0))
                    possMovesArray[i][j] = 1;
            }
        }
        bMovesLeft = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (possMovesArray[i][j]==1){
                    bMovesLeft = true;
                }
            }
        }
        if (bMovesLeft==false){
            if (checkGameOver()==true) gameOver();
            else AIPlays();
        }
    }

    public void restartGame(){
        initBoard();
        b_Score = 0;
        w_Score = 0;
        drawBoard();
    }

    public void gameOver(){
        isGameOver = true;
        String winner = " ";
        if (b_Score>w_Score) winner = "You win.";
        else if (b_Score<w_Score) winner = "Computer wins.";
        else winner = "Draw.";    //draw
        Frame f = new Frame();
        JOptionPane.showMessageDialog(f,"Game over! "+winner);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        for (int i = 0, by = 100; i < size && by <= 500; i++, by += 50) {   //check that mouse is clicked in hint
            for (int j = 0, bx = 100; j < size && bx <= 500; j++, bx += 50) {
                if ((x>=bx && x<=bx+50) && (y>=by && y<=by+50)) {
                    if (possMovesArray[i][j] == 1) {
                        board[i][j] = 0;
                        myAI.makeMove(board,i,j,0);
                        b_Score = myAI.score(board,0);
                        w_Score = myAI.score(board,1);
                        repaint();
                        mouseclicked = true;
                        //AI takes turn here
                        AIPlays();
                        break;
                    }
                }
            }
        }
    }

    public void AIPlays(){
        Point p=myAI.minimaxDecision(board,difficulty);
        if (p.x!=-1 || p.y!=-1){
            board[p.x][p.y]=1;
            myAI.makeMove(board,p.x,p.y,1);
            wMovesLeft = true;
            calculatePossibleMoves();
            b_Score = myAI.score(board,0);
            w_Score = myAI.score(board,1);
            repaint();
        }
        else{
            b_Score = myAI.score(board,0);
            w_Score = myAI.score(board,1);
            repaint();
            wMovesLeft = false;
            if (checkGameOver()){
                gameOver();
            }
        }
    }

    Boolean checkGameOver(){
        Boolean retval = false;
        if (!bMovesLeft && !wMovesLeft){
            retval = true;
        }
        if (checkBoardFilled()){
            retval = true;
        }
        return retval;
    }

    Boolean checkBoardFilled(){
        Boolean retval = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j]==-1){
                    retval = false;
                }
            }
        }
        return retval;
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
        if (action.equals("Restart")) {
            restartGame();
            repaint();
        }
    }
}