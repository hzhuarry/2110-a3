package controller;

import model.Board;
import model.Board.State;
import model.Game;
import model.Location;
import model.NotImplementedException;
import model.Player;
import org.eclipse.jdt.annotation.NonNull;

/**
 * A MinMaxAI is a controller that uses the minimax algorithm to select the next
 * move.  The minimax algorithm searches for the best possible next move, under
 * the assumption that your opponent will also always select the best possible
 * move.
 *
 * <p>The minimax algorithm assigns a score to each possible game configuration
 * g.  The score is assigned recursively as follows: if the game g is over and
 * the player has won, then the score is infinity.  If the game g is over and
 * the player has lost, then the score is negative infinity.  If the game is a
 * draw, then the score is 0.
 * 
 * <p>If the game is not over, then there are many possible moves that could be
 * made; each of these leads to a new game configuration g'.  We can
 * recursively find the score for each of these configurations.
 * 
 * <p>If it is the player's turn, then they will choose the action that
 * maximizes their score, so the score for g is the maximum of all the scores
 * of the g's.  However, if it is the opponent's turn, then the opponent will
 * try to minimize the score for the player, so the score for g is the
 * <em>minimum</em> of all of the scores of the g'.
 * 
 * <p>You can think of the game as defining a tree, where each node in the tree
 * represents a game configuration, and the children of g are all of the g'
 * reachable from g by taking a turn.  The minimax algorithm is then a
 * particular traversal of this tree.
 * 
 * <p>In practice, game trees can become very large, so we apply a few
 * strategies to narrow the set of paths that we search.  First, we can decide
 * to only consider certain kinds of moves.  For five-in-a-row, there are
 * typically at least 70 moves available at each step; but it's (usually) not
 * sensible to go on the opposite side of the board from where all of the other
 * pieces are; by restricting our search to only part of the board, we can
 * reduce the space considerably.
 * 
 * <p>A second strategy is that we can look only a few moves ahead instead of
 * planning all the way to the end of the game.  This requires us to be able to
 * estimate how "good" a given board looks for a player.
 * 
 * <p>This class implements the minimax algorithm with support for these two
 * strategies for reducing the search space.  The abstract method {@link
 * #moves(Board)} is used to list all of the moves that the AI is willing to
 * consider, while the abstract method {@link #estimate(Board)} returns
 * the estimation of how good the board is for the given player.
 */
public abstract class MinMaxAI extends Controller {

	/**
	 * Return an estimate of how good the given board is for me.
	 * A result of infinity means I have won.  A result of negative infinity
	 * means that I have lost.
	 */
	protected abstract int estimate(Board b);
	
	/**
	 * Return the set of moves that the AI will consider when planning ahead.
	 * Must contain at least one move if there are any valid moves to make.
	 */
	protected abstract Iterable<Location> moves(Board b);
	
	/**
	 * Create an AI that will recursively search for the next move using the
	 * minimax algorithm.  When searching for a move, the algorithm will look
	 * depth moves into the future.
	 *
	 * <p>choosing a higher value for depth makes the AI smarter, but requires
	 * more time to select moves.
	 */
	private int depth;
	protected MinMaxAI(Player me, int depth) {
		super(me);
		this.depth=depth;
	}
	
	//helper method
	private @NonNull int getScore(Board b, Player p, int d)
	{
		if(b.getState()==Board.State.HAS_WINNER&&b.getWinner().winner==me) {
			return Integer.MAX_VALUE;
		}
		
		else if(b.getState()==Board.State.HAS_WINNER&&b.getWinner().winner==me.opponent()) {
			return Integer.MIN_VALUE;
		}
		else if(b.getState()==Board.State.DRAW) {
			return 0;
		}
		else if(d==0) {
			return estimate(b);
		}
		Integer nextBestMove=null;
		for(Location l : moves(b)) {
			int nextScore=getScore(b.update(p, l),p.opponent(),d-1);
			if(nextBestMove==null) {
				nextBestMove=nextScore;
			}
			if(p==me) {
				nextBestMove=Integer.max(nextBestMove, nextScore);
			}
			else {
				nextBestMove=Integer.min(nextBestMove,nextScore);
			}
		}
		if(nextBestMove==null) {
			return estimate(b);
		}
		return nextBestMove;
	}

	/**
	 * Return the move that maximizes the score according to the minimax
	 * algorithm described above.
	 */
	protected @Override Location nextMove(Game g) {
		Location bestNext=null;
		int bestScore=Integer.MIN_VALUE;
		
		for(Location move: moves(g.getBoard())) {
			Board next = g.getBoard().update(me, move);
			//base
			if(next.getState()==State.HAS_WINNER&&next.getWinner().winner==me) {
				return move;
			}
			
			int currScore= getScore(next,me.opponent(),depth-1);
			if(currScore>bestScore) {
				bestNext=move;
				bestScore=currScore;
			}
		}
		return bestNext;
	}
	
}
