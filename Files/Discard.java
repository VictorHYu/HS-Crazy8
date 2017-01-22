import java.awt.*;
import java.util.*;

class Discard extends Deck
{
  public Discard()
  {
    deck.clear(); //initialize Discard pile as empty
  }
  
  //when Discard pile is shown, only display the last card added to it, face up
  public void show (Graphics g, int x, int y)
  {
    if(deck.size() >= 1)
      deck.get(deck.size()-1).show(g,x,y);
  }
  
  //deals all cards into a Pickup object except 1(shuffle discard pile back into pickup deck when pickup deck empty)
  public void dealAll (Pickup destination) 
  {
    for (int i = 0; i < deck.size() - 1; i++)
      destination.deck.add(deal());
    destination.shuffle(); //shuffle deck after new cards are all added  
  }
}