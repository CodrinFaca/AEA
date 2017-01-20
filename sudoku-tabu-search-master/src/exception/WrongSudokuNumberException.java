package exception;

public class WrongSudokuNumberException extends Exception {
	private static final long serialVersionUID = 1L;

	public WrongSudokuNumberException(String msg) {
		super(msg);
	}
}
