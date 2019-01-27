import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class BFS{
    public Queue<State> frontier;
    public State initialState;
    public SuperAgent sa;

    public BFS(State _initialState, SuperAgent _sa){
        frontier = new LinkedList<State>();
        initialState = new State(_initialState);
        sa = _sa;
    }

    //returns true if path found. false if no answer
    public boolean findPath(){
        if(goalTest(initialState)){ return true; }
        frontier.add(initialState);
        while(!frontier.isEmpty()){
            State currentState = frontier.remove();
            ArrayList<String> successors = currentState.availableMoves(sa);
            for(String successor: successors){
                State successorState = new State(currentState);
                if(successor.equals("TURN_ON")){successorState.on = true;}
                else if(successor.equals("TURN_OFF")){successorState.on = false;}
                else if(successor.equals("TURN_RIGHT") || successor.equals("TURN_AROUND")){
                    successorState.orientation = (currentState.orientation+1)%4;
                    successorState.turnAround = false;
                }
                else if(successor.equals("TURN_LEFT")){successorState.orientation = (currentState.orientation-1)%4;}
                else if(successor.equals("TURN_AROUND")){successorState.turnAround = true;}
                else if(successor.equals("GO")){
                    Coordinates newCoor;
                    if(currentState.orientation == 0){newCoor = new Coordinates(currentState.pos.x, currentState.pos.y+1);}
                    else if(currentState.orientation == 1){newCoor = new Coordinates(currentState.pos.x+1, currentState.pos.y);}
                    else if(currentState.orientation == 2){newCoor = new Coordinates(currentState.pos.x, currentState.pos.y-1);}
                    else{newCoor = new Coordinates(currentState.pos.x-1, currentState.pos.y);}
                    successorState.pos = newCoor;
                }

                if(goalTest(successorState)){return true;}
                else{frontier.add(successorState);}
            }
        }
        return false;
    }

    //returns true if at starting position, no dirt left and is OFF
    public boolean goalTest(State state){
        if(state.pos.equals(initialState.pos)
        && state.pos.equals(initialState.pos)
        && state.dirtsLeft == 0
        && state.on == false){
            return true;
        }
        else{
            return false;
        }
    }
}