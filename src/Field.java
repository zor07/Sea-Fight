import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by akarmov on 04.03.2016.
 */
public class Field {
    private JPanel field;
    private Cell[][] cells = new Cell[10][10];
    private boolean enemyField;
    private Player playerWhoMoves;
    private JLabel label;

    public Field(JLabel label, boolean fieldType) {
        this.label = label;
        this.field = new JPanel();
        this.field.setLayout(new GridLayout(10, 10));
        this.field.setMaximumSize(new Dimension(400, 400));
        this.enemyField = fieldType;

        for(int i = 0; i < 10; ++i) {
            for(int j = 0; j < 10; ++j) {
                this.cells[j][i] = new Cell(j, i, label, this);
                this.field.add(this.cells[j][i]);
            }
        }

        this.setShips();
    }

    public boolean isEnemyField() {
        return this.enemyField;
    }

    public JPanel getField() {
        return this.field;
    }

    public Cell getCell(int x, int y) {
        return this.cells[x][y];
    }

    public void setShips() {
        Random random = new Random();

        for(int shipSize = 1; shipSize <= 4; ++shipSize) {
            int shipCount = 5 - shipSize;

            for(int i = 0; i < shipCount; ++i) {
                boolean direction;
                int x;
                int y;
                do {
                    direction = random.nextBoolean();
                    if(direction) {
                        x = random.nextInt(10 - shipSize + 1);
                        y = random.nextInt(10);
                    } else {
                        x = random.nextInt(10);
                        y = random.nextInt(10 - shipSize + 1);
                    }
                } while(!this.validPlaceForShip(x, y, shipSize, direction));

                this.insertShip(x, y, shipSize, direction);
            }
        }

    }

    public boolean validPlaceForShip(int x, int y, int shipSize, boolean direction) {
        ArrayList cellsTotal = new ArrayList();
        int i;
        ArrayList cellsLocal;
        if(direction) {
            for(i = x; i < x + shipSize; ++i) {
                cellsLocal = this.getCell(i, y).getNeighbourCells(this);
                cellsTotal.addAll(cellsLocal);
            }
        } else {
            for(i = y; i < y + shipSize; ++i) {
                cellsLocal = this.getCell(x, i).getNeighbourCells(this);
                cellsTotal.addAll(cellsLocal);
            }
        }

        for(i = 0; i < cellsTotal.size(); ++i) {
            if(((Cell)cellsTotal.get(i)).getCellType() == 1) {
                return false;
            }
        }

        return true;
    }

    public void insertShip(int x, int y, int shipSize, boolean direction) {
        int i;
        if(direction) {
            for(i = x; i < x + shipSize; ++i) {
                this.getCell(i, y).setCellType((byte)1, true);
                this.getCell(i, y).setShipParameters(x, y, shipSize, direction);
            }
        } else {
            for(i = y; i < y + shipSize; ++i) {
                this.getCell(x, i).setCellType((byte)1, true);
                this.getCell(x, i).setShipParameters(x, y, shipSize, direction);
            }
        }

    }

    public void setPlayerWhoMoves(Player player) {
        this.playerWhoMoves = player;
    }

    public Player getPlayerWhoMoves() {
        return this.playerWhoMoves;
    }

    public JLabel getLabel() {
        return this.label;
    }

    public void reset() {
        for(int i = 0; i < 10; ++i) {
            for(int j = 0; j < 10; ++j) {
                this.cells[j][i].setCellType((byte)0, false);
                this.cells[j][i].setIcon((Icon)null);
            }
        }

        this.setShips();
    }
}
