package com.zor07.develop;

import com.zor07.develop.gui.Canvas;
import com.zor07.develop.players.ComputerPlayer;
import com.zor07.develop.players.HumanPlayer;
import com.zor07.develop.gui.MessageManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by anzor on 15.10.2016.
 */
public class SeaFight implements ActionListener, Runnable{

    Thread lastGame;
    boolean reset = false;
    public static ComputerPlayer computerPlayer = new ComputerPlayer();
    public static HumanPlayer humanPlayer = new HumanPlayer();
    private static SeaFight game = getInstance();

    private SeaFight(){
        humanPlayer.setOpponent(computerPlayer);
        computerPlayer.setOpponent(humanPlayer);
        humanPlayer.setActionListnerToCells();
    }
    public static void main(String[] args) {
        game.playGame();


    }

    void playGame(){
        lastGame = new Thread(this);
        lastGame.start();
    }

    public static SeaFight getInstance(){
        if (game == null) game = new SeaFight();
        return game;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        reset = true;
        if (!lastGame.isAlive()){
            lastGame = new Thread(this);
            lastGame.start();
        }
    }

    @Override
    public void run() {
        Canvas.getInstance().draw();
        MessageManager.getInstance().getStartMessage();
        boolean shootComanded = false;
        while(true){
            if (reset){
                reset = false;
                computerPlayer.reset();
                humanPlayer.reset();
                Canvas.getInstance().draw();
                MessageManager.getInstance().getStartMessage();
            }
            if (computerPlayer.moves() && !shootComanded) {
                computerPlayer.shoot();
                shootComanded = true;
            } else {
                shootComanded = false;
            }
            if (computerPlayer.getShipsToKill() == 0 || humanPlayer.getShipsToKill() == 0) {
                MessageManager.getInstance().getWinMessage(humanPlayer.getShipsToKill() == 0);
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
