//Assignment 3 - 3035437032

/**
 * The is an interface containing 2 abstract functions isValid() and getType()
 * 
 * @author Srijak Sengupta
 *
 */

public interface ITF
{
	/**
	 * This is an abstract method for checking if this is a valid hand.
	 *
	 *@return Whether the hand is valid or not in terms of true or false
	 */
	
	public abstract boolean isValid();
	
	/**
	 * This is an abstract method for returning a string specifying the type of this hand.
	 *
	 *@return A string specifying the type of this hand
	 */
	
	public abstract String getType();
}