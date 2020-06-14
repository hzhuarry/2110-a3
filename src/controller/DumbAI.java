package controller;

import model.Board;
import model.Game;
import model.Location;
import model.NotImplementedException;
import model.Player;

/**
 * A DumbAI is a Controller that always chooses the blank space with the
 * smallest column number from the row with the smallest row number.
 */
public class DumbAI extends Controller {

	public DumbAI(Player me) {
		super(me);
	}
	
	/**
	 * returns the first empty available space. If no spaces are available, the location will be null
	 */
	protected @Override Location nextMove(Game g) {
		for(Location l: Board.LOCATIONS)
		{
			if(g.getBoard().get(l)==null) {
				return l;
			}
		}
		return null;
	}
}
