package com.zor07.develop.gui;

import com.zor07.develop.SeaFight;
import com.zor07.develop.fieldParts.Cell;
import com.zor07.develop.fieldParts.Field;

import javax.swing.*;
import java.awt.*;

/**
 * главный фрейм, на котором отображается игра
 *
 * Класс синглтон
 */
public class Canvas extends JFrame {


    JPanel player1Field;
    JPanel player2Field;
    JPanel window;

    /**
     * Эллементы главной панели
     * textLabel - label для отображения текстовой информации
     * newGameButton - кнопка "Новая игра"
     * field1 - Поле игрока 1
     * field2 - Поле игрока 2
     */
    static JLabel textLabel;

    /**
     * Иконки для ячеек
     */
    private final ImageIcon alive = new ImageIcon(getClass().getResource("res/alive.png"));
    private final ImageIcon killed = new ImageIcon(getClass().getResource("res/killed.png"));
    private final ImageIcon injured = new ImageIcon(getClass().getResource("res/injured.png"));
    private final ImageIcon missed = new ImageIcon(getClass().getResource("res/missed.png"));
    private final ImageIcon nothing = new ImageIcon(getClass().getResource("res/null.png"));

    /**
     * наш синглтоновский инстанс
     */
    private static Canvas instance;

    /**
     * Закрытый конструктор по умолчанию, в котором поочередно:
     * Инициализируется главный фрейм
     * Инициализируются компоненты фрейма
     * Компоненты фрейма группируются в window,
     * компонент window добавляется в главный фрейм
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     */
    private Canvas() throws HeadlessException {
        super("Sea Fight");
        /*
         * Инициализируем фрейм
         */
        setSize(800, 600);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /*
         * Инициализируем компоненты
         */
        /*
      Главная панель, на которую крепится все остальные эллементы
     */
        window = new JPanel();



        initializeFields();

        textLabel = new JLabel("Мой ход");
        JButton newGameButton = new JButton("Новая игра");
        newGameButton.addActionListener(SeaFight.getInstance());


        /*
         * Добавляем компоненты на панель window
         */
        window.setLayout(new GridLayout(2, 2));

        window.add(textLabel);
        window.add(newGameButton);

        /*
         * добавляем панель window на наш фрейм и отображаем его
         */
        add(window);
        setVisible(true);


    }

    public void initializeFields(){
        player1Field = new JPanel();
        player2Field = new JPanel();

        initializeField(player1Field, SeaFight.computerPlayer.getField());
        initializeField(player2Field, SeaFight.humanPlayer.getField());

        window.add(player1Field);
        window.add(player2Field);
    }

    /**
     * функция создает отображаемую игровую панель игрового поля,
     * заполняя отображаемую игровую панель ячейками из матрицы
     * игрового поля
     * @param jPanel панель игрового поля
     * @param field матрица игрового поля
     */
    private void initializeField(JPanel jPanel, Field field){
        if (field == null) return;
        jPanel.setLayout(new GridLayout(10, 10));
        jPanel.setMaximumSize(new Dimension(400, 400));

        Cell[][] cells = field.getCells();
        for (int i = Field.minXY; i <= Field.maxXY; i++) {
            for (int j = Field.minXY; j <= Field.maxXY; j++) {
                cells[j][i].setIcon(nothing);
                jPanel.add(cells[j][i]);

            }
        }
    }


    /**
     * Функция отрисовывает поля игроков после каждого шага
     */
    public void draw(){

        Field humanField = SeaFight.humanPlayer.getField();
        Field computerField = SeaFight.computerPlayer.getField();

        updateField(humanField, false);
        updateField(computerField, true);
    }


    /**
     * Функция обновляет отображение поля игрока
     * @param field поле игрока
     * @param computerField true, если в обработку передается поле компьютерного игрока, иначе false
     */
    private void updateField(Field field, boolean computerField){
        if (field == null) return;
        Cell[][] cells = field.getCells();
        for (int i = Field.minXY; i <= Field.maxXY; i++) {
            for (int j = Field.minXY; j <= Field.maxXY ; j++) {
                Cell cell = cells[j][i];

                if (computerField){
                    updateComputerCell(cell);
                } else {
                    updateHumanCell(cell);
                }

            }
        }
    }


    /**
     * Функция обновляет отображение конкретной ячейки на поле компьютера
     * @param cell ячейка
     */
    private void updateComputerCell(Cell cell){
        Cell.CellState state = cell.getState();

        if (state != null)
            switch (state){
                case NULL:
                    cell.setIcon(nothing);
                    break;
                case INJURED:
                    cell.setIcon(injured);
                    break;
                case KILLED:
                    cell.setIcon(killed);
                    break;
                case MISSED:
                    cell.setIcon(missed);
                    break;
            }

    }

    /**
     * Функция обновляет отображение конкретной ячейки на поле человека
     * @param cell ячейка
     */
    private void updateHumanCell(Cell cell){
        Cell.CellState state = cell.getState();

        if (state != null)
            switch (state){
                case NULL:
                    cell.setIcon(nothing);
                    break;
                case MISSED:
                    cell.setIcon(missed);
                    break;
                case ALIVE:
                    cell.setIcon(alive);
                    break;
                case INJURED:
                    cell.setIcon(injured);
                    break;
                case KILLED:
                    cell.setIcon(killed);
                    break;
            }
    }


    /**
     * Функция для получения экземпляра класса Canvas
     * @return экземпляр класса Canvas
     */
    public static Canvas getInstance(){
        if (instance == null)
            instance = new Canvas();
        return instance;
    }
}
