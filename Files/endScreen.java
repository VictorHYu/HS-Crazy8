import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

class endScreen extends JFrame
{  
  private DrawArea board = new DrawArea (1000, 700); // Area for graphics to be displayed
  private boolean playerWin;
  private Image rain, firework1, firework2;
  
  public endScreen( boolean playerWin ) //takes the boolean to know whether player has won or not
  {
    this.playerWin = playerWin; 
    
    //load images
    try
    {
      // load files into Image objects
      rain = ImageIO.read (new File ("res/rain.png"));
      firework1 = ImageIO.read (new File ("res/Fireworks.png"));
      firework2 = ImageIO.read (new File ("res/Fireworks2.png")); 
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
        Main.playAgain = true;
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
      if (playerWin) //win screen
      {
        //fill background
        for (int i = 0; i < 125; i++)
        {
          Color back = new Color (0 + i , 128 + i, 255);
          g.setColor(back);
          g.fillRect(0 + (int) (i*2.5) ,0 + (int) (i* 2.5),1000 - i*5 ,700 - i*5 );
        }
        
        //draw image
        g.drawImage(firework1,150,250,null);
        g.drawImage(firework2,420,100,null);
        
        //draw title
        g.setColor (Color.black);
        g.setFont(new Font("Serif", Font.PLAIN, 75));
        g.drawString("YOU WIN!!!", 320, 150);  

        //draw message
        g.setFont(new Font("Serif", Font.PLAIN, 30));
        g.drawString("Click anywhere to play again!",350,600);
      }
      else //lose screen
      {
        //fill background
        for (int i = 0; i < 125; i++)
        {
          Color back = new Color (128+i, 0 + i, 255);
          g.setColor(back);
          g.fillRect(0 + (int) (i*2.5) ,0 + (int) (i* 2.5),1000 - i*5 ,700 - i*5 );
        }
        
        //draw title
        g.setColor (Color.black);
        g.setFont(new Font("Serif", Font.PLAIN, 75));
        g.drawString("You lost...", 340, 150);  
        
        //draw image
        g.drawImage(rain,350,240,null);
        g.drawImage(rain,250,270,null);
        g.drawImage(rain,450,210,null);
        g.drawImage(rain,550,230,null);
        g.drawImage(rain,150,250,null);
        g.drawImage(rain,650,260,null);
        
        //draw message
        g.setFont(new Font("Serif", Font.PLAIN, 30));
        g.drawString("Click anywhere to play again!",340,600);
      }
    }
  }
}
