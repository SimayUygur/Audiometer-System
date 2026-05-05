package model;

import java.util.HashMap;

public class Audiogram {

    private HashMap<Integer, Integer> rightEar = new HashMap<>();
    private HashMap<Integer, Integer> leftEar = new HashMap<>();

    public void addResult(int freq, int db, boolean right) {
        if (right) {
            rightEar.put(freq, db);
        } else {
            leftEar.put(freq, db);
        }
    }

    public HashMap<Integer, Integer> getRightEar() {
        return rightEar;
    }

    public HashMap<Integer, Integer> getLeftEar() {
        return leftEar;
    }
}