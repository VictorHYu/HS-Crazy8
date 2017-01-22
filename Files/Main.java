import java.awt.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.*; // for arraylist
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access

class Main 
{
  //these variables are accessed by GuiMain to determine what should be drawn
  private static Pickup pile = new Pickup();
  private static Hand[] hands = new Hand[4];
  private static Discard disc = new Discard();
  private static int players = 1;
  private static int computers = 3;
  private static GuiMain window;
  private static int clickX, clickY, hoverX, hoverY;
  private static boolean reverse = false, pass = false, oneCard = false, playable, playerWin, running = true;
  private static int turnCounter, turnMod;
  private static int twoStack = 0, AISpeed = 1000;
  public static boolean start, playAgain;
  
  //refreshes click integer values
  private static void refreshMouse()
  {
    int coords[] = GuiMain.getMouse();
    clickX = coords[0];
    clickY = coords[1];
    hoverX = coords[2];
    hoverY = coords[3];
    //System.out.println("X: " + clickX + " and Y: " + clickY); //for testing
  }
  
  //refreshes information retrieved from gui buttons
  private static void refreshButtons()
  {
    pass = GuiMain.getPass();
    oneCard = GuiMain.getOneCard();
  }
  
  //Basic AI
  private static boolean getAIMove (Hand hand, Card top)
  {
    //scan for first move
    for (int i = 0; i < hand.deck.size(); i++)
    {
      //if play is valid
      if ( checkPlay (hand.deck.get(i), top) )
      {
        //check special card
        specialCard(hand.deck.get(i));
        //play card in hand
        disc.deck.add(hand.deal(i));
        
        disc.faceUp(true);
        return true;
      }
    }
    //if no plays are valid, return false
    return false;
  }
  
  //check if AI wants to move more after first move
  private static boolean secondMove (Hand hand, Card top)
  {
    //scan hand
    for (int i = 0; i < hand.deck.size(); i++)
    {
      //if play is valid
      if ( checkPlay (hand.deck.get(i), top) && hand.deck.get(i).getRank() == top.getRank())
      {
        //check special card
        specialCard(hand.deck.get(i));
        //play card in hand
        disc.deck.add(hand.deal(i));
        
        disc.faceUp(true);
        return true;
      }
    }
    //if no plays are valid, return false
    return false;
  }
  
  //wait for user input
  //type: 0 first move of turn
  //      1 draw card then move
  //      2 playing more cards after the first
  private static boolean getPlayerMove(int moveType)
  {
    boolean cont = false;
    
    //loop until valid card is clicked AND it is playable
    do 
    {
      //keep accepting click locations until one is valid
      refreshMouse();
      refreshButtons();
      sleep (100);
      
      //if it is not first move this turn and user wishes to pass, move to next player
      if ((moveType == 2 || moveType == 1) && pass)
        return false;
      
      try
      {
        //check if card selected can be played
        if (checkPlay( hands[0].deck.get ((clickX - 400) / 20), disc.deck.get(disc.deck.size()-1)))
          cont = true;
        //check if card is second played -> then it has to be same rank
        if (moveType == 2 && hands[0].deck.get ((clickX - 400) / 20).getRank() != disc.deck.get(disc.deck.size()-1).getRank())
          cont = false;
      }
      catch (Exception e)
      {
        try
        {
        //check the last card selected - more clickable area
        if (clickX <= 400 + (hands[0].deck.size()-1)*20 + 73 &&
                 clickX >= 400 + (hands[0].deck.size()-1)*20
        )
        {
          //check if last card playable
          if (checkPlay( hands[0].deck.get (hands[0].deck.size()-1), disc.deck.get(disc.deck.size()-1)))
            cont = true;
          //check if card is second played -> then it has to be same rank
          if (moveType == 2 && hands[0].deck.get (hands[0].deck.size()-1).getRank() != disc.deck.get(disc.deck.size()-1).getRank())
            cont = false;
        }
        }
        catch (Exception e2)
        {
          cont = false;
        }
      }
        
      //check if card pile was clicked
      if (clickX >= 520 && 
          clickX <= 520 + 73 &&
          clickY >= 250 &&
          clickY <= 250 + 97)
        return false;
      
    } while (!(clickX >= 400 && //right of position of first card
             clickX <= 400 + (hands[0].deck.size()-1)*20 + 73 && //left of position of last card (73 is card width of the full card that is shown)
             clickY >= 450 && //below top of card
             clickY <= 450 + 97 && //above bottom of card (card height 97)
             cont == true
    ));
    
    //loop exits when valid, so play card and return true
    if (clickX >= 400 && clickX <= 400 + (hands[0].deck.size()-1)*20)
    {
      //check for special card effects
      disc.deck.add(hands[0].deal((clickX - 400) / 20));
      specialCard (disc.deck.get(disc.deck.size()-1));
    }
    else
    {
      //check for special card effects
      disc.deck.add(hands[0].deal(hands[0].deck.size()-1)); //special case for last card
      specialCard (disc.deck.get(disc.deck.size()-1));
    }
    disc.faceUp(true);
    window.repaint();
    return true;
  }
  
