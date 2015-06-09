package cs211.SudoSolver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Iterator;

/**
 * This class contains all of the methods relating the the algorithm to solve
 * the Sudoku puzzles being passed in as well as the editing of the array lists
 * used when solving a Sudoku. The class also handles the loading in of a file
 * which does include error checking along with relevant warnings relating to
 * the loading method.
 * 
 * @author ww3ref
 * 
 */

public class SudoMethod {
	/*variables simple, array list for retrieving and setting candidates
	 * from sudocell and int a to keep track of if a file has been loaded
	 * previously.						
	 * 
	 */
	private SudoPanel sp;
	private int a = 0; 					
	private ArrayList<String> candidates; 

	/**
	 * This constructor creates the class and its link to SudoPanel, also
	 * creates an array list called candidates, this array list will be used
	 * when setting candidates for a cell rather than having to continually
	 * reference that cell's own candidate list. Once the use of the candidate
	 * list is done the cell's own candidate list is set to the same as the the
	 * candidate list in this class which has had the modifications done to it.
	 * Class also calls fillList() which fills the candidate list with numbers
	 * 1-9.
	 * 
	 * @param sp
	 *            Link to the SudoPanel class for the easy calling of methods as
	 *            well as easy interaction between the two classes.
	 */

	public SudoMethod(SudoPanel sp) {
		this.sp = sp;
		candidates = new ArrayList<String>(9); // Starts new sudopanel, creates
		// candidate list and fills it.
		fillList();
	}

	/**
	 * Fills the candidate list held within this class with the numbers 1-9, the
	 * candidates are held as strings. This method is repeatedly used without
	 * for when the candidate list in this class needs resetting for use with
	 * another cell.
	 */

	public void fillList() {
		String x = null;
		for (int i = 1; i < 10; i++) {
			x = Integer.toString(i); // Same as method in sudocell, fills the
			// candidate list with 1-9
			candidates.add(x);
		}
	}

	/**
	 * 
	 * This method deals with the loading in of the file, the method then sets
	 * the board with the numbers read in from the file. The method contains
	 * various forms or error checking, including checking the file is not null
	 * and checking if all of the lines in the file are in the correct format.
	 * If any of the lines in the file are decided to be corrupt by the method
	 * the whole file is discarded. Once a file has been discarded the whole
	 * board is reset to blank and waits for another file to be loaded in. The
	 * method also keeps track of if a file has been loaded in previously, if so
	 * it resets all cells candidate lists to the original 1 - 9.
	 * 
	 * @param fileName
	 *            The Filename of the file being loaded in, an example for this
	 *            program being book57.sud
	 */

	public void loadSudo(File fileName) {

		fillList();
		if (fileName != null) // if file exists
		{
			BufferedReader br = null; // sets to null ready for start
			try {

				String line;
				//New buffered reader for opened file
				br = new BufferedReader(new FileReader(fileName)); 																												
				char r = 0;
				for (int i = 0; i < 9; i++) { // for 9 lines in a file.
					line = br.readLine(); // line now equals the read in line
					// from buffered reader
					if (line.length() == 9) { //Continue if line length 9
						line = line.replace(" ", "0"); // Replace spaces with 0
						for (int j = 0; j < 9; j++) { // for each char in row			
							r = line.charAt(j); 
							sp.setCellValue(i, j, (String.valueOf(r)));
						//Set cell value to string of r
							if (a > 0) {
								//file loaded previously, reset candidates.
								sp.setCandidatesForCell(i, j, candidates); 							
							}
						}
					}
					else {
						this.clearb(); // When a file is corrupt (has more or
						// less char's than it needs) clear
						// board, throw error, exit method.
						JOptionPane.showMessageDialog(null,
								"Corrupt Line in file or file corrupt",
								"File load error", JOptionPane.ERROR_MESSAGE);
						break;
					}
				}
			}
			catch (IOException e) {

				JOptionPane.showMessageDialog(null,
						"Unexpect File Reader issue, please try another file.",
						"File load error", JOptionPane.ERROR_MESSAGE);
			}
			try {
				br.close(); // close the buffered reader.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		sp.update(); // update board and increment a to signify a files been
		// loaded previously.
		a++;
	}

	/**
	 * Sets all values in the board to 0, used when resetting the board in
	 * specific circumstances. The method is rarely used but becomes useful in
	 * the load method if a file is corrupt, it can reset the board to its
	 * original state.
	 */

	public void clearb() {

		String b = ("0");

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) { // goes through board setting all
				// values to 0.
				sp.setCellValue(i, j, b);
			}
		}

	}

	/**
	 * Checks a cells row and column for values so that it can remove them from
	 * the candidate list. The method then sets the cell's candidate list to be
	 * the same as the one which has had the values removed.
	 * 
	 * 
	 * @param x
	 *            The 'x' value for the cell being checked.
	 * @param y
	 *            The 'y' value for the cell being checked.
	 */

