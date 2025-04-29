package problems;

import java.util.*;

public class ConnectFour implements Game<Square>{

    private final int ROWS = 6;
    private final int COLUMNS = 7;
    private final Map<Square, Mark> board;
    private final int WINCOUNT = 4;


    public ConnectFour() {
        board = new HashMap<>();
    }

    @Override
    public boolean isTerminal() {
        int utility = utility();
        if (Math.abs(utility) >= 1_000_000){
            return true;
        }
        return board.size() == ROWS * COLUMNS;
    }

    @Override
    public int utility() {
        if (isWinning(Mark.X)) return 1_000_000;
        if (isWinning(Mark.O)) return -1_000_000;


        return evaluateBoard(Mark.X) - evaluateBoard(Mark.O);
    }

    private boolean isWinning(Mark mark) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (countConsecutive(row, col, 0, 1, mark) >= WINCOUNT) return true;
                if (countConsecutive(row, col, 1, 0, mark) >= WINCOUNT) return true;
                if (countConsecutive(row, col, 1, 1, mark) >= WINCOUNT) return true;
                if (countConsecutive(row, col, 1, -1, mark) >= WINCOUNT) return true;
            }
        }
        return false;
    }

    private int evaluateBoard(Mark mark) {
        int score = 0;

        for (int row = 0; row < ROWS; row++) {
            Square center = new Square(row, COLUMNS / 2);
            if(board.get(center) == mark){
                score += 3;
            }
        }
        score += evaluateWindow(mark);

        return score;
    }

    private int evaluateWindow(Mark mark) {
        int score = 0;

        int[][] directions = {
                {0, 1},
                {1, 0},
                {1, 1},
                {1, -1}
        };
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                for (int[] direction : directions) {
                    int count = 0;
                    int empty = 0;
                    int opponentCount = 0;

                    for (int i = 0; i < WINCOUNT; i++) {
                        int r = row + direction[0] * i;
                        int c = col + direction[1] * i;

                        if(r < 0 || r >= ROWS || c < 0 || c >= COLUMNS){ break;}

                        Square sq = new Square(r, c);
                        Mark current = board.get(sq);

                        if(current == mark){count++;}
                        else if (current == null) {empty++;}
                        else {opponentCount++;}
                    }

                    if (count == 3 && empty == 1){score += 100;}
                    else if (count == 2 && empty == 2){score += 10;}
                    if(opponentCount == 3 && empty == 1){score -= 500;}
                }
            }
        }
        return score;
    }

    private int countConsecutive(int row, int col, int dRow, int dCol, Mark mark){
        int count = 0;
        for(int i = 0; i < WINCOUNT; i++){
            int r = row + dRow * i;
            int c = col + dCol * i;
            if(r < 0 || r >= ROWS || c < 0 || c >= COLUMNS)break;
            Square square = new Square(r, c);
            if(board.get(square) != mark)break;
            count++;
        }
        return count;
    }



    @Override
    public void undo(Square move, boolean isMax) {
        board.remove(move);
    }



    @Override
    public void execute(Square move, boolean isMax) {
        if(isMax){
            board.put(move, Mark.X);
        }
        else{
            board.put(move, Mark.O);
        }

    }

    @Override
    public List<Square> getAllRemainingMoves() {
        List<Square> moves = new ArrayList<>();
        List<Integer> preferredColumns = List.of(3, 2, 4, 1, 5, 0, 6);
        for(int col : preferredColumns){
            for(int row = ROWS - 1; row >= 0; row--){
                Square square = new Square(row, col);
                if(!board.containsKey(square)){
                    moves.add(square);
                    break;
                }
            }
        }

        return moves;
    }

    public boolean markedSquare(Square square) {
        return board.containsKey(square);
    }

    public void printBoard() {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String CYAN = "\u001B[36m";

        System.out.print("   ");

        for (int col = 0; col < COLUMNS; col++) {
            System.out.print(" " + col + "  ");
        }

        System.out.println();

        for (int row = 0; row < ROWS; row++) {
            System.out.print(" " + row + " ");
            for (int col = 0; col < COLUMNS; col++) {
                Square square = new Square(row, col);
                if (board.containsKey(square)) {
                    if (board.get(square) == Mark.X) {
                        System.out.print(" " + RED + "X" + RESET + " ");
                    } else {
                        System.out.print(" " + CYAN + "O" + RESET + " ");
                    }
                } else {
                    System.out.print("   ");
                }
                if (col < COLUMNS - 1) {
                    System.out.print("|");
                }
            }

            System.out.println();

            if (row < ROWS - 1) {
                System.out.print("   ");
                for (int col = 0; col < COLUMNS; col++) {
                    System.out.print("---");
                    if (col < COLUMNS - 1) {
                        System.out.print("+");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        game.getAllRemainingMoves().forEach(System.out::println);
        game.printBoard();
    }
}
