import java.io.File;

public abstract class Helpful
{
    //a helpful helper class (:

    /**
     * Centers a string in a given amount of character width
     * @param str String to center
     * @param width Width, in characters, to center str in
     * @return String centered in width spaces
     */
    public static String centerString(String str, int width) //was originally going to be used in EndPanel but ended up using GridBagLayout which does this automatically lol
    {
        int spaceStart = (width - str.length()) / 2 + str.length();
        String ret = String.format("%" + spaceStart + "s", str);
        ret = String.format("%-" + width + "s", ret);
        return ret;
    }

    public static String[] getMaps()
    {
        String[] maps = new File("maps").list();
        String[] ret = new String[maps.length - 2];
        int retI = 0;
        for(int i = 0; i < maps.length; i++)
        {
            maps[i] = maps[i].substring(0, maps[i].length() - 4);
            if(!maps[i].equals("HowToMakeMaps") && !maps[i].equals("maptest"))
            {
                ret[retI] = maps[i];
                retI ++;
            }
        }
        return ret;
    }

    public static double roundToTenths(double d)
    {

        return Math.round(d*10)/10.0;
    }

}

