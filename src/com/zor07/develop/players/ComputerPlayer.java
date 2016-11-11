package com.zor07.develop.players;

import com.zor07.develop.fieldParts.Cell;
import com.zor07.develop.fieldParts.Field;
import com.zor07.develop.gui.MessageManager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Класс компьютерного игрока
 */
public class ComputerPlayer extends Player {
    /**
     * Черновое поле компьютера для пометок
     */
    private Cell[][] enemyCells;

    /**
     * Список потенциальных целей слева, от раненой ячейки
     */
    private ArrayList<Integer> leftAims = new ArrayList();
    /**
     * Список потенциальных целей справа, от раненой ячейки
     */
    private ArrayList<Integer> rightAims = new ArrayList();
    /**
     * Список потенциальных целей снизу раненой ячейки
     */
    private ArrayList<Integer> downAims = new ArrayList();
    /**
     * Список потенциальных целей сверху раненой ячейки
     */
    private ArrayList<Integer> upAims = new ArrayList();

    /**
     * Список потенциальных целей.
     * Присваивается ссылка на один из следующих списков:
     * leftAims
     * rightAims
     * downAims
     * upAims
     */
    private ArrayList<Integer> currentAims;

    /**
     * Переменная определяющая нужно ли стрелять по потенциальным целям,
     * т.е. добивать раненный корабль или нет
     */
    private boolean shootToPotentialAims;
    /**
     * Переменная определяющая вычислены ли потенциальные цели
     */
    private boolean aimsCalculated;

    /**
     * Координата ячейки X на игровом поле, в которую производился последний случайный выстрел
     */
    private int lastX;

    /**
     * Координата ячейки Y на игровом поле, в которую производился последний случайный выстрел
     */
    private int lastY;

    /**
     * Конструктор класса
     * Очередь не наша
     * Инициализируем черновое поле
     */
    public ComputerPlayer() {
        super();
        myTurn = false;
        setEnemiField();
    }

    /**
     * Производит выстрел
     * Пока очередь компьютера он
     * вычисляет координаты и стреляет по ячейке
     * Обновляет данные после выстрела
     */
    public void shoot(){
        while (myTurn) {
            try {Thread.sleep(500);} catch (InterruptedException e) {}
            if (shipsToKill == 0) return;
            int x, y;
            do {
                int[] coordinates = getCoordinates();
                x = coordinates[0];
                y = coordinates[1];
                shootResult = opponent.getShot(x, y);
            } while (shootResult == null);
            afterShootingHandling(x, y);
        }
    }

    /**
     * Функция для вычисления координат выстрела
     * Если shootToPotentialAims == true вычисляются координаты для выстрела по потенциальной цели
     * Иначе вычисляются случайные координаты
     * @return int[2], где первый эллемент - координата X, а второй - координата Y
     */
    private int[] getCoordinates(){
        int[] coordinates;
        if (shootToPotentialAims)
            coordinates = getNextCoordinates();
        else
            coordinates = getRandomCoordinates();

        return coordinates;
    }


    /**
     * Вычисляет координаты для произведения выстрела в случайное место на игровом поле
     * @return  int[2], где первый эллемент - координата X, а второй - координата Y
     */
    private int[] getRandomCoordinates(){
        int[] coordinate = new int[2];
        Random random = new Random();
        int x, y;
        do{
            x = random.nextInt(10);
            y = random.nextInt(10);
        }while (enemyCells[x][y].getState() != Cell.CellState.NULL);

        coordinate[0] = x;
        coordinate[1] = y;

        lastX = x;
        lastY = y;

        return coordinate;
    }

    /**
     * Вычисляет координаты для произведения выстрела по одной из потенциальных целей
     * При первом вызове вычисляет все возможные потенциальные цели, далее по очереди стреляет по целям
     * слева, справа, снизу, сверху. За 1 вызов функции производится 1 выстрел.
     * @return  int[2], где первый эллемент - координата X, а второй - координата Y
     */
    private int[] getNextCoordinates() {
        if (!aimsCalculated) calculateAims();
        int[] res = new int[2];
        if(!leftAims.isEmpty()) {
            currentAims = leftAims;
            res[0] = leftAims.get(0);
            res[1] = lastY;
            leftAims.remove(0);
        } else if(!rightAims.isEmpty()) {
            currentAims = rightAims;
            res[0] = rightAims.get(0);
            res[1] = lastY;
            rightAims.remove(0);
        } else if(!downAims.isEmpty()) {
            currentAims = downAims;
            res[0] = lastX;
            res[1] = downAims.get(0);
            downAims.remove(0);
        } else if(!upAims.isEmpty()) {
            currentAims = upAims;
            res[0] = lastX;
            res[1] = upAims.get(0);
            upAims.remove(0);
        }
        return res;
    }

