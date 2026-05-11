package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Audiogram {

    private final Map<Integer, Integer> rightEar;
    private final Map<Integer, Integer> leftEar;

    // Başlangıç durumu için boş constructor
    public Audiogram() {
        this.rightEar = Collections.emptyMap();
        this.leftEar = Collections.emptyMap();
    }

    // Sınıf içinden yeni kopyalar üretmek için kullanılan private constructor
    private Audiogram(Map<Integer, Integer> rightEar, Map<Integer, Integer> leftEar) {
        // Dışarıdan müdahaleyi kesin olarak engellemek için Unmodifiable (Değiştirilemez) yapıyoruz
        this.rightEar = Collections.unmodifiableMap(rightEar);
        this.leftEar = Collections.unmodifiableMap(leftEar);
    }

    /**
     * MEVCUT NESNEYİ DEĞİŞTİRMEZ.
     * Onun yerine, eski verileri kopyalayıp yeni veriyi ekleyerek YEPYENİ bir Audiogram nesnesi döndürür.
     */
    public Audiogram addResult(int freq, int db, boolean isRight) {
        // Eski verilerin kopyasını al
        Map<Integer, Integer> newRight = new HashMap<>(this.rightEar);
        Map<Integer, Integer> newLeft = new HashMap<>(this.leftEar);

        // İlgili kulağa yeni frekans ve desibel (eşik) değerini ekle
        if (isRight) {
            newRight.put(freq, db);
        } else {
            newLeft.put(freq, db);
        }
        
        // Yepyeni bir nesne olarak geri döndür
        return new Audiogram(newRight, newLeft);
    }

    // Getter metodları
    public Map<Integer, Integer> getRightEar() { return rightEar; }
    public Map<Integer, Integer> getLeftEar() { return leftEar; }
}