//Assignment 4 - 3035437032

/**
 * The Flush class is a subclass of the Hand class, and is used to model a hand of Flush in a Big Two game.
 * This class should override the methods of the Hand class as appropriate.
 * 
 * @author Srijak Sengupta
 *
 */

public class Flush extends Hand implements ITF
{
	private static final long serialVersionUID = -1000L;
	
	/**
	 * This is a constructor for building a hand of flush with the specified player and list of cards.
	 * 
	 * @param player The value of this CardGamePlayer object signifies the player in the game who possesses the current hand of cards 
	 * @param cards The value of this CardList object signifies list of cards held by the active player
	 *
	 */
	
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * This is an overridden method for retrieving the top card of this hand of flush.
	 * 
	 * @return The top card of this hand of flush
	 *
	 */
	
	public Card getTopCard()
	{
		int toprank = -1;
		int top_card_index = -1;
		for(int i=0;i<5;i++)
		{
			if((this.getCard(i).rank)==1)
			{
				toprank = this.getCard(i).rank+14;
				top_card_index = i;
				break;
			}
			else
			if((this.getCard(i).rank)==0)
			{
				toprank = this.getCard(i).rank+13;
				top_card_index = i;
			}
			else
			if((this.getCard(i).rank)>toprank)
			{
				toprank = this.getCard(i).rank;
				top_card_index = i;
			}
		}
		return this.getCard(top_card_index);
	}
	
	/**
	 * This is an overridden method for checking if this hand of flush is a valid hand.
	 * 
	 * @return Whether the hand of flush is valid
	 *
	 */
	
	public boolean isValid()
	{
		if((this.size()==5)&&(((this.getCard(0).suit)==(this.getCard(1).suit))&&((this.getCard(1).suit)==(this.getCard(2).suit))&&((this.getCard(2).suit)==(this.getCard(3).suit))&&((this.getCard(3).suit)==(this.getCard(4).suit))))
		{
			return true;
		}
		else
		{
			return false;
		}	
	}
	
	/**
	 * This is an overridden method for returning a string specifying the type of this hand of flush.
	 * 
	 * @return A string specifying the type this hand of flush
	 *
	 */
	
	public String getType()
	{
		String type = "Flush";
		return type;
		
	}
}