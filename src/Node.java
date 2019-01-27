import java.util.Collection;

public class Node
{
	public State state;
	public ArrayList<Node> next;

	public Node(){
		state = new State();
		next = new ArrayList<Node>();
	}

	public Node(State _state){
		state = _state;
		next = new ArrayList<Node>();
	}
}