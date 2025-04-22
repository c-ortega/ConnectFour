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
        if (utility == 1 || utility == -1){
            return true;
        }
        return board.size() == ROWS * COLUMNS;
    }

    @Override
    public int utility() {
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLUMNS; col++){
                Square square = new Square(row, col);
                if(!board.containsKey(square)){continue;}
                Mark mark = board.get(square);

                if (countConsecutive(row, col, 0, 1, mark) >= WINCOUNT) return mark == Mark.X ? 1 : -1;
                if (countConsecutive(row, col, 1, 0, mark) >= WINCOUNT) return mark == Mark.X ? 1 : -1;
                if (countConsecutive(row, col, 1, 1, mark) >= WINCOUNT) return mark == Mark.X ? 1 : -1;
                if (countConsecutive(row, col, 1, -1, mark) >= WINCOUNT) return mark == Mark.X ? 1 : -1;
            }
        }


        return 0;
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
        for(int col = 0; col < COLUMNS; col++){
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
