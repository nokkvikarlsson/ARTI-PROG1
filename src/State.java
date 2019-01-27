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
    public String previousMove;
    public boolean turnAround;

    public State(){
        posX = 0;
        posY = 0;
        on = false;
        orientation = 0;
        dirtsLeft = 0;
        previousMove = null;
    }

    public State(State copy){
        this.posX = copy.posX;
        this.posY = copy.posY;
        this.on = copy.on;
        this.orientation = copy.orientation;
        this.dirtsLeft = copy.dirtsLeft;
        this.previousMove = copy.previousMove;
    }
    
    public void availableMoves(){

    }
}