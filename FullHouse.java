//Assignment 4 - 3035437032

/**
 * The FullHouse class is a subclass of the Hand class, and is used to model a hand of FullHouse in a Big Two game.
 * This class should override the methods of the Hand class as appropriate.
 * 
 * @author Srijak Sengupta
 *
 */

public class FullHouse extends Hand implements ITF
{
	private static final long serialVersionUID = -1000L;
	
	/**
	 * This is a constructor for building a hand of full house with the specified player and list of cards.
	 * 
	 * @param player The value of this CardGamePlayer object signifies the player in the game who possesses the current hand of cards 
	 * @param cards The value of this CardList object signifies list of cards held by the active player
	 *
	 */
	
	public FullHouse(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * This is an overridden method for retrieving the top card of this hand of full house.
	 * 
	 * @return The top card of this hand of full house
	 *
	 */
	
	public Card getTopCard()
	{
		//Sorting the cards in this hand
		this.sort();
		//Based on rank, the triplet may include cards placed in indices 0,1,2 or 2,3,4 after sorting
		//Thus, the strongest suit among the triplet cards will either be in indices 2 or 4 after sorting
		//Similarly, based on rank, the pair may include cards placed in indices 3,4 or 0,1 after sorting
		////Thus, the strongest suit among the pair cards will either be in indices 4 or 1 after sorting
		if(((this.getCard(0).rank)==(this.getCard(1).rank))&&((this.getCard(1).rank)==(this.getCard(2).rank)))
		{
			return this.getCard(2);
		}
		else
		{
			return this.getCard(4);
		}
	}
	
	/**
	 * This is an overridden method for checking if this hand of full house is a valid hand.
	 * 
	 * @return Whether the hand of full house is valid
	 *
	 */
	
	public boolean isValid()
	{
		if(this.size()!=5)
		{
			return false;
		}
		//Sorting the cards in this hand
		this.sort();
		//Based on rank, the triplet may include cards placed in indices 0,1,2 or 2,3,4 after sorting 
		//Similarly, based on rank, the pair may include cards placed in indices 3,4 or 0,1 after sorting
		if(((this.getCard(0).rank)==(this.getCard(1).rank))&&((this.getCard(1).rank)==(this.getCard(2).rank))&&((this.getCard(3).rank)==(this.getCard(4).rank)))
		{
			return true;
		}
		else
		if(((this.getCard(0).rank)==(this.getCard(1).rank))&&((this.getCard(2).rank)==(this.getCard(3).rank))&&((this.getCard(3).rank)==(this.getCard(4).rank)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This is an overridden method for returning a string specifying the type of this hand of full house.
	 * 
	 * @return A string specifying the type this hand of full house
	 *
	 */
	
	public String getType()
	{
		String type = "FullHouse";
		return type;
		
	}
}