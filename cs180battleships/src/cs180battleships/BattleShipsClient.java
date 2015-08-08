package cs180battleships;

// Client side of client/server BattleShips program
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.Socket;
import java.net.InetAddress;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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

public class BattleShipsClient extends JFrame implements Runnable {
	private JTextField idField; // text field to display player's mark
	private String idText;
	private JTextArea displayArea; // JTextArea to display output
	private String displayMessage;
	private JPanel boardPanel; // panel for BattleShips board
	private JPanel panel1; // panel to hold board
	private Square[][] board; // BattleShips board
	private JPanel boardPanel2; // panel for BattleShips board
	private JPanel panel2; // panel to hold board
	private Square[][] board2; // BattleShips board
	private Square currentSquare; // current square
	private Socket connection; // connection to the server
	private Scanner input; // input from server
	private Formatter output; // output to server
	private String BattleShipsHost; // host name for server
	private String myMark; // this client's mark
	private boolean myTurn; // determines which client's turn it is
	private final String X_MARK = "X"; // mark for first client
	private final String O_MARK = "O"; // mark for second client
	private ArrayList clientChangedListers = new ArrayList();

	// set up user-interface and board
	public BattleShipsClient(String host) {
		BattleShipsHost = host; // set name of server
		displayArea = new JTextArea(4, 30); // set up JTextArea
		displayArea.setEditable(false);
		getContentPane().add(new JScrollPane(displayArea), BorderLayout.SOUTH);

		boardPanel = new JPanel(); // set up panel for squares in board
		boardPanel.setLayout(new GridLayout(10, 10, 0, 0));
		boardPanel2 = new JPanel();
		boardPanel2.setLayout(new GridLayout(10, 10, 0, 0));

		board = new Square[10][10]; // create board
		board2 = new Square[10][10]; // create board

		// loop over the rows in the board
		for (int row = 0; row < board.length; row++) {
			// loop over the columns in the board
			for (int column = 0; column < board[row].length; column++) {
				// create square
				board[row][column] = new Square(" ", row * 10 + column);
				board2[row][column] = new Square(" ", row * 10 + column);

				boardPanel.add(board[row][column]); // add square
				boardPanel2.add(board2[row][column]); // add square
			} // end inner for
		} // end outer for

		idField = new JTextField(); // set up text field
		idField.setEditable(false);
		getContentPane().add(idField, BorderLayout.NORTH);

		panel1 = new JPanel(); // set up panel to contain boardPanel
		panel1.add(boardPanel, BorderLayout.CENTER); // add board panel
		getContentPane().add(panel1, BorderLayout.WEST); // add container panel

		panel2 = new JPanel(); // set up panel to contain boardPanel
		panel2.add(boardPanel2, BorderLayout.CENTER); // add board panel
		getContentPane().add(panel2, BorderLayout.EAST);

		setSize(650, 500); // set size of window
		setVisible(true); // show window

		startClient();
	} // end BattleShipsClient constructor

