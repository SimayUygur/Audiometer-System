package logic;

/**
 *Bu sınıf yan etkisi olmayan (pure) bir fonksiyondur.
 * 'down 10, up 5' kuralının testleri yapılabilir.
 */
public class HughsonWestlake {
    public int updateDb(int currentDb, boolean heard) {
        if (heard) {
            return currentDb - 10; // Duyarsa 10 azalt[cite: 10]
        } else {
            return currentDb + 5;  // Duymazsa 5 artır[cite: 10]
        }
    }
}