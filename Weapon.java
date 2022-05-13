import java.awt.*;

public class Weapon
{
    private String name;
    private int dmg;
    private int fireRate;
    private String fireSound;
    private String reloadSound;
    private Color color;

    public Weapon(String name, int dmg, int fireRate, String fireSound, String reloadSound, Color color)
    {
        this.name = name;
        this.dmg = dmg;
        this.fireRate = fireRate;
        this.fireSound = fireSound;
        this.reloadSound = reloadSound;
        this.color = color;

    }

    //needs getter methods
}
