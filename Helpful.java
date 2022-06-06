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

}