  //checks if a player can play another card after playing one again
  private static Boolean checkDouble(Hand hand, Card top)
  {
    //if card in hand has same rank as top card, then another can be played
    for (int i = 0; i < hand.deck.size(); i++)
    {
      if(hand.deck.get(i).getRank() == top.getRank())
        return true;
    }
    return false;
  }
  
  //takes 2 cards, checks if first card can be played on top of the second card
  private static Boolean checkPlay(Card play, Card top)
  {
    if (play.getRank() == 6) //if card is an 8 
      return true;
    else if (play.getRank() == top.getRank()) //same value
      return true;
    else if (play.getSuit() == top.getSuit()) //same rank
      return true;
    else
      return false;
  }
  
  private static void specialCard (Card played)
  {
    //if card is queen, reverse directions
    if (played.getRank() == 10)
    {
      twoStack = 0; //reset 2's counter
      GuiMain.addText("Reverse directions! \n");
      reverse = !reverse;
    }
    //else if card is a four, skip turn
    else if (played.getRank() == 2)
    {
      twoStack = 0; //reset 2's counter
      GuiMain.addText("Skip Turn! \n");
      if (reverse == true)
      {
        turnMod--;
      }
      else
      {
        turnMod++;
      }
    }
    //else if card is a two
    else if (played.getRank() == 0)
    {
      twoStack++;
      if (reverse == true)
      {
        GuiMain.addText("Player "  + (((turnCounter - 1) + 4) % 4) +  " draws " + (2*twoStack) + " cards! \n");
        for (int i = 0; i < 2*twoStack; i++)
        {
          hands[((turnCounter - 1) + 4) % 4].deck.add( pile.deal() );
          //if deck is empty shuffle discard pile back in
          if (pile.deck.size() == 0)
            disc.dealAll(pile);
        }
        
        hands[((turnCounter - 1) + 4) % 4].selectionSort();
        hands[((turnCounter - 1) + 4) % 4].quickSort(0, hands[((turnCounter - 1) + 4 ) % 4].deck.size() -1);
      }
      else //reverse is false 
      {
        GuiMain.addText("Player "  + ((turnCounter + 1) % 4) + " draws " + (2*twoStack) + " cards! \n");
        for (int i = 0; i < 2*twoStack; i++)
        {
          hands[(turnCounter + 1) % 4].deck.add( pile.deal() );
          //if deck is empty shuffle discard pile back in
          if (pile.deck.size() == 0)
            disc.dealAll(pile);
        }
        
        hands[((turnCounter + 1) + 4) % 4].selectionSort();
        hands[((turnCounter + 1) + 4) % 4].quickSort(0, hands[((turnCounter + 1) + 4 ) % 4].deck.size() -1);
      }
    }
      
    //else if card is an eight (AI)
    else if (played.getRank() == 6 && turnCounter >= 1 && turnCounter <= 3)
    {
      twoStack = 0; //reset 2's counter
      disc.deck.get(disc.deck.size() - 1).changeSuit(hands[turnCounter].checkSuit());
      GuiMain.addText("Player " + turnCounter + " changed the suit to " + played.toString() + "! \n");
    }
    
    //else if card is an eight (player)
    else if (played.getRank() == 6 && turnCounter == 0)
    {
      twoStack = 0; //reset 2's counter
      int choice = GuiMain.changeColour();
      disc.deck.get(disc.deck.size() - 1).changeSuit(choice);
      GuiMain.addText("You changed the suit to " + disc.deck.get(disc.deck.size() - 1).toString() + "! \n");
      GuiMain.changeVisibility(false);
      window.repaint();
    }
    else
      twoStack = 0; //reset 2's counter
  }
  
  //returns true if lastCard button is clicked by player first
  //else returns false
  private static boolean lastCard ()
  {
    //AI clicktime randomly generated between 1 and 4 seconds
    int clickTime = (int) (Math.random()*3000 + 1000);
    
    sleep(clickTime);
    
    refreshButtons();
    if (oneCard == true)
    {
      GuiMain.addText("You pressed the Last Card button first! \n");
      return true;
    }
    else
    {
      GuiMain.addText("You didn't press the Last Card button fast enough! \n");
      return false;
    }
  }
  
