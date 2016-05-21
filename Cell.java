import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by akarmov on 29.02.2016.
 */
public class Cell extends JButton implements ActionListener{
    private ImageIcon alive;
    private ImageIcon killed;
    private ImageIcon injured;
    private ImageIcon doNotShootHere;
    private int initialX;
    private int initialY;
    private int size;
    boolean direction;
    private byte cellType = 0;
    private int xCoordinate;
    private int yCoordinate;
    private JLabel label;
    private Field field;

    public Cell(int x, int y, JLabel label, Field field) {
        this.field = field;
        this.label = label;
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.alive = new ImageIcon(this.getClass().getResource("alive.png"));
        this.injured = new ImageIcon(this.getClass().getResource("injured.png"));
        this.killed = new ImageIcon(this.getClass().getResource("killed.png"));
        this.doNotShootHere = new ImageIcon(this.getClass().getResource("doNotShootHere.png"));
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(this.field.getPlayerWhoMoves().getEnemyField().equals(this.field) && this.field.getPlayerWhoMoves().isHuman()) {
            this.field.getPlayerWhoMoves().shoot(this.xCoordinate, this.yCoordinate);
        }

    }

    public ArrayList<Cell> getNeighbourCells(Field field) {
        ArrayList cells = new ArrayList();
        int fromX = this.getFromX(this.xCoordinate);
        int fromY = this.getFromY(this.yCoordinate);
        int toX = this.getToX(this.xCoordinate);
        int toY = this.getToY(this.yCoordinate);

        for(int x = fromX; x <= toX; ++x) {
            for(int y = fromY; y <= toY; ++y) {
                cells.add(field.getCell(x, y));
            }
        }

        return cells;
    }

    public ArrayList<Cell> getNeighbourCells(Cell cell) {
        ArrayList cells = new ArrayList();
        int fromX = this.getFromX(cell.xCoordinate);
        int fromY = this.getFromY(cell.yCoordinate);
        int toX = this.getToX(cell.xCoordinate);
        int toY = this.getToY(cell.yCoordinate);

        for(int x = fromX; x <= toX; ++x) {
            for(int y = fromY; y <= toY; ++y) {
                cells.add(this.field.getCell(x, y));
            }
        }

        return cells;
    }

    public int getFromX(int x) {
        return x <= 0?0:x - 1;
    }

    public int getToX(int x) {
        return x >= 9?9:x + 1;
    }

    public int getFromY(int y) {
        return y <= 0?0:y - 1;
    }

    public int getToY(int y) {
        return y >= 9?9:y + 1;
    }

    public void setCellType(byte i, boolean initialize) {
        this.cellType = i;
          if(initialize & !this.field.isEnemyField()) {
            switch(this.cellType) {
                case 0:
                    this.setIcon((Icon)null);
                    break;
                case 1:
                    this.setIcon(this.alive);
                    break;
                case 2:
                    this.setIcon(this.injured);
                    break;
                case 3:
                    this.setIcon(this.killed);
                    break;
                case 4:
                    this.setIcon(this.doNotShootHere);
            }
        }

    }

    public byte getCellType() {
        return this.cellType;
    }

    public void setShipParameters(int initialX, int initialY, int size, boolean direction) {
        this.initialX = initialX;
        this.initialY = initialY;
        this.size = size;
        this.direction = direction;
    }

    public boolean shipStillAlive() {
        int i;
        if(this.direction) {
            for(i = this.initialX; i < this.initialX + this.size; ++i) {
                if(this.field.getCell(i, this.initialY).getCellType() == 1) {
                    return true;
                }
            }
        } else {
            for(i = this.initialY; i < this.initialY + this.size; ++i) {
                if(this.field.getCell(this.initialX, i).getCellType() == 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public void killShip() {
        int i;
        if(this.direction) {
            for(i = this.initialX; i < this.initialX + this.size; ++i) {
                this.field.getCell(i, this.initialY).setIcon(this.killed);
                this.field.getCell(i, this.initialY).setCellType((byte)3, false);
                this.markAround(this.getNeighbourCells(this.field.getCell(i, this.initialY)));
            }
        } else {
            for(i = this.initialY; i < this.initialY + this.size; ++i) {
                this.field.getCell(this.initialX, i).setIcon(this.killed);
                this.field.getCell(this.initialX, i).setCellType((byte)3, false);
                this.markAround(this.getNeighbourCells(this.field.getCell(this.initialX, i)));
            }
        }

    }

    private void markAround(ArrayList<Cell> cells) {
        for(int i = 0; i < cells.size(); ++i) {
            if(((Cell)cells.get(i)).getCellType() != 3) {
                ((Cell)cells.get(i)).setCellType((byte)4, false);
                ((Cell)cells.get(i)).setIcon(this.doNotShootHere);
            }
        }

    }

    public void changeIcon(int type) {
        switch(type) {
            case 0:
                this.setIcon((Icon)null);
                break;
            case 1:
                this.setIcon(this.alive);
                break;
            case 2:
                this.setIcon(this.injured);
                break;
            case 3:
                this.setIcon(this.killed);
                break;
            case 4:
                this.setIcon(this.doNotShootHere);
        }

    }

    public JLabel getMyLabel() {
        return this.label;
    }

}
