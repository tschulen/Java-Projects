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

public class GameClient implements Runnable , GameBoardListener
{
	private String idText;
	private String displayMessage;
	//private Square currentSquare; // current square
	private Socket connection; // connection to the server
	private Scanner input; // input from server
	private Formatter output; // output to server
	private String BattleShipsHost; // host name for server
	//private String myMark; // this client's mark
	private boolean myTurn; // determines which client's turn it is
	//private final String X_MARK = "X"; // mark for first client
	//private final String O_MARK = "O"; // mark for second client
	private ArrayList clientChangedListers = new ArrayList();
	public GameBoard gameBoard;
	
	// set up user-interface and board
	public GameClient( String host )
	{
		BattleShipsHost = host; // set name of server
		
		gameBoard = new GameBoard();
		gameBoard.addGameBoardListener(this);
		
		startClient();
	} // end BattleShipsClient constructor
	
	// start the client thread
	public void startClient()
	{
		try
		{
			// make connection to server
			connection = new Socket(
				InetAddress.getByName( BattleShipsHost ), 12345 );
			
			// get streams for input and output
			input = new Scanner( connection.getInputStream() );
			output = new Formatter( connection.getOutputStream() );
		} // end try
		catch( IOException ioException )
		{
			ioException.printStackTrace();
		} // end catch
		
		// create and start worker thread for this client
		ExecutorService worker = Executors.newFixedThreadPool( 1 );
		worker.execute( this ); // execute client
	} // end method startClient
	
	// control thread that allows continuous update of displayArea
	public void run()
	{
		gameBoard.myMark = input.nextLine(); // get player's mark (X or O)
		
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					// display player's mark
					gameBoard.idField.setText( "You are Player\"" + gameBoard.myMark + "\"");
					idText="You are Player\"" + gameBoard.myMark + "\"";
				} // end method run
			} // end anonymous inner class
		); // end call to SwingUtilities.InvokeLater
		
		myTurn = ( gameBoard.myMark.equals( "X") ); // determine if client's turn
		
///////////
		//// insert wait for ship locations
///////////
		// receive messages sent to client and output them
		while ( true )
		{
			if ( input.hasNextLine() )
				processMessage( input.nextLine() );
		} // end while
	} // end method run
	
	// process messages received by client
	private void processMessage( String message )
	{
		// valid move occurred
		if ( message.equals( "Valid move." ) )
		{
			displayMessage( "Valid move, please wait.\n" );
			gameBoard.setMark( gameBoard.currentSquare, gameBoard.myMark ); // set mark in square
		} // end if
		else if ( message.equals( "Invalid move, try again" ) )
		{
			displayMessage( message + "\n" ); // display invalid move
			myTurn = true; // still this client's turn
		} // end else if
		else if ( message.equals( "Opponent moved" ) )
		{
			int location = input.nextInt(); //get move location
			input.nextLine(); // skip newline after int location
			int row = location / 10; //calculate row
			int column = location % 10; // calculate column
			
			gameBoard.setMark( gameBoard.board[ row ][ column ],
				( gameBoard.myMark.equals( "X" ) ? "O" : "X" ) ); //mark move
			displayMessage( "Opponent moved. Your turn.\n" );
			myTurn = true; // now this clients turn
		} // end else if
		else
			displayMessage( message + "\n" ); // display the message
	} // end method processMessage
	
	// manipulate displayArea in event-dispatch thread
	private void displayMessage( final String messageToDisplay )
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					gameBoard.displayArea.append( messageToDisplay ); //updates output
					setDisplayMessage(messageToDisplay);
					
				} // end method run
			} // end inner class
		); // end call to SwingUtilities.invokeLater
	} // end method displayMessage

	@SuppressWarnings("unchecked")
	public void addClientChangedListener(ClientChangedListener listener) {
		clientChangedListers.add(listener);
	}

	/**
	 * Allows an observer to unregister for BalanceChanged events.
	 */
	public void removeClientChangedListener(ClientChangedListener listener) {
		clientChangedListers.remove(listener);
	}

	/**
	 * Iterates through all listeners, informing them that a BalanceChanged event has occurred.
	 */
	private void fireClientChanged() {
		for (int i = 0; i < clientChangedListers.size(); i++) {
			ClientChangedListener listener = (ClientChangedListener) clientChangedListers.get(i);
			listener.clientChanged(this);
		}
	}

	public void setDisplayMessage(String displayMessage) {
		this.displayMessage = displayMessage;
		fireClientChanged();
	}

	public String getDisplayMessage() {
		return displayMessage;
	}

	public String getIdText() {
		return idText;
	}

	@Override
	public void gameBoardChanged(GameBoard gameBoard) {
		// if it is my turn
		if ( myTurn )
		{
			output.format( "%d\n", gameBoard.getCurrentSquareLocation() ); // send location to server
			output.flush();
			myTurn = false; // not my turn any more
		} // end if
	}
} // end class BattleShipsClient
