//Assignment 4 - 3035437032

/**
 * The Single class is a subclass of the Hand class, and is used to model a hand of single in a Big Two game.
 * This class should override the methods of the Hand class as appropriate.
 * 
 * @author Srijak Sengupta
 *
 */

public class Single extends Hand implements ITF
{
	private static final long serialVersionUID = -1000L;
	
	/**
	 * This is a constructor for building a hand of single with the specified player and list of cards.
	 * 
	 * @param player The value of this CardGamePlayer object signifies the player in the game who possesses the current hand of cards 
	 * @param cards The value of this CardList object signifies list of cards held by the active player
	 *
	 */
	
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * This is an overridden method for retrieving the top card of this hand of single.
	 * 
	 * @return The top card of this hand of single
	 *
	 */
	
	public Card getTopCard()
	{
		return this.getCard(0);
	}
	
	/**
	 * This is an overridden method for checking if this hand of single is a valid hand.
	 * 
	 * @return Whether the hand of single is valid
	 *
	 */
	
	public boolean isValid()
	{
		if(this.size()==1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This is an overridden method for returning a string specifying the type of this hand of single.
	 * 
	 * @return A string specifying the type this hand of single
	 *
	 */
	
	public String getType()
	{
		String type = "Single";
		return type;
		
	}
}