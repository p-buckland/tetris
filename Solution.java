import java.awt.*;
import java.io.*;
import java.util.*;

public class Solution {

  public static final String[] NAMES = {"START GAME", "END GAME"};

  private Point[][] grid;
  private Deque<Shape> queue;

  private Color[] colorOptions; 
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
    this.colorOptions = new Color[] { new Color(50, 158, 168), new Color(79, 235, 52), 
        new Color(168, 162, 50), new Color(154, 50, 168), new Color(168, 127, 50)};
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

    public void removeFullLines() {
    // Check to see if the line is full
    int numFullLines = 0;
    Boolean isFull = false;
    for (int i = 0; i <display.getNumRows(); i++){
      for (int j = 0; j < display.getNumColumns(); j++){
        if(grid[i][j] != null){ // Find if spot is taken. Inner loop looks at the collumn of a row
          numFullLines++; // How many pieces are in my row.
          if(numFullLines == display.getNumColumns()){ //Check it line is full
            isFull = true; // If the line is full 
          }
        }
      }
      // Since we already iterated over the collumns of the row t0 see if it was full 
      // we need to iterate throught the column again.
     for (int k = 0; k < display.getNumColumns(); k++){
        if(isFull){
          grid[i][k] = null;
        }
      }
      isFull = false;
      numFullLines = 0;
    }
  }
  public boolean canMoveDown(Shape currShape){

    for(Point p: currShape.getPoints()){

      if(p.getRow() + 1 >= display.getNumRows()){
        return false; 
      }
      //if the next space down is not null and is not a point part of your current shape then you can't move down
      else if( this.grid[p.getRow()+1][p.getColumn()] != null && 
          !currShape.getPoints().contains(this.grid[p.getRow()+1][p.getColumn()]) ){
        
        return false; 
      }

    }

    return true; 
  }

  public void step(Shape currShape){
    //if the shape cannot move down stop moving it
    removeFullLines();
    if(!canMoveDown(currShape)){
      currShape.stopMoving();
    }
    //if you can move down
    //make the grid null for all the old locations
    if(canMoveDown(currShape)){
      for(Point p: currShape.getPoints()){
       // int[] mouseLoc = display.getMouseLocation(); // Line added troymendoza
        
        this.grid[p.getRow()][p.getColumn()] = null;
        p.row = p.row + 1; 
        this.grid[p.getRow()][p.getColumn()] = p; 
      }
    }
    
  }

  public void mouseMoveCol(Shape currShape, int mouseCol ){
    for(Point p: currShape.getPoints()){
      // Troy added lines below.
        if (mouseCol> p.column){ // IF the mouse click you further right then column moves.
          this.grid[p.getRow()][p.getColumn()] = null; //Delete old item
          p.column = p.column + 1;
          this.grid[p.getRow()][p.getColumn()] = p; // Move pieve on screen
        }
        else if (mouseCol < p.column){
          this.grid[p.getRow()][p.getColumn()] = null; //Delete old item
          p.column = p.column - 1;
          this.grid[p.getRow()][p.getColumn()] = p; // Move pieve on screen
        }
      }
  }

  /**Shape class to represent a tetris piece 
   * 
   */
   private static class Shape{
    private ArrayList<Point> shape; 
    private boolean isMoving; 
    
    //TODO: update code to randomize the column 
    //TODO: update code to randomize the color of a list of colors 

    public Shape (int shapeType){
      this.shape = new ArrayList<Point>();
      this.isMoving = true; 
      

      //insert reverse order because arraylist holds order and you want to update them from bottom of the grid to top
      
      //square shape piece
      if (shapeType == 0){
        shape.add(new Point(1, 6, new Color(92, 88, 182)));
        shape.add(new Point(1, 5, new Color(92, 88, 182)));
        shape.add(new Point(0, 6, new Color(92, 88, 182)));
        shape.add(new Point(0, 5, new Color(92, 88, 182)));
      } //L shape piece
      else if(shapeType == 1){
        shape.add(new Point(2, 6, new Color(185, 87, 206)));
        shape.add(new Point(2, 5, new Color(185, 87, 206)));
        shape.add(new Point(1, 5, new Color(185, 87, 206)));
        shape.add(new Point(0, 5, new Color(185, 87, 206)));
      } //straight piece 
      else if (shapeType == 2){
        shape.add(new Point(3, 5, new Color(255, 145, 26)));
        shape.add(new Point(2, 5, new Color(255, 145, 26)));
        shape.add(new Point(1, 5, new Color(255, 145, 26)));
        shape.add(new Point(0, 5, new Color(255, 145, 26)));
      }
    }

    public ArrayList<Point> getPoints(){
      return this.shape;
    }
    public boolean getMoving(){
      return this.isMoving; 
    }
    public void stopMoving(){
      this.isMoving = false; 
    }

   }

  /** Point class - four points make up a tetris shape 
  * 
  */
  private static class Point {
    private int row;
    private int column;
    private Color color; 


    public Point(int row, int column, Color color) {
      this.row = row;
      this.column = column;
      this.color = color; 
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }

    public Color getColor(){
      return color; 
    }

  }

  /**
   * Special random number generating class to randomize tetris shapes & colors 
   */
  public static class RandomGenerator {
    private static Random randomNumberGeneratorForColumn;
    private static Random randomNumberGeneratorForColor;
    private static int numCols; 

    public RandomGenerator(int numCols) {
      randomNumberGeneratorForColumn = new Random();
      randomNumberGeneratorForColor = new Random();
      this.numCols = numCols; 

    }

    /**
     * Method that returns a random column to start the tetris piece in
     */
    public int getRandomColumn() {
      return randomNumberGeneratorForColumn.nextInt(numCols-1);
    }

    public int getRandomColor(){
      return randomNumberGeneratorForColor.nextInt(4);
    }
  }

  public static void main(String[] args) {
    //deleted all this as it was for testing
  }

  /** Runner that controls the window until it is closed. */ //need to change this until the grid is at the top
  public void run() {
    while(!display.getGameStarted()){
      display.pause(1);
    }
    //TODO: add code here that it will not run another time if there is a piece at the top
    while (!display.getGameEnded()){
      if(this.queue.size() == 0 ){
        queue.push(new Shape(1));
        queue.push(new Shape(2));
        queue.push(new Shape(0));
      }
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
    Shape currShape = queue.pop();
    //add the new piece to the grid 
    for(Point p: currShape.getPoints()){ 
      grid[p.getRow()][p.getColumn()] = p; 
    }
    
    //while the piece is moving
    //currently only one piece will fall, I need to update the step code to get it to stop moving when it hits something
    while(currShape.getMoving()){
      step(currShape);
      
      //display.pause(500); //pause to wait for mouse click
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null) { // Test if mouse clicked
        mouseMoveCol(currShape, mouseLoc[1]);
      }
      
      //keep score function here 
      updateDisplay();
      display.repaint();
      display.pause(500); // Troy commented this

      //display.pause(500); // Wait for redrawing and for mouse
      //TODO: function that looks for lines to delete and keeps score
      //updateDisplay(); 
      //display.repaint();
      //pause for only 1 milisecond 


    }
  }

  
  /** Interface for the UI of the SandLab. */
  //Add functions here if you want to bring them over from SandDisplay
  public interface SandDisplayInterface {
    public void repaint();

    public void pause(int milliseconds);

    public int[] getMouseLocation();

    public int getNumRows();

    public int getNumColumns();

    public void setColor(int row, int col, Color color);

    public int getSpeed();

    public int getTool();

    public boolean getGameStarted();

    public boolean getGameEnded(); 
  }
}