package com.zor07.develop.fieldParts;

import com.zor07.develop.factory.FieldFactory;

/**
 * Класс, моделирующий игровое поле
 */
public class Field {
    /**
     * Максимальное и минимальное значения координаты
     */
    public static final int maxXY = 9, minXY = 0;

    /**
     * Ячейки составляющие данное поле
     */
    private Cell[][] cells = new Cell[maxXY + 1][maxXY + 1];

    /**
     * Конструктор класса
     */
    public Field() {
        for (int i = minXY; i <= maxXY; i++) {
            for (int j = minXY; j <= maxXY; j++) {
                cells[j][i] = new Cell(j, i, Cell.CellState.NULL, null);
            }
        }

        FieldFactory.setBufferField(cells);
        FieldFactory.createShips(this);
    }

    /**
     * Геттер для ячеек поля
     * @return ячейки поля
     */
    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Сеттер для ячеек поля
     * @param cells ячейки поля
     */
    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    /**
     * Функция сбрасывает все ячейки поля в состояние NULL
     */
    public void resetCells(){
        for (int i = minXY; i <= maxXY; i++) {
            for (int j = minXY; j <= maxXY; j++) {
                cells[j][i].setState(Cell.CellState.NULL);
            }
        }

        FieldFactory.setBufferField(cells);
        FieldFactory.createShips(this);
    }
}
