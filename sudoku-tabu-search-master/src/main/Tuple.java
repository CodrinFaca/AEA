package main;
public class Tuple<X, Y> { 
  public final X x; 
  public final Y y; 
  public Tuple(X x, Y y) { 
    this.x = x; 
    this.y = y; 
  }
  
  @Override public String toString(){
		StringBuilder result = new StringBuilder();
		result.append("<");
		result.append(x.toString());
		result.append("-");
		result.append(y.toString());
		result.append(">");
		return result.toString();
	}
}