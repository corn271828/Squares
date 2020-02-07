package squares;

import squares.api.CharacterState;

public class Player {

    public CharacterState charState;
    public int xPosition; //Target position of the character in grid coordinates
    public int yPosition;
    public int xTarg; // Target position of the character in panel coordinates
    public int yTarg;
    public int xCoordinates; //Position of the upper left hand corner of the character pic in panel coordinates
    public int yCoordinates;
	public Level level;
    
}
