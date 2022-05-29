import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class MapReadWrite
{

    /**
     * Read wall data from a given text file
     * @param map the given text file
     * @return ArrayList of Walls generated from given text file
     */
    public static ArrayList<Wall> readWalls(String map)
    {
        ArrayList<Wall> ret = new ArrayList<>();
        Scanner readMap;
        try {
            readMap = new Scanner(new File("maps/" + map + ".txt"));
        } catch (FileNotFoundException e) {
            return null;
        }

        readMap.next(); //skip first line

        while(readMap.hasNext())
        {
            String temp = readMap.next();
            int x = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
            temp = temp.substring(temp.indexOf("-")+1);
            int y = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
            temp = temp.substring(temp.indexOf("-")+1);
            int width = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
            temp = temp.substring(temp.indexOf("-")+1);
            int height = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
            //System.out.println(x + " " + y + " " + width + " " + height);
            ret.add(new Wall(x, y, width, height));
        }

        readMap.close();
        return ret;
    }

    /**
     * Read border data from a given text file
     * @param map the given text file
     * @return Integer array of the width and height of the map and spawn coords
     */
    public static int[] readBorders(String map)
    {
        Scanner readMap;
        try {
            readMap = new Scanner(new File("maps/" + map + ".txt"));
        } catch (FileNotFoundException e) {
            return null;
        }

        String temp = readMap.nextLine();
        int width = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
        temp = temp.substring(temp.indexOf("-")+1);
        int height = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
        temp = temp.substring(temp.indexOf("-")+1);
        int spawnX = Integer.parseInt(temp.substring(0, temp.indexOf("-")));
        temp = temp.substring(temp.indexOf("-")+1);
        int spawnY = Integer.parseInt(temp.substring(0, temp.indexOf("-")));

        readMap.close();
        return new int[]{width, height, spawnX, spawnY};

    }

    /**
     * Write map data onto a given text file
     * @param map the given text file
     * @param walls the walls to save
     * @param width the width of the map
     * @param height the height of the map
     * @param spawnX the X of player spawn
     * @param spawnY the Y of player spawn
     */
    public static void writeMap(String map, ArrayList<Wall> walls, int width, int height, int spawnX, int spawnY)
    {
        String temp;
        File file = new File("maps/" + map + ".txt");
        try {
            temp = file.getAbsolutePath();
            file.delete();
        } catch (Exception e) {
            return;
        }
        file = new File(temp);

        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(width + "-" + height + "-" + spawnX + "-" + spawnY + "-\n");
            for(int i = 0; i < walls.size(); i++)
            {
                Wall w = walls.get(i);
                writer.write(w.getX() + "-" + w.getY() + "-" + w.getWidth() + "-" + w.getHeight() + "-\n");
            }
            writer.close();

        }catch(IOException e) {
            return;
        }
    }

}