  public static void drawHands(Graphics g)
  {   
    //draw player hands
    hands[0].show (g,8,9,true); //this draws the cards from the deck on the screen
    hands[1].show (g,4,4,true,true);
    hands[2].show (g,8,1,true);
    hands[3].show (g,14,4,true,false);
    //draw rest of deck as a pile, draw discard pile
    pile.show (g, 520, 250);
    disc.show (g, 400, 250);   
  }
  
  public static void drawBackground (Graphics g)
  {
    //these show the direction of play
    Image arrow = null, arrowReversed = null;
    Color back;
    
    //load arrow images
    try
    {
      //same image, but in two different directions
      arrow = ImageIO.read (new File ("cards/arrow.png"));
      arrowReversed = ImageIO.read (new File ("cards/arrowReversed.png"));
    }
    catch (Exception e)
    {
      System.out.println("Error- background arrow");
    }
    
    //set background color
    for (int i = 0; i < 125; i++)
    {
      back = new Color (255, i*2, 0);
      g.setColor(back);
      g.fillRect(0 + (int) (i*2.5) ,0 + (int) (i* 2.5),1000 - i*5 ,700 - i*5 );
    }
    //draw arrow direction over background depending on which way play is going
    if (reverse)
      g.drawImage(arrowReversed,380,160,null);
    else
      g.drawImage(arrow,380,160,null);
  }
  
  public static void sleep(int ms)
  {
    //sleep for specified amount of milliseconds
    //try catch required to use this method
    try {
      Thread.sleep(ms);
    }
    catch (Exception e) {
      System.out.println ("Sleep failed");
    }
  }
  
  private static boolean checkWin()
  {
    //loop through all hands
    for (int i = 0; i < 4; i++)
    {
      //check if no cards left
      if (hands[i].deck.size() == 0)
        return true;
    }
    //else
    return false;
  }
  //*******************************************************GAME SETUP***********************************************
  private static void setup()
  {
    //wipe all variables
    pile = new Pickup();
    hands = new Hand[4];
    disc = new Discard();
    boolean reverse = false, pass = false, oneCard = false, playable, playerWin, running = true;
    twoStack = 0;
    pile.shuffle(); //shuffle deck of 52 cards
    turnCounter = 0; //player 1 plays first
    GuiMain.text.setText(null);
    
    for (int i=0; i< players; i++)
    {
      //create Hand object in each array position
      hands[i] = new Hand(true);
      //deal 8 cards to each hand
      for (int j = 0; j < 8; j++)
        hands[i].deck.add(pile.deal());
      //sort each hand by suit, then value
      hands[i].selectionSort();
      hands[i].quickSort(0,hands[i].deck.size()-1);
    }
    
    for (int i = players; i < players + computers; i++)
    {
      //create Hand object in each array position
      hands[i] = new Hand(false);
      //deal 8 cards to each hand
      for (int j = 0; j < 8; j++)
        hands[i].deck.add(pile.deal());
      //sort each hand by suit, then value
      hands[i].selectionSort();
      hands[i].quickSort(0,hands[i].deck.size()-1);
    }
    
    disc.deck.add(pile.deal()); //take card from deck and put into discard
    while (disc.deck.get(0).getRank() == 6) //if 8 is flipped
    {
      //shuffle 8 back into deck
      pile.deck.add(disc.deal());
      pile.shuffle();
      //take new card from deck and put into discard
      disc.deck.add(pile.deal()); 
    }
  }
  
