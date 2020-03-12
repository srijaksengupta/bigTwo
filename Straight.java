//Assignment 4 - 3035437032
import java.util.Arrays;

/**
 * The Straight class is a subclass of the Hand class, and is used to model a hand of Straight in a Big Two game.
 * This class should override the methods of the Hand class as appropriate.
 * 
 * @author Srijak Sengupta
 *
 */

public class Straight extends Hand implements ITF
{
	private static final long serialVersionUID = -1000L;
	
	/**
	 * This is a constructor for building a hand of straight with the specified player and list of cards.
	 * 
	 * @param player The value of this CardGamePlayer object signifies the player in the game who possesses the current hand of cards 
	 * @param cards The value of this CardList object signifies list of cards held by the active player
	 *
	 */
	
	public Straight(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * This is an overridden method for retrieving the top card of this hand of straight.
	 * 
	 * @return The top card of this hand of straight
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
	 * This is an overridden method for checking if this hand of straight is a valid hand.
	 * 
	 * @return Whether the hand of straight is valid
	 *
	 */
	
	public boolean isValid()
	{
		if(this.size()!=5)
		{
			return false;
		}
		//Checking whether the 5 cards ranks are consecutive starts here
		int rks[]= {this.getCard(0).rank, this.getCard(1).rank, this.getCard(2).rank, this.getCard(3).rank, this.getCard(4).rank};
		for(int i=0;i<5;i++)
		{
			if(rks[i]==0)
			{
				rks[i] = 13;
			}
			else
			if(rks[i]==1)
			{
				rks[i] = 14;
			}
		}
		//We need to sort the array containing the ranks in ascending order to determine whether they are consecutive or not
		Arrays.sort(rks);
		//Iterating through the array and checking
		for(int j=1;j<rks.length-1;j++)
		{
			if((rks[j+1]-rks[j])!=1)
			{
				return false;
			}
		}
		//If all these conditions for invalidity are not satisfies, then we return true
		return true;
	}
	
	/**
	 * This is an overridden method for returning a string specifying the type of this hand of straight.
	 * 
	 * @return A string specifying the type this hand of straight
	 *
	 */
	
	public String getType()
	{
		String type = "Straight";
		return type;
		
	}
}