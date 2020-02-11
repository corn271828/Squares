package squares.api;

public class Coordinate {
	public int x, y;
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof Coordinate)) return false;
		Coordinate c = (Coordinate) o;
		return x == c.x && y == c.y;
	}
	@Override
	public int hashCode() {
		return x * 31 + y;
	}
	@Override
	public String toString() {
		return String.format("(%d, %d)", x, y);
	}
}
