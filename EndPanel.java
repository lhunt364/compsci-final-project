import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel
{
    private JLabel score2;
    private JLabel kills2;

    private Main main;

    private Font font;

    public EndPanel(int width, int height, Main main)
    {
        this.main = main;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.lightGray);
        font = new Font("Monospaced", Font.BOLD, 50);

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel gameOver = new JLabel("GAME OVER");
        gameOver.setFont(font);
        add(gameOver, gbc);

        gbc.gridy = 1;
        JLabel score1 = new JLabel("Score:");
        score1.setFont(font);
        add(score1, gbc);

        gbc.gridy = 2;
        score2 = new JLabel("joe");
        score2.setFont(font);
        add(score2, gbc);

        gbc.gridy = 3;
        JLabel kills1 = new JLabel("Kills:");
        kills1.setFont(font);
        add(kills1, gbc);

        gbc.gridy = 4;
        kills2 = new JLabel("nuts");
        kills2.setFont(font);
        add(kills2, gbc);

        gbc.gridy = 5;
        JButton menu = new JButton("Menu");
        menu.setFont(font);
        menu.addActionListener(e -> main.goToMenu());
        add(menu, gbc);

        setVisible(true);

    }

    /**
     * updates info to display
     */
    public void updateInfo(int score, int kills)
    {
        this.score2.setText("" + String.format("%,d", score));
        this.kills2.setText("" + String.format("%,d", kills));
    }

}
