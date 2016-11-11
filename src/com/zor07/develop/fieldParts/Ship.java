package com.zor07.develop.fieldParts;

import java.util.ArrayList;

/**
 * Класс описывающий корабль на игровом поле
 */
public class Ship {

    /**
     * Ячейки тела корабля
     */
    private ArrayList<Cell> cells;
    /**
     * Ячейки границ корабля
     * используются, для того, чтоб отмечать границы убитого корабля
     */
    private ArrayList<Cell> borders;


    /**
     * Геттер для ячеек корабля
     * @return ячейки корабля
     */
    public ArrayList<Cell> getCells() {
        return cells;
    }


    /**
     * Функция переводит:
     * все ячейки корабля составляющие его тело в состояние KILLED
     * все ячейки корабля составляющие его границы в состояние MISSED
     */
    public void die(){
        for (Cell cell : cells) {
            cell.setState(Cell.CellState.KILLED);
        }

        for (Cell cell : borders) {
            cell.setState(Cell.CellState.MISSED);
        }
    }

    /**
     * Сеттер для ячеек корабля, составляющих его тело
     * @param cells ячейки
     */
    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    /**
     * Сеттер для ячеек корабля, составляющих его границы
     * @param borders ячейки
     */
    public void setBorders(ArrayList<Cell> borders) {
        this.borders = borders;
    }
}
