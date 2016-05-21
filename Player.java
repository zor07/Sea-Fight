import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by akarmov on 08.03.2016.
 */
public class Player extends Thread{
    private volatile int shipsToKill = 20;
    private int lastX;
    private int lastY;
    private Field myField;
    private Field enemyField;
    private boolean human;
    private ArrayList<Integer> leftAims = new ArrayList();
    private ArrayList<Integer> rightAims = new ArrayList();
    private ArrayList<Integer> downAims = new ArrayList();
    private ArrayList<Integer> upAims = new ArrayList();
    private boolean shootToPotentialAims;
    private Player opponent;

    public Player(Field myField, Field enemyField) {
        this.myField = myField;
        this.enemyField = enemyField;
        this.human = false;
        this.myField.setPlayerWhoMoves(this);
        this.enemyField.setPlayerWhoMoves(this);
    }

    public Player(JLabel label1, JLabel label2) {
        this.human = true;
        this.myField = new Field(label1, false);
        this.enemyField = new Field(label2, true);
    }

    public Field getMyField() {
        return this.myField;
    }

    public Field getEnemyField() {
        return this.enemyField;
    }

    public void shoot() throws InterruptedException {
        if (shipsToKill == 0) return;
        if(!this.enemyField.getPlayerWhoMoves().isHuman()) {
            Random random = new Random();
            boolean x = false;
            boolean y = false;

            int x1;
            int y1;
            do {
                if(!this.shootToPotentialAims) {
                    x1 = random.nextInt(10);
                    y1 = random.nextInt(10);
                    this.lastX = x1;
                    this.lastY = y1;
                } else {
                    int[] type = this.getNextCoordinates();
                    x1 = type[0];
                    y1 = type[1];
                }

            } while(!this.shoot(x1, y1));
            Thread.sleep(1000L);
            byte type1 = this.enemyField.getCell(x1, y1).getCellType();
            switch(type1) {
                case 2:
                    this.shootToPotentialAims = true;
                    this.fillPotentialAims();
                    break;
                case 3:
                    this.shootToPotentialAims = false;
                    break;
                case 4:
                    this.fillPotentialAims();
            }
        }

    }

    public boolean shoot(int x, int y) {
        if (shipsToKill == 0) return true;
        boolean successfulShoot = false;
        Cell cell = this.enemyField.getCell(x, y);
        byte cellType = cell.getCellType();
        switch(cellType) {
            case 0:
                Random random;
                if(this.enemyField.getPlayerWhoMoves().isHuman()) {
                    random = new Random();
                    if(random.nextBoolean()) {
                        this.myField.getLabel().setText("Вы промазали, моя очередь.");
                    } else {
                        this.myField.getLabel().setText("Мне нравится, когда вы мажете.");
                    }
                } else {
                    random = new Random();
                    if(random.nextBoolean()) {
                        this.enemyField.getLabel().setText("Я косой. Стреляйте!");
                    } else {
                        this.enemyField.getLabel().setText("Вам везет, что мои выстрелы случайны...Стреляйте!");
                    }
                }

                this.myField.setPlayerWhoMoves(this.opponent);
                this.enemyField.setPlayerWhoMoves(this.opponent);
                cell.setCellType((byte)4, false);
                cell.changeIcon(4);
                successfulShoot = true;
                break;
            case 1:
                --this.shipsToKill;
                cell.setCellType((byte)2, false);
                cell.changeIcon(2);
                if(this.enemyField.getPlayerWhoMoves().isHuman()) {
                    this.myField.getLabel().setText("Вы ранили мой кораблик...Продолжайте");
                } else {
                    this.enemyField.getLabel().setText("Я ранил ваш корабль. Недолго ему осталось.");
                }

                if(!cell.shipStillAlive()) {
                    cell.killShip();
                    if(this.enemyField.getPlayerWhoMoves().isHuman()) {
                        if (shipsToKill > 0)
                            this.myField.getLabel().setText("Вы потопили корабль...Продолжайте");
                        else
                            this.myField.getLabel().setText("Вы выйграли");
                    } else {
                        if (shipsToKill > 0)
                            this.enemyField.getLabel().setText("Я потопил ваш корабль.");
                        else
                            this.enemyField.getLabel().setText("Я победил");
                    }
                }

                successfulShoot = true;
                break;
            case 2:
                successfulShoot = false;
                break;
            case 3:
                successfulShoot = false;
                break;
            case 4:
                successfulShoot = false;
        }

        return successfulShoot;
    }
    private int[] getNextCoordinates() {
        int[] res = new int[2];
        if(!this.leftAims.isEmpty()) {
            res[0] = ((Integer)this.leftAims.get(0)).intValue();
            res[1] = this.lastY;
            this.leftAims.remove(0);
            return res;
        } else if(!this.rightAims.isEmpty()) {
            res[0] = ((Integer)this.rightAims.get(0)).intValue();
            res[1] = this.lastY;
            this.rightAims.remove(0);
            return res;
        } else if(!this.downAims.isEmpty()) {
            res[0] = this.lastX;
            res[1] = ((Integer)this.downAims.get(0)).intValue();
            this.downAims.remove(0);
            return res;
        } else if(!this.upAims.isEmpty()) {
            res[0] = this.lastX;
            res[1] = ((Integer)this.upAims.get(0)).intValue();
            this.upAims.remove(0);
            return res;
        } else {
            return res;
        }
    }
    private void fillPotentialAims() {
        int x = this.lastX;
        int y = this.lastY;
        this.leftAims.clear();
        this.rightAims.clear();
        this.downAims.clear();
        this.upAims.clear();

        int i;
        for(i = x + 1; i < x + 4; ++i) {
            if(i < 10) {
                if(this.enemyField.getCell(i, y).getCellType() == 4) {
                    break;
                }

                if(this.enemyField.getCell(i, y).getCellType() != 2) {
                    this.leftAims.add(Integer.valueOf(i));
                }
            }
        }

        for(i = x - 1; i > x - 4; --i) {
            if(i >= 0) {
                if(this.enemyField.getCell(i, y).getCellType() == 4) {
                    break;
                }

                if(this.enemyField.getCell(i, y).getCellType() != 2) {
                    this.rightAims.add(Integer.valueOf(i));
                }
            }
        }

        for(i = y - 1; i > y - 4; --i) {
            if(i >= 0) {
                if(this.enemyField.getCell(x, i).getCellType() == 4) {
                    break;
                }

                if(this.enemyField.getCell(x, i).getCellType() != 2) {
                    this.downAims.add(Integer.valueOf(i));
                }
            }
        }

        for(i = y + 1; i < y + 4; ++i) {
            if(i < 10) {
                if(this.enemyField.getCell(x, i).getCellType() == 4) {
                    break;
                }

                if(this.enemyField.getCell(x, i).getCellType() != 2) {
                    this.upAims.add(Integer.valueOf(i));
                }
            }
        }

    }
    public boolean isHuman() {
        return this.human;
    }
    public void setOpponent(Player player) {
        this.opponent = player;
    }
    public int getShipsToKill() {
        return this.shipsToKill;
    }

    public void run() {
        while(true) {
            try {
                if(this.shipsToKill > 0 & this.opponent.getShipsToKill() > 0) {
                    this.shoot();
                    continue;
                }
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }
    }

    public void reset() {
        this.myField.reset();
        this.enemyField.reset();
        this.myField.setPlayerWhoMoves(this);
        this.enemyField.setPlayerWhoMoves(this);
        this.enemyField.getLabel().setText("Мой ход");
        this.shipsToKill = 20;
        this.opponent.shipsToKill = 20;
    }

}
