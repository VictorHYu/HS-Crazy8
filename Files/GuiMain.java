import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.*; // for arraylist
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

class GuiMain extends JFrame
{  
  private static int clickX = 0, clickY = 0, hoverX = 0, hoverY = 0, colourSelection;
  private static JLabel[] names = new JLabel[4];
  private static boolean playerClick = false, passBool = false, click;
  public static JTextArea text = new JTextArea (50,50);
  private static JScrollPane scroll;  
  private static JButton oneCard,pass,diamonds,clubs,hearts,spades;
  private static Image icon = null;
  private static JLabel eightPlayed; 
  
  public GuiMain()
  {
    
    //******************************************************GUI SETUP*******************************************************
    
    JLayeredPane layeredPane = getLayeredPane(); //create layeredpane instead of contentpane
    
    //textarea - displays player actions
    text.setEditable(false);
    scroll = new JScrollPane(text);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setBounds(50,450,300,150);    
    
    //drawArea - card graphics drawn here
    DrawArea board = new DrawArea (1000, 700); // Area for cards to be displayed
    
    //configure buttons
    oneCard = new JButton("Last Card!");
    oneCard.addActionListener(new ButtonListener());
    pass = new JButton("Pass");
    pass.addActionListener(new ButtonListener());
    
    diamonds = new JButton("Diamonds");
    diamonds.addActionListener(new ButtonListener());
    clubs = new JButton("Clubs");
    clubs.addActionListener(new ButtonListener());
    hearts = new JButton("Hearts");
    hearts.addActionListener(new ButtonListener());
    spades = new JButton("Spades");
    spades.addActionListener(new ButtonListener());

    try 
    {  
      icon = ImageIO.read (new File ("res/diamonds.jpg"));
      diamonds.setIcon(new ImageIcon(icon));
      
      icon = ImageIO.read (new File ("res/clubs.png"));
      clubs.setIcon(new ImageIcon(icon));
      
      icon = ImageIO.read (new File ("res/hearts.png"));
      hearts.setIcon(new ImageIcon(icon));
      
      icon = ImageIO.read (new File ("res/spades.png")); 
      spades.setIcon(new ImageIcon(icon));
    }
    catch (IOException ex) 
    {
      System.out.println("Image icon load error");
    }
    
    //configure jlabels
    names[1] = new JLabel ("CPU 1");
    names[2] = new JLabel ("CPU 2");
    names[3] = new JLabel ("CPU 3");
    names[0] = new JLabel ("You");
    
    for (int i = 0; i < 4; i++)
    {
      names[i].setOpaque(true);
      names[i].setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    eightPlayed = new JLabel(" 8");
    eightPlayed.setOpaque(true);
    eightPlayed.setBackground(Color.yellow);
    eightPlayed.setFont(new Font("Serif", Font.PLAIN, 75));
    changeVisibility(false); //hide buttons and label until 8 is played
    
    //detector for mouseclicks
    MouseAdapter adapter = new MouseAdapter ()
    { 
      //when mouse button is pressed
      public void mousePressed(MouseEvent e)
      { 
        //update public variables
        clickX = e.getX();
        clickY = e.getY();
      }
    };

    board.addMouseListener(adapter);
    board.addMouseMotionListener(adapter);
    
    //add board
    board.setBounds(0,0,1000,700);
    
    //add buttons
    oneCard.setBounds(820, 480,100,30);
    pass.setBounds(820,450,100,30);
    diamonds.setBounds(770,30,50,50);
    spades.setBounds(770,80,50,50);
    clubs.setBounds(820,30,50,50);
    hearts.setBounds(820,80,50,50);
    
    //add labels
    names[0].setBounds(470, 570, 60, 30);
    names[1].setBounds(100, 275, 60, 30);
    names[2].setBounds(470, 5, 60, 30);
    names[3].setBounds(850, 275, 60, 30);
    eightPlayed.setBounds(700, 20, 180, 120);
    
    //choose depths when adding to layeredPane
    layeredPane.add(board, new Integer(0));
    layeredPane.add(oneCard, new Integer(1));
    layeredPane.add(pass, new Integer(1));
    layeredPane.add(diamonds, new Integer(2));
    layeredPane.add(clubs, new Integer(2));
    layeredPane.add(hearts, new Integer(2));
    layeredPane.add(spades, new Integer(2));
    layeredPane.add(eightPlayed, new Integer(1));
    layeredPane.add(names[0], new Integer(1));
    layeredPane.add(names[1], new Integer(1));
    layeredPane.add(names[2], new Integer(1));
    layeredPane.add(names[3], new Integer(1));
    layeredPane.add(scroll, new Integer(1));
    
    pack ();
    setTitle ("Crazy Eights");
    setSize (1000, 700);
    setResizable(false);
    setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE); //closes window when X pressed
    setLocationRelativeTo (null);           // Center window.
    
    
  }
  
