package com.zor07.develop.fieldParts;

import com.zor07.develop.gui.Canvas;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Класс описывающий ячейку игрового поля
 */
public class Cell extends JButton{
    /**
     * Координаты ячейки в игровом поле
     */
    private int xCo, yCo;

    /**
     * Ссылка на корабль, которому принадлежит текущая ячейка
     */
    private Ship ship;

    /**
     * Состояние ячейки
     */
    private CellState state;

    /**
     * Конструктор класса
     * @param x координата Х в игровом поле
     * @param y координата Y в игровом поле
     * @param cellState состояние ячейки
     * @param ship ссылка на корабль, которому принадлежит данная ячейка
     */
    public Cell(int x, int y, CellState cellState, Ship ship) {
        super();
        this.xCo = x;
        this.yCo = y;
        this.state = cellState;
        this.ship = ship;
    }

    /**
     * Возможные состояния ячейки
     * NULL - пустая ячейка
     * ALIVE - Живая ячейка
     * INJURED - Подбитая ячейка еще живого корабля
     * KILLED - ячейка подбитого корабля
     * MISSEDCELL - ячейка в которую был произведен выстрел,
     *              но на которой не оказалось корабля
     */
    public enum CellState {
        NULL,
        MISSED,
        ALIVE,
        INJURED,
        KILLED
    }

    /**
     * Функция проверяет жива ли текущая ячейка
     * @return true если жива, иначе false
     */
    public boolean isAlive(){
        return state == CellState.ALIVE;
    }

    /**
     * Геттер для состояния ячейки
     * @return текущее состояние ячейки
     */
    public CellState getState() {
        return state;
    }

    /**
     * Сеттер для состояния ячейки.
     * После смены состояния ячейки происходит отрисовка всей игры
     * @param state состояние, в которое нужно перевести ячейку
     */
    public void setState(CellState state){
        this.state = state;
        Canvas.getInstance().draw();
    }

    /**
     * Сеттер для корабля
     * @param ship ссылка на корабль, которому принадлежит ячейка
     */
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    /**
     * Геттер координаты Х на игровом поле
     * @return координата Х на игровом поле
     */
    public int getXCo() {
        return xCo;
    }

    /**
     * Геттер координаты Y на игровом поле
     * @return координата Y на игровом поле
     */
    public int getYCo() {
        return yCo;
    }
    /**
     * Функция вызывается когда стреляют в текущую ячейку
     */
    public void getShot(){
        switch (state){
            case NULL:
                setState(CellState.MISSED);
                break;
            case ALIVE:
                if (leftAliveNeighboors()) {
                    setState(CellState.INJURED);
                } else {
                    ship.die();
                }
                break;
        }
    }

    /**
     * Инициализатор состояния ячейки.
     * Отдельно от сеттера, т.к. при изначальном назначении состояния
     * требуется не отрисовывать каждый раз всю игру
     * @param state состояние, в которое нужно перевести ячейку
     */
    public void inititalizeState(CellState state){
        this.state = state;
    }

    @Override
    public int hashCode() {
        return xCo * 37 + yCo * 37 + 37;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Cell)) return false;
        Cell cell = (Cell) obj;

        return (cell.xCo == xCo && cell.yCo == yCo);
    }

    /**
     * Функция проверяет остались ли живые соседи у данной ячейки.
     * Вызывается если ячейка является частью корабля и в неё попали
     * @return True, если корабль, которому принадлежит ячейка все еще имеет живые ячейки, иначе False
     */

    private boolean leftAliveNeighboors(){
        ArrayList<Cell> cells = ship.getCells();
        for (Cell cell : cells){
            if (!cell.equals(this) && cell.isAlive()) return true;
        }
        return false;
    }

}
