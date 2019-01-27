import java.util.ArrayList;
import java.util.Collection;

public class State
{
    public Coordinates pos;
    public boolean on;
    public int orientation; //0 = NORTH, 1 = EAST, 2 = SOUTH, 3 = WEST
    public ArrayList<Coordinates> dirtsLeft;
    public String previousMove;
    public boolean turnAround;
    //public Stack<String> moveHistory;

    public State(){
        pos = new Coordinates();
        on = false;
        orientation = 0;
        dirtsLeft = new ArrayList<Coordinates>();
        previousMove = null;
        turnAround = false;
        //moveHistory = new Linke
    }

    public State(State copy){
        this.pos = new Coordinates(copy.pos.x, copy.pos.y);
        this.on = copy.on;
        this.orientation = copy.orientation;
        this.dirtsLeft = new ArrayList<Coordinates>();
        for(Coordinates dirt: copy.dirtsLeft){
            this.dirtsLeft.add(new Coordinates(dirt.x, dirt.y));
        }
        if(copy.previousMove != null){this.previousMove = new String(copy.previousMove);}
        else{this.previousMove = null;}
        this.turnAround = copy.turnAround;
    }

    public boolean equals(State that){
        if(this.pos.equals(that.pos)
        && this.on == that.on
        && this.orientation == that.orientation
        && this.dirtsLeft.equals(that.dirtsLeft)
        && this.previousMove.equals(that.previousMove)
        && this.turnAround == that.turnAround){
            return true;
        }
        return false;
    }
    
    public ArrayList<String> availableMoves(SuperAgent sa){
        ArrayList<String> moves = new ArrayList<String>();
        //check if turn on
        if(previousMove == null){
            moves.add("TURN_ON");
            return moves;
        }
        //check if turn off
        if(dirtsLeft.size() == 0 && pos.equals(sa.startingPosition)){
            moves.add("TURN_OFF");
            return moves;
        }
        //check if dirt
        for(Coordinates dirt: dirtsLeft){
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
        //check obstacles
        boolean goObstacle = false; //boolean if obstacle at go
        boolean leftObstacle = false; //boolean if obstacle at left
        boolean rightObstacle = false; //boolean if obstacle at right
        Coordinates goCoor = calculateGo(orientation);
        Coordinates leftCoor = calculateGo((orientation+3)%4);
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
        //check borders
        if(goCoor.x == 0 || goCoor.y == 0 || goCoor.x == sa.roomHeight+1 || goCoor.y == sa.roomHeight+1){
            goObstacle = true;
        }
        if(leftCoor.x == 0 || leftCoor.y == 0 || leftCoor.x == sa.roomHeight+1 || leftCoor.y == sa.roomHeight+1){
            leftObstacle = true;
        }
        if(rightCoor.x == 0 || rightCoor.y == 0 || rightCoor.x == sa.roomHeight+1 || rightCoor.y == sa.roomHeight+1){
            rightObstacle = true;
        }
        //turn around if surrounded
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
        if(leftObstacle || (previousMove.compareTo("TURN_RIGHT") == 0) || (previousMove.compareTo("TURN_LEFT") == 0)){
            moves.remove("TURN_LEFT");
        }
        if(rightObstacle || (previousMove.compareTo("TURN_LEFT") == 0) || (previousMove.compareTo("TURN_RIGHT") == 0)){
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