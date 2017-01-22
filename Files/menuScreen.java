import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

class menuScreen extends JFrame
{  
  private DrawArea board = new DrawArea (1000, 700); // Area for graphics to be displayed
  private Image image;
  
  public menuScreen()
  {
    //load image
    try
    {
      image = ImageIO.read (new File ("res/crazyEights.gif")); // load file into Image object
    }
    catch (IOException e)
    {
      System.out.println("File load error");
    }
    
    //detector for mouseclicks
    MouseAdapter adapter = new MouseAdapter ()
    { 
      //when mouse button is pressed
      public void mousePressed(MouseEvent e)
      { 
        Main.start = true;
        dispose();
      }
    };
    
    //add components
    board.addMouseListener(adapter);
    board.addMouseMotionListener(adapter);
    add(board);

    pack ();
    setTitle ("Crazy Eights");
    setSize (1000, 700);
    setResizable(false);
    setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); //closes window when X pressed
    setLocationRelativeTo (null);           // Center window.
  }
  
  //drawing area of JPanel that uses graphics to draw cards on screen
  class DrawArea extends JPanel
  {
    public DrawArea (int width, int height)
    {
      this.setPreferredSize (new Dimension (width, height)); // size
    }
    
    public void paintComponent (Graphics g)
    {
      //fill background
      for (int i = 0; i < 125; i++)
      {
        Color back = new Color (255, i*2, 0);
        g.setColor(back);
        g.fillRect(0 + (int) (i*2.5) ,0 + (int) (i* 2.5),1000 - i*5 ,700 - i*5 );
      }
      
      //draw title
      g.setColor (Color.black);
      g.setFont(new Font("Serif", Font.PLAIN, 75));
      g.drawString("Crazy Eights", 300, 150);  
      
      //draw image
      g.drawImage(image,370,240,null);
      
      //draw message
      g.setFont(new Font("Serif", Font.PLAIN, 30));
      g.drawString("Click anywhere to play!",370,600);
      
      //draw names
      g.setFont(new Font("Serif", Font.PLAIN, 25));
      g.drawString("By: Victor, Aurthy, and Sophia",340, 200);
    }
  }
}
