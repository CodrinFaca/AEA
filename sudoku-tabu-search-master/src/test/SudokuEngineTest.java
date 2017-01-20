package test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.BoardGenerator;
import main.Coordinates;
import main.Helpers;
import main.Movement;
import main.SudokuEngine;
import main.Tuple;

import org.junit.Test;

import exception.WrongSudokuNumberException;
import exception.WrongSudokuSizeException;

public class SudokuEngineTest {

	@Test(expected=WrongSudokuSizeException.class)
	public void constructorTest1() throws WrongSudokuSizeException, WrongSudokuNumberException{
		int [][] wrongSizeBoard =
			{{1,2,3,4,5,6,7,8,9,1},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine e1 = new SudokuEngine(wrongSizeBoard,1,false,0,1000,false);
	}
	
	@Test(expected=WrongSudokuNumberException.class)
	public void constructorTest2() throws WrongSudokuSizeException, WrongSudokuNumberException{
		int [][] wrongNumbersBoard =
			{{1,2,3,4,5,6,7,8,10},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine e1 = new SudokuEngine(wrongNumbersBoard,1,false,0,1000,false);
	}
	
	@Test
	public void getCurrentConflictsNumberTest() throws WrongSudokuSizeException, WrongSudokuNumberException {
		int [][] b1 =
			{{1,2,3,4,5,6,7,8,9},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine e1 = new SudokuEngine(b1,1,false,0,1000,false);
		assertEquals(0, e1.getCurrentCostFunctionValue());
		
		int [][] b2 =
			{{3,2,1,4,5,6,7,8,9},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine e2 = new SudokuEngine(b2,1,false,0,1000,false);
		assertEquals(2, e2.getCurrentCostFunctionValue());
		
		int [][] b3 =
			{{1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1},
			 {1,1,1,1,1,1,1,1,1}};
		SudokuEngine e3 = new SudokuEngine(b3,1,false,0,1000,false);
		assertEquals(2*8*9, e3.getCurrentCostFunctionValue());
	}
	
	@Test
	public void fillBoardTest() throws WrongSudokuSizeException, WrongSudokuNumberException {
		int [][] inputBoard =
			{{1,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,3},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,2}};
		SudokuEngine engine = new SudokuEngine(inputBoard,1,false,0,1000,false);
		
		engine.fillBoard();
		int [][] filledBoard = engine.getCurrentState();
		
		assertTrue(Helpers.areCorrectAllBoxes(Helpers.convertToSudokuCells(filledBoard)));
		
		assertEquals(1, filledBoard[0][0]);
		assertEquals(3, filledBoard[1][8]);
		assertEquals(2, filledBoard[8][8]);
	}

	@Test
	public void generateNotBlockedMovements() throws WrongSudokuSizeException, WrongSudokuNumberException{
		int [][] b1 =
			{{1,2,3,4,5,6,7,8,9},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine engine = new SudokuEngine(b1,1,false,0,1000,false);
		assertEquals(0, engine.generateNotBlockedMovements().size());
		
		int [][] b2 =
			{{0,2,3,4,5,6,7,8,9},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine engine2 = new SudokuEngine(b2,1,false,0,1000,false);
		assertEquals(0, engine2.generateNotBlockedMovements().size());
		
		int [][] b3 =
			{{0,0,3,4,5,6,7,8,9},
			 {9,8,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine engine3 = new SudokuEngine(b3,1,false,0,1000,false);
		assertEquals(1, engine3.generateNotBlockedMovements().size());
		
		int [][] b4 =
			{{0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0}};
		SudokuEngine engine4 = new SudokuEngine(b4,1,false,0,1000,false);
		assertEquals(36*9, engine4.generateNotBlockedMovements().size());
		
	}
	
	@Test
	public void generateNeighborhood() throws WrongSudokuSizeException, WrongSudokuNumberException{
		int [][] b1 =
			{{1,0,3,4,5,6,7,8,9},
			 {9,0,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,7,9,2,1,3,5,4,6},
			 {5,4,6,8,7,9,2,1,3},
			 {3,1,2,6,4,5,9,7,8},
			 {7,9,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine engine = new SudokuEngine(b1,1,false,0,1000,false);
		engine.fillBoard();
		List<Tuple<Movement,Integer>> result = engine.generateNeighborhood();
		assertEquals(1, result.size());		
		assertTrue(result.get(0).x.from.equals(new Coordinates(0, 1)) || result.get(0).x.from.equals(new Coordinates(1, 1)));
		assertTrue(result.get(0).x.to.equals(new Coordinates(0, 1)) || result.get(0).x.to.equals(new Coordinates(1, 1)));
		assertTrue(result.get(0).y == 2);
	}
	@Test
	public void tabuNoAspCriterionNoLongterm() throws WrongSudokuSizeException, WrongSudokuNumberException {
		int [][] b1 =
			{{1,0,3,4,5,6,7,8,9},
			 {9,0,7,3,2,1,6,5,4},
			 {6,5,4,9,8,7,3,2,1},
			 {2,3,1,5,6,4,8,9,7},
			 {8,0,9,2,1,3,5,4,6},
			 {5,4,0,8,7,9,2,1,3},
			 {0,0,2,6,4,5,9,7,8},
			 {0,0,8,1,3,2,4,6,5},
			 {4,6,5,7,9,8,1,3,2}};
		SudokuEngine engine = new SudokuEngine(b1,1,false,0,1000,false);
		engine.runTabuSearch();
		assertEquals(0, engine.getCurrentCostFunctionValue());
		
		int [][] b2 =
			{{1,0,3,4,0,0,7,0,0},
			 {9,0,7,0,2,1,0,0,4},
			 {0,0,0,0,8,7,0,2,0},
			 {2,3,0,0,6,0,0,0,7},
			 {8,0,0,0,1,0,5,0,6},
			 {5,4,0,8,7,0,2,1,3},
			 {0,0,2,6,4,0,0,0,8},
			 {0,0,8,0,3,0,4,0,5},
			 {4,0,5,7,9,8,1,0,2}};
		SudokuEngine engine2 = new SudokuEngine(b2,5,false,0,1000,false);
		engine2.runTabuSearch();
		assertEquals(0, engine2.getCurrentCostFunctionValue());	
		
		int [][] b3 =
			{{1,2,3,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0},
			 {0,0,0,0,0,0,0,0,0}};
		SudokuEngine engine3 = new SudokuEngine(b3,5,false,0,1000,false);
		engine3.runTabuSearch();
		assertEquals(0, engine3.getCurrentCostFunctionValue());
	}
	
	private int [][] getSudokuBoard(int seed, int leftNmbsCount){
		
		BoardGenerator bg = new BoardGenerator(seed);
		bg.generateCorrectBoard(9);
		
//		Helpers.printBoard(bg.getCurrentBoard());
//		System.out.print("---");
		
		bg.removeNumbers(leftNmbsCount);
		
//		Helpers.printBoard(bg.getCurrentBoard());
		
		return bg.getCurrentBoard();
	}

	private void testMultipleTimes(int [] seeds, boolean useAspCr, int longTermTabuListLength, int maxItCount, int leftNmbsCount)
			throws WrongSudokuSizeException, WrongSudokuNumberException{
		
		for (int j = 5; j < 300; j+=10) {
			int sum = 0;
			for (int i = 0; i < seeds.length; i++) {
				SudokuEngine e1 =
						new SudokuEngine(getSudokuBoard(seeds[i],leftNmbsCount),j,useAspCr,longTermTabuListLength,maxItCount,false);
				e1.runTabuSearch();
//				System.out.print(e1.getIterationsCount() + ";");
				
				sum += e1.getIterationsCount();	
			}
//			System.out.println();
			System.out.print(sum/seeds.length + ",");
		}
		System.out.println();
	}
	
	private void testForParams(boolean useAspCr, int longTermTabuListLength, int [] seeds, int maxIterationsCount)
			throws WrongSudokuSizeException, WrongSudokuNumberException{
		System.out.println("Asp cr: " + useAspCr);
		System.out.println("longTermTabuListLength: " + longTermTabuListLength);
		for (int i = 80; i >= 0; i-=10) {
			testMultipleTimes(seeds, useAspCr, longTermTabuListLength, maxIterationsCount, i);
		}
	}
	
	@Test
	public void tabuAspCriterionLongterm() throws WrongSudokuSizeException, WrongSudokuNumberException {
		
		int shortTermTabuListLength = 50;
		int longTermTabuListLength = 200;
		int maxIterationsCount = 1000;
		
		System.out.println("shortTermTabuListLength: " + shortTermTabuListLength);
		System.out.println("longTermTabuListLength: " + longTermTabuListLength);
		System.out.println("maxIterationsCount: " + maxIterationsCount);
		System.out.println();
		
		SudokuEngine e1 = new SudokuEngine(getSudokuBoard(10,10),shortTermTabuListLength,false,0,maxIterationsCount,false);
		e1.runTabuSearch();
		System.out.println("No asp cr, no longterm list, it count: " + e1.getIterationsCount());
		System.out.println("Conflicts number: " + e1.getCurrentCostFunctionValue());
		System.out.println();
		
		SudokuEngine e2 = new SudokuEngine(getSudokuBoard(10,10),shortTermTabuListLength,true,0,maxIterationsCount,false);
		e2.runTabuSearch();
		System.out.println("Asp cr, no longterm list, it count: " + e2.getIterationsCount());
		System.out.println("Conflicts number: " + e2.getCurrentCostFunctionValue());
		System.out.println();
		
		SudokuEngine e3 = new SudokuEngine(getSudokuBoard(10,10),shortTermTabuListLength,true,longTermTabuListLength,maxIterationsCount,false);
		e3.runTabuSearch();
		System.out.println("Asp cr, longterm list, it count: " + e3.getIterationsCount());
		System.out.println("Conflicts number: " + e3.getCurrentCostFunctionValue());
		System.out.println();
		
		SudokuEngine e4 = new SudokuEngine(getSudokuBoard(10,10),shortTermTabuListLength,false,longTermTabuListLength,maxIterationsCount,false);
		e4.runTabuSearch();
		System.out.println("No asp cr, longterm list, it count: " + e4.getIterationsCount());
		System.out.println("Conflicts number: " + e4.getCurrentCostFunctionValue());
		System.out.println();
	}
	
	@Test
	public void testsForGraphs() throws WrongSudokuSizeException, WrongSudokuNumberException {

		int maxIterationsCount = 2000;
		
		Random randGenerator = new Random();
		randGenerator.setSeed(10);
		int seedsCount = 20;
		int [] seeds = new int [seedsCount];
		for (int i = 0; i < seedsCount; i++)
			seeds[i] = randGenerator.nextInt();

		boolean useAspCr = false;
		int longTermTabuListLength = 0;
		testForParams(useAspCr, longTermTabuListLength, seeds, maxIterationsCount);
		
		useAspCr = true;
		longTermTabuListLength = 0;
		testForParams(useAspCr, longTermTabuListLength, seeds, maxIterationsCount);
	}
}



