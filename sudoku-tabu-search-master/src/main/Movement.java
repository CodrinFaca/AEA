package main;

public class Movement {
	
	public Movement(Coordinates from, Coordinates to){
		this.from = from;
		this.to = to;
	}
	
	public Coordinates from;
	public Coordinates to;
	
	@Override public String toString(){
		StringBuilder result = new StringBuilder();
		result.append(from.toString());
		result.append(to.toString());
		return result.toString();
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof Movement))return false;
	    Movement otherMovement = (Movement)other;
	    return ((from.equals(otherMovement.from) && to.equals(otherMovement.to) ));
	}
	@Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }
}
