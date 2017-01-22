class Hand extends Deck
{
  private boolean faceUp;
  
  public Hand(boolean faceUp)
  {
    super();
    deck.clear(); //initialize Hand objects as empty Decks
    this.faceUp = faceUp;
  }
  
  //same method but always flips cards down
  public void selectionSort()
  {
    super.selectionSort();
    //if player hand, keep cards face up
    if (this.faceUp == true)
      super.faceUp(true);
    //else it is AI hand, keep cards face down
    else
      super.faceUp(false);
  }
  
  //same method but always flips cards down
  public void quickSort(int low, int high)
  {
    super.quickSort(low,high);
        //if player hand, keep cards face up
    if (this.faceUp == true)
      super.faceUp(true);
    //else it is AI hand, keep cards face down
    else
      super.faceUp(false);
  }
  
  //takes in a suit to be searched and returns number of cards found
  private int search(int suit)
  {
    int counter = 0;
    for (int i = 0; i < deck.size(); i++)
    {
      if (deck.get(i).getSuit() == suit)
        counter ++;
    }
    return counter;
  }
  
  //returns most common suit found
  public int checkSuit()
  {
    //use above method to find number of cards in each suit
    int d, c, h, s;
    d = search(3);
    c = search (2);
    h = search (1);
    s = search (0);
    
    //return highest
    if (d >= c && d >= h && d >= s)
      return 3;
    else if (c >= d && c >= h && c >= s)
      return 2;
    else if (h >= d && h >= c && h >= s)
      return 1;
    else 
      return 0;
  }
}