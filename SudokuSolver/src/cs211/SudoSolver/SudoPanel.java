package cs211.SudoSolver;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class is used to set up the panel surrounding the board, the board
 * itself and starts the thread for solving through buttons. The class basically
 * contains the basis of the program, all of the visual side rather than any
 * actual part of solving the sudoku really. The class does start the thread for
 * solving but does not contain the algorithm for solving. This is essentially
 * the GUI for the program.
 * 
 * 
 * @author Thomas Keogh - thk11
 * 
 */

public class SudoPanel extends JFrame implements ActionListener, Runnable {

	private JPanel buttonPanel, gridPanel; // Buttons across the top
	private JButton loadButton, solveButton; // buttons
	private SudoCell[][] board; // Actual creation of board.
	private SudoMethod sm;
	private Thread solving;
	private boolean running = true; // Boolean used to set if the solve thread is
									// running or not, true to run, false to
									// end.
	

	/**
	 * The constructor builds the GUI including the board and buttons. Also adds
	 * actionlistener's to the button's as well as some minor pieces to do with
	 * how the board is set up and options regarding the size and position of
	 * the program as a whole.
	 * 
	 */

	public SudoPanel() {

		setTitle("Sudoku Solver Assignment - thk11");
		/*
		 * Sets up buttonpanel and its layout including gaps, a panel for the
		 * board with a 9,9 layout. Creates buttons and the actual 2D Array of
		 * SudoCell which is essentially the board, Sudo Cell's are then placed
		 * on the gridpanel.
		 */
		buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		gridPanel = new JPanel(new GridLayout(9, 9));
		gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		loadButton = new JButton("Load");
		solveButton = new JButton("Solve");
		board = new SudoCell[9][9];
		makeBoard();
		this.add(gridPanel);
		/*
		 * Buttons are added to panel, dimensions are set and action listeners
		 * added.
		 */
		buttonPanel.add(solveButton);
		buttonPanel.add(loadButton);
		this.setVisible(true);
		this.add(buttonPanel, BorderLayout.NORTH);
		this.setSize(new Dimension(600, 600));
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadButton.addActionListener(this);
		solveButton.addActionListener(this);
		sm = new SudoMethod(this);

	}

	/**
	 * 
	 * Starts the thread called solving, this then starts to solve the loaded in
	 * grid.
	 */

	public void move() {
		solving = new Thread(this); // Starts thread.
		solving.start();
	}

	/**
	 * 
	 * Handles what happen's if a button is pressed, just call's correct
	 * methods.
	 */

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {

			File openDestination = openChooser(); // openChooser returns a file
													// which is set as
													// OpenDestination.
			sm.loadSudo(openDestination); // Load method loads the selected file
											// from JFileChooser.
		}
		if (e.getSource() == solveButton) {

			resume(); // Sets the boolean to true to allow the thread to run.
			move(); // Calls move which starts the thread of solving.

		}

	}

	/**
	 * This method launches a JFileChooser which in turn returns the file the
	 * user has chosen. Once the file has been chosen it is then handles by the
	 * load method inside SudoMethod.
	 * 
	 * 
	 * @return Returns the file to be opened into the grid.
	 */

	public File openChooser() {
		File open = null;
		JFileChooser chooser = new JFileChooser(); // Sets the file to null and
													// starts the JFileChooser.
// Sets the current directory to the directory the project/jar is ran from.
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); 
		int o = chooser.showOpenDialog(this);
		if (o == JFileChooser.APPROVE_OPTION) {
			open = chooser.getSelectedFile(); // If user confirms a file to open
												// retrieve the file.
		}
		return open; // return file.

	}

	/**
	 * Handles the actual making of the 9x9 cell's, which in this case are my
	 * own Class called SudoCell. The method goes through two for loops creating
	 * the board and setting the value inside them, it then adds the board to
	 * the SudoPanel.
	 */

	public void makeBoard() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// With two for loops running 9 times 'runs' a total of 81
				// times.
				board[i][j] = new SudoCell(); // current value of for loops used
												// to create SudoCell's
				board[i][j].setText(board[i][j].getValue());
				gridPanel.add(board[i][j]); // Add the new SudoCell to the
											// gridPanel, grid panel also set to
											// layout 9x9 to accomodate all
										// cells.
			}
		}
	}

	/**
	 * This method handles setting the values within the grid. Continually
	 * called throughout SudoMethod to set the cell to the value passed in by
	 * several different methods.
	 * 
	 * @param i
	 *            Essentially the 'x' value of the cell.
	 * @param j
	 *            Essentially the 'y' value of the cell.
	 * @param string
	 *            The value that the cell will be set to.
	 */

	public void setCellValue(int i, int j, String string) {
		board[i][j].setValue(string); // Sets the value of the two passed in
										// int's to the string that is also
										// passed in.

	}

	/**
	 * Updates the board to reflect the values that the cell's are actually set
	 * to, without this the board would never change.
	 * 
	 */

	public void update() {
		/*Set's text of all SudoCell's to the value it contains, a form of refresh
		 * 
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				board[i][j].setText(board[i][j].getValue()); 

			}

		}

	}

	/**
	 * This method sets a passed in array list as the candidate list of a cell
	 * thats co ordinates are also passed in. Specifically useful when editing
	 * candidate lists as without it candidate lists for cell's would never
	 * change.
	 * 
	 * 
	 * @param i
	 *            Works out as the 'x' value for the cell.
	 * @param j
	 *            Works out as the 'y' value for the cell.
	 * @param list
	 *            The list to be set as the cell's candidate list.
	 */
	public void setCandidatesForCell(int i, int j, ArrayList<String> list) {
		board[i][j].setCandidates(list); // Sets a cells candidate list to the
										// same as one passed in, cell
									   // indicated by i,j being passed in.
	}

	/**
	 * Takes in a cell's co ordinates and returns the candidate list for the
	 * cell.
	 * 
	 * 
	 * @param x
	 *            The 'x' value of the cell.
	 * @param y
	 *            The 'y' value of the cell.
	 * @return Returns the candidate list for specified cell.
	 */

	public ArrayList<String> getList(int x, int y) {

		return board[x][y].getCandidates(); // Returns the candidates for a
											// cell, useful when doing hidden
											// singles.
	}

	/**
	 * Returns the value stored within a cell, cell co ordinates passed in with
	 * method call. Specifically useful for checking/creating candidate lists.
	 * 
	 * 
	 * @param x
	 *            The 'x' value of the cell
	 * @param y
	 *            The 'y' value of the cell
	 * @return Returns the value stored within the cell.
	 */
	public String getBoardValue(int x, int y) {
		return board[x][y].getValue(); // Returns the value of a cell,
										// specifically useful in the finding
										// naked singles algorithm.
	}

	/**
	 * 
	 * Part of the thread, calls the solve method and and an update for the
	 * board. Method essentially starts the chain of methods that is the
	 * algorithm.
	 */
	public void run() {

		while (running) { // while boolean set to true
			sm.solve(); // call the solve method which handles running the
						// algorithms to solve the board
			update(); // updates board to reflect true values.
		}
	}

	/**
	 * Sets the boolean running to false, thus stopped the thread if it is
	 * running.
	 */
	public void suspend() {
		running = false; // essentially suspends thread if it is running.
	}

	/**
	 * Sets the boolean in the run method to true, allowing for solving to
	 * start.
	 */
	public void resume() {
		running = true; // sets running to true to allow a thread to run.
	}
	
	public boolean getBool(){
		return running;   //Used for JUnit.
	}

}