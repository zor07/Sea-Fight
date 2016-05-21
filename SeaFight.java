import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by akarmov on 28.02.2016.
 */
public class SeaFight extends JFrame  implements ActionListener {
    JPanel window = new JPanel();
    JLabel l1 = new JLabel("Мой ход");
    JButton newGameButton = new JButton("Новая игра");
    JLabel l2 = new JLabel("2");
    Player humanPlayer;
    Player computerPlayer;

    public static void main(String[] args) throws InterruptedException {
        SeaFight seaFight = new SeaFight();
        seaFight.playGame();
    }

    public void actionPerformed(ActionEvent e) {
        this.computerPlayer.reset();
    }

    public SeaFight() throws InterruptedException {
        super("SeaFight");
        this.humanPlayer = new Player(this.l1, this.l2);
        this.computerPlayer = new Player(this.humanPlayer.getEnemyField(), this.humanPlayer.getMyField());
        this.newGameButton.addActionListener(this);
        this.setSize(800, 600);
        this.setResizable(true);
        this.setDefaultCloseOperation(3);
        this.window.setLayout(new GridLayout(2, 2));
        this.humanPlayer.setOpponent(this.computerPlayer);
        this.computerPlayer.setOpponent(this.humanPlayer);
        this.window.add(this.humanPlayer.getMyField().getField());
        this.window.add(this.humanPlayer.getEnemyField().getField());
        this.window.add(this.l1);
        this.window.add(this.newGameButton);
        this.add(this.window);
        this.setVisible(true);
    }

    public void playGame() throws InterruptedException {
        this.computerPlayer.start();
    }

}
