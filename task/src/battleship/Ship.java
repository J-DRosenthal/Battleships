package battleship;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final String name;
    private final int length;
    private List<List<Integer>> coordinates;
    private boolean destroyed;



    Ship(String name, int length) {
        this.name = name;
        this.length = length;
        this.coordinates = new ArrayList<>();
        this.destroyed = false;
    }

    int getLength() {
        return length;
    }

    String getName() {
        return name;
    }

    public List<List<Integer>> getCoordinates() {
        return coordinates;
    }

    void addCoordinate(List<Integer> coordinate) {
        this.coordinates.add(coordinate);
    }

    boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }


}
