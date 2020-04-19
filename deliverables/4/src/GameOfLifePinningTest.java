import org.junit.*;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class GameOfLifePinningTest {
	/*
	 * READ ME: You may need to write pinning tests for methods from multiple
	 * classes, if you decide to refactor methods from multiple classes.
	 *
	 * In general, a pinning test doesn't necessarily have to be a unit test; it can
	 * be an end-to-end test that spans multiple classes that you slap on quickly
	 * for the purposes of refactoring. The end-to-end pinning test is gradually
	 * refined into more high quality unit tests. Sometimes this is necessary
	 * because writing unit tests itself requires refactoring to make the code more
	 * testable (e.g. dependency injection), and you need a temporary end-to-end
	 * pinning test to protect the code base meanwhile.
	 *
	 * For this deliverable, there is no reason you cannot write unit tests for
	 * pinning tests as the dependency injection(s) has already been done for you.
	 * You are required to localize each pinning unit test within the tested class
	 * as we did for Deliverable 2 (meaning it should not exercise any code from
	 * external classes). You will have to use Mockito mock objects to achieve this.
	 *
	 * Also, you may have to use behavior verification instead of state verification
	 * to test some methods because the state change happens within a mocked
	 * external object. Remember that you can use behavior verification only on
	 * mocked objects (technically, you can use Mockito.verify on real objects too
	 * using something called a Spy, but you wouldn't need to go to that length for
	 * this deliverable).
	 */

	// Test Objects
	MainPanel testMainPanel;
	Cell testCell;
	Cell[][] testCells;

	// Convenience
	int size;
	Cell deadCell;
	Cell liveCell;
	Cell[] deadRow;

	@Before
	public void setUp() {
		size = 5;
		deadCell = new Cell(false);
		liveCell = new Cell(true);

		testMainPanel = new MainPanel(size);
		testCells = new Cell[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Cell deadCell = Mockito.mock(Cell.class);
				Mockito.when(deadCell.getAlive()).thenReturn(false);
				Mockito.when(deadCell.toString()).thenReturn(" ");
				testCells[i][j] = deadCell;
			}
		}

		// Initialize Vertical Blinker Pattern
		Mockito.when(testCells[1][2].getAlive()).thenReturn(true);
		Mockito.when(testCells[2][2].getAlive()).thenReturn(true);
		Mockito.when(testCells[3][2].getAlive()).thenReturn(true);

		Mockito.when(testCells[1][2].toString()).thenReturn("X");
		Mockito.when(testCells[2][2].toString()).thenReturn("X");
		Mockito.when(testCells[3][2].toString()).thenReturn("X");

		testMainPanel.setCells(testCells);
	}

	/**
	 * Test case for MainPanel.iterateCell()
	 * Preconditions: MainPanel instance testPanel has been initialized with a vertical blinker pattern
	 * Execution steps: Call testPanel.iterateCell(2, 2)
	 * Postconditions: iterateCell(2, 2) should have returned true
	 */
	@Test
	public void testIterateCellAlive() {
		assertTrue(testMainPanel.iterateCell(2, 2));
	}

	/**
	 * Test case for MainPanel.iterateCell()
	 * Preconditions: MainPanel instance testPanel has been initialized with a vertical blinker pattern
	 * Execution steps: Call testPanel.iterateCell(1, 1)
	 * Postconditions: iterateCell(1, 1) should have returned false
	 */
	@Test
	public void testIterateCellDead() {
		assertFalse(testMainPanel.iterateCell(1, 1));
	}

	/**
	 * Test case for Cell.toString()
	 * Preconditions: Cell instance liveCell has been initialized with true/alive
	 * Execution steps: Call liveCell.toString()
	 * Postconditions: liveCell.toString() should have returned 'X'
	 */
	@Test
	public void testToStringAlive() {
		assertEquals("X", liveCell.toString());
	}

	/**
	 * Test case for Cell.toString()
	 * Preconditions: Cell instance deadCell has been initialized with false/dead
	 * Execution steps: Call deadCell.toString()
	 * Postconditions: deadCell.toString() should have returned '.'
	 */
	@Test
	public void testToStringDead() {
		assertEquals(".", deadCell.toString());
	}

	/**
	 * Test case for MainPanel.calculateNextIteration()
	 * Preconditions: MainPanel instance testPanel has been initialized with a vertical blinker pattern
	 * Execution steps: Call testPanel.calculateNextIteration()
	 * Postconditions: All cells other than those comprising the horizontal blinker pattern (1,2 2,2 3,2) should
	 *  have been set to dead. Those cells part of the horizontal blinker pattern should have been set to alive.
	 */
	@Test
	public void testCalculateNextIterationBlinkerPattern() {
		testMainPanel.calculateNextIteration();

		// Validate all cells other than horizontal blinker pattern were set dead
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// 1,2 2,2 3,2
				if (i == 2 && (j == 1 || j == 2 || j == 3)) {
					Mockito.verify(testCells[i][j]).setAlive(true);
				} else {
					Mockito.verify(testCells[i][j]).setAlive(false);
				}
			}
		}
	}

}
