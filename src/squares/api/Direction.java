package squares.api;

public enum Direction {
    UP   ("Up",    0, -1),
    LEFT ("Left", -1,  0),
    RIGHT("Right", 1,  0),
    DOWN ("Down",  0,  1);

    public final String name;
    public final int x, y;
    private Direction(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}; 
