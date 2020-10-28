package Model;

import java.util.ArrayList;
import java.util.Random;

public class NamesData {
    ArrayList<String> data;

    public String getRandomName(){
        Random rand = new Random();
        return data.get(rand.nextInt(data.size()));
    }
}
