import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class SuperAgent implements Agent
{
	private Random random = new Random();
	public int roomLength;
	public int roomHeight;
	public Coordinates startingPosition;
	public ArrayList<Coordinates> dirts;
	public ArrayList<Coordinates> obstacles;
	public int orientation;
	//public Stack<String> moves;

	public SuperAgent() {
		roomLength = 0;
		roomHeight = 0;
		
		startingPosition = new Coordinates();

		dirts = new ArrayList<Coordinates>();
		obstacles = new ArrayList<Coordinates>();

		int orientation = 0; //0 = NORTH, 1 = EAST, 2 = SOUTH, 3 = WEST

		//moves = new Stack<String>();
	}

    public void init(Collection<String> percepts) {
		/*
			Possible percepts are:
			- "(SIZE x y)" denoting the size of the environment, where x,y are integers
			- "(HOME x y)" with x,y >= 1 denoting the initial position of the robot
			- "(ORIENTATION o)" with o in {"NORTH", "SOUTH", "EAST", "WEST"} denoting the initial orientation of the robot
			- "(AT o x y)" with o being "DIRT" or "OBSTACLE" denoting the position of a dirt or an obstacle
			Moving north increases the y coordinate and moving east increases the x coordinate of the robots position.
			The robot is turned off initially, so don't forget to turn it on.
		*/
		Pattern perceptNamePattern = Pattern.compile("\\(\\s*([^\\s]+)\\s*(\\w+).*");
		for (String percept:percepts) {
			Matcher perceptNameMatcher = perceptNamePattern.matcher(percept);
			if (perceptNameMatcher.matches()) {
                String perceptName = perceptNameMatcher.group(1);
                //Prints out the home cell of the robot.
				if (perceptName.equals("HOME")) {
					Matcher m = Pattern.compile("\\(\\s*HOME\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Robot is at " + m.group(1) + "," + m.group(2));
						//Set Starting position variables
						startingPosition.x = Integer.parseInt(m.group(1));
						startingPosition.y = Integer.parseInt(m.group(2));
                    }
                //Detects and prints the orientation of the robot.
                } else if (perceptName.equals("ORIENTATION")) {
                    //This regex can probably be shortened
                    Matcher m = Pattern.compile("\\(\\s*ORIENTATION\\s+(NORTH+)*\\)|\\(\\s*ORIENTATION\\s+(SOUTH+)*\\)|\\(\\s*ORIENTATION\\s+(WEST+)*\\)|\\(\\s*ORIENTATION\\s+(EAST+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Robot faces " + m.group(1));
						//Set orientation variable
						if(m.group(1).compareTo("NORTH") == 0){
							orientation = 0;
						} else if(m.group(1).compareTo("EAST") == 0){
							orientation = 1;
						} else if(m.group(1).compareTo("SOUTH") == 0){
							orientation = 2;
						} else {
							orientation = 3;
						}
					}
                //Prints all obstacles in the environment
                } else if (perceptNameMatcher.group(2).equals("OBSTACLE")) {
                    Matcher m = Pattern.compile("\\(\\s*AT\\s*OBSTACLE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("OBSTACLE detected at " + m.group(1) + "," + m.group(2));
						//Add detected obstacle to obstacles variable
						Coordinates newObstacle = new Coordinates(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
						obstacles.add(newObstacle);
                    }
                //Prints all cells which contain DIRT in the environment
                } else if (perceptNameMatcher.group(2).equals("DIRT")) {
                    Matcher m = Pattern.compile("\\(\\s*AT\\s*DIRT\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("DIRT detected at " + m.group(1) + "," + m.group(2));
						//Add detected dirt to dirts variable
						Coordinates newDirt = new Coordinates(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
						dirts.add(newDirt);
                    }
                //Prints the size of the environment
                } else if (perceptName.equals("SIZE")) {
                    Matcher m = Pattern.compile("\\(\\s*SIZE\\s+([0-9]+)\\s+([0-9]+)\\s*\\)").matcher(percept);
					if (m.matches()) {
						System.out.println("Size of the environment is " + m.group(1) + " by " + m.group(2));
						//Set room height and length
						roomLength = Integer.parseInt(m.group(1));
						roomHeight = Integer.parseInt(m.group(2));
                    }
                }
                 else {
					System.out.println("other percept:" + percept);
				}
			} else {
				System.err.println("strange percept that does not match pattern: " + percept);
			}
		}
		//Print constructor variables to check they are correct
		System.out.println("Room size: (" + roomLength + "," + roomHeight + ")");
		System.out.println("Starting Position: (" + startingPosition.x + "," + startingPosition.y + ")");
		System.out.println("Starting Orientation: " + orientation);
		
		System.out.print("Dirt coordinates: ");
		for(Coordinates dirt: dirts){
			System.out.print("(" + dirt.x + "," + dirt.y + ") ");
		}
		System.out.println("");

		System.out.print("Obstacle coordinates: ");
		for(Coordinates obstacle: obstacles){
			System.out.print("(" + obstacle.x + "," + obstacle.y + ") ");
		}
		System.out.println("");
		/**************************************************************END CHECK */
		//CREATE THE MOVES STACK
		State initialState = new State();
		initialState.pos = new Coordinates(this.startingPosition.x, this.startingPosition.y);
		initialState.orientation = this.orientation;
		initialState.dirtsLeft = this.dirts.size();
		System.out.println("INITIALIZING BFS*****************");
		BFS bfs = new BFS(initialState, this);
		boolean foundpath = bfs.findPath();
		if(foundpath){
			System.out.println("PATH FOUND******************");
		}
		else{
			System.out.println("PATH NOT FOUND**************");
		}
    }

    public String nextAction(Collection<String> percepts) {
		/*String move = moves.pop();
		return move;*/
		System.out.print("perceiving:");
		for(String percept:percepts) {
			System.out.print("'" + percept + "', ");
		}
		System.out.println("");
		String[] actions = { "TURN_ON", "TURN_OFF", "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" };
		return actions[random.nextInt(actions.length)];
	}
}