	// start the client thread
	public void startClient() {
		try {
			// make connection to server
			connection = new Socket(InetAddress.getByName(BattleShipsHost),
					12345);

			// get streams for input and output
			input = new Scanner(connection.getInputStream());
			output = new Formatter(connection.getOutputStream());
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch

		// create and start worker thread for this client
		ExecutorService worker = Executors.newFixedThreadPool(1);
		worker.execute(this); // execute client
	} // end method startClient

	// control thread that allows continous update of displayArea
	public void run() {
		myMark = input.nextLine(); // get player's mark (X or O)

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// display player's mark
				setIdText("You are Player\"" + myMark + "\"");
				idField.setText("You are Player\"" + myMark + "\"");
			} // end method run
		} // end anonymous inner class
				); // end call to SwingUtilities.InvokeLater

		myTurn = (myMark.equals(X_MARK)); // determine if client's turn

		// receive messages sent to client and output them
		while (true) {
			if (input.hasNextLine())
				processMessage(input.nextLine());
		} // end while
	} // end method run

	// process messages received by client
	private void processMessage(String message) {
		// valid move occurred
		if (message.equals("Valid move.")) {
			displayMessage("Valid move, please wait.\n");
			setMark(currentSquare, myMark); // set mark in square
		} // end if
		else if (message.equals("Invalid move, try again")) {
			displayMessage(message + "\n"); // display invalid move
			myTurn = true; // still this client's turn
		} // end else if
		else if (message.equals("Opponent moved")) {
			int location = input.nextInt(); // get move location
			input.nextLine(); // skip newline after int location
			int row = location / 10; // calculate row
			int column = location % 10; // calculate column

			setMark(board[row][column], (myMark.equals(X_MARK) ? O_MARK
					: X_MARK)); // mark move
			displayMessage("Opponent moved. Your turn.\n");
			myTurn = true; // now this clients turn
		} // end else if
		else
			displayMessage(message + "\n"); // display the message
	} // end method processMessage

	// manipulate displayArea in event-dispatch thread
	private void displayMessage(final String messageToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				displayArea.append(messageToDisplay); // updates output
				setDisplayMessage(messageToDisplay);

			} // end method run
		} // end inner class
				); // end call to SwingUtilities.invokeLater
	} // end method displayMessage

	// utility method to set mark on board in event-dispatch thread
	private void setMark(final Square squareToMark, final String mark) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				squareToMark.setMark(mark); // set mark in square
			} // end method run
		} // end anonymous inner class
				); // end call to SwingUtilities.invokeLater
	} // end method setMark

	// send message to server indicating clicked square
	public void sendClickedSquare(int location) {
		// if it is my turn
		if (myTurn) {
			output.format("%d\n", location); // send location to server
			output.flush();
			myTurn = false; // not my turn any more
		} // end if
	} // end method sendClickedSquare

	// set current Square
	public void setCurrentSquare(Square square) {
		currentSquare = square; // set current square to argument
	} // end method setCurrentSquare

	// / private inner class for the square on the board
	@SuppressWarnings("serial")
	private class Square extends JPanel {
		private String mark; // mark to be drawn in this square
		private int location; // location of square
		BufferedImage image;

		public Square(String squareMark, int squareLocation) {
			mark = squareMark; // set mark for this square
			location = squareLocation; // set location of this square

			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					setCurrentSquare(Square.this); // set current square

					// send location of this square
					sendClickedSquare(getSquareLocation());
				} // end method mouseReleased
			} // end anonymous inner class
			); // end call to addMouseListener
		} // end Square constructor

		// return preferred size of Square
		public Dimension getPreferredSize() {
			return new Dimension(30, 30); // return preferred size
		} // end method getPreferredSize

		// return minimum size of Square
		public Dimension getMinimumSize() {
			return getPreferredSize(); // return preferred size
		} // end method getMinimumSize

		// set mark for Square
		public void setMark(String newMark) {
			try {
				String imageName = "Explosion.jpg";
				File input = new File(imageName);
				image = ImageIO.read(input);
			} catch (IOException ie) {
				System.out.println("Error:" + ie.getMessage());
			}
			
			mark = newMark; // set mark of square
			repaint(); // repaint square
		} // end method setMark

		// return Square location
		public int getSquareLocation() {
			return location; // return location of square
		} // end method getSquareLocation

		// draw Square
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.drawRect(0, 0, 29, 29); // draw square
		//	g.drawString(mark, 11, 20); // draw mark
			g.drawImage(image, 0, 0, null); // draw mark
		} // end method paintComponent
	} // end inner-class Square

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
	 * Iterates through all listeners, informing them that a BalanceChanged
	 * event has occurred.
	 */
	private void fireClientChanged() {
		for (int i = 0; i < clientChangedListers.size(); i++) {
			ClientChangedListener listener = (ClientChangedListener) clientChangedListers
					.get(i);
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

	public void setIdText(String idText) {
		this.idText = idText;
	}

	public String getIdText() {
		return idText;
	}
} // end class BattleShipsClient
