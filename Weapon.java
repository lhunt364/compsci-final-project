import java.awt.*;

public class Weapon
{
    private String name; //name of gun
    private int dmg; //damage bullets do
    private int fireRate; //firerate
    private String fireSound; //fire sound
    private String reloadSound; //reload sound (when ammo > 0)
    private String emptyReloadSound; //reload sound (when ammo == 0)
    private Color color; //color of the representation of the gun
    private int magSize; //size of the magazine of the gun
    private int ammo; //current ammo count (0 to magSize+1)
    private int length; //length of the gun in pixels

    public Weapon(String name, int dmg, int fireRate, String fireSound, String reloadSound, String emptyReloadSound, Color color, int magSize, int length)
    {
        this.name = name;
        this.dmg = dmg;
        this.fireRate = fireRate;
        this.fireSound = fireSound;
        this.reloadSound = reloadSound;
        this.emptyReloadSound = emptyReloadSound;
        this.color = color;
        this.magSize = magSize;
        this.length = length;
        ammo = magSize + 1;

    }

    //needs getter methods

    public Color getColor() {
        return color;
    }

    public int getLength() {
        return length;
    }
}
