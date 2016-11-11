package com.zor07.develop.gui;

import com.zor07.develop.players.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Класс для генерации сообщений для пользователя
 * Синглтон
 */
public class MessageManager {
    /**
     * Иснтанс экземпляра
     */
    private static MessageManager instance;
    /**
     * Список сообщений, для отображения во время того,
     * как промажет человек
     */
    private ArrayList<String> onHumanMiss;
    /**
     * Список сообщений, для отображения во время того,
     * как человек ранит корабль компьютера
     */
    private ArrayList<String> onHumanInjure;
    /**
     * Список сообщений, для отображения во время того,
     * как человек потопит корабль компьютера
     */
    private ArrayList<String> onHumanKill;

    /**
     * Список сообщений, для отображения во время того,
     * как промажет компьютер
     */
    private ArrayList<String> onPCMiss;
    /**
     * Список сообщений, для отображения во время того,
     * как компьютер ранит корабль человека
     */
    private ArrayList<String> onPCInjure;

    /**
     * Список сообщений, для отображения во время того,
     * как компьютер потопит корабль человека
     */
    private ArrayList<String> onPCKill;

    /**
     * Список сообщений, для отображения во время того,
     * как компьютер ранит корабль человека
     */

    /**
     * Список сообщений, для оповещений о победе компа
     */
    private ArrayList<String> onPCWin;

    /**
     * Список сообщений, для оповещений о победе человека
     */
    private ArrayList<String> onHumanWin;



    public static MessageManager getInstance(){
        if (instance == null)
            instance = new MessageManager();
        return instance;
    }

    /**
     * Функция случайным образом получает сообщения из определенного списка
     * Выбранное сообщение отображается пользователю. Передается в textLabel
     * @param human boolean, если true сообщение нужно сгенерить после выстрелачеловека.
     *              Сообщение генерится из одного из следующих списков:
     *              onHumanMiss,
     *              onHumanInjure,
     *              onHumanKill
     *
     *              Иначе из одного из следующих списков:
     *              onPCMiss,
     *              onPCInjure,
     *              onPCKill
     *
     * @param shootResult Результат произведенного выстрела.
     *                    Значение определяет выбор списка, из которого будет взято случайное сообщение
     */
    public void getMessage(boolean human, Player.ShootResult shootResult){
        if (shootResult == null) return;
        int index;
        Random random = new Random();
        if (human){
            switch (shootResult){
                case MISSED:
                    index = random.nextInt(onHumanMiss.size());
                    Canvas.textLabel.setText(onHumanMiss.get(index));
                    break;
                case INJURED:
                    index = random.nextInt(onHumanInjure.size());
                    Canvas.textLabel.setText(onHumanInjure.get(index));
                    break;
                case KILLED:
                    index = random.nextInt(onHumanKill.size());
                    Canvas.textLabel.setText(onHumanKill.get(index));
                    break;
            }
        } else {
            switch (shootResult){
                case MISSED:
                    index = random.nextInt(onPCMiss.size());
                    Canvas.textLabel.setText(onPCMiss.get(index));
                    break;
                case INJURED:
                    index = random.nextInt(onPCInjure.size());
                    Canvas.textLabel.setText(onPCInjure.get(index));
                    break;
                case KILLED:
                    index = random.nextInt(onPCKill.size());
                    Canvas.textLabel.setText(onPCKill.get(index));
                    break;
            }
        }
    }

    /**
     * Функция отображает сообщение по завершении игры (победы одного из игроков)
     * @param humanWin true, если победил человек, иначе false
     */
    public void getWinMessage(boolean humanWin){
        Random random = new Random();
        int index;
        if (humanWin){
            index = random.nextInt(onHumanWin.size());
            Canvas.textLabel.setText(onHumanWin.get(index));
        } else {
            index = random.nextInt(onPCWin.size());
            Canvas.textLabel.setText(onPCWin.get(index));
        }
    }

    /**
     * Сообщение перед первым ходом. Очередь человека.
     */
    public void getStartMessage(){
        Canvas.textLabel.setText("Стреляй");
    }

    /**
     * Закрытый конструктор.
     * При вызове инициализирует все списки
     */
    private MessageManager(){
        initOnHumanInjure();
        initOnHumanKill();
        initOnHumanMiss();
        initOnHumanWin();
        initOnPCInjure();
        initOnPCKill();
        initOnPCMiss();
        initOnPCWin();
    }

    /**
     * Инициализатор списка onHumanMiss
     */
    private void initOnHumanMiss(){
        onHumanMiss = new ArrayList<>();
        onHumanMiss.add("Вы промазали!");
        onHumanMiss.add("Вам не везет!");
        onHumanMiss.add("Косой!");
    }
    /**
     * Инициализатор списка onHumanInjure
     */
    private void initOnHumanInjure(){
        onHumanInjure = new ArrayList<>();
        onHumanInjure.add("Вы ранили меня!");
        onHumanInjure.add("Больно!");
        onHumanInjure.add("Ты подстрелил мой черный зад!");
        onHumanInjure.add("Не надо, пожалуйста!");
    }
    /**
     * Инициализатор списка onHumanKill
     */
    private void initOnHumanKill(){
        onHumanKill = new ArrayList<>();
        onHumanKill.add("Вы потопили мой кораблик!");
        onHumanKill.add("Like a boss!");
    }

    /**
     * Инициализатор списка onPCMiss
     */
    private void initOnPCMiss(){
        onPCMiss = new ArrayList<>();
        onPCMiss.add("Я промазал!");
        onPCMiss.add("Сегодня тебе везет!");
    }
    /**
     * Инициализатор списка onPCInjure
     */
    private void initOnPCInjure(){
        onPCInjure = new ArrayList<>();
        onPCInjure.add("Я ранил вас!");
        onPCInjure.add("ХА! Больно?");
    }
    /**
     * Инициализатор списка onPCKill
     */
    private void initOnPCKill(){
        onPCKill = new ArrayList<>();
        onPCKill.add("Я отправил ваш корабль кормить рыб!");
        onPCKill.add("Я позабочусь о твоих детях");
    }

    /**
     * Инициализатор списка onPCWin
     */
    private void initOnPCWin(){
        onPCWin = new ArrayList<>();
        onPCWin.add("Тебя сделал твой комп, пожалуйся своей мамочке!");
        onPCWin.add("Я позабочусь о твоих детях");
        onPCWin.add("Может морской бой - это не для тебя?");
        onPCWin.add("Мой рандомный генератор работает играет в морской бой лучше тебя. Я выйграл.");
    }

    /**
     * Инициализатор списка onHumanWin
     */
    private void initOnHumanWin(){
        onHumanWin = new ArrayList<>();
        onHumanWin.add("Ладно, ладно, ты победил. Давай заново...");
        onHumanWin.add("Позаботься о моей жене...");
        onHumanWin.add("я всего лишь набор нулей и единиц. Ты одолел несуществующего соперника");
    }







}
