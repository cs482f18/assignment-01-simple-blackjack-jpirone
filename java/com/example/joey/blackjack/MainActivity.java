package com.example.joey.blackjack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Blackjack blackjack;

    private ImageView playerCard1;
    private ImageView playerCard2;
    private ImageView playerCard3;
    private ImageView playerCard4;
    private ImageView playerCard5;

    private ImageView dealerCard1;
    private ImageView dealerCard2;
    private ImageView dealerCard3;
    private ImageView dealerCard4;
    private ImageView dealerCard5;

    private TextView playerTotal;
    private TextView dealerTotal;
    private TextView status;

    /**
     * On create of the app, the game is started.
     * @param savedInstanceState needed for super() call
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blackjack = new Blackjack();
        blackjack.startGame();

        String[] playerCards = blackjack.getPlayerCards();
        String[] dealerCards = blackjack.getDealerCards();

        playerCard1 = (ImageView) findViewById(R.id.playerCard1);
        playerCard2 = (ImageView) findViewById(R.id.playerCard2);
        dealerCard1 = (ImageView) findViewById(R.id.dealerCard1);
        dealerCard2 = (ImageView) findViewById(R.id.dealerCard2);

        String convertPlayer1 = blackjack.convertCard(playerCards[0]);
        String convertPlayer2 = blackjack.convertCard(playerCards[1]);
        int pid1 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertPlayer1, null, null);
        int pid2 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertPlayer2, null, null);
        playerCard1.setImageResource(pid1);
        playerCard2.setImageResource(pid2);

        String convertDealer2 = blackjack.convertCard(dealerCards[1]);
        int did2 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertDealer2, null, null);
        dealerCard1.setImageResource(R.drawable.red_back);
        dealerCard2.setImageResource(did2);

        fixAce(playerCards[0]);
        fixAce(playerCards[1]);

        playerTotal = (TextView) findViewById(R.id.playerTotal);
        playerTotal.setText(Integer.toString(blackjack.getPlayerTotal()));
    }

    /**
     * On hit method for "hit" button
     * Get the card and then display it to the
     * correct ImageView
     * @param v the view (button) being hit
     */
    public void getCard(View v)
    {
        if(!blackjack.isBust(blackjack.getPlayerTotal()))
        {
            String card = blackjack.getCard(true);
            String convertCard= blackjack.convertCard(card);
            int id = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertCard, null, null);

            playerCard3 = (ImageView) findViewById(R.id.playerCard3);
            playerCard4 = (ImageView) findViewById(R.id.playerCard4);
            playerCard5 = (ImageView) findViewById(R.id.playerCard5);

            if (playerCard3.getDrawable() == null) {
                playerCard3.setImageResource(id);
            } else if (playerCard4.getDrawable() == null) {
                playerCard4.setImageResource(id);
            } else if (playerCard5.getDrawable() == null) {
                playerCard5.setImageResource(id);
            } else {
                //Something went wrong
            }

            fixAce(card);

            playerTotal = (TextView) findViewById(R.id.playerTotal);
            playerTotal.setText(Integer.toString(blackjack.getPlayerTotal()));

            if(blackjack.isBust(blackjack.getPlayerTotal()))
            {
                status = (TextView) findViewById(R.id.status);
                status.setText(blackjack.whoWon(blackjack.getPlayerTotal(), blackjack.getDealerTotal()));
                Button stand = (Button) findViewById(R.id.stand);
                stand.setClickable(false);
                Button hit = (Button) findViewById(R.id.hit);
                hit.setClickable(false);
            }
        }
    }

    /**
     * On click method for "stand" button
     * Allows dealer to play and stops player from
     * hitting :"hit" and "stand"
     * Also displays the winner of the hand
     * @param v the view (button) being clicked
     */
    public void stand(View v)
    {
        playerTotal = (TextView) findViewById(R.id.playerTotal);
        playerTotal.setText(Integer.toString(blackjack.getPlayerTotal()));

        Button stand = (Button) findViewById(R.id.stand);
        stand.setClickable(false);
        Button hit = (Button) findViewById(R.id.hit);
        hit.setClickable(false);
        String[] dealerCards = blackjack.getDealerCards();
        String convertDealer1 = blackjack.convertCard(dealerCards[0]);
        int did1 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertDealer1, null, null);
        dealerCard1 = (ImageView) findViewById(R.id.dealerCard1);
        dealerCard1.setImageResource(did1);

        String[] dealerHand = blackjack.dealerPlay(blackjack.getPlayerTotal());

        String cardOnTie = blackjack.tie(blackjack.getPlayerTotal(), blackjack.getDealerTotal());
        if(!cardOnTie.equals(""))
        {
            for(int i = 0; i < dealerHand.length; i++)
            {
                if(dealerHand == null)
                {
                    dealerHand[i] = cardOnTie;
                    break;
                }
            }
        }

        dealerCard3 = (ImageView) findViewById(R.id.dealerCard3);
        dealerCard4 = (ImageView) findViewById(R.id.dealerCard4);
        dealerCard5 = (ImageView) findViewById(R.id.dealerCard5);


        // Skip first two indexes (already displayed)
        for(int i = 2; i < dealerHand.length; i++)
        {
            if(dealerHand[i] != null) {
                String convertCard = blackjack.convertCard(dealerHand[i]);
                int id = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertCard, null, null);

                if (dealerCard3.getDrawable() == null) {
                    dealerCard3.setImageResource(id);
                } else if (dealerCard4.getDrawable() == null) {
                    dealerCard3.setImageResource(id);
                } else if (dealerCard5.getDrawable() == null) {
                    dealerCard4.setImageResource(id);
                } else {
                    //Something went wrong
                }
            }
        }

        if(dealerCard3.getDrawable() == null && blackjack.isBlackjack(dealerCards[0], dealerCards[1]))
        {
            dealerTotal = (TextView) findViewById(R.id.dealerTotal);
            dealerTotal.setText("21");

            status = (TextView) findViewById(R.id.status);
            status.setText(blackjack.whoWon(blackjack.getPlayerTotal(), 21));
        }
        else
        {
            dealerTotal = (TextView) findViewById(R.id.dealerTotal);
            dealerTotal.setText(Integer.toString(blackjack.getDealerTotal()));

            status = (TextView) findViewById(R.id.status);
            status.setText(blackjack.whoWon(blackjack.getPlayerTotal(), blackjack.getDealerTotal()));
        }
    }

    /**
     * New game button for to allow the player
     * to play again.
     * @param v view (button) being clicked
     */
    public void newGame(View v)
    {
        reset();
        blackjack = new Blackjack();
        blackjack.startGame();

        String[] playerCards = blackjack.getPlayerCards();
        String[] dealerCards = blackjack.getDealerCards();

        playerCard1 = (ImageView) findViewById(R.id.playerCard1);
        playerCard2 = (ImageView) findViewById(R.id.playerCard2);
        dealerCard1 = (ImageView) findViewById(R.id.dealerCard1);
        dealerCard2 = (ImageView) findViewById(R.id.dealerCard2);

        String convertPlayer1 = blackjack.convertCard(playerCards[0]);
        String convertPlayer2 = blackjack.convertCard(playerCards[1]);

        int pid1 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertPlayer1, null, null);
        int pid2 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertPlayer2, null, null);
        playerCard1.setImageResource(pid1);
        playerCard2.setImageResource(pid2);

        String convertDealer2 = blackjack.convertCard(dealerCards[1]);
        int did2 = getResources().getIdentifier("com.example.joey.blackjack:drawable/" + convertDealer2, null, null);
        dealerCard1.setImageResource(R.drawable.red_back);
        dealerCard2.setImageResource(did2);

        fixAce(playerCards[0]);
        fixAce(playerCards[1]);

         playerTotal = (TextView) findViewById(R.id.playerTotal);
        playerTotal.setText(Integer.toString(blackjack.getPlayerTotal()));
    }

    /**
     * Reset the TextViews and ImageViews to be
     * instantiated again
     */
    public void reset()
    {
        playerCard1 = (ImageView) findViewById(R.id.playerCard1);
        playerCard2 = (ImageView) findViewById(R.id.playerCard2);
        playerCard3 = (ImageView) findViewById(R.id.playerCard3);
        playerCard4 = (ImageView) findViewById(R.id.playerCard4);
        playerCard5 = (ImageView) findViewById(R.id.playerCard5);

        dealerCard1 = (ImageView) findViewById(R.id.dealerCard1);
        dealerCard2 = (ImageView) findViewById(R.id.dealerCard2);
        dealerCard3 = (ImageView) findViewById(R.id.dealerCard3);
        dealerCard4 = (ImageView) findViewById(R.id.dealerCard4);
        dealerCard5 = (ImageView) findViewById(R.id.dealerCard5);

        playerTotal = (TextView) findViewById(R.id.playerTotal);
        dealerTotal = (TextView) findViewById(R.id.dealerTotal);
        status = (TextView) findViewById(R.id.status);

        playerCard1.setImageResource(0);
        playerCard2.setImageResource(0);
        playerCard3.setImageResource(0);
        playerCard4.setImageResource(0);
        playerCard5.setImageResource(0);

        dealerCard1.setImageResource(0);
        dealerCard2.setImageResource(0);
        dealerCard3.setImageResource(0);
        dealerCard4.setImageResource(0);
        dealerCard5.setImageResource(0);

        playerTotal.setText("");
        dealerTotal.setText("");
        status.setText("");

        Button stand = (Button) findViewById(R.id.stand);
        stand.setClickable(true);
        Button hit = (Button) findViewById(R.id.hit);
        hit.setClickable(true);


    }

    /**
     * Method to allow the user to chose the value
     * for their ace.
     * @param card the current card
     */
    public void fixAce(String card)
    {
        String[] playerCards = blackjack.getPlayerCards();

        String[] cardSplit = card.split(",");
        String value = cardSplit[0];
        for(String s : playerCards)
        {
            if(s != null)
            {
                String[] split = s.split(",");
                if (split[0].equals("1") && split[0].equals(value))
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Should the ace count for 1 or 11?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "1",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "11",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    blackjack.fixAce();
                                    playerTotal = (TextView) findViewById(R.id.playerTotal);
                                    playerTotal.setText(Integer.toString(blackjack.getPlayerTotal()));
                                    if(blackjack.getPlayerTotal() == 21)
                                    {
                                        Button hit = (Button) findViewById(R.id.hit);
                                        hit.setClickable(false);
                                    }
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

        }
    }
}
