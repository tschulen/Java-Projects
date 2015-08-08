package cs180battleships;
//server side of client/server BattleShips program
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BattleShipsServer implements Runnable , GameStateChangedListener {
	//private String[] board = new String[100]; // BattleShip board
	private String outputMessage;
	//private Player[] players; // array of players
	private ServerSocket server; // server socket to connect with clients
	//private int currentPlayer; // keeps track of player with current move
	//private int otherPlayer;
//	private final static int PLAYER_X = 0; // constant for first player
//	private final static int PLAYER_O = 1; // constant for second player
//	private final static String[] MARKS = { "X", "O" }; // array of marks
	public ExecutorService runGame; // will run players
//	public Lock gameLock; // to lock game for synchronization
//	public Condition otherPlayerConnected; // to wait for other player
//	public Condition otherPlayerTurn; // to wait for other players turn
	@SuppressWarnings("unchecked")
	private ArrayList serverChangedListers = new ArrayList();
	private GameState gameState;
	

	// set up BattleShips server and GUI that displays messages
	public BattleShipsServer() {

		// create ExecutorService with thread for each player
		runGame = Executors.newFixedThreadPool(2);

		gameState = new GameState();
		gameState.addGameStateChangedListener(this);

		//players = new Player[2]; // create array of players
		//currentPlayer = PLAYER_X; // set current player to first player
		try {
			server = new ServerSocket(12345, 2); // set up server socket
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
			System.exit(1);
		} // end catch

		setOutputMessage("Server awaiting connections\n");
		// create and start worker thread for this client
	} // end BattleShipsServer constructor

	// wait for two connections so game can be played
	public void run() {
		// wait for each client to connect
		for (int i = 0; i < gameState.getPlayerCount(); i++) {
			try // wait for connection, create Player, start runnable
			{
				gameState.players[i]=gameState.new StatePlayer(server.accept(), i);
				//players[i] = new Player(server.accept(), i);
				runGame.execute(gameState.players[i]); // execute player runnable
			} // end try
			catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(1);
			} // end catch
		} // end for

		gameState.gameLock.lock(); // lock game to signal player X's thread

		try {
			gameState.players[0].setSuspended(false); // resume player x
			gameState.otherPlayerConnected.signal(); // wake up player X's thread
		} // end try
		finally {
			gameState.gameLock.unlock(); // unlock game after signaling player X
		} // end finally
	}// end method execute

	// display message in outputArea
	private void displayMessage(final String messageToDisplay) {
		// display message from event-dispatch thread of execution
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setOutputMessage(messageToDisplay);
			}// end method run
		}// end inner class
				); // end call to swingutilities.invokelater
	} // end method display message
	
	//place code in this method to determine whether game over
	//public boolean isGameOver()
	//{
	//	return gameState.isGameOver(); // this is left as an exercise
	//} // end method isGameOver
	
	public void setOutputMessage(String message){
		outputMessage=message;
		fireServerChanged();
	}
	public String getMessage() {
		return outputMessage;
	}
	/**
	 * Allows an observer to register for BalanceChanged events.
	 */
	@SuppressWarnings("unchecked")
	public void addServerChangedListener(ServerChangedListener listener) {
		serverChangedListers.add(listener);
	}

	/**
	 * Allows an observer to unregister for BalanceChanged events.
	 */
	public void removeServerChangedListener(ServerChangedListener listener) {
		serverChangedListers.remove(listener);
	}

	/**
	 * Iterates through all listeners, informing them that a BalanceChanged event has occurred.
	 */
	private void fireServerChanged() {
		for (int i = 0; i < serverChangedListers.size(); i++) {
			ServerChangedListener listener = (ServerChangedListener) serverChangedListers.get(i);
			listener.serverChanged(this);
		}
	}

	@Override
	public void gameStateChanged(GameState gameState) {
		// TODO Auto-generated method stub
		setOutputMessage(gameState.getMessage());
	}
	
} // end class BattleShipsServer
