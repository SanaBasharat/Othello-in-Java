public class AI {//the basic algorithm of minmax was taken from geeks for geeks and updated accordingly
    AI(){}

    int score(int board[][], int piece)
    {
        int total = 0;
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
            {
                if (board[x][y] == piece)
                    total++;
            }
        return total;
    }

    int heuristic(int board[][], int whoseTurn)
    {
        int opponent = 0;
        if (whoseTurn == 0)
            opponent = 1;
        int ourScore = score(board, whoseTurn);
        int opponentScore = score(board, opponent);
        return (ourScore - opponentScore);
    }




    void copyBoard(int src[][], int dest[][])
    {
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
                dest[x][y]=src[x][y];
    }

    boolean checkFlip(int board[][], int x, int y, int deltaX, int deltaY, int myPiece, int opponentPiece)
    {
        if ( (x >= 0) && (x < 8) && (y >= 0) && (y < 8) && board[x][y] == opponentPiece)
        {
            while ((x >= 0) && (x < 8) && (y >= 0) && (y < 8))
            {
                x += deltaX;
                y += deltaY;
                if ((x >= 0) && (x < 8) && (y >= 0) && (y < 8) &&board[x][y] == -1) // not consecutive
                    return false;
                if ((x >= 0) && (x < 8) && (y >= 0) && (y < 8) &&board[x][y] == myPiece)
                    return true; // At least one piece we can flip
                else
                {
// It is an opponent piece, just keep scanning in our direction
                }
            }
        }
        return false; // Either no consecutive opponent pieces or hit the edge
    }

    boolean validMove(int board[][], int x, int y, int piece)
    {
// Check that the coordinates are empty
        if (board[x][y] != -1)
            return false;
// Figure out the character of the opponent's piece
        int opponent = 0;
        if (piece == 0)
            opponent = 1;
// If we can flip in any direction, it is valid
// Check to the left
        if (checkFlip(board, x - 1, y, -1, 0, piece, opponent))
            return true;
// Check to the right
        if (checkFlip(board, x + 1, y, 1, 0, piece, opponent))
            return true;
// Check down
        if (checkFlip(board, x, y - 1, 0, -1, piece, opponent))
            return true;
// Check up
        if (checkFlip(board, x, y + 1, 0, 1, piece, opponent))
            return true;
// Check down-left
        if (checkFlip(board, x - 1, y - 1, -1, -1, piece, opponent))
            return true;
// Check down-right
        if (checkFlip(board, x + 1, y - 1, 1, -1, piece, opponent))
            return true;
// Check up-left
        if (checkFlip(board, x - 1, y + 1, -1, 1, piece, opponent))
            return true;
// Check up-right
        if (checkFlip(board, x + 1, y + 1, 1, 1, piece, opponent))
            return true;
        return false; // If we get here, we didn't find a valid flip direction
    }

    int getMoveList(int board[][], int moveX[], int moveY[], int piece)
    {
        int numMoves = 0; // Initially no moves found
// Check each square of the board and if we can move there, remember the coords
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++)
            {
                if (validMove(board, x, y, piece)) // remember coordinates
                {
                    moveX[numMoves] = x;
                    moveY[numMoves] = y;
                    numMoves++; // Increment number of moves found
                }
            }
        return numMoves;
    }

    void flipPieces(int board[][], int x, int y, int deltaX, int deltaY, int myPiece, int opponentPiece)
    {
        while (board[x][y] == opponentPiece)
        {
            board[x][y] = myPiece;
            x += deltaX;
            y += deltaY;
        }
    }

    void makeMove(int board[][], int x, int y, int piece)
    {
// Put the piece at x,y
        board[x][y] = piece;
// Figure out the character of the opponent's piece
        char opponent = 0;
        if (piece == 0)
            opponent = 1;
// Check to the left
        if (checkFlip(board, x - 1, y, -1, 0, piece, opponent))
            flipPieces(board, x - 1, y, -1, 0, piece, opponent);
// Check to the right
        if (checkFlip(board, x + 1, y, 1, 0, piece, opponent))
            flipPieces(board, x + 1, y, 1, 0, piece, opponent);
// Check down
        if (checkFlip(board, x, y-1, 0, -1, piece, opponent))
            flipPieces(board, x, y-1, 0, -1, piece, opponent);
// Check up
        if (checkFlip(board, x, y + 1, 0, 1, piece, opponent))
            flipPieces(board, x, y + 1, 0, 1, piece, opponent);
// Check down-left
        if (checkFlip(board, x-1, y - 1, -1, -1, piece, opponent))
            flipPieces(board, x-1, y - 1, -1, -1, piece, opponent);
// Check down-right
        if (checkFlip(board, x + 1, y - 1, 1, -1, piece, opponent))
            flipPieces(board, x + 1, y - 1, 1, -1, piece, opponent);
// Check up-left
        if (checkFlip(board, x - 1, y + 1, -1, 1, piece, opponent))
            flipPieces(board, x - 1, y + 1, -1, 1, piece, opponent);
// Check up-right
        if (checkFlip(board, x + 1, y + 1, 1, 1, piece, opponent))
            flipPieces(board, x + 1, y + 1, 1, 1, piece, opponent);
    }

    boolean gameOver(int board[][])
    {
        int XMoveX[]=new int [60];
        int XMoveY[]=new int [60];
        int OMoveX[]=new int [60];
        int OMoveY[]=new int [60];

        int numXMoves, numOMoves;
        numXMoves=getMoveList(board, XMoveX, XMoveY , 1);
        numOMoves=getMoveList(board, OMoveX, OMoveY , 0);
        if ((numXMoves == 0) && (numOMoves == 0))
            return true;
        return false;
    }

    int minimaxValue(int board[][], int originalTurn, int currentTurn, int searchPly)
    {
        if ((searchPly == 4) || gameOver(board)) // Change to desired ply lookahead
        {
            return heuristic(board, originalTurn);
        }
        int moveX[]=new int [60];
        int moveY[]=new int [60];
        int numMoves;
        char opponent = 1;
        if (currentTurn == 1)
            opponent = 0;
        numMoves=getMoveList(board, moveX, moveY, currentTurn);
        if (numMoves == 0) // if no moves skip to next player's turn
        {
            return minimaxValue(board, originalTurn, opponent, searchPly + 1);
        }
        else
        {
// Remember the best move
            int bestMoveVal = -99999; // for finding max
            if (originalTurn != currentTurn)
                bestMoveVal = 99999; // for finding min
// Try out every single move
            for (int i = 0; i < numMoves; i++)
            {
// Apply the move to a new board
                int tempBoard[][]=new int[8][8];
                copyBoard(board, tempBoard);
                makeMove(tempBoard, moveX[i], moveY[i], currentTurn);
// Recursive call
                int val = minimaxValue(tempBoard, originalTurn, opponent,searchPly + 1);
// Remember best move
                if (originalTurn == currentTurn)
                {
// Remember max if it's the originator's turn
                    if (val > bestMoveVal)
                        bestMoveVal = val;
                }
                else
                {
// Remember min if it's opponent turn
                    if (val < bestMoveVal)
                        bestMoveVal = val;
                }
            }
            return bestMoveVal;
        }
    }


    Point minimaxDecision(int board[][],int difficulty)
    {
        Point point=new Point();
        int moveX[], moveY[];
        moveX=new int[60];
        moveY=new int[60];
        int numMoves;
        int whoseTurn=1;
        char opponent = 1;
        if (whoseTurn == 1)
            opponent = 0;
        numMoves=getMoveList(board, moveX, moveY, whoseTurn);
        if (numMoves == 0) // if no moves return -1
        {
            point.x = -1;
            point.y = -1;
        }
        else
        {
// Remember the best move
            int bestMoveVal = -99999;
            int bestX = moveX[0], bestY = moveY[0];
// Try out every single move
            for (int i = 0; i < numMoves; i++)
            {
// Apply the move to a new board
                int tempBoard[][]=new int [8][8];
                copyBoard(board, tempBoard);
                makeMove(tempBoard, moveX[i], moveY[i], whoseTurn);
// Recursive call, initial search ply = 1
                int val = minimaxValue(tempBoard, whoseTurn, opponent, 5-difficulty);
// Remember best move
                if (val > bestMoveVal)
                {
                    bestMoveVal = val;
                    bestX = moveX[i];
                    bestY = moveY[i];
                }
            }
// Return the best x/y
            point.x = bestX;
            point.y = bestY;
        }
        return point;
    }


}
