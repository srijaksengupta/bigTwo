//Assignment 3 - 3035437032

/**
 * The BigTwoDeck class is a subclass of the Deck class, and is used to model a deck of cards used in a Big Two card game.
 * It should override the initialize() method it inherited from the Deck class to create a deck of Big Two cards.
 * 
 * @author Srijak Sengupta
 *
 */

public class BigTwoDeck extends Deck
{
	private static final long serialVersionUID = -1000L;
	
	/**
	 * A method for initialising a deck of Big Two cards.
	 * It should remove all cards from the deck, create 52 Big Two cards and add them to the deck.
	 *
	 */
	
	public void initialize() 
	{
		removeAllCards();
		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 13; j++) 
			{
				BigTwoCard bigtwocard = new BigTwoCard(i, j);
				this.addCard(bigtwocard);
			}
		}
	}
}