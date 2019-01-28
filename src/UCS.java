import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Collections;

public class UCS{
    public ArrayList<State> frontier;
    public State initialState;
    public SuperAgent sa;
    public ArrayList<String> visited;

    public UCS(State _initialState, SuperAgent _sa){
        frontier = new ArrayList<State>();
        initialState = new State(_initialState);
        sa = _sa;
        visited = new ArrayList<String>();
    }

    //returns true if path found. false if no answer
    public State findPath(){
        if(goalTest(initialState)){ return initialState; }
        frontier.add(frontier.size(),initialState);
        while(!frontier.isEmpty()){
            State currentState = frontier.remove(0);
            visited.add(currentState.getString());
            //PRINT CURRENT STATE
            currentState.printState();
            if(goalTest(currentState)){return currentState;}
            ArrayList<String> successors = currentState.availableMovesNotStrict(sa);
            for(String successor: successors){
                State successorState = new State(currentState);
                successorState.parent = currentState;
                successorState = fillSuccessor(successor, successorState, currentState);
                //if successor exists in history, dont add to frontier
                if(!visited.contains(successorState.getString())){
                    frontier.add(frontier.size(),successorState);
                    Collections.sort(frontier);
                }
            }
        }
        return null;
    }

    //returns true if at starting position, no dirt left and is OFF
    public boolean goalTest(State state){
        if(state.pos.equals(initialState.pos)
        && state.dirtsLeft.size() == 0
        && state.on == false){
            return true;
        }
        else{
            return false;
        }
    }

    public State fillSuccessor(String successor, State successorState, State currentState){
        if(successor.equals("TURN_ON")){
            successorState.on = true;
            successorState.previousMove = "TURN_ON";
            successorState.moveHistory.add("TURN_ON");
            successorState.cost = currentState.cost + 1;
        }
        else if(successor.equals("TURN_OFF")){
            successorState.on = false;
            successorState.previousMove = "TURN_OFF";
            successorState.moveHistory.add("TURN_OFF");
            if(successorState.pos.equals(sa.startingPosition)){
                successorState.cost = currentState.cost + 1 + 50*(successorState.dirtsLeft.size());
            }else{
                successorState.cost = currentState.cost + 100 + 50*(successorState.dirtsLeft.size());
            }
        }
        else if(successor.equals("SUCK")){
            int dirtIndex = -1;
            for(int i = 0; i < successorState.dirtsLeft.size(); i++){
                if(successorState.pos.equals(successorState.dirtsLeft.get(i))){
                    dirtIndex = i;
                }
            }
            successorState.dirtsLeft.remove(dirtIndex);
            successorState.previousMove = "SUCK";
            successorState.moveHistory.add("SUCK");
            if(dirtIndex == -1){
                successorState.cost = currentState.cost + 5;
            }
            else{
                successorState.cost = currentState.cost + 1;
            }
        }
        else if(successor.equals("TURN_RIGHT")){
            successorState.orientation = (currentState.orientation+1)%4;
            successorState.previousMove = "TURN_RIGHT";
            successorState.moveHistory.add("TURN_RIGHT");
            successorState.cost = currentState.cost + 1;
        }
        else if(successor.equals("TURN_LEFT")){
            successorState.orientation = (currentState.orientation+3)%4;
            successorState.previousMove = "TURN_LEFT";
            successorState.moveHistory.add("TURN_LEFT");
            successorState.cost = currentState.cost + 1;
        }
        else if(successor.equals("GO")){
            Coordinates newCoor;
            if(currentState.orientation == 0){newCoor = new Coordinates(currentState.pos.x, currentState.pos.y+1);}
            else if(currentState.orientation == 1){newCoor = new Coordinates(currentState.pos.x+1, currentState.pos.y);}
            else if(currentState.orientation == 2){newCoor = new Coordinates(currentState.pos.x, currentState.pos.y-1);}
            else{newCoor = new Coordinates(currentState.pos.x-1, currentState.pos.y);}
            successorState.pos = newCoor;
            successorState.previousMove = "GO";
            successorState.moveHistory.add("GO");
            successorState.cost = currentState.cost + 1;
        }
        return successorState;
    }
}