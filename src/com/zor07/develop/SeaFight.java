package com.zor07.develop;

import com.zor07.develop.gui.Canvas;
import com.zor07.develop.players.ComputerPlayer;
import com.zor07.develop.players.HumanPlayer;
import com.zor07.develop.gui.MessageManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Главный класс игры
 */
public class SeaFight implements ActionListener, Runnable{
    /**
     * Ссылка на поток игры
     */
    private Thread lastGame;
    /**
     * Флаг для начала новой игры потоком
     */
    private boolean reset = false;

    /**
     * Компьютерный игрок
     */
    public static ComputerPlayer computerPlayer = new ComputerPlayer();

    /**
     * Игрок - человек
     */
    public static HumanPlayer humanPlayer = new HumanPlayer();

    /**
     * Инстанс игры. Класс - синглтон
     */
    private static SeaFight game = getInstance();


    /**
     * Закрытый конструктор класса.
     * Каждому игроку назначается оппонент.
     */
    private SeaFight(){
        humanPlayer.setOpponent(computerPlayer);
        computerPlayer.setOpponent(humanPlayer);
    }

    /**
     * Метод main
     * Начинаем игру
     */
    public static void main(String[] args) {
        game.playGame();
    }

    /**
     * Создаем новый поток, передаем в него текущий класс
     * Запускаем
     */
    void playGame(){
        lastGame = new Thread(this);
        lastGame.start();
    }

    /**
     * Геттер для инстанса игры
     * @return инстанс игры
     */
    public static SeaFight getInstance(){
        if (game == null) game = new SeaFight();
        return game;
    }

    /**
     * Здесь обрабатывается только нажатие кнопки "Новая игра"
     * Выставляем флаг reset = true;
     * Если поток последней игры был завершен (кто-то победил), создаем и запускаем новый поток
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        reset = true;
        if (!lastGame.isAlive()){
            lastGame = new Thread(this);
            lastGame.start();
        }
    }

    /**
     * Метод run
     * первым делом отрисовываем все элементы
     * После в цикле каждые 10 миллисекунд:
     *     Заставляем копьютер стрелять, если сейчас его очередь ходить
     *     Проверяем не завершилась ли игра.
     *     Проверяем флаг на рестарт. Если его выставили рестартим
     *
     */
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