	public void checkColRow(int x, int y) {
		/*
		 * For a passed in cell location, go through its row and remove from the
		 * candidate list any found value Continue to do this for the column it
		 * is in before setting the current candidate list in here to that of
		 * the cell's candidate list. Method essentially creates candidate list.
		 */
		for (int i = 0; i < 9; i++) {
			String a = sp.getBoardValue(i, y);
			removeCandidate(a);
		}
		for (int j = 0; j < 9; j++) {
			String a = sp.getBoardValue(x, j);
			removeCandidate(a);
			sp.update();
		}
		sp.setCandidatesForCell(x, y, candidates); // Setting of candidate list
		// to cell.
	}

	/**
	 * Checks if the cell contains a hidden single in its candidate list for the
	 * row it is in. If a check comes back positive the value for the cell is
	 * set, the board is updated and candidates for the whole board are then
	 * re-evaluated.
	 */

	public void checkHidden() {
		ArrayList<String> temp = new ArrayList<String>();
		/*
		 * For each cell in the grid, if the value is zero check for hidden
		 * values in row. If hidden values method returns an array of size 1
		 * this indicates a hidden single and the value should be set to it.
		 * After this has been set re set candidates for all cell's as now some
		 * will have changed with the introduction of new cell.
		 */
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				figCand();
				if (sp.getBoardValue(i, j).equals("0")) {
					temp = checkHiddenRow(i, j);
					if (temp.size() == 1) {
						sp.setCellValue(i, j, temp.get(0));
						sp.update();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sp.setCandidatesForCell(i, j, null);
						figCand();
					}
				}
				temp.clear();
			}
		}

	}

	/**
	 * Checks to see if the cell contains a hidden single for the column it is
	 * in, if so sets the value to the hidden single. After a positive check is
	 * found the board is updated to match the value and the candidates for the
	 * whole board are then reconfigured.
	 */

	/*
	 * Checks the grid for hidden singles in columns, code works exactly like
	 * the hidden singles for rows method does, again if a value is set
	 * reconfigures board candidates.
	 */
	public void checkHiddenC() {
		ArrayList<String> temp = new ArrayList<String>();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				figCand();
				if (sp.getBoardValue(i, j).equals("0")) {
					temp = checkHiddenCol(i, j);
					if (temp.size() == 1) {
						sp.setCellValue(i, j, temp.get(0));
						sp.update();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sp.setCandidatesForCell(i, j, null);
						figCand();
					}
				}
				temp.clear();
			}
		}

	}

	/**
	 * An iterator is used to go over a candidate list and remove the value
	 * passed in by any method that calls it.
	 * 
	 * @param a
	 *            The String passed in for removal from the cell's candidate
	 *            list.
	 */

	public void removeCandidate(String a) {

		for (Iterator<String> iter = candidates.iterator(); iter.hasNext();) {
			String s = iter.next();
			if (a.equals(s)) { // iterator takes a passed in string, which will
				// be a single value and if it finds it
				iter.remove(); // in candidates removes it.
			}
		}

	}

	/**
	 * Checks to see which grid in the board the passed in cell is in. The
	 * method then scans the grid to remove any values found in it from the
	 * cell's candidate list.
	 * 
	 * @param x
	 *            The 'x' Value of the cell to be checked.
	 * @param y
	 *            The 'y' Value of the cell to be checked.
	 */

	public void checkSegment(int x, int y) {
		/*
		 * Checks a cells segment for values and removes them. Cell found by
		 * using the fact that int's cannot be fractions, exampled needed to
		 * explain properly. e.g the cell 5,4 is passed in, so 5/3 = 1 *3 = 3,
		 * 4/3 = 1 *3 = 3. this leaves us with x = 3, y =3 which will be used to
		 * adjust getBoardValue to the right cell. The 3,3 indicates the
		 * adjustment needed to get the right segment. the adjustment is added
		 * to the i,j value so that the correct value is retrieved rather than
		 * whatever cell i,j would indicate, most likely a cell in the top left
		 * rather than the middle segment which is where 5,4 is located.
		 */

		int rowAdd = (x / 3) * 3;
		int colAdd = (y / 3) * 3;
		for (int i = 0; i < 3; i++)
			// xy's box
			for (int j = 0; j < 3; j++) {
				String a = sp.getBoardValue(i + rowAdd, j + colAdd);
				removeCandidate(a);
			}

	}

	/**
	 * Checks to see if the passed in cell has any hidden singles in its
	 * candidate list for its row, this is done by removing all of the values in
	 * every other cell in its row candidate list from its own candidate list,
	 * if the return only contains one string then that is the hidden single.
	 * 
	 * 
	 * @param x
	 *            The 'x' value of the cell to be checked.
	 * @param y
	 *            The 'y' value of the cell to be checked.
	 * @return An Array List of all the unique values the cell contains in its
	 *         candidate list.
	 */

	public ArrayList<String> checkHiddenRow(int x, int y) {

		ArrayList<String> unique = new ArrayList<String>();
		ArrayList<String> curr = new ArrayList<String>(); // Creates two arrays
		// and makes one of
		// them contain
		// candidates for
		// passed in cell
		curr.addAll(sp.getList(x, y)); // Then uses a for loop to go through the
		// row and as long as the cell is not
		// the one passed in

		for (int i = 0; i < 9; i++) {
			if (i != y) {
				if (sp.getBoardValue(x, i).equals("0")) {
					candidates = sp.getList(x, i); // if the board value is 0,
					// get its candidates and
					// add them to unique
					for (String s : candidates) { // unique then contains all
						// the values from candidate
						// lists from row BUT the
						// ones in passed in cell
						if (!(unique.contains(s))) {
							unique.add(s);

						}
					}
				}
			}
		}
		curr.removeAll(unique); // remove all values from rest of row from our
		// passed in cell
		return curr; // return resulting array list
	}

	/**
	 * Checks to see if the passed in cell has any hidden singles in its
	 * candidate list for the column it is in, again done by removing all of the
	 * values in every other cell in its column candidate list from its own
	 * candidate list, if the return only contains one string then that is the
	 * hidden single.
	 * 
	 * @param x
	 *            The 'x' value of the cell to be checked.
	 * @param y
	 *            The 'y' value of the cell to be checked.
	 * @return An Array List of all the unique values the cell contains in its
	 *         candidate list
	 */

	public ArrayList<String> checkHiddenCol(int x, int y) {

		ArrayList<String> unique = new ArrayList<String>();
		ArrayList<String> curr = new ArrayList<String>();
		figCand();
		if (sp.getBoardValue(x, y).equals("0")) {
			curr.addAll(sp.getList(x, y));

			for (int i = 0; i < 9; i++) {
				if (i != x) {
					if (sp.getBoardValue(i, y).equals("0")) { 
//exactly same as checkHiddenRow but i,y not x,j											
						candidates = sp.getList(i, y);
						for (String s : candidates) {
							if (!(unique.contains(s))) {
								unique.add(s);
							}
						}
					}
				}

			}
		}
		curr.removeAll(unique);
		return curr;
	}

	/**
	 * The solved method handles the calling of all the method's that result in
	 * the solving of the grid. The cell's in the board are initially given
	 * their candidate lists and if it contains a naked single it is set, the
	 * cell's are then checked for hidden single's. Once this is all done the
	 * method checks if it should continue running by checking if the solving
	 * algorithms has changed any of the cell's values, if not it checks if the
	 * bored is solved or not and provides the user with the relevant message.
	 * 
	 */

	public void solve() {

		int x = unsolved();
		figCand();
		checkHidden(); // Solve method continually calls the seperate algorithms
		// until the unsolved before a run is the same as after.
		checkHiddenC();
		int y = unsolved();
		if (x == y) {
			sp.suspend();
			if (unsolved() == 0) { // if unsolved is zero the grid is considered
				// solved
				JOptionPane.showMessageDialog(null,
						"Sudoku grid solved, please load another grid.",
						"Solved", JOptionPane.INFORMATION_MESSAGE);
			}
			if (unsolved() > 0) { // If its greater than zero, its not solved
				// and solving has failed.
				JOptionPane
				.showMessageDialog(
						null,
						"Sudoku grid too complex for this solver, please try another grid.",
						"Not Solved", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * Checks the whole board and provides the cell's with their candidate lists
	 * as well as solving any naked singles. The method checks columns, rows and
	 * 3x3 grids for their candidate lists and if any return a single candidate
	 * it sets the relevant cell to that value.
	 * 
	 * 
	 */

	public void figCand() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sp.getBoardValue(i, j).equals("0")) {
					candidates.clear(); // goes through every cell thats 0,
					// resets its candidate list.
					fillList();

					checkColRow(i, j);
					checkSegment(i, j);				
					// check its row and column for values
					// and remove them from candidate list.
					if (candidates.size() == 1) {
						sp.setCellValue(i, j, candidates.get(0)); // if
						//if cand list has 1 value must be that value, set it.
						candidates.clear();
						sp.update(); // wipe candidates as they are not needed and
						// update the board.
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
					sp.setCandidatesForCell(i, j, deepCopyOfCan(candidates)); // set
					//candidates resulted from here set to cell candidates.
				}
			}
		}

	}

	/**
	 * Creates a copy of a passed in array list. This method is used when
	 * copying the candidate list in the SudoMethod class to the candidate list
	 * in the actual cell.
	 * 
	 * 
	 * @param list
	 *            The array list passed in for copying to the cell's candidate
	 *            list
	 * @return The copy of the list passed in, ready to be set as a cell's
	 *         candidate list.
	 */

	public ArrayList<String> deepCopyOfCan(ArrayList<String> list) {
		ArrayList<String> dCopy = new ArrayList<String>();
		for (String s : list) {

			dCopy.add(s); // takes in a list and creates a copy of it into
			// another list, returns the created new list.
		} // used for setting cell candidates after using candidate list in
		// here.
		System.out.println("Original list"+list+" new list "+dCopy);

		return dCopy;

	}

	/**
	 * Method used for checking how many unsolved cell's remain in the grid.
	 * 
	 * @return Returns the number of unsolved cell's within the board.
	 */

	public int unsolved() {

		int a = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				if (sp.getBoardValue(i, j).equals("0")) { // goes through board
					// counting values
					// that are 0
					a++; // increments counter then returns it later on.
				}

			}

		}

		return a;
	}
}
