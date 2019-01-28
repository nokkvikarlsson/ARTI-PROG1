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
    public State findPath(){
        if(goalTest(initialState)){ return initialState; }
        frontier.add(initialState);
        while(!frontier.isEmpty()){
            State currentState = frontier.remove();
            //PRINT CURRENT STATE
            currentState.printState();
            ArrayList<String> successors = currentState.availableMoves(sa);
            for(String successor: successors){
                State successorState = new State(currentState);
                for(State s: frontier){
                    if(successorState.equals(s)){
                        continue;
                    }
                }
                if(successor.equals("TURN_ON")){successorState.on = true; successorState.previousMove = "TURN_ON"; successorState.moveHistory.add("TURN_ON"); }
                else if(successor.equals("TURN_OFF")){successorState.on = false; successorState.previousMove = "TURN_OFF"; successorState.moveHistory.add("TURN_OFF");}
                else if(successor.equals("SUCK")){
                    int dirtIndex = 0;
                    for(int i = 0; i < successorState.dirtsLeft.size(); i++){
                        if(successorState.pos.equals(successorState.dirtsLeft.get(i))){
                            dirtIndex = i;
                        }
                    }
                    successorState.dirtsLeft.remove(dirtIndex);
                    successorState.previousMove = "SUCK";
                    successorState.moveHistory.add("SUCK");
                }
                else if(successor.equals("TURN_RIGHT") || successor.equals("TURN_AROUND")){
                    successorState.orientation = (currentState.orientation+1)%4;
                    successorState.turnAround = false;
                    successorState.previousMove = "TURN_RIGHT";
                    successorState.moveHistory.add("TURN_RIGHT");
                }
                else if(successor.equals("TURN_LEFT")){successorState.orientation = (currentState.orientation+3)%4; successorState.previousMove = "TURN_LEFT"; successorState.moveHistory.add("TURN_LEFT");}
                else if(successor.equals("TURN_AROUND")){successorState.turnAround = true; successorState.previousMove = "TURN_RIGHT"; successorState.moveHistory.add("TURN_RIGHT");}
                else if(successor.equals("GO")){
                    Coordinates newCoor;
                    if(currentState.orientation == 0){newCoor = new Coordinates(currentState.pos.x, currentState.pos.y+1);}
                    else if(currentState.orientation == 1){newCoor = new Coordinates(currentState.pos.x+1, currentState.pos.y);}
                    else if(currentState.orientation == 2){newCoor = new Coordinates(currentState.pos.x, currentState.pos.y-1);}
                    else{newCoor = new Coordinates(currentState.pos.x-1, currentState.pos.y);}
                    successorState.pos = newCoor;
                    successorState.previousMove = "GO";
                    successorState.moveHistory.add("GO");
                }

                if(goalTest(successorState)){return successorState;}
                else{frontier.add(successorState);}
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
}