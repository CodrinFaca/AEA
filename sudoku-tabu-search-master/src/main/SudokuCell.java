package main;
import java.util.HashSet;

public class SudokuCell {
	private int value;
    private boolean filled;
    private HashSet<Integer> tried;

    public SudokuCell(){
        this(0);
    }
    
    public SudokuCell(int value){
        filled = false;
        tried = new HashSet<Integer>();
        this.value = value;
    }

    public boolean isFilled() {
        return filled;
    }

    public int get() {
        return value;
    }

    public void set(final int number){  	
        filled = true;
        value = number;
        tried.add(number);
    }

    public void clear() {
        value = 0;
        filled = false;
    }

    public void reset() {
        clear();
        tried.clear();
    }

    public void show() {
        filled = true;
    }

    public void hide() {
        filled = false;
    }

    public boolean isTried(final int number){    	
        return tried.contains(number);
    }

    public void tryNumber(final int number){   	
        tried.add(number);
    }

    public int numberOfTried() {
        return tried.size();
    }
}
