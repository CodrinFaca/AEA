package main;

public class Coordinates {
	
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int x;
	public int y;
	
	@Override public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("<");
		result.append(x);
		result.append(",");
		result.append(y);
		result.append(">");
		return result.toString();
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Coordinates))return false;
	    Coordinates otherMyClass = (Coordinates)other;
	    return x == otherMyClass.x && y == otherMyClass.y;
	}
	
	@Override
    public int hashCode() {
        return 10*x + y;
    }
}
