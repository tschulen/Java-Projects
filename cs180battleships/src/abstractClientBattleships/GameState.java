package abstractClientBattleships;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GameState {
	private static final int totalTargets = 5;
	@SuppressWarnings("rawtypes")
	private ArrayList shipNames=new ArrayList();
	//private String[] shipNames = {"A","B","C","D","E","F","G","H","I","J" };
	private int playerCount = 2;
	public StatePlayer[] players;;
	@SuppressWarnings({ "rawtypes" })
	private ArrayList gameStateChangedListers = new ArrayList();
	private String message;
	Lock gameLock; // to lock game for synchronization
	Condition otherPlayerConnected; // to wait for other player
	Condition otherPlayerTurn; // to wait for other players turn
	private int currentPlayer; // keeps track of player with current move
	private int otherPlayer;
	boolean bothPlayersActive = false;
	public int winner;
	private boolean newMessageAvailible=false;

	public GameState() {
		setShipNames();
		players = new StatePlayer[playerCount];

		gameLock = new ReentrantLock(); // create lock for game

		// condition variable for both players being connected
		otherPlayerConnected = gameLock.newCondition();

		// condition variable for the other players turn
		otherPlayerTurn = gameLock.newCondition();
		currentPlayer = 0;// set current player to first player
	}

	@SuppressWarnings("unchecked")
	private void setShipNames() {
		shipNames.add("A");
		shipNames.add("B");
		shipNames.add("C");
		shipNames.add("D");
		shipNames.add("E");
		shipNames.add("F");
		shipNames.add("G");
		shipNames.add("H");
		shipNames.add("I");
		shipNames.add("J");
		
	}

	public String move(int player, int target) {
		String result = new String();
		if (!isHit(player, target)) {
			result = "Invalid Move, Already Hit!";
		} else if (isShipLocation(player, target) && !isHit(player, target)) {
			result = "Direct Hit!";
		} else if (!isShipLocation(player, target) && !isHit(player, target)) {
			result = "You missle hit the water";
		} else {
			// no other option...i think...
		}
		return result;
	}

	// determine whether location is occupied
	public boolean isHit(int player, int location) {
		// checks if target has already been hit("X")
		if (players[player].board[location].equals("X")) {
			return true;
		} else {
			return false;
		}
	} // end method isOCccupied

	public boolean isShipLocation(int player, int target) {
		boolean shipLocation;
		char shipName=players[player].board[target].charAt(0);
		switch(shipName){
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
			shipLocation=true;
			break;
		default:
			shipLocation=false;
			break;	
		}
		
		//shipLocation = players[player].board[target].contains("A");
		return shipLocation;
	}
	public int getShipNumber(int player,int target){
		String shipName=new String(players[otherPlayer].board[target]);
		return shipNames.indexOf(shipName);
	}
	public void hitTarget(int player, int target) {
		players[player].hitShip(player,getShipNumber(player, target));
		players[player].board[target] = "X";
		// record hit to proper ship
		players[player].hitCount++;
		// add to ships hit count
	}

	public boolean isSunk(int player, int target) {
		boolean sunk = false;
		String shipName = players[player].board[target].toString();
		int shipNumber = Integer.parseInt(shipName);
		if (players[player].fleet.ships[shipNumber].isSunk()) {
			sunk = true;
		}
		return sunk;
	}

	// determine if move is valid
	public boolean validateAndMove(int location, int player) {
		// while not current player, must wait for turn
		while (player != currentPlayer) {
			gameLock.lock(); // lock game to wait for other player to go

			try {
				otherPlayerTurn.await(); // wait for player's turn
			} // end try
			catch (InterruptedException exception) {
				exception.printStackTrace();
			} // end catch
			finally {
				gameLock.unlock(); // unlock game after waiting
			} // end finally
		} // end while
		otherPlayer = (currentPlayer + 1) % 2; // set other player
		// if location not occupied, make move
		if (!isHit(otherPlayer, location)) {
			if(isShipLocation(otherPlayer, location) && bothPlayersActive){
				hitTarget(otherPlayer, location);
			}
			//hitTarget(otherPlayer, location);
			currentPlayer = otherPlayer; // change player

			// let new current player know that move occurred
			players[currentPlayer].otherPlayerMoved(location);

			gameLock.lock(); // lock game to signal other player to go
			try {
				otherPlayerTurn.signal(); // signal other player to continue
			} // end try
			finally {
				gameLock.unlock();
			} // end finally
			return true; // notify player that move was valid
		} // end if
		else
			// move was not valid
			return false; // notify player that move was invalid
	} // end method validatAndMove

	public boolean isGameOver() {
		boolean gameOver = false;
		if (bothPlayersActive) {
			if (players[0].hitCount == totalTargets) {
				winner=2;
				gameOver = true;
			} else if (players[1].hitCount == totalTargets) {
				winner=1;
				gameOver = true;
			}
		} else {
			gameOver = false;
		}
		return gameOver;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	// ////////////////////////
	// INNER PLAYER CLASS
	// ////////////////////////
	public class StatePlayer implements Runnable {
		private final String[] MARKS = { "X", "O" }; // array of marks
		public int hitCount = 0;
		public String[] board = new String[100];
		Fleet fleet = new Fleet();
		private int playerNumber;
		private String mark;
		private Socket connection;
		private Scanner input;
		private Formatter output;
		private boolean suspended;

		// set up Player thread
		public StatePlayer(Socket socket, int number) {
			board = new String[100]; // BattleShip board
			playerNumber = number; // store this player's number
			mark = MARKS[playerNumber]; // specify player's mark
			connection = socket; // store socket for client

			for (int i = 0; i < 100; i++)
				board[i] = new String("O"); // create player 1 BattleShip
			// board
			
			///////////////////////DEBUG SHIP SETUP
			setShip(this,"A",0,4);

			try // obtain streams from Socket
			{
				input = new Scanner(connection.getInputStream());
				output = new Formatter(connection.getOutputStream());
			} // end try
			catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(1);
			} // end catch
		} // end Player constructor

		private void setShip(StatePlayer statePlayer, String shipId,
				int startSquare, int endSquare) {
			//int shipLength;
			if ((endSquare - startSquare) < 5) {// ship is horizontal
				//shipLength = (endSquare - startSquare) + 1;
				for (int i = startSquare; i <= endSquare; i++) {
					this.board[i] = shipId;
				}
			} else {// ship is vertical
				//shipLength = ((endSquare - startSquare) / 10) + 1;
				for (int i = startSquare; i <= endSquare; i += 10) {
					this.board[i] = shipId;
				}
			}
			
		}

		public void setShip(int player, String shipId, int startSquare,
				int endSquare) {
			int shipLength;
			if ((endSquare - startSquare) < 4) {// ship is horizontal
				shipLength = (endSquare - startSquare) + 1;
				for (int i = startSquare; i <= shipLength; i++) {
					players[player].board[i] = shipId;
				}
			} else {// ship is vertical
				shipLength = ((endSquare - startSquare) / 10) + 1;
				for (int i = startSquare; i <= shipLength; i += 10) {
					players[player].board[i] = shipId;
				}
			}
		}
		public void hitShip(int player,int shipNumber){
			//players[player].fleet.ships[shipNumber].hitCount++;
		}

		// send message that other player moved
		public void otherPlayerMoved(int location) {
			output.format("Opponent moved\n");
			output.format("%d\n", location); // send location of move
			output.flush(); // flush output
		} // end method otherPlayer Moved

		// control thread's execution
		public void run() {
			// send client its mark (X or O), process messages from client
			try {
				setMessage("Player " + mark + " connected\n");
				// displayMessage("Player " + mark + " connected\n" );
				output.format("%s\n", mark); // send player's mark
				output.flush(); // flush output
////////////
	// Wait for ships to be set	
////////////
				// if player x, wait for another palayer to arrive
				if (playerNumber == 0) {
					output.format("%s\n%s", "Player X connected",
							"Waiting for another player\n");
					output.flush(); // flush output

					gameLock.lock(); // lock game to wait for second player
					try {
						while (suspended) {
							otherPlayerConnected.await(); // wait for player O
						} // end while
					} // end try
					catch (InterruptedException exception) {
						exception.printStackTrace();
					} // end catch
					finally {
						gameLock.unlock(); // unlock game after second player
					} // end finally

					// send message that other player connected
					output.format("Other player connected. Your move. \n ");
					output.flush(); // flush output
				} // end if
				else {
					output.format("Player O connected, please wait\n");
					output.flush(); // flush output
					bothPlayersActive=true;
				} // end else

				// /while game not over
				while (!isGameOver()) {
					int location = 0; // initialize move location

					if (input.hasNext())
						location = input.nextInt(); // get move location

					// check for valid move
					if (validateAndMove(location, playerNumber)) {
						setMessage("\nlocation: " + location);
						// displayMessage( "\nlocation: " + location );
						output.format("Valid move.\n"); // notify client
						output.flush(); // flush output
					} // end if
					else // move was invalid
					{
						output.format("Invalid move, try again\n");
						output.flush(); // flush output
					} // end else
				} // end while
				output.format("Game Over! player "+winner+" wins.\n"); // notify client
				output.flush(); // flush output
			} // end try
			finally {
				try {
					connection.close(); // close connection to client
				} // end try
				catch (IOException ioException) {
					ioException.printStackTrace();
					System.exit(1);
				} // end catch
			} // end finally
		} // end method run

		// set whether or not thread is suspended
		public void setSuspended(boolean status) {
			suspended = status; // set value of suspended
		} // end method setSuspended

		public class Fleet {
			Ship[] ships = new Ship[10];

			public Fleet() {
				// construct fleet of ships
				for(int i=0;i<4;i++)
					ships[i] = new Ship(2, i+1);
				for(int j=4;j<7;j++)
					ships[j] = new Ship(3, j+1);
				for(int k=7;k<9;k++)
					ships[k] = new Ship(4, k+1);
				ships[9]= new Ship(5, 10);
			}
		}

		public class Ship {
			int targets;
			int hitCount = 0;
			int shipNumber;

			public boolean isSunk() {
				return (hitCount == targets);
			}

			public Ship(int targets, int shipNumber) {
				this.targets = targets;
				this.shipNumber = shipNumber;

			}
		}
	}

	// ///////////////////////
	// END OF PLAYER CLASS

	// ///////////////////////////////////////////////
	// Observer methods
	@SuppressWarnings("unchecked")
	public void addGameStateChangedListener(GameStateChangedListener listener) {
		gameStateChangedListers.add(listener);
	}

	/**
	 * Allows an observer to unregister for BalanceChanged events.
	 */
	public void removeGameStateChangedListener(GameStateChangedListener listener) {
		gameStateChangedListers.remove(listener);
	}

	/**
	 * Iterates through all listeners, informing them that a BalanceChanged
	 * event has occurred.
	 */
	@SuppressWarnings("unused")
	private void fireGameStateChanged() {
		for (int i = 0; i < gameStateChangedListers.size(); i++) {
			GameStateChangedListener listener = (GameStateChangedListener) gameStateChangedListers
					.get(i);
			listener.gameStateChanged(this);
		}
	}

	public void setMessage(String newMessage) {
		message = newMessage;
		newMessageAvailible=true;
		//fireGameStateChanged();
	}

	public String getMessage() {
		newMessageAvailible=false;
		return message;
	}

	public boolean newMessage() {
		return newMessageAvailible;
	}

}
