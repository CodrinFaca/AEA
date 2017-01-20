package main;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Random;

public class BoardGenerator {

	private SudokuCell [][] board;
	private int size;
	Random randGenerator = new Random();
	private int seed;
	
	public BoardGenerator(){
	}
	
	public BoardGenerator(int seed){
		this.seed = seed;
		randGenerator.setSeed(seed);
	}

	public boolean isCorrect(final int size){
		return Helpers.isCorrect(board);
	}

	private boolean isSafe(final int num, final int row, final int col) {
		return Helpers.isSafe(board, num, row, col);
	}

	private int variantsPerCell() {
        return size;
    }

	private int getRandomIndex() {
        return (int) randGenerator.nextInt(9) + 1;
    }

	private Coordinates nextCell(final int row, final int col) {
		return Helpers.nextCell(row, col, size);
	}

	private void generateCorrectBoard(final int row, final int col) {
		if (!board[size - 1][size - 1].isFilled() ) {
			while (board[row][col].numberOfTried() < variantsPerCell()) {
				int candidate = 0;
				do {
					candidate = getRandomIndex();
				} while (board[row][col].isTried(candidate));

				if (isSafe(candidate, row, col)) {
					board[row][col].set(candidate);
					Coordinates nextCell = nextCell(row, col);
					if (nextCell.x < size && nextCell.y < size)
						generateCorrectBoard(nextCell.y, nextCell.x);
				}
				else
					board[row][col].tryNumber(candidate);
			}
			if (!board[size - 1][size - 1].isFilled())
				board[row][col].reset();
		}
	}

	public void generateCorrectBoard(final int s){
		size = s;

		board = new SudokuCell[size][size];
		for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            	board[i][j] = new SudokuCell();
            }
        }

		generateCorrectBoard(0, 0);

	}

	public void removeNumbers(final int leftNumbersNmb){
		ArrayList<Integer> cellsToRemove = new ArrayList<Integer>();
		for (int i = 0; i < size * size; i++)
			cellsToRemove.add(i);

		for (int i = 0; i < leftNumbersNmb; i++) {
			Collections.shuffle(cellsToRemove, randGenerator);
			cellsToRemove.remove(0);
		}

		for (int i = 0; i < cellsToRemove.size(); i++) {
			Coordinates currentCell = currentCell(cellsToRemove.get(i));
			board[currentCell.y][currentCell.x].clear();
		}
	}

	private Coordinates currentCell(final int currectCell) {
		return Helpers.currentCell(currectCell, size);
	}

	public int [][] getCurrentBoard(){
		int [][] intBoard = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				intBoard[i][j] = board[i][j].get();
			}
		}
		return intBoard;
	}
}