    /**
     * Функция вычисляет все потенциальные цели.
     * Вычисляются по три ячейки слева, справа, снизу, сверху от ячейки с координатами (lastX, lastY)
     * и заносятся в соответствующие списки (leftAims, rightAims, upAims, downAims)
     */
    private void calculateAims() {
        clearAims();
        int x = lastX, y = lastY;
        aimsCalculated = true;
        for(int i = x + 1; i < x + 4; ++i) {
            if(i < 10) {
                if (enemyCells[i][y].getState() == Cell.CellState.MISSED) break;
                if (enemyCells[i][y].getState() == Cell.CellState.NULL) rightAims.add(i);
            } else break;

        }
        for(int i = x - 1; i > x - 4; --i) {
            if(i >= 0) {
                if (enemyCells[i][y].getState() == Cell.CellState.MISSED) break;
                if (enemyCells[i][y].getState() == Cell.CellState.NULL) leftAims.add(i);
            } else break;
        }
        for(int i = y - 1; i > y - 4; --i) {
            if(i >= 0) {
                if (enemyCells[x][i].getState() == Cell.CellState.MISSED) break;
                if (enemyCells[x][i].getState() == Cell.CellState.NULL) upAims.add(i);
            } else break;
        }
        for(int i = y + 1; i < y + 4; ++i) {
            if(i < 10) {
                if (enemyCells[x][i].getState() == Cell.CellState.MISSED) break;
                if (enemyCells[x][i].getState() == Cell.CellState.NULL) downAims.add(i);
            } else break;
        }
    }

    /**
     * Очищает все списки потенциальных целей
     */
    private void clearAims(){
        leftAims.clear();
        rightAims.clear();
        downAims.clear();
        upAims.clear();
    }


    /**
     * Обновляет данные после выстрела.
     * Если последним выстрелом компьютер:
     * Промазал:Если стрелял по потенциальным целям, то текущий список потенциальных целей очищается
     *          Помечается ячейка на черновом поле как "промазанная"
     *          Сменятеся очередь хода
     *
     * Ранил:   shootToPotentialAims переводится в true
     *          Помечается ячейка на черновом поле как "раненая"
     *
     * Убил:    Вызывается функция, чтоб пометить в черновике убитый корабль
     *          Декриментируется кол-во для потопления
     *          shootToPotentialAims и aimsCalculated переводятся в false
     * @param x координата Х ячейки на игровом поле, в которую производился последний выстрел
     * @param y координата Y ячейки на игровом поле, в которую производился последний выстрел
     */
    @Override
    void afterShootingHandling(int x, int y){
        boolean sleep = false;
        switch (shootResult){
            case MISSED:
                if (shootToPotentialAims) currentAims.clear();
                enemyCells[x][y].setState(Cell.CellState.MISSED);
                changeTurn();
                break;
            case INJURED:

                shootToPotentialAims = true;
                enemyCells[x][y].setState(Cell.CellState.INJURED);
                sleep = true;
                break;
            case KILLED:
                markKilled(x, y);
                sleep = true;
                shipsToKill --;
                shootToPotentialAims = false;
                aimsCalculated = false;
                break;
        }
        MessageManager.getInstance().getMessage(false, shootResult);
        try {
            if (sleep) Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    /**
     * Функция переводит все раненые ячейки на черновом поле в убитые
     * Вокруг всех убитых ячеек ячейки переводятся в состояние "мимо", чтоб не стрелять по ним
     * @param x Координата X ячейки на игровом поле, в которую производился последний выстрел
     * @param y Координата Y ячейки на игровом поле, в которую производился последний выстрел
     */
    private void markKilled(int x, int y){
        enemyCells[x][y].setState(Cell.CellState.INJURED);
        for (int i = Field.minXY; i <= Field.maxXY; i++) {
            for (int j = Field.minXY; j <= Field.maxXY; j++) {
                Cell cell = enemyCells[j][i];
                if (cell.getState() == Cell.CellState.INJURED){
                    cell.setState(Cell.CellState.KILLED);
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if ((k >= Field.minXY) && (k <= Field.maxXY) &&
                                (l >= Field.minXY) && (l <= Field.maxXY)){
                                    if (enemyCells[l][k].getState() == Cell.CellState.NULL)
                                        enemyCells[l][k].setState(Cell.CellState.MISSED);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Инициализирует черновое игровое поле
     */
    private void setEnemiField(){
        enemyCells = new Cell[Field.maxXY + 1][Field.maxXY + 1];
        for (int i = Field.minXY; i <= Field.maxXY; i++) {
            for (int j = Field.minXY; j <= Field.maxXY; j++) {
                enemyCells[j][i] = new Cell(j, i, Cell.CellState.NULL, null);
            }
        }
    }

    /**
     * во время новой игры
     * очередь не наша
     * Корабли для потопления = 10
     * Заново инициализируем игровое и черновое поля
     */
    @Override
    public void reset() {
        super.reset();
        myTurn = false;
        setEnemiField();
    }

}