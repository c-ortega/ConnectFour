package core_algorithms;

import problems.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the classic Minimax search algorithm (without alpha-beta pruning).
 *
 * Assumptions:
 * - Human is the MAX player
 * - AI is the MIN player
 *
 * @param <A> the type representing a move or action in the game
 */
public class Minimax<A> {
    protected final Game<A> game;

    /**
     * Record to store the score of a game state and the path of moves leading to it.
     */
    public record ScoreMove<A> (int score, List<A> pathOfMoves){}

    public Minimax(Game<A> game) {
        this.game = game;
    }

    /**
     * Performs a minimax search and returns the best move for the current player.
     *
     * @return the first move on the path to the best outcome for the MIN player (i.e., AI)
     */
    public A minimaxSearch(){
        ScoreMove<A> b = min();
        return b.pathOfMoves().get(0);
    }

    /**
     * MAX node in the minimax tree. Returns the move path that maximizes utility.
     *
     * @return best score and path of moves for the MAX player
     */
    public ScoreMove<A> max(){
        if(game.isTerminal()){
            return new ScoreMove<>(game.utility(),new ArrayList<>());
        }else{
            int bestScore = Integer.MIN_VALUE;
            List<A> bestPath = new ArrayList<>();
            for(A move : game.getAllRemainingMoves()){
                game.execute(move, true);
                ScoreMove<A> a = min();
                game.undo(move, true);
                if (a.score() > bestScore ||
                        //If the eventual utility score is the same, prefer the shorter path to get there
                        //i.e., win faster
                        (a.score()==bestScore && a.pathOfMoves().size()+1 < bestPath.size())){
                    bestScore = a.score();
                    bestPath = a.pathOfMoves();
                    bestPath.add(0, move);
                }
            }
            return new ScoreMove<>(bestScore,bestPath);
        }
    }

    /**
     * MIN node in the minimax tree. Returns the move path that minimizes utility.
     *
     * @return best score and path of moves for the MIN player
     */
    public ScoreMove<A> min(){
        if(game.isTerminal()){
            return new ScoreMove<>(game.utility(), new ArrayList<>());
        }else {
            int bestScore = Integer.MAX_VALUE;
            List<A> bestPath = new ArrayList<>();
            for (A move : game.getAllRemainingMoves()) {
                game.execute(move, false);
                ScoreMove<A> b = max();
                game.undo(move, false);
                if (b.score() < bestScore ||
                        //This is optional: if the eventual utility score is the same,
                        // prefer the shorter path to get there, i.e., win faster.
                        (b.score()==bestScore && b.pathOfMoves().size()+1 < bestPath.size())) {
                    bestScore = b.score();
                    bestPath = b.pathOfMoves();
                    bestPath.add(0, move);
                }
            }
            return new ScoreMove<>(bestScore, bestPath);
        }

    }
}
