package squares.api;

public class AABB {
	public final int lx, ly, rx, ry;
	public AABB(Coordinate one, Coordinate two) {
		lx = Math.min(one.x, two.x);
		ly = Math.min(one.y, two.y);
		rx = Math.max(one.x, two.x);
		ry = Math.max(one.y, two.y);
	}
	public boolean contains(Coordinate c) {
		return contains(c.x, c.y);
	}
	public boolean contains(int x, int y) {
		return x >= lx && x <= rx && y >= ly && y <= ry;
	}
}
