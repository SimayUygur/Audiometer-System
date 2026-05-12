package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Audiogram {

    private final Map<Integer, Integer> rightEar;
    private final Map<Integer, Integer> leftEar;

    public Audiogram() {
        this.rightEar = Collections.emptyMap();
        this.leftEar = Collections.emptyMap();
    }

    private Audiogram(Map<Integer, Integer> rightEar, Map<Integer, Integer> leftEar) {
        
        this.rightEar = Collections.unmodifiableMap(rightEar);
        this.leftEar = Collections.unmodifiableMap(leftEar);
    }

    public Audiogram addResult(int freq, int db, boolean isRight) {
        Map<Integer, Integer> newRight = new HashMap<>(this.rightEar);
        Map<Integer, Integer> newLeft = new HashMap<>(this.leftEar);

        if (isRight) {
            newRight.put(freq, db);
        } else {
            newLeft.put(freq, db);
        }

        return new Audiogram(newRight, newLeft);
    }

    public Map<Integer, Integer> getRightEar() { return rightEar; }
    public Map<Integer, Integer> getLeftEar() { return leftEar; }
}
