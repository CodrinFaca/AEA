package main;

public class SudokuBoard {

	private int [][] board;
	
	public SudokuBoard(int [][] initialBoard){
		board = initialBoard;
	}
	
	public int size(){
		return board.length;
	}
	
	public int [][] getBoard(){
		return board;
	}
	
	public void insert(Coordinates position, int value){			
		board[position.x][position.y] = value;
	}
	
	public void swapNumbersByCoordinates(Coordinates c1, Coordinates c2){		
		int tmp = board[c1.x][c1.y];
		board[c1.x][c1.y] = board[c2.x][c2.y];
		board[c2.x][c2.y] = tmp;
	}
}
