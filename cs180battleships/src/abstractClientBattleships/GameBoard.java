package abstractClientBattleships;
// Client side of client/server BattleShips program
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class GameBoard extends JFrame
{
	public JTextField idField; // text field to display player's mark
	public String idText;
	public JTextArea displayArea; // JTextArea to display output
	public String displayMessage;
	public JPanel boardPanel; // panel for BattleShips board
	public JPanel panel2; // panel to hold board
	public Square[][] board; // BattleShips board
	Square currentSquare; // current square
	public int currentSquareLocation;
	public Socket connection; // connection to the server
	public Scanner input; // input from server
	public Formatter output; // output to server
	public String BattleShipsHost; // host name for server
	public String myMark; // this client's mark
	public boolean myTurn; // determines which client's turn it is
	public final String X_MARK = "X"; // mark for first client
	public final String O_MARK = "O"; // mark for second client
	private ArrayList gameBoardListeners = new ArrayList();
	
	public GameBoard(){
		displayArea = new JTextArea( 4, 30 ); // set up JTextArea
		displayArea.setEditable( false );
		add( new JScrollPane( displayArea ), BorderLayout.SOUTH );
		
		boardPanel = new JPanel(); // set up panel for squares in board
		boardPanel.setLayout( new GridLayout( 10, 10, 0, 0 ) );
		
		board = new Square [ 10 ][ 10 ]; //create board
		
		// loop over the rows in the board
		for( int row = 0; row < board.length; row++ )
		{
			//loop over the columns in the board
			for ( int column = 0; column < board[ row ].length; column ++ )
			{
				//create square
				board[ row ][ column ] = new Square( " ", row * 10 + column );
				boardPanel.add( board[ row ][ column ] ); //add square
			} // end inner for
		} // end outer for
		
		idField = new JTextField(); // set up text field
		idField.setEditable( false );
		add( idField, BorderLayout.NORTH );
		
		panel2 = new JPanel(); // set up panel to contain boardPanel
		panel2.add( boardPanel, BorderLayout.CENTER); // add board panel
		add( panel2, BorderLayout.CENTER ); //add container panel
		
		setSize( 500, 500 ); // set size of window
		setVisible( true ); // show window
	}
	public void setMark( final Square squareToMark, final String mark)
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					squareToMark.setMark( mark ); // set mark in square
				} // end method run
			} // end anonymous inner class
		); // end call to SwingUtilities.invokeLater
	} // end method setMark
	
	// set current Square
	public void setCurrentSquare( Square square )
	{
		currentSquare = square; // set current square to argument
		currentSquareLocation = square.location;
	} // end method setCurrentSquare
	
	public int getCurrentSquareLocation() {
		return currentSquareLocation;
	}
	@SuppressWarnings("unchecked")
	public void addGameBoardListener(GameBoardListener listener) {
		gameBoardListeners.add(listener);
	}

	/**
	 * Allows an observer to unregister for BalanceChanged events.
	 */
	public void removeGameBoardListener(GameBoardListener listener) {
		gameBoardListeners.remove(listener);
	}

	/**
	 * Iterates through all listeners, informing them that a BalanceChanged event has occurred.
	 */
	private void fireGameBoardChanged() {
		for (int i = 0; i < gameBoardListeners.size(); i++) {
			GameBoardListener listener = (GameBoardListener) gameBoardListeners.get(i);
			listener.gameBoardChanged(this);
		}
	}

	/// private inner class for the square on the board
	@SuppressWarnings("serial")
	private class Square extends JPanel
	{
		public String mark; // mark to be drawn in this square
		public int location; // location of square
		
		public Square( String squareMark, int squareLocation )
		{
			mark = squareMark; // set mark for this square
			location = squareLocation; // set location of this square
			
			addMouseListener(
				new MouseAdapter()
				{
					public void mouseReleased( MouseEvent e )
					{
						setCurrentSquare( Square.this ); // set current square
						
						// send location of this square
						fireGameBoardChanged();
						//sendClickedSquare( getSquareLocation() );
					} // end method mouseReleased
				} // end anonymous inner class
			); // end call to addMouseListener
		} // end Square constructor
		
		// return preferred size of Square
		public Dimension getPreferredSize()
		{
			return new Dimension( 30, 30 ); // return preferred size
		} // end method getPreferredSize
		
		// return minimum size of Square
		public Dimension getMinimumSize()
		{
			return getPreferredSize(); // return preferred size
		} // end method getMinimumSize
		
		// set mark for Square
		public void setMark( String newMark )
		{
			mark = newMark; // set mark of square
			repaint(); //repaint square
		} // end method setMark
		
		// return Square location
		public int getSquareLocation()
		{
			return location; // return location of square
		} // end method getSquareLocation
		
		// draw Square
		public void paintComponent( Graphics g )
		{
			super.paintComponent( g );
			
			g.drawRect(0, 0, 29, 29); // draw square
			g.drawString( mark, 11, 20); // draw mark
		} // end method paintComponent
	} // end inner-class Square
	
} // end class BattleShipsClient
