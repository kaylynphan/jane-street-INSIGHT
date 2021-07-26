import java.io.*;
import java.util.Scanner;

/**
 * Main.java includes a Driver class (Main) as well as a Robot class, Obstacle class, and Command class.
 * @author kaylynphan
 */

public class Main {

	public static void main(String[] args) {
		double greatestDistance = 0.0;
		int initX = 0;
		int initY = 0;
		String initDirection = "";
		int numObstacles;
		int numCommands;
		Robot wally;
		//these initializations will be overridden
		Obstacle[] obstacles = new Obstacle[1];
		Command[] commands = new Command[1];
		
		// use try-catch block in case of FileNotFoundException
		try {
			File file = new File("input.txt");
			Scanner input = new Scanner(file);
			
			String initPos = input.nextLine();
			int spaceIndex = initPos.indexOf(" ");
			initX = Integer.parseInt(initPos.substring(0, spaceIndex));
			initPos = initPos.substring(spaceIndex + 1);
			spaceIndex = initPos.indexOf(" ");
			initY = Integer.parseInt(initPos.substring(0, spaceIndex));
			initDirection = initPos.substring(spaceIndex + 1);
			/*
			//debug
			System.out.println(initX);
			System.out.println(initY);
			System.out.println(initDirection);
			*/
			
			numObstacles = input.nextInt();
			numCommands = input.nextInt();
			/*
			//debug
			System.out.println(numObstacles);
			System.out.println(numCommands);
			*/
			obstacles = new Obstacle[numObstacles];
			commands = new Command[numCommands];
			
		
				//create array of obstacles
				obstacles = new Obstacle[numObstacles];
				for (int i = 0; i < numObstacles; i++) {
					int x = input.nextInt();
					int y = input.nextInt();
					/*
					//debug
					System.out.println(x);
					System.out.println(y);
					*/
					obstacles[i] = new Obstacle(x, y);
				}
				
				//create array of commands
				commands = new Command[numCommands];
				for (int i = 0; i < numCommands; i++) {
					String commandString = input.nextLine();
					if (!commandString.equals("")) {
						if (commandString.equals("R")) {
							commands[i] = new Command("R");
							//System.out.println("R");
						} else if (commandString.equals("L")) {
							commands[i] = new Command("L");
							//System.out.println("L");
						} else {
							commands[i] = new Command(Integer.parseInt(commandString.substring(2)));
							//System.out.println(commandString);
						}
					}
				}	
				//debug
				for (int i = 0; i < commands.length; i++) {
					System.out.println(commands[i]);
				}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		wally = new Robot(initX, initY, initDirection);
		wally.setObstacles(obstacles);
		wally.setCommands(commands);
		wally.followCommands();
	}
}

/**
 * The Robot class stores information about a robot that moves in four directions and can detect obstacles.
 * @author kaylynphan
 */
class Robot {
	int currentDirection;
	// north = 0, east = 1, south = 2, west = 3
	Obstacle[] obstacles;
	Command[] commands;
	int x;
	int y;
	double maxDistanceFromOrigin = 0.0;
	
	/**
	 * Constructor that creates a new Robot (such as Wally).
	 * @param x - robot's initial x position
	 * @param y - robot's initial y position
	 * @param direction - the direction the robot is initially facing
	 */
	public Robot(int x, int y, String direction) {
		this.x = x;
		this.y = y;
		if (direction.equals("N")) {
			this.currentDirection = 0;
		} else if (direction.equals("E")) {
			this.currentDirection = 1;
		} else if (direction.equals("S")) {
			this.currentDirection = 2;
		} else {
			this.currentDirection = 3;
		}
	}
	
	/**
	 * Method that programs Wally with information about obstacles.
	 * @param o - array of Obstacle objects.
	 */
	public void setObstacles(Obstacle[] o) {
		this.obstacles = o;
	}
	
	/**
	 * Method that programs Wally with a list of Command objects.
	 * @param c - array of Command objects.
	 */
	public void setCommands(Command[] c) {
		this.commands = c;
	}
	
	/**
	 * Method that makes Wally follow the list of commands provided to him.
	 */
	public void followCommands() {	
		for (int i = 0; i < commands.length; i++ ) {
			move(commands[i]);
		}
	}
	
	/**
	 * Method that makes Wally carry out a single command.
	 * @param c - the Command that Wally will interpret and follow.
	 */
	public void move(Command c) {
		if (c != null) {
			if (c.turn.equals("L")) {
				currentDirection--;
				if (currentDirection == -1) {
					currentDirection = 3;
				}
			} else if (c.turn.equals("R")) {
				currentDirection++;
				if (currentDirection == 4) {
					currentDirection = 0;
				}
			} else {
				moveForward(c.numSteps);
			}
		}
	}
	
	/**
	 * Method that moves Wally forward by a certain number of steps.
	 * @param numSteps - the number of steps that Wally moves forward.
	 */
	public void moveForward(int numSteps) {
		for (int i = 0; i < numSteps; i++) {
			moveForwardOneStep();
		}
	}
	
	/**
	 * Method that moves Wally forward by one step. If Wally encounters an obstacle, Wally moves back to the original position.
	 */
	public void moveForwardOneStep() {
		switch(currentDirection) {
			case 0:
				y++;
				break;
			case 1:
				x++;
				break;
			case 2:
				y--;
				break;
			case 3:
				x--;
				break;
		}
		
		for (int i = 0; i < obstacles.length; i++) {
			if (obstacles[i].x == x && obstacles[i].y == y) {
				//undo movement
				switch(currentDirection) {
					case 0:
						y--;
						break;
					case 1:
						x--;
						break;
					case 2:
						y++;
						break;
					case 3:
						x++;
						break;
				}
			}
		}
		calcDistanceFromOrigin();
	}
	
	/**
	 * Method that calculates Wally's distance from origin at the moment the method is called. The method also updates Wally's farthest distance from origin.
	 */
	public void calcDistanceFromOrigin() {
		double distance = (Math.sqrt((x * x) + (y * y)));
		if (distance > maxDistanceFromOrigin) {
			maxDistanceFromOrigin = distance;
			System.out.println("new max distance: " + distance);
		}
	}
}

/**
 * The Obstacle class stores information about an obstacle that Wally may encounter.
 * @author kaylynphan
 */
class Obstacle {
	int x;
	int y;
	
	/**
	 * Constructor used to create an Obstacle object.
	 * @param x - the obstacle's x position
	 * @param y - the obstacle's y position
	 */
	public Obstacle(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
}

/**
 * The Command class stores information about a single command that Wally follows.
 * @author kaylynphan
 */
class Command {
	int numSteps;
	String turn; // This will be L or R or M n

	/**
	 * Constructor used for turn commands.
	 * @param turn - String that dictates the direction of Wally's turn.
	 * Precondition: turn is always "L" or "R"
	 */
	public Command(String turn) {
		this.turn = turn;
		this.numSteps = 0;
	}
	
	/**
	 * Constructor used for non-turn commands. In these cases, Wally is moving forward.
	 * @param numSteps - the number of steps Wally is taking
	 * Precondition: numSteps >= 0
	 */
	public Command(int numSteps) {
		this.numSteps = numSteps;
		this.turn = "M"; // no turn at all
	}
	
	/**
	 * Returns a command in String format.
	 */
	@Override
	public String toString() {
		return "Command: Direction: " + this.turn + " Num Steps: " + numSteps;
 	}
}
