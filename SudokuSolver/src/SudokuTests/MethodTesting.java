package SudokuTests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.*;

import cs211.SudoSolver.SudoMethod;
import cs211.SudoSolver.SudoPanel;

public class MethodTesting {
	
	private ArrayList<String> candidates; 
	private SudoPanel sp;
	private SudoMethod sm;
	
	@Before
	public void begin(){
		sp = new SudoPanel();
		sm = new SudoMethod(sp);
		candidates = new ArrayList<String>();
	}
	
	@Test
	public void checkBoardValue(){
	sp.setCellValue(3,3,"8");
	sm.clearb();
	assertEquals("Board hasnt been made","0",sp.getBoardValue(3,3));
	}
	
	@Test
	public void testCanCopy(){
		candidates.add("4");
		assertEquals("Lists not same",candidates,sm.deepCopyOfCan(candidates));
		
	}
	
	@Test
	public void testSolve(){
		
	assertEquals("Method count wrong",81,sm.unsolved());
	}

}
