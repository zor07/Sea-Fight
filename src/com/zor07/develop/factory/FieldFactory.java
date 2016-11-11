package com.zor07.develop.factory;

import com.zor07.develop.fieldParts.Cell;
import com.zor07.develop.fieldParts.Field;
import com.zor07.develop.fieldParts.Ship;

import java.util.ArrayList;
import java.util.Random;

/**
 * Класс для осуществления всех расчетов
 */
public class FieldFactory {

    /**
     * Буферная матрица дял расчетов
     */
    private static Cell[][] bufferField;

    /**
     * Ориентация корабля
     */
    private enum Orientation{
        HORIZONTAL,
        VERTICAL
    }


    /**
     * Сеттер для буферной матрицы ячеек
     * @param bufferField
     */
    public static void setBufferField(Cell[][] bufferField) {
        FieldFactory.bufferField = bufferField;
    }

    /**
     * Функция расставляет случайным образом корабли на поле
     * согласно правилам морского боя.
     * 1 четырехпалубный
     * 2 трехпалубных
     * 3 двухпалубных
     * 4 однопалубных
     * @return int[10][10] - поле с расставленными кораблями
     */
    public static void createShips(Field field){
        ArrayList<Ship> ships = new ArrayList<>();
        Random random = new Random();

        for (int shipSize = 4; shipSize >= 1 ; --shipSize) {
            int shipsCount = 5 - shipSize;
            for (int i = 0; i < shipsCount; ++i) {
                Orientation orientation;
                int x, y;
                do {
                    orientation = getNextOrientation();
                    if (orientation == Orientation.HORIZONTAL){
                        x = random.nextInt(10 - shipSize + 1);
                        y = random.nextInt(10);
                    } else {
                        x = random.nextInt(10);
                        y = random.nextInt(10 - shipSize + 1);
                    }

                } while (!validPlaceForShip(x, y, shipSize, orientation));

                ships.add(createShip(x, y, shipSize, orientation));
            }

        }

        field.setCells(bufferField);
        bufferField = null;
    }


    /**
     * Инициализирует буферную матрицу
     */
    public static void initializeBuffer(){
        bufferField = new Cell[Field.maxXY + 1][Field.maxXY + 1];
        for (int i = Field.minXY; i <= Field.maxXY; i++) {
            for (int j = Field.minXY; j <= Field.maxXY; j++) {
                bufferField[j][i] = new Cell(j, i, Cell.CellState.NULL, null);
            }
        }
    }


    /**
     * Функция проверяет возможно ли установить корабль
     * со следующими параметрами:
     *
     * @param size - размер корабля
     * @param orientation - ориентация
     * @param  x - координата по оси Х
     * @param y - координата по оси Y
     *
     * @return true, если возможно, иначе false
     */

    private static boolean validPlaceForShip(int x, int y, int size, Orientation orientation){
        int xFrom = x - 1,
            yFrom = y - 1,
            xTo, yTo;

        if (orientation == Orientation.VERTICAL){
            xTo = x + size;
            yTo = y + 1;
            if (xTo - 1 > Field.maxXY) return false;

        } else {
            xTo = x + 1;
            yTo = y + size;
            if (yTo - 1 > Field.maxXY) return false;
        }

        if (xFrom < Field.minXY) xFrom = Field.minXY;
        if (yFrom < Field.minXY) yFrom = Field.minXY;
        if (yTo > Field.maxXY) yTo = Field.maxXY;
        if (xTo > Field.maxXY) xTo = Field.maxXY;

        for (int i = xFrom; i <= xTo; i++) {
            for (int j = yFrom; j <= yTo; j++) {
                if (bufferField[j][i].isAlive()) return false;
            }
        }
        return true;
    }



    /**
     * Функция создает корабль
     * @param x - координата корабля по оси Х
     * @param y - координата корабля по оси Y
     * @param size - размер корабля
     * @param orientation - ориентация
     * @return корабль
     */
    private static Ship createShip(int x, int y, int size, Orientation orientation){
        int xTo, yTo;

        if (orientation == Orientation.VERTICAL){
            xTo = x + size - 1;
            yTo = y;
        } else {
            xTo = x;
            yTo = y + size - 1;
        }

        /*
         * Создаем список ячеек формирующийх корабль
         * Создаем корабль
         * В каждую ячейку протягиваем ссылку на корабль, которому она принадлежит
         * После ссылку на список ячеек протягиваем в корабль
         */
        ArrayList<Cell> cells = new ArrayList<>();
        ArrayList<Cell> borders = new ArrayList<>();
        Ship ship = new Ship();
        for (int i = x; i <= xTo; i++) {
            for (int j = y; j <= yTo; j++) {
                bufferField[j][i].inititalizeState(Cell.CellState.ALIVE);
                bufferField[j][i].setShip(ship);
                cells.add(bufferField[j][i]);
            }
        }

        for (int k = x - 1; k <= xTo + 1; k++) {
            for (int l = y - 1; l <= yTo + 1; l++) {
                if (k >= Field.minXY && k <= Field.maxXY && l >= Field.minXY && l <= Field.maxXY)
                    if (bufferField[l][k].getState() != Cell.CellState.ALIVE)
                        if (!borders.contains(bufferField[l][k]))
                            borders.add(bufferField[l][k]);
            }
        }

        ship.setCells(cells);
        ship.setBorders(borders);
        return ship;
    }


    /**
     * Случайным образом генерирует ориентацию корабля
     * @return случайную ориантацию
     */
    private static Orientation getNextOrientation(){

        Random rnd = new Random();
        if (rnd.nextBoolean())
            return Orientation.HORIZONTAL;
        else
            return Orientation.VERTICAL;
    }

}
