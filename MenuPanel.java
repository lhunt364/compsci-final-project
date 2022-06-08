import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPanel extends JPanel
{
    private Font font;
    private Main main;
    private CardLayout cardLayout;
    private MainMenuPanel mmp;
    private PlayPanel pp;
    private OptionsPanel op;

    public MenuPanel(int width, int height, Main main)
    {
        this.main = main;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.lightGray);
        font = new Font("Monospaced", Font.BOLD, 50);

        cardLayout = new CardLayout();
        setLayout(cardLayout);
        mmp = new MainMenuPanel(width, height, this);
        add(mmp, "main");
        pp = new PlayPanel(width, height, this, main);
        add(pp, "play");
        op = new OptionsPanel(width, height, this, main);
        add(op, "option");
    }

    public void showMain()
    {
        cardLayout.show(this, "main");
    }
    public void showPlay()
    {
        cardLayout.show(this,"play");
    }

    public void showOption()
    {
        cardLayout.show(this, "option");
    }
}

/**
 * first menu panel. holds buttons for playing and for options
 */
class MainMenuPanel extends JPanel
{
    private MenuPanel menu;
    private Font font;

    public MainMenuPanel(int width, int height, MenuPanel menu)
    {
        this.menu = menu;
        font = new Font("Monospaced", Font.BOLD, 50);
        Font font2 = new Font("Monospaced", Font.BOLD, 30);
        setPreferredSize(new Dimension(width, height));

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel title = new JLabel("Breaking Fat: Hank Schrader");
        title.setFont(font2);
        add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel title2 = new JLabel("Escapes The Ghostavos");
        title2.setFont(font2);
        add(title2, gbc);

        gbc.gridy = 2;
        JLabel image = new JLabel(new ImageIcon("hannkschrader50x50.png"));
        add(image, gbc);

        font = new Font("Monospaced", Font.BOLD, 35);
        gbc.gridy = 3;
        JButton play = new JButton("Play");
        play.setFont(font);
        play.addActionListener(e -> menu.showPlay());
        add(play, gbc);

        gbc.gridy = 4;
        JButton options = new JButton("Options");
        options.setFont(font);
        options.addActionListener(e -> menu.showOption());
        add(options, gbc);
    }
}

class PlayPanel extends JPanel
{
    private MenuPanel menu;
    private Font font;
    private Main main;

    public PlayPanel(int width, int height, MenuPanel menu, Main main)
    {
        this.menu = menu;
        this.main = main;
        font = new Font("Monospaced", Font.BOLD, 30);
        setPreferredSize(new Dimension(width, height));

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JButton back = new JButton("Back");
        back.setFont(font);
        back.addActionListener(e -> menu.showMain());
        add(back, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel mapLabel = new JLabel("Map: ");
        mapLabel.setFont(font);
        add(mapLabel, gbc);

        gbc.gridx = 1;
        String[] maps = Helpful.getMaps();
        System.out.println(maps);
        JComboBox<String> mapComboBox = new JComboBox<>(maps);
        mapComboBox.setFont(font);
        add(mapComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel wepLabel = new JLabel("Weapon: ");
        wepLabel.setFont(font);
        add(wepLabel, gbc);

        gbc.gridx = 1;
        String[] weps = generateWeaponsArr();
        System.out.println(weps);
        JComboBox<String> wepComboBox = new JComboBox<>(weps);
        wepComboBox.setFont(font);
        add(wepComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel difLabel = new JLabel("Mode: ");
        difLabel.setFont(font);
        add(difLabel, gbc);

        gbc.gridx = 1;
        String[] difs = {"Easy", "Normal", "Hard", "Impossible"};
        JComboBox<String> difComboBox = new JComboBox<>(difs);
        difComboBox.setFont(font);
        add(difComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton play = new JButton("Play");
        play.setFont(font);
        play.addActionListener(e -> {
            main.updateGameInfo(wepComboBox.getSelectedIndex(), (String) mapComboBox.getSelectedItem(), getDif(difComboBox.getSelectedIndex()));
            main.goToGame();
        });
        add(play, gbc);

    }

    public String[] generateWeaponsArr()
    {
        ArrayList<Weapon> weps = main.getWeapons();
        String[] ret = new String[weps.size()];
        for(int i = 0; i < weps.size(); i++)
        {
            ret[i] = weps.get(i).getName();
        }
        return ret;
    }

    public double getDif(int i)
    {
        double[] difs = {1, 2, 4, 10};
        return difs[i];
    }

}


class OptionsPanel extends JPanel
{
    private MenuPanel menu;
    private Font font;
    private Main main;

    public OptionsPanel(int width, int height, MenuPanel menu, Main main)
    {
        this.menu = menu;
        this.main = main;
        font = new Font("Monospaced", Font.BOLD, 30);
        setPreferredSize(new Dimension(width, height));

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        JButton back = new JButton("Back");
        back.setFont(font);
        back.addActionListener(e -> menu.showMain());
        add(back, gbc);


        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel gameVol = new JLabel("Weapon Volume: ");
        gameVol.setFont(font);
        add(gameVol, gbc);

        gbc.gridx = 2;
        JLabel gameVol2 = new JLabel((int)(main.getGunVolume())+"");
        gameVol2.setFont(font);
        add(gameVol2, gbc);

        gbc.gridx = 1;
        JButton gameVolDown = new JButton("<");
        gameVolDown.addActionListener(e -> {
            main.decreaseGameVol();
            gameVol2.setText((int)(main.getGunVolume())+"");
        });
        add(gameVolDown, gbc);

        gbc.gridx = 3;
        JButton gameVolUp = new JButton(">");
        gameVolUp.addActionListener(e -> {
            main.increaseGameVol();
            gameVol2.setText((int)(main.getGunVolume())+"");
        });
        add(gameVolUp, gbc);


        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel musicVol = new JLabel("Music Volume: ");
        musicVol.setFont(font);
        add(musicVol, gbc);

        gbc.gridx = 2;
        JLabel musicVol2 = new JLabel((int)(main.getMusicVolume())+"");
        musicVol2.setFont(font);
        add(musicVol2, gbc);

        gbc.gridx = 1;
        JButton musicVolDown = new JButton("<");
        musicVolDown.addActionListener(e -> {
            main.decreaseMusicVol();
            main.resetMusic();
            musicVol2.setText((int)(main.getMusicVolume())+"");
        });
        add(musicVolDown, gbc);

        gbc.gridx = 3;
        JButton musicVolUp = new JButton(">");
        musicVolUp.addActionListener(e -> {
            main.increaseMusicVol();
            main.resetMusic();
            musicVol2.setText((int)(main.getMusicVolume())+"");
        });
        add(musicVolUp, gbc);
    }

}
