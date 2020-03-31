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
	Cell[] deadRow;

	@Before
	public void setUp() {
		/*
		 * TODO: initialize the text fixture. For the initial pattern, use the "blinker"
		 * pattern shown in:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Examples_of_patterns
		 * The actual pattern GIF is at:
		 * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#/media/File:Game_of_life_blinker.gif
		 * Start from the vertical bar on a 5X5 matrix as shown in the GIF.
		 */

		size = 5;

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


	@Test
	public void testIterateCellAlive() {
		assertTrue(testMainPanel.iterateCell(2, 2));
	}

	@Test
	public void testIterateCellDead() {
		assertFalse(testMainPanel.iterateCell(1, 1));
	}

	/**
	 * Verifies calculateNextIteration()'s output given a blinker pattern'
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
