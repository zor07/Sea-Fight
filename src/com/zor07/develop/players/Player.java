package com.zor07.develop.players;


import com.zor07.develop.fieldParts.Cell;
import com.zor07.develop.fieldParts.Field;

/**
 * Абстрактный класс описывающий игрока
 */

public abstract class Player{
    /**
     * Оппонент игрока
     */
    Player opponent;
    /**
     * Игровое поле
     */
    Field field;
    /**
     * Определяет очередность ходов
     */
   boolean myTurn;

    /**
     * Кол-во кораблей для потопления
     */
   int shipsToKill = 10;

    /**
     * Результат выстрела
     *                * MISSED
     *                * INJURED
     *                * KILLED
     */
    ShootResult shootResult;

    /**
     * Конструктор класса
     * Инициализируется игровое поле
     */
    Player() {
        field = new Field();
    }

    /**
     * Определяет очередность текущего хода
     * @return true, если сейчас очередь ходить этого игрока (myTurn == true), иначе false
     */
    public boolean moves(){
        return myTurn;
    }

    /**
     * Возвращает кол-во кораблей для потопления
     */
    public int getShipsToKill() {
        return shipsToKill;
    }

    /**
     * Геттер для игрового поля
     * @return игровое поле игрока
     */
    public Field getField() {
        return field;
    }

    /**
     * Сеттер для оппонента
     * @param opponent оппонент игрока
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * Функция для смены очереди хода
     */
    void changeTurn(){
        myTurn = !myTurn;
        opponent.myTurn = !opponent.myTurn;
    }

    /**
     * Состояния которые может принимать результат выстрела (Промазал, ранил, убил)
     */
    public enum ShootResult {
        MISSED,
        INJURED,
        KILLED
    }

    /**
     * Функция вызывается когда стреляют в текущего игрока и он должен "Быть подстрелян"
     * @param x координата Х ячейки на игровом поле, в которую производится выстрел
     * @param y координата Y ячейки на игровом поле, в которую производится выстрел
     * @return Результат выстрела (Промазал, ранил, убил)
     */
    ShootResult getShot(int x, int y){
        Cell cell = field.getCells()[x][y];
        if (cell.getState() == Cell.CellState.NULL || cell.getState() == Cell.CellState.ALIVE) {
            cell.getShot();
            switch (cell.getState()) {
                case MISSED:
                    return ShootResult.MISSED;
                case INJURED:
                    return ShootResult.INJURED;
                case KILLED:
                    return ShootResult.KILLED;
                default:
                    return null;
            }
        } return null;
    }

    /**
     * Абстрактная функция, для обновления данных после произведения выстрела
     * @param x координата Х ячейки на игровом поле, в которую производился последний выстрел
     * @param y координата Y ячейки на игровом поле, в которую производился последний выстрел
     */
    abstract void afterShootingHandling(int x, int y);

    /**
     * Функция приводит все данные в изначальное состояние
     * Вызывается для начала новой игры
     */
    public void reset(){
        field.resetCells();
        shipsToKill = 10;
    }

}
