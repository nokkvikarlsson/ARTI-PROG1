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
    public int Posx;
    public int Posy;
    public boolean On;
    public int Orientation; //1 = NORTH, 2 = EAST, 3 = SOUTH, 4 = WEST
    public int DirtsCleaned;
    //available moves()
    //public void execute(MOVE)
}