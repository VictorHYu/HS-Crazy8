import java.awt.*;
import java.util.*;

class Deck
{
  //this array is public because its parameters must be accessed from main (deck.length) to check the array length
  //for JComboBox options
  public ArrayList <Card> deck = new ArrayList <Card>();
  
  //deck constructor
  public Deck()
  {
    for (int x = 0 ; x < 52 ; x++)  // for each card in standard deck
      deck.add(new Card (x)); // create card
  }
  
  public void show (Graphics g, int x, int y, boolean horizontal)  // draws card face up or face down starting at given coordinates
  {
    if (horizontal)
    {
      for (int c = 0 ; c < deck.size() ; c++)
        deck.get(c).show (g, c * 20 + 50*x, 50* y); //draws card in rows and columns 
    }
    else
    {
      for (int c = 0 ; c < deck.size() ; c++)
        deck.get(c).show (g, 50*x, c*20 + 50* y); //draws card in rows and columns 
    }
  }
  
  //overloaded method to draw facedown rotated cards, boolean determines orientation
  public void show (Graphics g, int x, int y, boolean horizontal, boolean left)  // draws card face up or face down starting at given coordinates
  {
    if (left)
    {
      for (int c = 0 ; c < deck.size() ; c++)
        deck.get(c).show (g, 50*x, c * 20 + 50* y, true); //draws card in rows and columns 
    }
    else
    {
      for (int c = 0 ; c < deck.size() ; c++)
        deck.get(c).show (g, 50*x, c*20 + 50* y, false); //draws card in rows and columns 
    }
  }
  
  public void shuffle ()
  {
    for (int j = 0; j < 3; j++)
    {
      int size = deck.size();
      //temporary array to save data
      ArrayList <Card> temp = new ArrayList <Card>();
      temp = deck; 
      //one by one copy card objects from deck into temp;
      for(int i = 0; i < size; i++)
        temp.add ( deal ((int)(Math.random()*deck.size()))); //copy card using random number generator
      //copy temp card array back into main deck
      deck = temp;
    }
  }
  
  //removes card from specified index of deck
  public Card deal(int index)
  {
    Card removed = new Card (0);
    //check if index to remove is valid
    if (index < deck.size() && index >= 0)
    {
      removed = deck.remove(index); //remove from arraylist
      return removed;
    }
    else
      return null;
  }
  
  //deals top card
  public Card deal()
  {
    return deal(0);
  }
  
  //adds specified card to deck
  public void add(Card newCard)
  {
    deck.add(newCard); //add new card
  }
  
  public void quickSort (int low, int high)
  {
    //base case, do nothing if smallest pieces have been sorted
    if(high <= low || low >= high)
    {
    }
    
    else
    {
      Card temp;
      //Uses leftmost element as pivot
      int pivot = deck.get(low).getRank();
      int pivotSuit = deck.get(low).getSuit();
      int i = low + 1; //start checking indexes after the leftmost element
      
      //partition array 
      for(int j = low + 1; j<= high; j++)
      {
        if(pivot > deck.get(j).getRank()) //swap if rank less
        {
          //swap positions of cards
          temp = deck.get(j); 
          deck.set(j,deck.get(i)); 
          deck.set(i,temp);     
          i++; 
        }
      }
      
      //put pivot in right position
      deck.set(low, deck.get(i-1)); 
      deck.set((i-1), new Card(pivotSuit*13 + pivot));
      
      //recursively call quicksort again
      quickSort(low, i - 2); 
      quickSort(i, high);
    }
  }
  
  //sort cards by suit and then rank with selection sort algorithm. getID() method in Card ranks cards and gives them
  //integer value to be sorted by, then selection sort arranges cards from smallest ID to largest
  public void selectionSort()
  {
    int minimum;
    Card temp;
    //loop through all array positions
    for (int i = 0; i < deck.size() - 1; i++)
    {
      minimum = i;
      //loop through unsorted array
      for (int j = i + 1; j < deck.size(); j++)
      {
        //check if current position is smallest value in unsorted part
        if ((deck.get(j).getID()) < (deck.get(minimum).getID())) 
          minimum = j; 
      }
      //if the current position is not the smallest value in the unsorted portion of array, swap with smallest value
      if (minimum != i)
      {
        temp = deck.get(i);
        deck.set(i, deck.get(minimum));
        deck.set(minimum, temp);
      }
    }
  }
  
  //turns all cards faceup or facedown
  public void faceUp(Boolean up)
  {
    for(int i = 0; i < deck.size(); i++) //loop through all cards
    {
      if (up)
      {
        if (!deck.get(i).getFaceup()) //flip if card is face down
          deck.get(i).flip();
      }
      else
      {
        if (deck.get(i).getFaceup()) //flip if card is face up
          deck.get(i).flip();
      }
    }
  }
}