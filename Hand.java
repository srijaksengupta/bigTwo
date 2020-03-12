//Assignment 4 - 3035437032

/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. 
 * 
 * @author Srijak Sengupta
 *
 */

public class Hand extends CardList implements ITF
{
	private static final long serialVersionUID = -1000L;
	private CardGamePlayer player;
	
	/**
	 * This is a constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The value of this CardGamePlayer object signifies list of players in the game
	 * @param cards The value of this CardList object signifies list of cards held by the active player
	 *
	 */
	
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player = player;
		for(int i=0;i<cards.size();i++)
		{
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * This is a getter method for retrieving the player of this hand.
	 * 
	 * @return The player of this hand
	 *
	 */
	
	public CardGamePlayer getPlayer()
	{
		return this.player;
	}
	
	/**
	 * This is a getter method for retrieving the top card of this hand.
	 * 
	 * @return The top card of this hand which is null in Hand class
	 *
	 */
	
	public Card getTopCard()
	{
		return null;
	}
	
	/**
	 * This is a method for checking if this hand beats a specified hand.
	 * 
	 * @param hand This hand of the active player
	 * @return Whether this hand beats a specified hand in terms of true or false
	 *
	 */
	
	public boolean beats(Hand hand)
	{
		if((hand.size()==1)||(hand.size()==2)||(hand.size()==3))
		{
			//We need to check if the size of this hand and the specified hand is the same
			//We also have to check whether this hand is valid
			//If the above 2 checks pass, we need to compare the top cards of the hands and check which hand beats the other and return true or false accordingly
			if(this.size()==hand.size())
			{
				if(this.isValid())
				{
					if((this.getTopCard().compareTo(hand.getTopCard()))==1)
				    {
						return true;
				    }
				}
			}
		}
		if(hand.size()==5)
		{
			if(this.size()==hand.size())
			{
				if(this.isValid())
				{
					//Handling the Straight hand case
					if(this.getType()=="Straight")
					{
						if(this.getType()==hand.getType())
						{
							if((this.getTopCard().compareTo(hand.getTopCard()))==1)
							{
								return true;
							}
							else
							if((this.getTopCard().compareTo(hand.getTopCard()))==-1)
							{
								return false;
							}
						}
						else
						if(this.getType()!=hand.getType())
						{
							return false;
						}
					}
					//Handling the Flush hand case
					if(this.getType()=="Flush")
					{
						if(this.getType()==hand.getType())
						{
							if((this.getTopCard().compareTo(hand.getTopCard()))==1)
							{
								return true;
							}
							else
							if((this.getTopCard().compareTo(hand.getTopCard()))==-1)
							{
								return false;
							}
						}
						else
						if(hand.getType()=="Straight")
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					//Handling the Full House hand case
					if(this.getType()=="FullHouse")
					{
						if(this.getType()==hand.getType())
						{
							if((this.getTopCard().compareTo(hand.getTopCard()))==1)
							{
								return true;
							}
							else
							if((this.getTopCard().compareTo(hand.getTopCard()))==-1)
							{
								return false;
							}
						}
						else
						if(hand.getType()=="Straight"||hand.getType()=="Flush")
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					//Handling the Quad hand case
					if(this.getType()=="Quad")
					{
						if(this.getType()==hand.getType())
						{
							if((this.getTopCard().compareTo(hand.getTopCard()))==1)
							{
								return true;
							}
							else
							if((this.getTopCard().compareTo(hand.getTopCard()))==-1)
							{
								return false;
							}
						}
						else
						if(hand.getType()=="Straight"||hand.getType()=="Flush"||hand.getType()=="FullHouse")
						{
							return true;
						}
						else
						{
							return false;
						}
					}
					//Handling the Straight Flush hand case
					if(this.getType()=="StraightFlush")
					{
						if(this.getType()==hand.getType())
						{
							if((this.getTopCard().compareTo(hand.getTopCard()))==1)
							{
								return true;
							}
							else
							if((this.getTopCard().compareTo(hand.getTopCard()))==-1)
							{
								return false;
							}
						}
						else
						if(this.getType()!=hand.getType())
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * This is a method for checking if this is a valid hand.
	 * 
	 * @return Whether the hand is valid in terms of true or false (false in Hand class)
	 *
	 */
	
	public boolean isValid()
	{
		return false;
	}
	
	/**
	 * This is a method for returning a string specifying the type of this hand.
	 * 
	 * @return A string specifying the type of hand (null string in Hand class)
	 *
	 */
					
	public String getType()
	{
		return null;
	}						
}