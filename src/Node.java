import java.util.Collection;

/* State (class or an object):
	Posx
	Posy
	On
	Orientation
	DirtsCleaned
	availableMoves() //if on dirt, only move avaible is suck (saves memory by reducing possible branches)
	execute(MOVE)
*/

public class Node
{
	public State state;
	//next nodes
}