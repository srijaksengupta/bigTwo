//Assignment 4 - 3035437032
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game and handle all user actions.
 *
 * @author Srijak Sengupta
 *
 */

public class BigTwoTable implements CardGameTable
{
	private BigTwoClient game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	private boolean[] exist;
	private JTextArea msgArea2;
	private JTextField msgBox;
	
	/**
	 * This is a constructor for creating a BigTwoTable GUI.
	 *
	 * @param game A reference to a card game associated with this table
	 *
	 */

	public BigTwoTable(BigTwoClient game)
	{
		this.game = game;
		//As the active player can have at most 13 card, therefore we set the size of selected array to 13
		selected = new boolean[13];

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Big Two (Srijak)");
		
		//Creating an array for storing existence of players
		exist = new boolean[4];
		//Initialising all elements of this array to false
		for (int i = 0; i < 4; i++)
		{
			exist[i] = false;
		}

		//We set the initial active player
		setActivePlayer(game.getPlayerID());

		//JMenuBar, JMenu, JMenuItem
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("Game");
		JMenuItem menuconnect = new JMenuItem("Connect");
		JMenuItem menuquit = new JMenuItem("Quit");
		menuconnect.addActionListener(new ConnectMenuItemListener());
		menuquit.addActionListener(new QuitMenuItemListener());
		menu1.add(menuconnect);
		menu1.add(menuquit);
		menuBar.add(menu1);
		frame.setJMenuBar(menuBar);

		//Loading the cardImages array
		cardImages = new Image [4][13];
		String cardsource = new String();
		char suit[] = {'d','c','h','s'};
		char rank[] = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		for (int i = 0; i < 4; i++)
		{
			for(int j = 0 ; j < 13;j++)
			{
				//We create the source by concatenating
				cardsource = "src/cards/" + rank[j] + suit[i] + ".gif";
				cardImages[i][j] = new ImageIcon(cardsource).getImage();
			}
		}

		//Loading the avatars array
		avatars = new Image[4];
		avatars[0] = new ImageIcon("src/avatars/batman.png").getImage();
		avatars[1] = new ImageIcon("src/avatars/flash.png").getImage();
		avatars[2] = new ImageIcon("src/avatars/greenlantern.png").getImage();
		avatars[3] = new ImageIcon("src/avatars/superman.png").getImage();
		//Loading the image for the back of the cards
		cardBackImage = new ImageIcon("src/cards/b.gif").getImage();

		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(600,600));
		bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel,BoxLayout.Y_AXIS));
		bigTwoPanel.setBackground(Color.BLACK);

		JPanel lowerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		//Adding the listeners to the respective buttons
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		//Finally we add these two buttons to the button panel
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		//We then add the button panel to the lower panel
		lowerPanel.add(buttonPanel);
		
		JPanel tPanel = new JPanel();
		tPanel.setPreferredSize(new Dimension(500,500));
		tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.PAGE_AXIS));

		//Handling the text area for showing the current game status as well as end of game messages.
		msgArea = new JTextArea ();
		msgArea.setLineWrap(true);
		//Making a vertical scroller
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tPanel.add(scroller);
		
		//Handling the text area which is the the chat area.
		msgArea2 = new JTextArea ();
		msgArea2.setLineWrap(true);
		//Making a vertical scroller
		JScrollPane scroller2 = new JScrollPane(msgArea2);
		scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tPanel.add(scroller2);
		
		//Handling the text box which the clients use to send messages
		JLabel textlabel = new JLabel("Message: ");
		msgBox = new JTextField(30);
		//Adding the key listener to the text field
		msgBox.addKeyListener(new EnterKeyListener());
		//Creating a panel (msgPanel) for adding the text label and msgBox
		JPanel msgPanel = new JPanel();
		msgPanel.add(textlabel);
		msgPanel.add(msgBox);
		//We add the msgPanel to the lower panel
		lowerPanel.add(msgPanel);
		
		frame.add(bigTwoPanel, BorderLayout.CENTER);
		frame.add(lowerPanel, BorderLayout.SOUTH);
		frame.add(tPanel, BorderLayout.EAST);
		
		frame.setSize(1400, 1400);
		frame.setVisible(true);
		msgArea.setEditable(false);
		msgArea2.setEditable(false);
	}

	@Override
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer = activePlayer;
	}

	@Override
	public int[] getSelected()
	{
		int count1 = 0;
		for(int i=0;i<13;i++)
		{
			if(selected[i]==true)
			{
				count1++;
			}
		}

		int cardselindices[] = null;
		int count2 = 0;

		if(count1 != 0)
		{
			cardselindices = new int[count1];
			for(int j=0;j<13;j++)
			{
				if(selected[j]==true)
				{
					cardselindices[count2] = j;
					count2++;
				}
			}
		}
		return cardselindices;
	}

	@Override
	public void resetSelected()
	{
		//We need to basically make the cards selected unselected
		for(int i=0;i<13;i++)
		{
			selected[i] = false;
		}
	}

	@Override
	public void repaint()
	{
		this.resetSelected();
		frame.repaint();
	}

	@Override
	public void printMsg(String msg)
	{
		msgArea.append(msg + "\n");
	}
	
	/**
	 * This is a method which prints the specified string to the message area for chat
	 *
	 * @param msg The string to be printed to the message area for chat
	 *
	 */
	
	public void printChat(String msg) 
	{
		msgArea2.append(msg+ "\n");
	}

	@Override
	public void clearMsgArea()
	{
		msgArea.setText("");
	}
	
	/**
	 * This is a method which clears the message area for chat.
	 *
	 */
	
	public void clearChatArea()
	{
		msgArea2.setText("");
	}
	
	@Override
	public void reset()
	{
		frame.setVisible(false);
		//Making a BigTwoDeck instance, shuffling it and starting a game
		BigTwoDeck deck = new BigTwoDeck();
		deck.shuffle();
		game.start(deck);
		//Printing out information about game restart to the information text field
		printMsg("Game Restarted!");
	}

	@Override
	public void enable()
	{
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	@Override
	public void disable()
	{
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * This is a method setting the existence of player with playerID passed as argument to false.
	 *
	 * @param playerID The playerID of the player whose existence is to be set to false
	 *
	 */
	
	public void setNon_Existence(int playerID)
	{
		exist[playerID] = false;
	}
	
	/**
	 * This is a method setting the existence of player with playerID passed as argument to true.
	 *
	 * @param playerID The playerID of the player whose existence is to be set to true
	 *
	 */
	
	public void setExistence(int playerID)
	{
		exist[playerID] = true;
	}

	/**
	 * The BigTwoPanel class is an inner class that extends the JPanel class and implements the MouseListener interface.
	 * It overrides the paintComponent() method and the the mouseClicked() method.
	 *
	 * @author Srijak Sengupta
	 *
	 */

	public class BigTwoPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = -1000L;

		/**
		 * This is a constructor for creating a BigTwoPannel class and adds the MouseListener to the BigTwoPannel
		 *
		 */

		public BigTwoPanel()
		{
			this.addMouseListener(this);
		}

		/**
		 * This is an overridden method inherited from the JPanel class to draw the card game table.
		 *
		 * @param g A Graphics object which is the actual drawing canvas which provides us with graphic based functions
		 *
		 */
		
		public void paintComponent(Graphics g)
		{
			
			super.paintComponent(g);
			//We create an instance of the Graphics2D class because it can do more than a Graphics object
			Graphics2D g2D = (Graphics2D) g;
			this.setBackground(Color.GREEN.darker().darker().brighter());

			//We run a nested loop to serve the existence of different numbers of players playing the game at that time
			if(exist[0]==true)
			{
				int numcards = game.getPlayerList().get(0).getNumOfCards();
				//This check is done to change colour of currently active player to blue as per sample GUI
				if ((game.getCurrentIdx()==0) && (numcards != 0))
		        {
		        		g.setColor(Color.BLUE);
		        }
				if(activePlayer == 0)
				{
					g.drawString("You", 10, 25);
				}
				else
				{
					g.drawString(game.getPlayerList().get(0).getName(), 10, 25);
				}
				g.setColor(Color.BLACK);
				g.drawImage(avatars[0], 10, 45, this);
			}
			
			//Here, we use the Graphics2D object to draw a line as in the sample GUI
			g2D.drawLine(0, 150, 1500, 150);
			
			//We take the case when the active player is "Player 0"
			if(game.getPlayerID() == 0)
			{
				CardList clist = game.getPlayerList().get(0).getCardsInHand();
				int numcards2 = game.getPlayerList().get(0).getNumOfCards();
				for(int i=0;i<numcards2;i++)
				{
					if(selected[i]==true)
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 50-20, this);
					}
					else
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 50, this);
					}
				}
			}
			else
			{
				int numcards3 = game.getPlayerList().get(0).getNumOfCards();
				for(int i=0;i<numcards3;i++)
				{
					g.drawImage(cardBackImage, 155+40*i, 50, this);
				}
			}
			
			if(exist[1]==true)
			{
				int numcards = game.getPlayerList().get(1).getNumOfCards();
				//This check is done to change colour of currently active player to blue as per sample GUI
				if ((game.getCurrentIdx()==1) && (numcards != 0))
		        {
		        		g.setColor(Color.BLUE);
		        }
				if(activePlayer == 1)
				{
					g.drawString("You", 10, 170);
				}
				else
				{
					g.drawString(game.getPlayerList().get(1).getName(), 10, 170);
				}
				g.setColor(Color.BLACK);
				g.drawImage(avatars[1], 10, 170, this);
			}
			
			//Here, we use the Graphics2D object to draw a line as in the sample GUI
			g2D.drawLine(0, 300, 1500, 300);

			//We then take the case when the active player is "Player 1"
			if(game.getPlayerID() == 1)
			{
				CardList clist = game.getPlayerList().get(1).getCardsInHand();
				int numcards2 = game.getPlayerList().get(1).getNumOfCards();
				for(int i=0;i<numcards2;i++)
				{
					if(selected[i]==true)
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 200-20, this);
					}
					else
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 200, this);
					}
				}
			}
			else
			{
				int numcards3 = game.getPlayerList().get(1).getNumOfCards();
				for(int i=0;i<numcards3;i++)
				{
					g.drawImage(cardBackImage, 155+40*i, 200, this);
				}
			}
			
			
			if(exist[2]==true)
			{
				int numcards = game.getPlayerList().get(2).getNumOfCards();
				//This check is done to change colour of currently active player to blue as per sample GUI
				if ((game.getCurrentIdx()==2) && (numcards != 0))
		        {
		        		g.setColor(Color.BLUE);
		        }
				if(activePlayer == 2)
				{
					g.drawString("You", 10, 335);
				}
				else
				{
					g.drawString(game.getPlayerList().get(2).getName(), 10, 335);
				}
				g.setColor(Color.BLACK);
				g.drawImage(avatars[2], 10, 335, this);
			}
			
			//Here, we use the Graphics2D object to draw a line as in the sample GUI
			g2D.drawLine(0, 450, 1500, 450);

			//We then take the case when the active player is "Player 2"
			if(game.getPlayerID() == 2)
			{
				CardList clist = game.getPlayerList().get(2).getCardsInHand();
				int numcards2 = game.getPlayerList().get(2).getNumOfCards();
				for(int i=0;i<numcards2;i++)
				{
					if(selected[i]==true)
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 350-20, this);
					}
					else
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 350, this);
					}
				}
			}
			else
			{
				int numcards3 = game.getPlayerList().get(2).getNumOfCards();
				for(int i=0;i<numcards3;i++)
				{
					g.drawImage(cardBackImage, 155+40*i, 350, this);
				}
			}
			
			if(exist[3]==true)
			{
				int numcards = game.getPlayerList().get(3).getNumOfCards();
				//This check is done to change colour of currently active player to blue as per sample GUI
				if ((game.getCurrentIdx()==3) && (numcards != 0))
		        {
		        		g.setColor(Color.BLUE);
		        }
				if(activePlayer == 3)
				{
					g.drawString("You", 10, 465);
				}
				else
				{
					g.drawString(game.getPlayerList().get(3).getName(), 10, 465);
				}
				g.setColor(Color.BLACK);
				g.drawImage(avatars[3], 10, 465, this);
			}
			
			//Here, we use the Graphics2D object to draw a line as in the sample GUI
			g2D.drawLine(0, 600, 1500, 600);

			//We then take the case when the active player is "Player 3"
			if(game.getPlayerID() == 3)
			{
				CardList clist = game.getPlayerList().get(3).getCardsInHand();
				int numcards2 = game.getPlayerList().get(3).getNumOfCards();
				for(int i=0;i<numcards2;i++)
				{
					if(selected[i]==true)
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 500-20, this);
					}
					else
					{
						g.drawImage(cardImages[clist.getCard(i).getSuit()][clist.getCard(i).getRank()], 155+40*i, 500, this);
					}
				}
			}
			else
			{
				int numcards3 = game.getPlayerList().get(3).getNumOfCards();
				for(int i=0;i<numcards3;i++)
				{
					g.drawImage(cardBackImage, 155+40*i, 500, this);
				}
			}

			if(!game.getHandsOnTable().isEmpty())
			{
				Hand lastHandOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size() -1);
				g.drawString("Last Hand played by " + lastHandOnTable.getPlayer().getName(), 10, 615);
				for (int i = 0; i < lastHandOnTable.size(); i++)
	    		{
	    			g.drawImage(cardImages[lastHandOnTable.getCard(i).getSuit()][lastHandOnTable.getCard(i).getRank()], 155+40*i, 623, this);
	    		}
			}
			else
			{
				g.drawString("No Hand on the table", 10, 615);
			}

			repaint();
		}
		
		/**
		 * This is an overridden method from the MouseListener interface to handle mouse click events.
		 *
		 * @param event An event which is an object of the MouseEvent class
		 *
		 */
	
		public void mouseClicked(MouseEvent event)
		{
			//We start iterating from the last card in the active players hand.
			//This simplifies handling of overlapping cards and enables click on right side of cards as well
			int start = game.getPlayerList().get(activePlayer).getNumOfCards()-1;
			boolean check = false;
			int cardwidth = 73;
			int cardlength = 97;
			int sx = 155;
			int sy = 40;
			
			//We first initialise the selected array and flag using start as an array index
			//One thing we note is that we are using values we used in paintComponent() function ,i.e. 155 and 4 which are starting x and starting y values
			if ((event.getX() >= (sx+(start*sy))) && (event.getX() <= (sx+(start*sy)+cardwidth)))
			{
				if ((selected[start] == true) && (event.getY() >= (50 + 145 * (activePlayer-20))) && (event.getY() <= (50 + 145 * (activePlayer+cardlength-20))))
				{
					check = true;
					selected[start] = false;
				}
				else
				if((selected[start] == false) && (event.getY() >= (50 + 145 * activePlayer)) && (event.getY() <= (50 + 145 * (activePlayer+cardlength)))) 
				{
					check = true;
					selected[start] = true;
				}
			}
			
			//Initialising start 
			start = game.getPlayerList().get(activePlayer).getNumOfCards()-2;
			while(start >= 0 && check==false)
			{
				if ((event.getX() >= (sx+(start*sy))) && (event.getX() <= (sx+(start*sx)+sx)))
				{
					if ((selected[start] == true) && (event.getY() >= (50 + (145 * activePlayer-20))) && (event.getY() <= (50 + 145 * (activePlayer+cardlength-20))))
					{
						check = true;
						selected[start] = false;
					}
					else
					if((selected[start] == false) && (event.getY() >= (50 + (145 * activePlayer))) && (event.getY() <= (50 + ((145 * activePlayer)+cardlength))))
					{
						check = true;
						selected[start] = true;
					} 
				}
				else 
				if ((event.getX() >= (sx+(start*sy)+sy)) && (event.getX() <= (sx+(start*sy)+cardwidth)) && (event.getY() >= (50 + (145 * activePlayer-20))) && (event.getY() <= (50 + (145 * activePlayer+cardlength-20))))
				{
					if ((selected[start+1] == false) && (selected[start] == true))
					{
						check = true;
						selected[start] = false;
					}
				}
				else 
				if ((event.getX() >= (sx+(start*sy)+sy)) && (event.getX() <= (sx+(start*sy)+cardwidth)) && (event.getY() >= (50 + (145 * activePlayer))) && (event.getY() <= (50 + (145 * activePlayer+cardlength)))) 
				{
					if ((selected[start+1] == true) && (selected[start] == false))
					{
						check = true;
						selected[start] = true;
					}
				}
				start = start-1;
			}
			
			this.repaint();	
		}

		@Override
		public void mousePressed(MouseEvent event)
		{

		}

		@Override
		public void mouseReleased(MouseEvent event)
		{

		}

		@Override
		public void mouseEntered(MouseEvent event)
		{

		}

		@Override
		public void mouseExited(MouseEvent event)
		{

		}
	}

	/**
	 * The PlayButtonListener class is an inner class that implements the ActionListener interface.
	 * It implements the actionPerformed() method from the ActionListener interface to handle button-click events for the Play button.
	 *
	 * @author Srijak Sengupta
	 *
	 */


	public class PlayButtonListener implements ActionListener
	{
		/**
		 * This is an overridden method which handles button-click events for the Play button
		 * When the Play button is clicked, we call the makeMove() method of the CardGame object to make a move.
		 *
		 * @param event An event which is an object of the ActionEvent class
		 *
		 */

		public void actionPerformed(ActionEvent event)
		{
			if (game.getCurrentIdx() == activePlayer)
			{
				if(getSelected()!=null)
				{
					game.makeMove(activePlayer, getSelected());
					resetSelected();
					repaint();
				}
				else
				{
					printMsg("Player " + activePlayer  + "'s turn:");
					printMsg("No cards selected!");
					repaint();
				}
			}
			//This else case handles the scenario of a player trying to play when it is not his/her turn
			else
			{
				printMsg("Not your turn!");
				resetSelected();
				repaint();
			}
		}
	}

	/**
	 * The PassButtonListener class is an inner class that implements the ActionListener interface.
	 * It implements the actionPerformed() method from the ActionListener interface to handle button-click events for the Pass button.
	 *
	 * @author Srijak Sengupta
	 *
	 */

	public class PassButtonListener implements ActionListener
	{
		/**
		 * This is an overridden method which handles button-click events for the Pass button.
		 * When the Pass button is clicked, we call the makeMove() method of the CardGame object to make a move.
		 *
		 * @param event An event which is an object of the ActionEvent class
		 *
		 */

		public void actionPerformed(ActionEvent event)
		{
			if (game.getCurrentIdx() == activePlayer)
			{
				//As it is a "Pass" move, null is passed as a parameter along with activePlayer variable while calling makeMove() function
				game.makeMove(activePlayer, null);
				resetSelected();
				repaint();
			}
			//This else case handles the scenario of a player trying to pass when it is not his/her turn
			else
			{
				printMsg("Not your turn!");
				resetSelected();
				repaint();
			}
		}
	}

	/**
	 * The RestartMenuItemListener class is an inner class that implements the ActionListener interface.
	 * It implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the Restart menu item.
	 *
	 * @author Srijak Sengupta
	 *
	 */

	public class ConnectMenuItemListener implements ActionListener
	{
		/**
		 * This is an overridden method which handles menu-item-click events for the Connect menu item.
		 *
		 * @param event An event which is an object of the ActionEvent class
		 *
		 */

		
		public void actionPerformed(ActionEvent event)
		{
			if(game.getPlayerID() == -1) 
			{
				game.makeConnection();
			}
			//This else case handles the scenario where the player is already connected
			else
			if((game.getPlayerID() >= 0)&&(game.getPlayerID() <= 3))
			{
				printMsg("Player connection already established!");
			}
		}
		
	}

	/**
	 * The QuitMenuItemListener class is an inner class that implements the ActionListener interface.
	 * It implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the Quit menu item.
	 *
	 * @author Srijak Sengupta
	 *
	 */

	public class QuitMenuItemListener implements ActionListener
	{
		/**
		 * This is an overridden method which handles menu-item-click events for the Quit menu item.
		 *
		 * @param event An event which is an object of the ActionEvent class
		 *
		 */

		public void actionPerformed(ActionEvent event)
		{
			printMsg("Game ended!");
			System.exit(0);
		}
	}
	
	/**
	 * The EnterKeyListener class is an inner class that implements the KeyListener interface.
	 * It implements the keyPressed() method from the KeyListener interface to to handle the inputs from text field msgBox.
	 *
	 * @author Srijak Sengupta
	 *
	 */

	
	private class EnterKeyListener implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent event) 
		{
			if(event.getKeyCode() == KeyEvent.VK_ENTER)
			{
				String msg = msgBox.getText();
				//If message field is not empty, we handle the message by calling sendMessage() function
				System.out.println(msg);
				game.sendMessage(new CardGameMessage(7,activePlayer,msg));
				//Resetting text in text field msgBox
				msgBox.setText("");
				msgBox.requestFocus();
			}
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			
		}

		@Override
		public void keyTyped(KeyEvent e) 
		{
			
		}
	}
}