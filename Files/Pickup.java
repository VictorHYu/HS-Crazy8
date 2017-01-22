import java.awt.*;
import java.util.*;

class Pickup extends Deck
{
  //initialize with full deck - use constructor from Deck and flip all cards down
  public Pickup()
  {
    super();
    faceUp(false);
  }
  
  //when deck needs to be displayed, draw cards facedown 1 pixel apart so deck size can be seen
  public void show (Graphics g, int x, int y)
  {
    faceUp(false);
    for (int c = 0 ; c < deck.size() ; c++)
      deck.get(c).show(g,x + (int) (Math.floor(c/4)*2),y);
  }
  
  public Card deal ()
  {
    faceUp(true);
    return super.deal(0);
  }
}