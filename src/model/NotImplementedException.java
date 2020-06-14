package model;

/**
 * A NotImplementedException indicates that the software is incomplete.  You
 * should remove all use "throw new NotImplementedException()" as your default
 * method implementation, and you should remove all NotImplementedExeptions
 * before submitting/publishing your work.
 */
public class NotImplementedException extends Error {

	/** Sets the message to indicate that the method was not implemented. */
	public NotImplementedException() {
		super("Code incomplete: method not implemented.");
	}
}
