import java.util.Collection;

/* State (class or an object):
	Posx
	Posy
	On
	Orientation
	DirtsCleaned
	availableMoves() //if on dirt, only move avaible is suck (saves memory by reducing         possible branches)
	execute(MOVE)
*/

public class State
{
    public int posX;
    public int posY;
    public boolean on;
    public int orientation; //0 = NORTH, 1 = EAST, 2 = SOUTH, 3 = WEST
    public int dirtsLeft;
    //available moves()
    //public void execute(MOVE)
}