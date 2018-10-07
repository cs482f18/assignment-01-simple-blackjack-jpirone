package com.example.joey.blackjack;

import android.util.Log;

import java.util.Random;
import java.util.ArrayList;

/**
 * Basic Blackjack class to act as a model for the view
 * Allows the model
 *
 */
public class Blackjack
{
    private ArrayList<String> pickedCards;
    private String[] playerCards;
    private String[] dealerCards;

    /**
     * Constructor that instantiates
     *   the picked cards
     *   the players cards
     *   the dealers cards
     */
    public Blackjack()
    {
        pickedCards = new ArrayList<String>();
        playerCards = new String[5];
        dealerCards = new String[5];
    }

    /**
     * Start method to get the game running
     *   gives two cards to the player
     *   gives two cards to the dealer
     *   adds the cards to the respective hands
     */
    public void startGame()
    {
        String playerFirstCard = getCard(true);
        String playerSecondCard = getCard(true);
        playerCards[0] = playerFirstCard;
        playerCards[1] = playerSecondCard;

        String dealerFirstCard = getCard(false);
        String dealerSecondCard = getCard(false);
        dealerCards[0] = dealerFirstCard;
        dealerCards[1] = dealerSecondCard;

    }

    /**
     * Get a random card from the value and return as
     *   number,suit
     * @param player whether the card draw is for the player or dealer
     * @return the string in expected card format
     */
    public String getCard(boolean player)
    {
        String currentCard = "";
        boolean newCard = false;

        while(newCard == false)
        {
            /*
             * 11 - Jack
             * 12 - Queen
             * 13 - King
             */
            int cardNumber;
            String cardSuit;

            Random rand = new Random();

            cardNumber = rand.nextInt(13) + 1;
            int randomCardSuit = rand.nextInt(4) + 1;

            if (randomCardSuit == 1)
                cardSuit = "hea";
            else if (randomCardSuit == 2)
                cardSuit = "dia";
            else if (randomCardSuit == 3)
                cardSuit = "club";
            else if (randomCardSuit == 4)
                cardSuit = "spa";
            else
                cardSuit = "None";

            StringBuilder sb = new StringBuilder();
            // Separated by a comma!

            sb.append(Integer.toString(cardNumber) + ",");
            sb.append(cardSuit);
            currentCard = sb.toString();

            // CHECK ME!
            if(pickedCards.isEmpty()) {
                newCard = true;
            }
            for (String card : pickedCards)
            {
                if (!card.equals(currentCard))
                {
                    newCard = true;
                }
            }
        }

        if(player == true)
        {
            for(int i = 0; i < playerCards.length; i++)
            {
                try
                {
                    if(playerCards[i] == null)
                    {
                        playerCards[i] = currentCard;
                        pickedCards.add(currentCard);
                        return currentCard;
                    }
                }
                catch(NullPointerException npe)
                {
                    Log.w("MainActivity", "Null Pointer Exception line");
                }
            }
        }
        else
        {
            for(int i = 0; i < dealerCards.length; i++)
            {
                try
                {
                    if(dealerCards[i] == null)
                    {
                        dealerCards[i] = currentCard;
                        pickedCards.add(currentCard);
                        return currentCard;
                    }
                }
                catch(NullPointerException npe)
                {
                    Log.w("MainActivity", "Null Pointer Exception line");
                }
            }
        }
        pickedCards.add(currentCard);
        return currentCard;
    }

