package Model;

import java.util.ArrayList;
import java.util.Random;

public class LocationData {
    ArrayList<Location> data;

    public Location getRandomLocation(){
        Random rand = new Random();
        return data.get(rand.nextInt(data.size()));
    }
}
