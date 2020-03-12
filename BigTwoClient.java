//Assignment 4 - 3035437032
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. It is used to model a Big Two card game that supports 4 players playing over the internet.
 *
 * @author Srijak Sengupta
 *
 */

public class BigTwoClient implements CardGame, NetworkGame
{
	private int numOfPlayers;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID; 
	private String playerName; 
	private String serverIP; 
	private int serverPort; 
	private Socket sock;
	private ObjectOutputStream oos;
	private int currentIdx; 
	private BigTwoTable table; 
	private ObjectInputStream ois;
	
	/**
	 * This is a constructor for creating a Big Two client.
	 *
	 */
	
	
	public BigTwoClient()
	{
		handsOnTable = new ArrayList<Hand>();
		playerList = new ArrayList<CardGamePlayer>();
		
		//Here, we create 4 players and add them to the list of players
		for (int i = 0; i<4; i++)
		{
			this.playerList.add(new CardGamePlayer());
		}
		
		//Here, we create a Big Two table which builds the GUI for the game and handles user actions
		table = new BigTwoTable(this);
		table.disable();
		
		//As stated in the behaviour of the Big Two client, it should prompt the user to enter his/her name when it starts.
		playerName = (String)JOptionPane.showInputDialog("Enter your name: ");
		
		//Setting the serverIP address and TCP serverPort
		setServerIP("127.0.0.1");
		setServerPort(2396);
		
		//Here, we make a connection to the game server by calling the makeConnection() method from the NetworkGame interface
		makeConnection();
		table.repaint();
	}
	
	//The methods below are all NetworkGame interface methods which need to be implemented
	
	@Override
	public int getPlayerID()
	{
		return playerID;
	}
		
