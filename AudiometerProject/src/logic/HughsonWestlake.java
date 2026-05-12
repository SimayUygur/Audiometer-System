package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HughsonWestlake {

    public static class Trial {
        private final int db;
        private final boolean heard;

        public Trial(int db, boolean heard) {
            this.db = db;
            this.heard = heard;
        }

        public int getDb() {
            return db;
        }

        public boolean isHeard() {
            return heard;
        }
    }

    public int updateDb(int currentDb, boolean heard) {
        if (heard) {
            return currentDb - 10;
        } else {
            return currentDb + 5;
        }
    }

    public Optional<Integer> determineThreshold(List<Trial> trials) {
        if (trials.size() < 3) {
            return Optional.empty();
        }

        Map<Integer, List<Trial>> ascendingTrialsByDb = collectAscendingTrials(trials);

        return ascendingTrialsByDb.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 3)
                .filter(entry -> entry.getValue().stream().filter(Trial::isHeard).count() >= 2)
                .sorted(Map.Entry.comparingByKey()) 
                .map(Map.Entry::getKey)
                .findFirst();
    }

    private Map<Integer, List<Trial>> collectAscendingTrials(List<Trial> trials) {
        List<Trial> ascendingTrials = new ArrayList<>();

        for (int i = 1; i < trials.size(); i++) {
            Trial previous = trials.get(i - 1);
            Trial current = trials.get(i);

            if (current.getDb() > previous.getDb()) {
                ascendingTrials.add(current);
            }
        }

        return ascendingTrials.stream()
                .collect(Collectors.groupingBy(Trial::getDb));
    }
}