    /**
     * The ace can either be a 1 or an 11
     * To account for this, we ask the user
     * which value they want and then handle it here
     *   if they want 11, change value to 14
     * @return the dealers hand with new ace value
     */
    public String[] fixAce()
    {
        int index = 0;
        for(String s : playerCards)
        {
            if(s != null)
            {
                String[] split = s.split(",");
                if(split[0].equals("1"))
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("14,"+split[1]);
                    playerCards[index] = sb.toString();
                    Log.w("hi", "StringBuilder: "+ sb.toString());
                    for(String str : playerCards)
                        Log.w("hi", "Cards: "+ str);
                    return playerCards;
                }
                index++;
            }
        }
        return playerCards;
    }

    /**
     * Accessor for the player cards
     * @return player cards array
     */
    public String[] getPlayerCards()
    {
        return playerCards;
    }

    /**
     * Accessor for the dealer cards
     * @return dealer cards array
     */
    public String[] getDealerCards()
    {
        return dealerCards;
    }

    /**
     * Find the total value of the players
     * hand based off of their cards
     * @return total value
     */
    public int getPlayerTotal()
    {
        /*
         * 11 - Jack
         * 12 - Queen
         * 13 - King
         */
        int total = 0;
        for(String card : playerCards)
        {
            try {
                //Some unknown values in the array after instantiating.
                if(card.contains(","))
                {
                    String[] split = card.split(",");
                    int value = Integer.parseInt(split[0]);
                    if (isFaceCard(split[0]))
                        value = 10;
                    if(value == 14)
                        value = 11;
                    total += value;
                }
            }
            catch(NullPointerException npe)
            {
                Log.w("MainActivity", "Null Pointer Exception line");
            }
        }
        return total;
    }

    /**
     * Get the total value of the dealer
     * hand based off of their cards
     * @return
     */
    public int getDealerTotal()
    {
        /*
         * 11 - Jack
         * 12 - Queen
         * 13 - King
         */
        int total = 0;
        for(String card : dealerCards)
        {
            try {
                //Some unknown values in the array after instantiating.
                if(card.contains(","))
                {
                    String[] split = card.split(",");
                    int value = Integer.parseInt(split[0]);
                    if (isFaceCard(split[0]))
                        value = 10;

                    total += value;
                }
            }
            catch(NullPointerException npe)
            {
                Log.w("MainActivity", "Null Pointer Exception line");
            }
        }
        return total;
    }

    /**
     * Find if the card is a face card or not
     *   11 is jack
     *   12 is queen
     *   13 is king
     * @param cardNumber the card
     * @return true if its a face card, otherwise false
     */
    public boolean isFaceCard(String cardNumber)
    {
        if(cardNumber.equals("11"))
            return true;
        else if(cardNumber.equals("12"))
            return true;
        else if(cardNumber.equals("13"))
            return true;
        else
            return false;
    }

    /**
     * Find if the dealer or player has blackjack
     *   (ace and <= 10)
     * @param card1 the first card of the hand
     * @param card2 the second card of the hand
     * @return true if blackjack, otherwise false
     */
    public boolean isBlackjack(String card1, String card2)
    {
        String[] cardArray1 = card1.split(",");
        String[] cardArray2 = card2.split(",");
        String cardValue1 = cardArray1[0];
        String cardValue2 = cardArray2[0];

        if(isFaceCard(cardValue1) || isFaceCard(cardValue2))
        {
            if(cardValue1.equals("1") || cardValue2.equals("1"))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Find if the player or dealer busted
     *      (went over the value of 21)
     * @param value the total card summation
     * @return true if bust, otherwise false
     */
    public boolean isBust(int value)
    {
        if(value >= 22)
            return true;
        return false;
    }

    /**
     * Give a message to show who the winner is of the hand
     *   Looks at all different situations possible
     * @param playerTotal players total card summation
     * @param dealerTotal dealers total card summation
     * @return message with winner information
     */
    public String whoWon(int playerTotal, int dealerTotal)
    {

        // both players get blackjack
        if(isBlackjack(playerCards[0], playerCards[1]) && isBlackjack(dealerCards[0], dealerCards[1]))
        {
            return "Draw: No Winner";
        }
        // one player gets blackjack
        else if(isBlackjack(playerCards[0], playerCards[1]))
        {
            return "The Player Wins!";
        }
        else if(isBlackjack(dealerCards[0], dealerCards[1]))
        {
            return "The Dealer Wins";
        }
        // a player busts
        else if(isBust(playerTotal))
        {
            return "The Dealer Wins";
        }
        else if(isBust(dealerTotal))
        {
            return "The Player Wins";
        }
        // a player has a higher total value
        else if(playerTotal > dealerTotal)
        {
            return "The Player Wins";
        }
        else if(dealerTotal > playerTotal)
        {
            return "The Dealer Wins";
        }
        else
        {
            return "Missing a case";
        }
    }

    /**
     * Code to all the dealer (CPU) to play
     *
     * @param playerTotal the total summation of the players cards
     * @return the dealers cards
     */
    public String[] dealerPlay(int playerTotal)
    {
        String[] splitCard1 = dealerCards[0].split(",");
        int card1Value = Integer.parseInt(splitCard1[0]);

        String[] splitCard2 = dealerCards[1].split(",");
        int card2Value = Integer.parseInt(splitCard2[0]);

        // Dealer Cards are 21
        if(card1Value == 1 && (card2Value == 10 || card2Value == 11 || card2Value == 12 || card2Value == 13))
            return dealerCards;
        else if(card2Value == 1 && (card1Value == 10 || card1Value == 11 || card1Value == 12 || card1Value == 13))
            return dealerCards;

        int dealerTotal = card1Value + card2Value;

        int index = 2;
        while(dealerTotal <= playerTotal)
        {
            try
            {
                String card = getCard(false);
                dealerCards[index] = card;
                String[] split = card.split(",");
                int value = Integer.parseInt(split[0]);
                if (isFaceCard(split[0]))
                    value = 10;
                dealerTotal += value;
                index++;
            }
            catch(ArrayIndexOutOfBoundsException aiobe)
            {
                Log.w("MainActivity", "ArrayIndexOutOfBounds Exception");
                break;
            }
        }

        return dealerCards;
    }

    /**
     * Double checking to ensure there isn't a tie
     * between the dealer and the player
     * @param playerTotal the players summation
     * @param dealerTotal the dealers summation
     * @return
     */
    public String tie(int playerTotal, int dealerTotal)
    {
        String card = "";
        if(playerTotal == dealerTotal && playerTotal != 21)
        {
            card = getCard(false);
        }
        return card;
    }

    /**
     * Convert the card string to the the string the
     * images are expecting
     *   We need to keep the getCard() with its return
     *   type so we can have the proper integer values
     * @param card The card being converted
     * @return string in image.png form
     */
    public String convertCard(String card)
    {
        String[] split = card.split(",");
        String cardNumber = split[0];
        if(cardNumber.equals("1"))
            cardNumber = "ace";
        else if(cardNumber.equals("2"))
            cardNumber = "two";
        else if(cardNumber.equals("3"))
            cardNumber = "three";
        else if(cardNumber.equals("4"))
            cardNumber ="four";
        else if(cardNumber.equals("5"))
            cardNumber = "five";
        else if(cardNumber.equals("6"))
            cardNumber = "six";
        else if(cardNumber.equals("7"))
            cardNumber = "seven";
        else if(cardNumber.equals("8"))
            cardNumber = "eight";
        else if(cardNumber.equals("9"))
            cardNumber = "nine";
        else if(cardNumber.equals("10"))
            cardNumber = "ten";
        else if(cardNumber.equals("11"))
            cardNumber = "jack";
        else if(cardNumber.equals("12"))
            cardNumber = "queen";
        else if(cardNumber.equals("13"))
            cardNumber = "king";

        String cardSuit = split[1];

        StringBuilder sb = new StringBuilder();
        sb.append(cardSuit);
        sb.append("_");
        sb.append(cardNumber);
        return sb.toString();
    }
}
