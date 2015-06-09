package cs211.SudoSolver;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * 
 * This class handles the creation and properties of an actual cell within the
 * grid. This includes its candidate lists and value.
 * 
 * @author Thomas Keogh - thk11
 * 
 */
public class SudoCell extends JLabel {

	/**
	 * variables inclue the value of the cell, so number 0-9 0 for unsolved the
	 * candidate list for each specific cell as there is 81 instances of this
	 * class and minor points like the appearance of anything in the
	 * cell's/adding a border to them.
	 */

	private ArrayList<String> candidates;
	private String value;
	private Border blackSpace = BorderFactory.createLineBorder(Color.BLACK, 3);
	private Font f = new Font("arial", Font.BOLD, 48);

	/**
	 * 
	 * Set's each cell up, including appearance,value and initial candidates.
	 */
	public SudoCell() {

		/*
		 * Sets border and colour text as well as initial value, initial value
		 * could really be anything but its 0 for logics sake. Each cell's Array
		 * List is then started and set to have a maximum of 9 and then filled
		 * with 1-9.
		 */
		this.setBorder(blackSpace);
		this.setForeground(Color.black);
		value = "0";
		this.setFont(f);
		candidates = new ArrayList<String>(9);
		fillList();
	}

	/**
	 * Method used for returning values contained within cells. Used a lot when
	 * creating candidate lists based on row/column/grid values.
	 * 
	 * @return The value contained within a cell
	 */

	public String getValue() {

		return value; // Simply returns value of a cell

	}

	/**
	 * Set's the value of a cell using a passed in string.
	 * 
	 * 
	 * @param b
	 *            Sets a passed in string to be the value of a cell.
	 */
	public void setValue(String b) {
		this.value = b; // Sets the value of cell.
	}

	/**
	 * Returns the candidate list for the cell in question, initial candidates
	 * just numbers 1-9.
	 * 
	 * @return The candidate list for the cell.
	 */

	public ArrayList<String> getCandidates() {
		return candidates; // Returns the cell's candidate list.
	}

	/**
	 * Takes a passed in list and sets it to be the candidate list for the cell,
	 * each cell stores its own candidate list which can then be retrieved and
	 * edited then saved back.
	 * 
	 * 
	 * @param candidates
	 *            The array list passed in that will become the new candidate
	 *            list for the cell.
	 */

	public void setCandidates(ArrayList<String> candidates) {
		this.candidates = candidates; // Sets a cells candidates.
	}

	/**
	 * 
	 * Fills the candidate list in the cell with the numbers 1-9, used in
	 * initial construction of each cell.
	 */

	public void fillList() {
		String x = null;
		for (int i = 1; i < 10; i++) { // Same as method seen in SudoMethod.
										// Fills the candidate list with numbers
			x = Integer.toString(i); // one through nine. Parses the loops value
										// to a string and adds it.
			candidates.add(x);

		}

	}

}
