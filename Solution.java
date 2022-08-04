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


  /*Function that controls removing the full lines and calls helper functions
   * to clear the grid of a full line and then shift down all points above
   * 
   */
  public int countRemovedLines(){
    int linesRemoved = 0; 
    for (int i = 0; i < display.getNumRows(); i++){
      if(removeFullLines(i)){
        linesRemoved ++; 
      }
    }
    deleteEmptyLines();
    return linesRemoved; 

  }

  //return a boolean true if the given row is full and removed 
  public boolean removeFullLines(int row) {
    // Check to see if the given row is empty
    for (int i = 0; i <display.getNumColumns(); i++){
      if(grid[row][i] == null){
        return false; 
      }
    }

      //if you make it here the row is empty delete it
     for (int j = 0; j < display.getNumColumns(); j++){
          grid[row][j] = null; 
      }
      return true; 
  }


  //After a row is deleted because it's full this is called to shift everything down
  //Called by countRemovedLines()
  public void deleteEmptyLines(){
    //loop through the grid from bottom to top, if there is nothing below move it one down
    for (int i = display.getNumRows() - 1; i >= 0; i--){
      for (int j = display.getNumColumns() - 1; j >= 0; j--){
        Point p = this.grid[i][j];
        if(i + 1 < display.getNumRows()){
          if(this.grid[i+1][j] == null){
            this.grid[i][j] = null;
            this.grid[i+1][j] = p; 
          }
        }
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
      
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null) { // Test if mouse clicked
        mouseMoveCol(currShape, mouseLoc[1]);
      }
      
      int linesRemoved = countRemovedLines(); //removes full lines and counts them
      //TODO: implement score(linesRemoved); as the score will change based on how many removed at once, HashMap<>
      
      updateDisplay();
      display.repaint();
      display.pause(500); 
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