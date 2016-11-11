package com.zor07.develop.players;

import com.zor07.develop.fieldParts.Cell;
import com.zor07.develop.fieldParts.Field;
import com.zor07.develop.gui.MessageManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Класс игрока - человека
 */
public class HumanPlayer extends Player implements ActionListener {

    /**
     * Конструктор класса. Первый ход присваивается человеку
     */
    public HumanPlayer() {
        super();
        myTurn = true;
    }


    /**
     * Игрок стреляет по ячейкам соперника, кликая по ним.
     * Если сейчас очередь человека (myTurn == true)
     * его клик обрабатывается, вычисляются координаты ячейки (x, y) по которой человек кликнул
     * и вызывается функция shoot(x, y)
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (myTurn) {
            Cell cell = (Cell) e.getSource();
            shoot(cell.getXCo(), cell.getYCo());
        }
    }

    /**
     * Задаем ActionListner на все ячейки вражеского поля
     */
    public void setActionListnerToCells(){
        Cell[][] cells = opponent.getField().getCells();
        for (int i = Field.minXY; i <= Field.maxXY; i++) {
            for (int j = Field.minXY; j <= Field.maxXY; j++) {
                cells[j][i].addActionListener(this);
            }
        }
    }

    /**
     * После выстрела, если корабль врага был потоплен декриментируем shipsToKill
     * Извлекаем сообщение из MessageManager
     * @param x координата Х ячейки на игровом поле, в которую производился последний выстрел
     * @param y координата Y ячейки на игровом поле, в которую производился последний выстрел
     */
    @Override
    void afterShootingHandling(int x, int y) {
        if (shootResult == ShootResult.KILLED) shipsToKill--;
        MessageManager.getInstance().getMessage(true, shootResult);
    }

    /**
     * для новой игры
     * очередь присваиваем себе
     * Корабли для уничтожения приравниваем 10
     * заново генерим игровое поле
     */
    @Override
    public void reset() {
        super.reset();
        myTurn = true;
        shipsToKill = 10;
    }

    /**
     * Функция вызывается при произведении выстрела человеком по ячейке соперника
     * @param x координата Х ячейки на игровом поле соперника, в которую производится выстрел
     * @param y координата Y ячейки на игровом поле соперника, в которую производится выстрел
     */
    private void shoot(int x, int y) {
        shootResult = opponent.getShot(x, y);
        if (shootResult == ShootResult.MISSED){
            changeTurn();
        }
        afterShootingHandling(0, 0);
    }
}
