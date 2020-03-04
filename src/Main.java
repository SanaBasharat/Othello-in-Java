public class Main {
    public int bScore;
    public int wScore;

    Main(){
        bScore = 0;
        wScore = 0;
    }


    public static void main(String[] args) {
        Board b = new Board(8);
        b.initBoard();
        b.drawBoard();
    }
}
