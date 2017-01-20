package exception;

public class WrongSudokuSizeException extends Exception {
	private static final long serialVersionUID = 1L;

	public WrongSudokuSizeException(String msg) {
		super(msg);
	}
}
