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
public class MinimaxPrune<A> {
    protected final Game<A> game;

    /**
     * Record to store the score of a game state and the path of moves leading to it.
     */
    public record ScoreMove<A> (int score, List<A> pathOfMoves){}

    public MinimaxPrune(Game<A> game) {
        this.game = game;
    }

    /**
     * Performs a minimax search and returns the best move for the current player.
     *
     * @return the first move on the path to the best outcome for the MIN player (i.e., AI)
     */
    public A minimaxSearch(int depthLimit){
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        ScoreMove<A> b = min(alpha, beta, depthLimit);

        if(b.pathOfMoves() == null || b.pathOfMoves().isEmpty()){
            throw new IllegalStateException("No Valid moves found");
        }
        return b.pathOfMoves().get(0);
    }

    /**
     * MAX node in the minimax tree. Returns the move path that maximizes utility.
     *
     * @return best score and path of moves for the MAX player
     */
    public ScoreMove<A> max(int alpha, int beta, int depth) {
        if (game.isTerminal() || depth == 0)
            return new ScoreMove<>(game.utility(), List.of());

        List<A> bestPath = null;
        int  bestScore = Integer.MIN_VALUE;

        for (A move : game.getAllRemainingMoves()) {
            game.execute(move, true);
            ScoreMove<A> response = min(alpha, beta, depth - 1);
            game.undo(move, true);

            if (response.score > bestScore) {
                bestScore = response.score;
                bestPath  = new ArrayList<>(response.pathOfMoves());
                bestPath.add(0, move);
            }

            alpha = Math.max(alpha, bestScore);
            if (alpha >= beta) {
                // prune: we already recorded bestPath, so safe to break
                break;
            }
        }

        return new ScoreMove<>(bestScore, bestPath != null ? bestPath : List.of());
    }




    /**
     * MIN node in the minimax tree. Returns the move path that minimizes utility.
     *
     * @return best score and path of moves for the MIN player
     */
    public ScoreMove<A> min(int alpha, int beta, int depth) {
        if (game.isTerminal() || depth == 0)
            return new ScoreMove<>(game.utility(), List.of());

        List<A> bestPath = null;
        int  bestScore = Integer.MAX_VALUE;

        for (A move : game.getAllRemainingMoves()) {
            game.execute(move, false);
            ScoreMove<A> response = max(alpha, beta, depth - 1);
            game.undo(move, false);

            if (response.score < bestScore) {
                bestScore = response.score;
                bestPath  = new ArrayList<>(response.pathOfMoves());
                bestPath.add(0, move);
            }

            beta = Math.min(beta, bestScore);
            if (beta <= alpha) {
                // prune with bestPath preserved
                break;
            }
        }

        return new ScoreMove<>(bestScore, bestPath != null ? bestPath : List.of());
    }

}
