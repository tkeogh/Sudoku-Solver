package SudokuTests;
import static org.junit.Assert.*;

import org.junit.*;


import cs211.SudoSolver.SudoCell;
import cs211.SudoSolver.SudoPanel;

public class CellTesting {
	
	private SudoCell sc;
	private SudoPanel sp;
	
	@Before
	public void begin(){
		
		sc = new SudoCell();
		sp = new SudoPanel();
		
		
		
	}
	
	@Test
	public void testGetValue(){
		assertEquals("Issue with starting values/getting values","0",sc.getValue());
	}
	
	@Test
	public void testArray(){
		assertNotNull("array is null", sc.getCandidates());
	}
	
	
	

}
