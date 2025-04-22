package solutions;

import core_algorithms.MinimaxPrune;
import problems.ConnectFour;
import problems.Mark;
import problems.Square;

import java.util.Scanner;

public class ConnectFourRunner extends MinimaxPrune<Square> {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private Mark turn = Mark.X;
    private final ConnectFour game;
    private final int DEPTH_LIMIT = 15;

    public ConnectFourRunner(ConnectFour game) {
        super(game);
        this.game = game;
    }

    public void play(){
        while(!game.isTerminal()){
            game.printBoard();
            System.out.println();
            if(turn == Mark.X){
                game.execute(getUserMove(), true);
                turn = Mark.O;
            }else {
                System.out.println("AI's turn:");
                game.execute(minimaxSearch(DEPTH_LIMIT), false);
                turn = Mark.X;
            }
        }
        game.printBoard();
        announceWinner(game.utility());
    }

    private Square getUserMove(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.printf("Your turn: enter a column (0-%d): ", COLUMNS - 1);
            if(scanner.hasNextInt()){
                int column = scanner.nextInt();
                if(column >= 0 && column < COLUMNS){
                    for(int row = ROWS - 1; row >= 0; row--){
                        Square square = new Square(row, column);
                        if(!game.markedSquare(square)){
                            return square;
                        }
                    }
                    System.out.println("Column is full. Try another.");
                } else{
                    System.out.println("Please enter a valid column.");
                }
            }else{
                scanner.next();
                System.out.println("Please enter a valid column.");
            }
        }

    }

    private void announceWinner(int utility){
        if(utility == 1){
            System.out.println("\nPlayer (X) wins!");
        }else if(utility == -1){
            System.out.println("\nPlayer (O) wins!");
        }else {
            System.out.println("\nIt's a draw!");
        }
    }

    public static void main(String[] args) {
        ConnectFourRunner runner = new ConnectFourRunner(new ConnectFour());
        runner.play();
    }
}
