package SudokuTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import cs211.SudoSolver.SudoPanel;

public class PanelTesting {
	
	private SudoPanel sp;
	private ArrayList<String> candidates;

	@Before
	public void begin(){
		sp = new SudoPanel();
	}
	
	@Test
	public void testInitialConstructor(){
		assertEquals("Board hasnt been made","0",sp.getBoardValue(5,5));
		
	}
	
	@Test
	public void testOrigValue(){
		assertNotNull("Board has null values",sp.getBoardValue(3, 3));
	}
	
	@Test
	public void testSetGetValues(){
		sp.setCellValue(4, 4, "3");
		assertEquals("Value hasnt been set","3",sp.getBoardValue(4,4));
		
	}
	
	@Test
	public void testSetCands(){
		candidates = new ArrayList<String>(9);
		candidates.add("4");
		sp.setCandidatesForCell(3,3,candidates);
		assertEquals("array hasnt been set properly",candidates,sp.getList(3,3));
		
	}
	
	
	@Test
	public void booleanForThread(){
		sp.resume();
		assertEquals("array hasnt been set properly",true,sp.getBool());
		sp.suspend();
		assertEquals("array hasnt been set properly",false,sp.getBool());
		
	}
	
	
	
	
	

}
