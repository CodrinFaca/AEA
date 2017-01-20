package test;
import static org.junit.Assert.*;

import java.util.Arrays;

import main.Coordinates;
import main.SudokuBoard;

import org.junit.Test;

public class SudokuBoardTest {

	@Test
	public void swapNumbersByCoordinatesTest(){
		int [][] boardBefore =
			{{1,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,2}};
		int [][] boardAfter =
			{{2,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,1}};
		
		SudokuBoard sudokuBoard1 = new SudokuBoard(boardBefore);
		sudokuBoard1.swapNumbersByCoordinates(new Coordinates(0, 0), new Coordinates(8, 8));
		
		assertTrue(Arrays.deepEquals(sudokuBoard1.getBoard(), boardAfter));
	}
}
