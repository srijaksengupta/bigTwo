//Assignment 3 - 3035437032

/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game.
 * It should override the compareTo() method it inherited from the Card class to reflect the ordering of cards used in a Big Two card game.
 * 
 * @author Srijak Sengupta
 *
 */

public class BigTwoCard extends Card
{
	private static final long serialVersionUID = -1000L;
	/**
	 * This is a constructor for building a card with the specified suit and rank.
	 * 
	 * @param suit
	 *            an int value between 0 and 3 representing the suit of a card:
	 *            <p>
	 *            0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank
	 *            an int value between 0 and 12 representing the rank of a card:
	 *            <p>
	 *            0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11
	 *            = 'Q', 12 = 'K'
	 *
	 */
	
	public BigTwoCard(int suit, int rank)
	{
		super(suit,rank);
	}
	
	/**
	 * A method for comparing the order of this card with the specified card.
	 * Returns a negative integer, zero, or a positive integer as this card is less than, equal to, or greater than the specified card.
	 * 
	 * @param card The value of this Card object signifies the card to be compared
	 *
	 */
	
	public int compareTo(Card card)
	{
		//Since we cannot modify the ranks, we create copies
		int rankthis = this.rank;
		int rankcard = card.rank;
		//Since A and 2 are the 2 largest card, their rank values have to changed for comparison
		if(rankthis == 0)
		{
			rankthis = rankthis + 13;
		}
		else
		if(rankthis == 1)
		{
			rankthis = rankthis + 14;
		}
		
		if(rankcard == 0)
		{
			rankcard = rankcard + 13;
		}
		else
		if(rankcard == 1)
		{
			rankcard = rankcard + 14;
		}
		//The suit values don't have special cases like in rank and do not need to be changed
		//Modifying the code in the super class as class
		if (rankthis > rankcard) 
		{
			return 1;
		} 
		else if (rankthis < rankcard) 
		{
			return -1;
		} 
		else if (this.suit > card.suit) 
		{
			return 1;
		} 
		else if (this.suit < card.suit) 
		{
			return -1;
		} 
		else 
		{
			return 0;
		}
	}
}