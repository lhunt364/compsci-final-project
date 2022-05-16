import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;

public class Weapon
{
    private String name; //name of gun
    private int dmg; //damage bullets do
    private int fireRate; //firerate
    private String[] fireSounds; //fire sound
    private String reloadSound; //reload sound (when ammo > 0)
    private String emptyReloadSound; //reload sound (when ammo == 0)
    private int reloadTime;
    private int emptyReloadTime;
    private Color color; //color of the representation of the gun
    private int magSize; //size of the magazine of the gun
    private int ammo; //current ammo count (0 to magSize+1)
    private int length; //length of the gun in pixels
    private boolean reloading; //true if reloading, false if not
    private Main main;

    public Weapon(String name, int dmg, int fireRate, String[] fireSounds, String reloadSound, String emptyReloadSound, int reloadTime, int emptyReloadTime, Color color, int magSize, int length, Main main)
    {
        this.name = name;
        this.dmg = dmg;
        this.fireRate = fireRate;
        this.fireSounds = fireSounds;
        this.reloadSound = reloadSound;
        this.emptyReloadSound = emptyReloadSound;
        this.reloadTime = reloadTime;
        this.emptyReloadTime = emptyReloadTime;
        this.color = color;
        this.magSize = magSize;
        this.length = length;
        ammo = magSize + 1;
        reloading = false;
        this.main = main;

    }

    public void fire()
    {
        System.out.println("fired. ammo = " + ammo);
        if(ammo > 0)
        {
            try {
                Clip clip = AudioSystem.getClip();
                int temp = (int)(Math.random()*fireSounds.length);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fireSounds[temp]));
                clip.open(inputStream);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(20f * (float) Math.log10(main.getGunVolume()));
                clip.start();
            } catch (Exception e) {}
            ammo--;
        }
        else reload(false);
    }

    /**
    reload the weapon. if finish is false, then it plays the reload sound and starts the reload timer. if finish is true, then it finishes reloading and adds ammo
     */
    public void reload(boolean finish)
    {
        if(reloading) return;
        if(ammo == 0)
        {
            if(finish) ammo = magSize;
            else
            {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(emptyReloadSound));
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(main.getGunVolume()));
                    clip.start();
                    reloadWait(emptyReloadTime, this);
                } catch (Exception e) {}
            }
        }
        else
        {
            if(finish) ammo = magSize + 1;
            else
            {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(reloadSound));
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(20f * (float) Math.log10(main.getGunVolume()));
                    clip.start();
                    reloadWait(reloadTime, this);
                } catch (Exception e) {}
            }
        }
    }

    public void reloadWait(int millis, Weapon weapon)
    {
        new Thread(() -> {

            try {
                reloading = true;
                Thread.sleep(millis);
                System.out.println("joe");
                reloading = false;
                weapon.reload(true);
            } catch (InterruptedException e) {}

        }).start();

    }

    public Color getColor() {return color;}

    public int getLength() {return length;}

    public String[] getFireSounds() {
        return fireSounds;
    }

    public String getReloadSound() {
        return reloadSound;
    }

    public String getEmptyReloadSound() {
        return emptyReloadSound;
    }

    public int getFireRate() {
        return fireRate;
    }
}
