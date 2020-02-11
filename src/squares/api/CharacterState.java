package squares.api;

public enum CharacterState {
    NORMAL      (true),
    DEAD        (false),
    MOVING      (true),
    LOCKED      (true),
    WINE        (false),
    RESTARTING  (false),
    FASTMOVING  (true);

    public final boolean vulnerable;
    private CharacterState(boolean v) {
        vulnerable = v;
    }
}
