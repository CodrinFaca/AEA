package main;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Helpers {

	public static Integer [] getNumbers(int start, int end){
		Integer [] numbers = new Integer[end - start + 1];
		for (int i = 0, number = start; number <= end; number++, i++) {
			numbers[i] = number;
		}
		return numbers;
	}
	
	public static SudokuCell [][] convertToSudokuCells(int [][] board){
		int length = board.length;
		SudokuCell [][] sudokuCells = new SudokuCell[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				sudokuCells[i][j] = new SudokuCell(board[i][j]);
			}
		}
		return sudokuCells;
	}
	
	public static boolean isCorrect(SudokuCell [][] board){
		for (int i = 0; i < board.length; i++)
			if (!isCorrectCol(board, i) || !isCorrectRow(board, i))
				return false;
		for (int row = 0; row < board.length; row += 3)
			for (int col = 0; col < board.length; col += 3)
				if (!isCorrectBox(board, row, col))
					return false;
		
		return true;
	}
	
	public static boolean isSafe(SudokuCell [][] board, int num, int row, int col) {
		return (!usedInCol(board, num, col) && !usedInRow(board, num, row) && !usedInBox(board, num, row - row%3, col - col%3));
	}
	
	private static boolean isCorrectCol(SudokuCell [][] board, int col) {
		ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
		for (int i = 1; i <= board.length; i++)
			availableNumbers.add(i);
		
		for (int row = 0; row < board.length; row++)
			availableNumbers.remove(new Integer(board[row][col].get()));
		
		if (availableNumbers.size() == 0)
			return true;
		
		return false;
			
	}
	
	private static boolean isCorrectRow(SudokuCell [][] board, int row) {
		ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
		for (int i = 1; i <= board.length; i++)
			availableNumbers.add(i);
		
		for (int col = 0; col < board.length; col++)
			availableNumbers.remove(new Integer(board[row][col].get()));
		
		if (availableNumbers.size() == 0)
			return true;
		
		return false;
			
	}
	
	public static boolean areCorrectAllBoxes(SudokuCell [][] board){
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if(!isCorrectBox(board, i*3, j*3))
					return false;
			}
		}
		return true;
	}
	
	private static boolean isCorrectBox(SudokuCell [][] board, int boxStartRow, int boxStartCol) {
		ArrayList<Integer> availableNumbers = new ArrayList<Integer>();
		for (int i = 1; i <= board.length; i++)
			availableNumbers.add(i);
		
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
				availableNumbers.remove(new Integer(board[row + boxStartRow][col + boxStartCol].get()));
		
		if (availableNumbers.size() == 0)
			return true;
		
		return false;
	}
	
	private static boolean usedInCol(SudokuCell [][] board, int num, int col) {
		for (int row = 0; row < board.length; row++)
			if (board[row][col].get() == num)
				return true;

		return false;
	}
	
	private static boolean usedInRow(SudokuCell [][] board, int num, int row) {
		for (int col = 0; col < board.length; col++)
			if (board[row][col].get() == num)
				return true;

		return false;
	}
	
	private static boolean usedInBox(SudokuCell [][] board, int num, int boxStartRow, int boxStartCol) {
		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++)
				if (board[row + boxStartRow][col + boxStartCol].get() == num)
					return true;
		
		return false;
	}

	public static Coordinates nextCell(final int row, final int col, final int size) {
        int r = row, c = col;
        if (c < size - 1) {
            c++;
        } else {
            c = 0;
            ++r;
        }
        return new Coordinates(c, r);
    }

	public static Coordinates currentCell(final int cell, final int size) {
        int col = cell % size;
        int row = cell / size;

        return new Coordinates(col, row);
    }

	public static void printBoard(int [][] board) {
		System.out.print("{");
		for (int y = 0; y < board.length; y++) {
			if(y == 0)
				System.out.print("{");
			else
				System.out.print(" {");
			for (int x = 0; x < board.length; x++){
				System.out.print(board[y][x]);
				if(x!=board.length-1)
					System.out.print(",");
			}
				
			System.out.print("}");
			
			if(y < board.length-1)
				System.out.println();
		}
		System.out.print("}");
		System.out.println();
	}
	
	public static boolean hasProperSize(int [][] sudokuBoard){
		if (sudokuBoard.length != 9)
			return false;
		for (int i = 0; i < sudokuBoard.length; i++) {
			if(sudokuBoard[i].length != 9)
				return false;
		}
		return true;
	}
	
	public static boolean hasProperNumbers(int [][] sudokuBoard){
		for (int i = 0; i < sudokuBoard.length; i++) {
			for (int j = 0; j < sudokuBoard.length; j++) {
				if(sudokuBoard[i][j] < 0 || sudokuBoard[i][j] > 9)
					return false;
			}
		}
		return true;
	}
	
	public static boolean isSudokuIndexGood(int index){
		return index <= 8 && index >= 0;
	}
	
	public static List<Movement> generateAllPossibleMovements(int x, int y){
		List<Coordinates> allPossibleCoords = new LinkedList<Coordinates>();
		
		for (int i = x*3; i < (x+1)*3; i++) {
			for (int j = y*3; j < (y+1)*3; j++) {
				allPossibleCoords.add(new Coordinates(i, j));
			}
		}
		
		List<Movement> movements = new LinkedList<Movement>();
		for (int i = 0; i < allPossibleCoords.size(); i++) {
			for (int j = i+1; j < allPossibleCoords.size(); j++) {
				movements.add(new Movement(allPossibleCoords.get(i),allPossibleCoords.get(j)));
			}
		}
		
		return movements;
	}
	
	// checking how many positions are conflicting
	// we don't check boxes assuming that boxes don't contain conflicts
	public static int getCurrentConflictsNumber(int [][] board){
		
		int conflictsNumber = 0;
		
		for (int i = 0; i < board.length; i++) {
			ArrayList<Integer> row = new ArrayList<Integer>();
			for (int j = 0; j < board.length; j++) {
				row.add(board[i][j]);
			}
			Set<Integer> rowUniq = new HashSet<Integer>(row);
			conflictsNumber += row.size() - rowUniq.size();
		}
		
		for (int i = 0; i < board.length; i++) {
			ArrayList<Integer> column = new ArrayList<Integer>();
			for (int j = 0; j < board.length; j++) {
				column.add(board[j][i]);
			}
			Set<Integer> columnUniq = new HashSet<Integer>(column);
			conflictsNumber += column.size() - columnUniq.size();
		}
		
		return conflictsNumber;
	}
}
