package squares.api;

public enum Direction {
    UP("Up"),
    LEFT("Left"),
    RIGHT("Right"),
    DOWN("DOWN");

    public final String name;
    private Direction(String name) {
        this.name = name;
    }
}; 