  //***********************************************runs the game*****************************************
  private static void run()
  {
    //run until a player has won
    while (checkWin() == false)
    {
      turnCounter = (turnCounter + 4 )% 4;
      
      GuiMain.changeTurn(0);
      
      //player turn
      if (checkWin() == false && turnCounter == 0)
      {
        GuiMain.resetMouse(); //reset mouse so actions cant be buffered
        
        //if this method returns false, player has clicked the stack of cards and wishes to draw
        if (!getPlayerMove(0))
        {
          //draw a card
          hands[0].deck.add( pile.deal() );
          //sort hand again
          hands[0].selectionSort();
          hands[0].quickSort(0,hands[0].deck.size()-1);
          
          //check if deck is empty
          if (pile.deck.size() == 0)
            disc.dealAll(pile);
          
          window.repaint();
          
          //check if player can play a card
          playable = false;
          for (int i = 0; i <= hands[0].deck.size()-1; i++)
          {
            if (checkPlay(hands[0].deck.get(i), disc.deck.get(disc.deck.size()-1)))
              playable = true;
          } 
          if (playable) //let user play more cards, or pass
          {
            GuiMain.resetButtons();
            refreshButtons();
            getPlayerMove(1);
          }
        }
        else //card was played
        {
          GuiMain.resetButtons(); //resets gui button info
          refreshButtons();
          while (checkDouble(hands[0],disc.deck.get(disc.deck.size()-1)) && pass == false) //card was played and another can be played with it (same rank)
          {
            //let user play more cards, or pass
            GuiMain.resetButtons();
            getPlayerMove(2);
          }
        } 
        
        //Check if player has one card
        if (hands[0].deck.size() == 1)
        {
          if (!lastCard()) //trigger last card event, returns false if player clicks button too late
          {
            //player draws two cards
            for (int i = 0; i < 2; i++)
            {
              //draw a card
              hands[0].deck.add( pile.deal() );
              //sort hand again
              hands[0].selectionSort();
              hands[0].quickSort(0,hands[0].deck.size()-1);
          
              //check if deck is empty
              if (pile.deck.size() == 0)
                disc.dealAll(pile);
              }
          }
        }
        
        window.repaint();
        sleep (AISpeed);
        
        //move to next person
        if (reverse == false)
        {
          //accounts for skipped turns
          turnCounter += turnMod;
          turnMod = 0;
          turnCounter++;
        }
        else // move to previous person
        {
          //accounts for skipped turns
          turnCounter += turnMod;
          turnMod = 0;
          turnCounter--;
        }
        
        turnCounter = (turnCounter + 4 )% 4;
      }
      
      //loop through AI turns 1 to 3
      while (turnCounter <= 3 && turnCounter >= 1)
      {
        GuiMain.changeTurn(turnCounter);
        //only run if nobody has won yet
        if (checkWin() == false)
        {
          if(!getAIMove(hands[turnCounter], disc.deck.get(disc.deck.size()-1)))
          {
            //draw a card
            hands[turnCounter].deck.add( pile.deal() );
            //sort hand again
            hands[turnCounter].selectionSort();
            hands[turnCounter].quickSort(0,hands[turnCounter].deck.size()-1);
            
            window.repaint();
            sleep(AISpeed);
            
            //try to move again
            getAIMove(hands[turnCounter], disc.deck.get(disc.deck.size()-1));
            
            //check if deck is empty
            if (pile.deck.size() == 0)
              disc.dealAll(pile);
          }
          else //AI played a card and can make more moves
          {
            window.repaint();
            sleep(AISpeed/2);
            while (secondMove(hands[turnCounter], disc.deck.get(disc.deck.size()-1))) //keep letting ai play cards if valid
            {
              window.repaint();
              sleep(AISpeed/2);
            }
          }
          window.repaint();
          sleep(AISpeed);
        }
        
        GuiMain.resetButtons(); //resets gui button info
        refreshButtons();
        
        //Check if player has one card
        if (hands[turnCounter].deck.size() == 1)
        {
          GuiMain.addText("CPU " + turnCounter + " has one card left! \n");
          if (lastCard()) //trigger last card event, returns true if player clicks before cpu
          {
            //player draws two cards
            for (int i = 0; i < 2; i++)
            {
              //draw a card
              hands[turnCounter].deck.add( pile.deal() );
              //sort hand again
              hands[turnCounter].selectionSort();
              hands[turnCounter].quickSort(0,hands[turnCounter].deck.size()-1);
          
              //check if deck is empty
              if (pile.deck.size() == 0)
                disc.dealAll(pile);
              }
          }
        }
        
        window.repaint();
        
        //move to next person
        if (reverse == false)
        {
          //accounts for skipped turns
          turnCounter += turnMod;
          turnMod = 0;
          turnCounter++;
        }
        else // move to previous person
        {
          //accounts for skipped turns
          turnCounter += turnMod;
          turnMod = 0;
          turnCounter--;
        }
      }
    }
    //check winner
    int win = 0;
    for (int i = 0; i < 4; i++)
    {
      if (hands[i].deck.size() == 0)
        win = i;
    }
    GuiMain.addText("Player " + win + " Wins! \n");
    if (win == 0)
      playerWin = true;
    else
      playerWin = false;
    window.dispose(); //close game window
  }
  
  //opening screen
  private static void menuScreen()
  {
    start = false;
    menuScreen menu = new menuScreen();
    menu.setVisible(true);
    while (start == false) //waits for player click before continuing
    {
      sleep(1000);
    }
  }
  
  //endscreen displaying who has won
  private static void endScreen()
  {
    playAgain = false;
    endScreen end = new endScreen(playerWin);
    end.setVisible(true);
    while (playAgain == false) //waits for player click before continuing
    {
      sleep(1000);
    }
  }
  
  public static void main (String[] args)
  {
    while(running)
    {
      menuScreen(); //opening screen
      setup(); //setup a new game
    
      //create new window that is holding all the gui components
      window = new GuiMain ();
      window.setVisible (true);
    
      run(); //runs game logic
      endScreen(); //display who has won
    }
  }
}
