package test;
import static org.junit.Assert.*;
import main.BoardGenerator;

import org.junit.Test;

public class BoardGeneratorTest {

	@Test
	public void generateCorrectBoardTest() {
		BoardGenerator bg = new BoardGenerator();
		bg.generateCorrectBoard(9);
		assertTrue(bg.isCorrect(9));
	}

	@Test
	public void removeNumbersTest() {
		int size = 9;
		BoardGenerator bg = new BoardGenerator();
		bg.generateCorrectBoard(size);
		bg.removeNumbers(12);
		assertFalse(bg.isCorrect(size));
	}
	
	@Test
	public void removeNumbersTest2() {
		int size = 9;
		BoardGenerator bg = new BoardGenerator();
		bg.generateCorrectBoard(size);
		bg.removeNumbers(size*size);
		assertTrue(bg.isCorrect(size));
	}
	
	@Test
	public void removeNumbersTest3() {
		int size = 9;
		BoardGenerator bg = new BoardGenerator();
		bg.generateCorrectBoard(size);
		bg.removeNumbers(0);
		assertFalse(bg.isCorrect(size));
		
		int board[][] = bg.getCurrentBoard();
		int counter = 0;
		for (int y = 0; y < size; y++)
			for (int x = 0; x <size; x++) {
				if (board[y][x] == 0)
					counter++;
			}
		
		assertEquals(size*size, counter);
				
	}
}