  class ButtonListener implements ActionListener
    {
      public void actionPerformed (ActionEvent e)
      {
        if (e.getSource() == oneCard)
        {
          playerClick = true;
        }
        else if (e.getSource() == pass)
        {
          passBool = true;
        }
        else if (e.getSource() == diamonds)
        {
          colourSelection = 3;
          click = true;
        }
        else if (e.getSource() == clubs)
        {
          colourSelection = 2;
          click = true;
        }
        else if (e.getSource() == hearts)
        {
          colourSelection = 1;
          click = true;
        }
        else if (e.getSource() == spades)
        {
          colourSelection = 0;
          click = true;
        }
      }
    }
  
  //returns mouse coordinates
  public static int[] getMouse()
  {
    //copy values
    int[] coords = new int [4];
    coords [0] = GuiMain.clickX;
    coords [1] = GuiMain.clickY;
    coords [2] = GuiMain.hoverX;
    coords [3] = GuiMain.hoverY;
    
    //reset values
    GuiMain.clickX = 0;
    GuiMain.clickY = 0;
    GuiMain.hoverX = 0;
    GuiMain.hoverY = 0;
    
    return coords;
  }
  
  //resets stored mouse values to prevent input buffering
  public static void resetMouse()
  {
    GuiMain.clickX = 0;
    GuiMain.clickY = 0;
    GuiMain.hoverX = 0;
    GuiMain.hoverY = 0;
  }
  
  //returns whether user has clicked pass button
  public static boolean getPass()
  {
    return passBool;
  }
  
  //returns whether user has clicked oneCard button
  public static boolean getOneCard()
  {
    return playerClick;
  }
  
  //resets the status of user button clicks
  public static void resetButtons()
  {
    passBool = false;
    playerClick = false;
  }
  
  //changes colour of label based on which player's turn it is
  public static void changeTurn (int player)
  {
    for (int i = 0; i <4; i++)
    {
      if (i == player)
        names[i].setBackground(Color.yellow);
      else
        names[i].setBackground(Color.white);
    }
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
      Main.drawBackground(g);
      Main.drawHands(g);
    }
  }
  
  //adds text to jtextarea
  public static void addText (String s)
  {
    text.append(s);
    text.setCaretPosition(text.getDocument().getLength());
  }
  
  //changes visibility of colour change buttons
  public static void changeVisibility (Boolean choice)
  {
    diamonds.setVisible(choice);
    clubs.setVisible(choice);
    hearts.setVisible(choice);
    spades.setVisible(choice);
    eightPlayed.setVisible(choice);
  }
  
  //gets user input from jbuttons to change colour
  public static int changeColour ()
  {
    changeVisibility (true); //show buttons when player plays an 8
    colourSelection = -1; //reset value of selection
    click = false;
    resetMouse();
    while (click == false)
    {
      Main.sleep(1000);
    }
    return colourSelection;
  }
}
