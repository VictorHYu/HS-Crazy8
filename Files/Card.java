import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

class Card
{
  //images all private because no subclasses are used
  private int rank, suit;
  private Image image;
  private boolean faceup = true;
  private static Image cardback; // shared image for back of card 
  private static Image rotated1; // shared image for back of card (rotated)
  private static Image rotated2; // shared image for back of card (rotated)
  
  //card constructor
  public Card(int id)
  {
    //input id 0-51
    rank = id % 13; //rank 0-12
    suit = id / 13; //suit 0-3
    
    image = null;
    try
    {
      image = ImageIO.read (new File ("cards/" + (id + 1) + ".gif")); // load file into Image object
      cardback = ImageIO.read (new File ("cards/bnew.gif")); // load file into Image object
      rotated1 = ImageIO.read (new File ("cards/bnew2.gif")); // load file into Image object
      rotated2 = ImageIO.read (new File ("cards/bnew2.gif")); // load file into Image object
    }
    catch (IOException e)
    {
      System.out.println ("File not found");
    }
  }
  
  public void show (Graphics g, int x, int y)  // draws card face up or face down
  {
    if (faceup) //check faceup boolean parameter
      g.drawImage (image, x, y, null); //draw faceup card
    else 
      g.drawImage (cardback, x, y, null); //draw facedown card
  }
  
//overloaded method draws rotated facedown cards, face left or right based on boolean parameter passing  
  public void show (Graphics g, int x, int y, boolean left)  // draws card face up or face down
  {
    if (left)
      g.drawImage (rotated1, x, y, null); //draw facedown card rotated
    else
      g.drawImage (rotated2, x, y, null); //draw facedown card rotated
  }
  //getter method to check the private faceup parameter (boolean)
  public boolean getFaceup()
  {
    return faceup;
  }
  
  //getter method to check the private rank parameter (int)
  public int getRank()
  {
    return rank;
  }
  
  //getter method to check the private suit parameter (int)
  public int getSuit()
  {
    return suit;
  }
  
  //getter method used to get the desired card sort orders for selection sort - combines suit and rank to find ID value
  public int getID()
  {
    return (13-rank) + suit*13;
  }
  
  //flips the card by changing boolean parameter
  public void flip()
  {
    faceup = !faceup;
  }
  
  //compares this card with another sent through parameter passing
  public Boolean equals(Card second)
  {
    //check if same suit and rank
    if (getSuit() == second.getSuit() && getRank() == second.getRank())
      return true;
    else
      return false;
  }
  
  //returns suit
  public String toString()
  {
    if (this.suit == 0)
      return "Spades";
    else if (this.suit == 1)
      return "Hearts";
    else if (this.suit == 2)
      return "Clubs";
    else 
      return "Diamonds";
  }
  
  public void changeSuit(int suit)
  {
    int id;
    
    this.suit = suit;
    id = suit * 13 + rank;
    
    try
    {
      image = ImageIO.read (new File ("cards/" + (id + 1) + ".gif")); // load file into Image object
    }
    catch (IOException e)
    {
      System.out.println ("File not found");
    }
  }
}
