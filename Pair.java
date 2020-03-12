//Assignment 4 - 3035437032

/**
 * The Pair class is a subclass of the Hand class, and is used to model a hand of Pair in a Big Two game.
 * This class should override the methods of the Hand class as appropriate.
 * 
 * @author Srijak Sengupta
 *
 */

public class Pair extends Hand implements ITF
{
	private static final long serialVersionUID = -1000L;
	
	/**
	 * This is a constructor for building a hand of pair with the specified player and list of cards.
	 * 
	 * @param player The value of this CardGamePlayer object signifies the player in the game who possesses the current hand of cards 
	 * @param cards The value of this CardList object signifies list of cards held by the active player
	 *
	 */
	
	public Pair(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * This is an overridden method for retrieving the top card of this hand of pair.
	 * 
	 * @return The top card of this hand of pair
	 *
	 */
	
	public Card getTopCard()
	{
		if((this.getCard(0).suit)>(this.getCard(1).suit))
		{
			return this.getCard(0);
		}
		else
		{
			return this.getCard(1);
		}
	}
	
	/**
	 * This is an overridden method for checking if this hand of pair is a valid hand.
	 * 
	 * @return Whether the hand of pair is valid
	 *
	 */
	
	public boolean isValid()
	{
		if((this.size()==2)&&((this.getCard(0).rank)==(this.getCard(1).rank)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This is an overridden method for returning a string specifying the type of this hand of pair.
	 * 
	 * @return A string specifying the type this hand of pair
	 *
	 */
	
	public String getType()
	{
		String type = "Pair";
		return type;
		
	}
}