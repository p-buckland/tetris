import java.awt.*;
import java.io.*;
import java.util.*;

public class Solution {


  //public static final int EMPTY = 0;
  //public static final int TETRIS = 1;

  public static final String[] NAMES = {"Add Score here", "show next piece"};

  private Point[][] grid;
  private Deque<Shape> queue;

  private SandDisplayInterface display;
  private RandomGenerator random;

  /**
   * Constructor.
   *
   * @param display The display to use for this run
   * @param random The random number generator to use to pick random points
   */
  public Solution(SandDisplayInterface display, RandomGenerator random) {
    this.display = display;
    this.random = random;
    this.grid = new Point[display.getNumRows()][display.getNumColumns()];
    this. queue = new ArrayDeque<Shape>();

    //adding two tetris pieces to the queue
    queue.push(new Shape(0));
    queue.push(new Shape(0));
  }


  /**
   * Called when the user clicks on a location.
   *
   * @param col
   */
  private void locationClicked(int row, int col, int tool) {
    // TODO: when a user clicks on a location it will drop the animated tetris piece 
    // and turn it into a static piece

    
  }

  /** Copies each element of grid into the display. */
  public void updateDisplay() {

    for (int i = 0; i <display.getNumRows(); i++){
      for (int j = 0; j < display.getNumColumns(); j++){
        
        if(grid[i][j] != null){
          display.setColor(i, j, grid[i][j].getColor());
        }else if(grid[i][j] == null){
          display.setColor(i, j, Color.BLACK);
         }
        }

      }
    }

  /** called repeatadly - moves the animated tetis peice down 1*/
  public boolean step() {
    //move the points down one for the moving piece
    for (int i = display.getNumRows() -1; i >= 0; i--){
      for (int j = display.getNumColumns() -1; j >=0; j--){

        if(grid[i][j] != null && (i +1) < display.getNumRows()){
          if(grid[i][j].getMoving()){
            grid[i+1][j] = grid[i][j];
            grid[i][j] = null; 
          }
        }

        //TODO: add code that makes the status of a shape not moving if it hits something

      }
    }
    
    return false; //if you couldn't move the piece any further down 
  }

  /**Classes 
   */

   private static class Shape{
    private ArrayList<Point> shape; 
    private boolean isMoving; 
    
    //TODO: update code to randomize the location in the first row
    //TODO: update code to randomize the color of a list of colors 

    public Shape (int shapeType){
      this.shape = new ArrayList<Point>();
      this.isMoving = true; 

      //square shape piece
      if (shapeType == 0){
        shape.add(new Point(0, 5, new Color(79, 235, 52)));
        shape.add(new Point(0, 6, new Color(79, 235, 52)));
        shape.add(new Point(1, 5, new Color(79, 235, 52)));
        shape.add(new Point(1, 6, new Color(79, 235, 52)));

      }
    }

    public ArrayList<Point> getPoints(){
      return this.shape;
    }
    public boolean getMoving(){
      return this.isMoving; 
    }

   }


  private static class Point {
    private int row;
    private int column;
    private boolean moving; //true if falling, false if static 
    private boolean isPiece; 
    private Color color; 


    public Point(int row, int column, Color color) {
      this.row = row;
      this.column = column;
      this.moving = true; 
      this.isPiece = true; 
      this.color = color; 
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }

    public void setRow(){
      this.row = row +1;
    }

    public void setColumn(){
      this.column = column;
    }

    public boolean getMoving(){
      return moving; 
    }

    public boolean getIsPiece(){
      return isPiece; 
    }

    public void notMoving(){
      this.moving = false; 
    }

    public Color getColor(){
      return color; 
    }

  }

  /**
   * Special random number generating class to help get consistent results for testing.
   *
   * <p>Please use getRandomPoint to get an arbitrary point on the grid to evaluate.
   *
   * <p>When dealing with water, please use getRandomDirection.
   */
  public static class RandomGenerator {
    private static Random randomNumberGeneratorForPoints;
    private static Random randomNumberGeneratorForDirections;
    private int numRows;
    private int numCols;

    public RandomGenerator(int seed, int numRows, int numCols) {
      randomNumberGeneratorForPoints = new Random(seed);
      randomNumberGeneratorForDirections = new Random(seed);
      this.numRows = numRows;
      this.numCols = numCols;
    }

    public RandomGenerator(int numRows, int numCols) {
      randomNumberGeneratorForPoints = new Random();
      randomNumberGeneratorForDirections = new Random();
      this.numRows = numRows;
      this.numCols = numCols;
    }


    /**
     * Method that returns a random direction.
     *
     * @return an int indicating the direction of movement: 0: Indicating the water should attempt
     *     to move down 1: Indicating the water should attempt to move right 2: Indicating the water
     *     should attempt to move left
     */
    public int getRandomDirection() {
      return randomNumberGeneratorForDirections.nextInt(3);
    }
  }

  public static void main(String[] args) {
    //deleted all this as it was for testing
  }

  /** Runner that advances the display a determinate number of times. */
  private void runNTimes(int times) {
    for (int i = 0; i < times; i++) {
      runOneTime();
    }
  }

  /** Runner that controls the window until it is closed. */ //need to change this until the grid is at the top
  public void run() {
    while (true) {
      runOneTime();
    }
  }


  //THIS IS WHAT CONTROLS THE GAME
  /**
   * Runs one iteration of the display. Note that one iteration may call step repeatedly depending
   * on the speed of the UI.
   */
  private void runOneTime() {
    
    //pop a piece out of the queue
    Shape currPiece = queue.pop();
    //add the new piece to the grid 
    for(Point p: currPiece.getPoints()){
      grid[p.getRow()][p.getColumn()] = p; 
    }
    
    //while the piece is moving
    //currently only one piece will fall, I need to update the step code to get it to stop moving when it hits something
    while(currPiece.getMoving()){
      step();
      updateDisplay();
      display.repaint();
      display.pause(1000); // Wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null) { // Test if mouse clicked
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }

  /**
   * An implementation of the SandDisplayInterface that doesn't display anything. Used for testing.
   */
  static class NullDisplay implements SandDisplayInterface {
    private int numRows;
    private int numCols;

    public NullDisplay(int numRows, int numCols) {
      this.numRows = numRows;
      this.numCols = numCols;
    }

    public void pause(int milliseconds) {}

    public int getNumRows() {
      return numRows;
    }

    public int getNumColumns() {
      return numCols;
    }

    public int[] getMouseLocation() {
      return null;
    }

    public int getTool() {
      return 0;
    }

    public void setColor(int row, int col, Color color) {}

    public int getSpeed() {
      return 1;
    }

    public void repaint() {}
  }

  /** Interface for the UI of the SandLab. */
  public interface SandDisplayInterface {
    public void repaint();

    public void pause(int milliseconds);

    public int[] getMouseLocation();

    public int getNumRows();

    public int getNumColumns();

    public void setColor(int row, int col, Color color);

    public int getSpeed();

    public int getTool();
  }
}
