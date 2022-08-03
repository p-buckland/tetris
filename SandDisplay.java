import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class SandDisplay extends JComponent
    implements MouseListener,
        MouseMotionListener,
        ActionListener,
        Solution.SandDisplayInterface {
  private Image image;
  private int cellSize;
  private JFrame frame;
  private int tool;
  private int numRows;
  private int numCols;
  private int[] mouseLoc;
  private JButton[] buttons;
  private boolean gameStarted; 
  private boolean gameEnded; 

  public SandDisplay(String title, int numRows, int numCols, String[] buttonNames) {
    this.numRows = numRows;
    this.numCols = numCols;
    this.gameStarted = false; 
    this.gameEnded = false; 
    tool = 1;
    mouseLoc = null;

    // determine cell size
    cellSize = Math.max(1, 600 / Math.max(numRows, numCols));
    image = new BufferedImage(numCols * cellSize, numRows * cellSize, BufferedImage.TYPE_INT_RGB);

    frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
    frame.getContentPane().add(topPanel);

    setPreferredSize(new Dimension(numCols * cellSize, numRows * cellSize));
    addMouseListener(this);
    addMouseMotionListener(this);
    topPanel.add(this);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
    topPanel.add(buttonPanel);

    //Buttons code to work on if needed
    buttons = new JButton[buttonNames.length];

    for (int i = 0; i < buttons.length; i++) {
      buttons[i] = new JButton(buttonNames[i]);
      buttons[i].setActionCommand("" + i);
      buttons[i].addActionListener(this);
      buttonPanel.add(buttons[i]);
    }

    buttons[tool].setSelected(true);

    frame.pack();
    frame.setVisible(true);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(image, 0, 0, null);
  }

  public void pause(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public int getNumRows() {
    return numRows;
  }

  public int getNumColumns() {
    return numCols;
  }

  public int[] getMouseLocation() {
    return mouseLoc;
  }

  public int getTool() {
    return tool;
  }

  public boolean getGameStarted(){
    return gameStarted;
  }

  public boolean getGameEnded(){
    return gameEnded;
  }

  public void setColor(int row, int col, Color color) {
    Graphics g = image.getGraphics();
    g.setColor(color);
    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
  }

  public void mouseClicked(MouseEvent e) {
    int row = e.getXOnScreen();
    int collumn = e.getYOnScreen();
    System.out.print("The mouse as been clicked at [row, col]");
    System.out.print(Arrays.toString(toLocation(e)) + "\n");
  }

  public void mousePressed(MouseEvent e) {
    mouseLoc = toLocation(e);
  }

  public void mouseReleased(MouseEvent e) {
    mouseLoc = null;
  }

  public void mouseEntered(MouseEvent e) {}

  public void mouseExited(MouseEvent e) {}

  public void mouseMoved(MouseEvent e) {}

  public void mouseDragged(MouseEvent e) {
    mouseLoc = toLocation(e);
  }

  public int[] toLocation(MouseEvent e) {
    int row = e.getY() / cellSize;
    int col = e.getX() / cellSize;
    if (row < 0 || row >= numRows || col < 0 || col >= numCols) return null;
    int[] loc = new int[2];
    loc[0] = row;
    loc[1] = col;
    return loc;
  }

  public void actionPerformed(ActionEvent e) {
    tool = Integer.parseInt(e.getActionCommand()); 
    if (tool == 0){
      gameStarted = true; 
    }else if (tool == 1){
      gameEnded = true; 
    }
  }
  

  public static void main(String[] args) {
    // Interactive mode, create the GUI and run forever
    int numRows = 20;
    int numCols = 10;
    Solution lab =
        new Solution(
            new SandDisplay("Tetris", numRows, numCols, Solution.NAMES), //here will peek the upcoming pieces (if possible)
            new Solution.RandomGenerator(numCols));
    lab.run();
  }

  //doesn't allow us to delete this so leaving as blank we don't use
  @Override
  public int getSpeed() {
    return 0;
  }
}