	@Override
	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}
		
	@Override
	public String getPlayerName()
	{
		return playerName;
	}
		
	@Override
	public void setPlayerName(String playerName)
	{
		playerList.get(playerID).setName(playerName);
	}
		
	@Override
	public String getServerIP()
	{
		return serverIP;
	}
		
	@Override
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}
		
	@Override
	public int getServerPort()
	{
		return serverPort;
	}
		
	@Override
	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}
		
	@Override
	public void makeConnection()
	{
		//This is a method for making a socket connection with the game server
		//To catch exceptions, we use try-catch exception handling method
		try {
			//Here, we make a socket connection with the game server
			sock = new Socket(this.serverIP, this.serverPort);
		}catch (Exception ex1) {ex1.printStackTrace();}
			
		//Upon successful connection, we should create an ObjectOutputStream for sending messages to the game server
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			//We create an ObjectOutputStream as well
			ois = new ObjectInputStream(sock.getInputStream());
		}catch (IOException ex2) {ex2.printStackTrace();}
			
		//Here, we create a thread for receiving messages from the game server
		Runnable threadJob = new ServerHandler();
		Thread thread1 = new Thread(threadJob);
		thread1.start();
			
		//Here, we send a message of the type JOIN to the game server, with playerID being -1 and data being a reference to a string representing the name of the local player
		sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
			
		//Here we send a message of the type READY to the game server, with playerID and data being -1 and null, respectively
		sendMessage(new CardGameMessage(4, -1, null));
			
		table.repaint();
	}
		
	@Override
	public void parseMessage(GameMessage message)
	{
		//Handling message type PLAYER_LIST
		//Here, the playerID specifies the playerID (i.e., index) of the local player of this client
		//In addition to this, the data here is a reference to a regular array of strings specifying the names of the existing players.
		if(message.getType() == 0)
		{
			//Setting player ID of the player who has connected 
			playerID = message.getPlayerID();
			//Setting active player 
			table.setActivePlayer(playerID);
			//Setting the names of participants
			for (int i = 0; i < 4; i++)
			{
				//We check if the particular data part of the message is null or not 
				if (((String[])message.getData())[i] != null)
				{
					this.playerList.get(i).setName(((String[])message.getData())[i]);
					table.setExistence(i);
				}
			}
			table.repaint();
		}
			
		//Handling message type JOIN
		//Here, The playerID specifies the playerID (i.e., index) of the new player
		//In addition to this, the data here is a reference to a string specifying the name of this new player.
		else
		if(message.getType() == 1)
		{
			//Setting the name of the player who joined the game
			this.playerList.get(message.getPlayerID()).setName((String)message.getData());
			//Setting existence of the new player
			table.setExistence(message.getPlayerID());
			table.repaint();
			//Printing details about the message in text area for game messages
			table.printMsg("Player " + playerList.get(message.getPlayerID()).getName() + " joined the game!");
		}
			
		//Handling message type FULL
		//Here, the playerID and data in this message are -1 and null
		else
		if(message.getType() == 2)
		{
			playerID = -1;
			//Printing details about the message in text area for game messages
			table.printMsg("Server is full and cannot join the game!");
			table.repaint();
		}
			
		//Handling message type QUIT
		//Here, the playerID specifies the playerID (i.e., index) of the player who leaves the game
		//In addition to this, the data is a reference to a string representing the IP address and TCP port of this player
		else
		if(message.getType() == 3)
		{
			//Printing details about the message in text area for game messages
			table.printMsg("Player " + message.getPlayerID() + " - " + playerList.get(message.getPlayerID()).getName() + " left the game!");
			//The player is removed from the game by setting his/her name to an empty string
			playerList.get(message.getPlayerID()).setName("");
			//Now that the player has left the game, we have to set the non-existence of the player
			table.setNon_Existence(message.getPlayerID());
				
			//If a game is in progress, the client should stop the game
			//Then, the client should send a message of type READY, with playerID and data being -1 and null, respectively, to the server
			if(this.endOfGame()==false)
			{
				table.disable();
				//Sending a message of type READY
				this.sendMessage(new CardGameMessage(4, -1, null));
				//Then, we remove all the cards for that client's board
				for (int j = 0; j < 4; j++)
				{
					playerList.get(j).removeAllCards();
				}
				table.repaint();
			}
			table.repaint();
		}
			
		//Handling message type READY
		//Here, the playerID specifies the playerID (i.e., index) of the player who is ready
		//In addition to this, the data is null
		else
		if(message.getType() == 4)
		{
			//Printing details about the message in text area for game message
			table.printMsg("Player " + message.getPlayerID() + " - " + playerList.get(message.getPlayerID()).getName() + " is ready!");
			//Now that the player is ready, we create the handsOnTable Array list
			handsOnTable = new ArrayList<Hand>();
			table.repaint();
		}
			
		//Handling message type START
		//Here, the is -1 and the data is a reference to a (shuffled) BigTwoDeck object
		else
		if(message.getType() == 5)
		{
			//We start a new game with the new deck of cards (already shuffled)
			start((BigTwoDeck)message.getData());
			//Printing details about the message in text area for game message
			table.printMsg("Game starts!");
			table.enable();
			table.repaint();
		}
			
		//Handling message type MOVE
		//Here, the playerID and data are -1 and a reference to a regular array of integers specifying the indices of the cards selected by the local player, respectively
		else
		if(message.getType() == 6)
		{
			//We call checkmove() to check the move made by the player
			checkMove(message.getPlayerID(), (int[])message.getData());
			table.repaint();
			
		}
			
		//Handling message type MSG
		//Here, the playerID and data are -1 and a reference to a string representing the text in the text field, respectively
		else
		if(message.getType() == 7)
		{
			//Printing message from the text input field to chat area
			table.printChat((String)message.getData());
		}
			
		else
		{
			table.printMsg("Incorrect message type!");
			table.repaint();
		}
	}
		
	@Override
	public void sendMessage(GameMessage message)
	{
		try {
			//Here, we send the specified message to the game server
			oos.writeObject(message);
		}catch (Exception ex) {ex.printStackTrace();}
	}
	
	/**
	* The ServerHandler class is an inner class that implements the Runnable interface.
	*
	* @author Srijak Sengupta
	*
	*/
		
	class ServerHandler implements Runnable
	{
		@Override
		public void run() 
		{
			CardGameMessage msg = null;
			//IO Operations can cause exception and thus, we enclose these operations in a try and catch exception handling block
			try
			{
				while ((msg = (CardGameMessage) ois.readObject()) != null)
				{
					//Upon receiving a message, the parseMessage() method from the NetworkGame interface should be called to parse the messages accordingly
					parseMessage(msg);
				}
			} catch (Exception ex) {ex.printStackTrace();}
			
			table.repaint();
		}
	}
	
	
	//The methods below are all CardGame interface methods which need to be implemented
	
	@Override
	public int getNumOfPlayers()
	{
		return numOfPlayers;
	}
	
	@Override
	public Deck getDeck()
	{
		return deck;
	}
	
	@Override
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return playerList;
	}
	
	@Override
	public ArrayList<Hand> getHandsOnTable()
	{
		return handsOnTable;
	}
	
	@Override
	public int getCurrentIdx()
	{
		return currentIdx;
	}
	
	@Override
	public void start(Deck deck)
	{
		this.deck = deck;
		//First, we remove all the cards from the table
		getHandsOnTable().clear();
		
		//We need to distribute the 52 cards in the deck between 4 players.
		//Therefore, each player will get 13 cards each from index 0 to 12.
		//Before, distributing, we first remove all the cards from the players going player by player
		int card_no_index = 0;
		for(int i = 0; i < 4; i++)    
		{
			getPlayerList().get(i).removeAllCards();
			for(int j = 0; j < 13; j++)
				
			{
				getPlayerList().get(i).addCard(deck.getCard(card_no_index+i+j));
			
			}
			card_no_index = card_no_index + 12;
		}
		
		//After distributing, each player gets their respective cards in hand and sorts them accordingly
		for(int i = 0; i < 4;i++)
		{
			getPlayerList().get(i).sortCardsInHand();
		}
		
		currentIdx = -1;
		//The rules of the game states that we have to identify the player who holds the 3 of Diamonds
		//Then, we need to set the currentIdx of the BigTwoClient instance to the playerID (i.e., index) of the player who holds the 3 of Diamonds
		for(int i = 0; i < 4;i++)
		{
			if(getPlayerList().get(i).getCardsInHand().contains(new Card(0,2)))
			{
				currentIdx = i;
				break;
			}
		}
		table.repaint();
		table.setActivePlayer(playerID);
	}
	
	@Override
	public void makeMove(int playerID, int[] cardIdx)
	{
		//Here, we create a CardGameMessage object of the type MOVE, with the playerID and data in this message being -1 and cardIdx, respectively
		//Then, we send it to the game server using the sendMessage() method from the NetworkGame interface
		CardGameMessage movemsg = new CardGameMessage(6, playerID, cardIdx);
		sendMessage(movemsg);
	}
	
	@Override
	public void checkMove(int playerID, int[] cardIdx)
	{
		if(cardIdx!=null) 
		{
			//Here, we handle the case when there are no hands on the table
			if(getHandsOnTable().size() == 0)
			{
				//This CardList object stores the cards which the active player plays at that turn
				CardList cardsplayed = new CardList();
				
				//Loading the cardsplayed array
				for(int i=0; i<cardIdx.length; ++i)
				{
					cardsplayed.addCard(getPlayerList().get(currentIdx).getCardsInHand().getCard(cardIdx[i]));	
				}
				
				Hand hand=composeHand(getPlayerList().get(currentIdx), cardsplayed);
				
				if(hand!=null)
				{
					if(hand.getCard(0).getRank()!=2||hand.getCard(0).getSuit()!=0)
					{
						table.printMsg("<==Not a legal move!!!");
					}
					else
					{
						table.printMsg(getPlayerList().get(currentIdx).getName()+"'s move : "+"{"+hand.getType()+"}");
						
						//Printing cards in the hand played
						for (int j=0; j<hand.size(); ++j)
						{
							table.printMsg(" ["+hand.getCard(j).toString()+"]");
						}
						
						getPlayerList().get(currentIdx).removeCards(hand);
						currentIdx = (currentIdx + 1) % 4;
						//Adding hand played to the hands on the table
						getHandsOnTable().add(hand);
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn :");
					}
				}
				else
				{
					table.printMsg("<==Not a legal move!!!");
				}
			}
			
			else
			{
				//This CardList object stores the cards which the active player plays at that turn
				CardList cardsplayed = new CardList();
				
				//Loading the cardsplayed array
				for(int i=0; i<cardIdx.length; ++i)
				{
					cardsplayed.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
					
				}
				
				Hand hand=composeHand(playerList.get(currentIdx), cardsplayed);
				
				if(getHandsOnTable().get(getHandsOnTable().size()-1).getPlayer().getName()==playerList.get(currentIdx).getName())
				{
					if (hand!=null)
					{
						table.printMsg(playerList.get(currentIdx).getName()+"'s move : "+"{"+hand.getType()+"}");
						
						//Printing cards in the hand played
						for (int j=0; j<hand.size(); ++j)
						{
							table.printMsg(" ["+hand.getCard(j).toString()+"]");
						}
						
						playerList.get(currentIdx).removeCards(hand);
						currentIdx = (currentIdx + 1) % 4;
						//Adding hand played to the hands on the table
						handsOnTable.add(hand);
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn :");
					}
					else
					{
						table.printMsg("<==Not a legal move!!!");
					}
				}
				
				else
				{
					if(hand!=null)
					{
						if (hand.size()==getHandsOnTable().get(getHandsOnTable().size()-1).size())
						{
							//Here, we handle the case where the selected hand is weaker than the last hand on the table
							if (getHandsOnTable().get(getHandsOnTable().size()-1).beats(hand)==true)
							{
								table.printMsg("<==Not a legal move!!!");
							}
							else 
							if(hand!=null)
							{
								table.printMsg(playerList.get(currentIdx).getName()+"'s move : "+"{"+hand.getType()+"}");
								
								//Printing cards in the hand played
								for (int j=0; j<hand.size(); ++j)
								{
									table.printMsg(" ["+hand.getCard(j).toString()+"]");
								}

								playerList.get(currentIdx).removeCards(hand);
								currentIdx = (currentIdx + 1) % 4;
								//Adding hand played to the hands on the table
								handsOnTable.add(hand);
								table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn : ");
							}
						}
						
						else
						{
							table.printMsg("<==Not a legal move!!!");
						}
					}
					
					else
					{
						table.printMsg("<==Not a legal move!!!");
					}
				}
			}
		}
		else
		{
			if(cardIdx==null)
			{
				//The case where there are no hands on the table
				if(getHandsOnTable().size()==0)
				{
					table.printMsg("<==Not a legal move!!!");
				}
				
				else if(handsOnTable.get(getHandsOnTable().size()-1).getPlayer().getName()==playerList.get(currentIdx).getName())
				{
					table.printMsg("<==Not a legal move!!!");
				}
				
				//Handling a valid pass move
				else
				{
					table.printMsg(playerList.get(currentIdx).getName()+"'s move: "+"{pass}");
					currentIdx = (currentIdx + 1) % 4;
					table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn :");
				}
			}
		}
		
		//This case handles the scenario when the game ends
		if(endOfGame()==true)
		{
			//Sets the active player to -1 in order to indicate end of game
			table.setActivePlayer(-1);
			table.repaint();
			
			//Printing of end of game information starts here
			String endms = "Game ends\n";
			
			int i=0;
		    while(i < getPlayerList().size())
		    {
		      if(getPlayerList().get(i).getCardsInHand().size() == 0)
		      {
		        //Prints the winner of the game
		    	endms = endms + getPlayerList().get(i).getName() + " wins the game \n";
		      }
		      else
		      {
		        //Prints the card information the remaining 3 players
		    	endms = endms + getPlayerList().get(i).getName() + " has " + getPlayerList().get(i).getCardsInHand().size() + " cards in hand\n";
		      }
		      i++;
		    }
		    
		    int okopt = JOptionPane.showOptionDialog(null, endms, null, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			//When the user clicks the “OK” button on the dialog box, the dialog box should close
		    //Also, the client should send a message of the type READY, with playerID and data being -1 and null, respectively, to the server
		    if(okopt == JOptionPane.OK_OPTION)
			{
				//Here, we send a message of the type READY
				sendMessage(new CardGameMessage(4,-1,null));
			}
			
		    table.disable();
		}
		//This case handles the scenario when the game has not ended
		else
		{
			//Resetting the cards selected to non selected position
			table.resetSelected();
			//Enabling and disabling buttons for the active player
			if(currentIdx == this.playerID)
			{
				table.enable();
			}
			else
			{
				table.disable();
			}
			
			table.repaint();
		}
	} 

	@Override
	public boolean endOfGame()
	{
		for(int i=0;i<getPlayerList().size();i++)
		{
			int numcards = getPlayerList().get(i).getNumOfCards();
			if(numcards == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This is the main method for creating an instance of BigTwoClient.
	 * 
	 * @param args unused
	 *
	 */
	
	public static void main(String[] args)
	{
		BigTwoClient bigtwoclient = new BigTwoClient();
	}
	
	/**
	 * This is a method for returning a valid hand from the specified list of cards of the player.
	 * 
	 * @param player The value of this CardGamePlayer object signifies a given player who's turn it is currently
	 * @param cards The value of this CardList object signifies the specified list of cards of the player
	 *
	 * @return A valid hand from the specified list of cards of the player
	 * 
	 */
	
	private static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		Single single = new Single(player,cards);
		Pair pair = new Pair(player,cards);
		Triple triple = new Triple(player,cards);
		Straight straight = new Straight(player,cards);
		Flush flush = new Flush(player, cards);
		FullHouse fullhouse = new FullHouse(player,cards);
		Quad quad = new Quad(player,cards);
		StraightFlush straightflush = new StraightFlush(player,cards);
		
		//Checking and returning a valid hand of cards from the specified list of cards of the player
		//Returns null if no valid hand can be composed from the specified list of cards
		//We start checking from the strongest hands because they are more beneficial
		//This is also done to solve problems of a hand of straight flush being treated as a hand of flush  
		if(straightflush.isValid())
		{
			return straightflush;
		}
		if(quad.isValid())
		{
			return quad;
		}
		if(fullhouse.isValid())
		{
			return fullhouse;
		}
		if(flush.isValid())
		{
			return flush;
		}
		if(straight.isValid())
		{
			return straight;
		}
		if(triple.isValid())
		{
			return triple;
		}
		if(pair.isValid())
		{
			return pair;
		}
		if(single.isValid())
		{
			return single;
		}
		return null;
	}	
}
