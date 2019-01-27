import java.util.ArrayList;
import java.util.Collection;

public class State
{
    public Coordinates pos;
    public boolean on;
    public int orientation; //0 = NORTH, 1 = EAST, 2 = SOUTH, 3 = WEST
    public int dirtsLeft;
    public String previousMove;
    public boolean turnAround;

    public State(){
        pos = new Coordinates();
        on = false;
        orientation = 0;
        dirtsLeft = 0;
        previousMove = null;
        turnAround = false;
    }

    public State(State copy){
        this.pos = new Coordinates(copy.pos.x, copy.pos.y);
        this.on = copy.on;
        this.orientation = copy.orientation;
        this.dirtsLeft = copy.dirtsLeft;
        if(copy.previousMove != null){this.previousMove = new String(copy.previousMove);}
        else{this.previousMove = null;}
        this.turnAround = copy.turnAround;
    }
    
    public ArrayList<String> availableMoves(SuperAgent sa){
        ArrayList<String> moves = new ArrayList<String>();
        //check if turn on
        if(previousMove == null){
            moves.add("TURN_ON");
            return moves;
        }
        //check if turn off
        if(dirtsLeft == 0 && pos.equals(sa.startingPosition)){
            moves.add("TURN_OFF");
            return moves;
        }
        //check if dirt
        for(Coordinates dirt: sa.dirts){
            if(pos.equals(dirt)){
                moves.add("SUCK");
                return moves;
            }
        }
        //check if turning around
        if(turnAround){
            moves.add("TURN_RIGHT");
            return moves;
        }
        //**check walls**
        //check front wall
        boolean goObstacle = false; //boolean if obstacle at go
        boolean leftObstacle = false; //boolean if obstacle at left
        boolean rightObstacle = false; //boolean if obstacle at right
        Coordinates goCoor = calculateGo(orientation);
        Coordinates leftCoor = calculateGo((orientation-1)%4);
        Coordinates rightCoor = calculateGo((orientation+1)%4);
        for(Coordinates obs: sa.obstacles){
            if(goCoor.equals(obs)){
                goObstacle = true;
            }
            if(leftCoor.equals(obs)){
                leftObstacle = true;
            }
            if(rightCoor.equals(obs)){
                rightObstacle = true;
            }
        }
        if(goObstacle && rightObstacle && leftObstacle){
            moves.add("TURN_AROUND");
            return moves;
        }
        //remove moves that would bump to wall
        moves.add("GO");
        moves.add("TURN_RIGHT");
        moves.add("TURN_LEFT");
        if(goObstacle){
            moves.remove("GO");
        }
        if(leftObstacle){
            moves.remove("TURN_LEFT");
        }
        if(rightObstacle){
            moves.remove("TURN_RIGHT");
        }
        return moves;
    }

    //calculates where the robot would be if it had the new oritentation
    private Coordinates calculateGo(int newOrientation){
        Coordinates newCoor;
        if(newOrientation == 0){
            newCoor = new Coordinates(pos.x, pos.y+1);
        }else if(newOrientation == 1){
            newCoor = new Coordinates(pos.x+1, pos.y);
        }else if(newOrientation == 2){
            newCoor = new Coordinates(pos.x, pos.y-1);
        }else{
            newCoor = new Coordinates(pos.x-1, pos.y);
        }
        return newCoor;
    }
}