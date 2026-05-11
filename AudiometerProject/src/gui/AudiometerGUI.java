package gui;

import chart.AudiogramChart;
import logic.HughsonWestlake;
import model.Audiogram;
import serial.SerialHandler;
import util.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AudiometerGUI {

    private JFrame frame;
    private JComboBox<Integer> freqBox;
    private JTextField dbField;
    private JTextArea logArea;
    private JComboBox<String> earSelectBox;

    private SerialHandler serial;
    private HughsonWestlake algorithm;
    private Audiogram audiogram;
    private AudiogramChart chart;

    private int currentDb = 50;
    private int currentFreq = 1000;
    
    // Mevcut kulak/frekans için tüm deneme geçmişini tutar
    private final List<HughsonWestlake.Trial> currentTrials = new ArrayList<>();

    public AudiometerGUI() {
        serial = new SerialHandler();
        algorithm = new HughsonWestlake();
        audiogram = new Audiogram();
        chart = new AudiogramChart();

        // 1. UI bileşenleri her şeyden önce yüklenmeli (NPE Yarış Durumu Koruması)
        initUI();

        // 2. Olay Dinleyicisini (Callback) kur
        serial.setOnDataReceived(msg -> {
            SwingUtilities.invokeLater(() -> {
                onResponse(true); 
                log("Arduino (Donanım): Hasta butona bastı!");
            });
        });

        // 3. UI hazır olduktan sonra seri porta bağlan
        boolean connected = serial.connect(errorMsg -> log("DONANIM HATASI: " + errorMsg));
        if (connected) {
            log("Sistem -> Donanım entegrasyonu başarılı (COM2 hazır).");
        }
    }

    private void initUI() {
        frame = new JFrame("Clinical Audiometer Test Management");
        frame.setSize(950, 650);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        freqBox = new JComboBox<>(Constants.FREQUENCIES);
        dbField = new JTextField("50", 5);
        currentFreq = (int) freqBox.getSelectedItem();
        
        // Klinik olarak daha güvenli olan kulak seçimi (Checkbox yerine ComboBox)
        earSelectBox = new JComboBox<>(new String[]{"Right Ear (Red O)", "Left Ear (Blue X)"});

        JButton sendBtn = new JButton("Send Tone");
        // JButton simulateBtn = new JButton("Simulate: Heard");
        // JButton noResponseBtn = new JButton("No Response");

        // Frekans değiştiğinde eşik geçmişini sıfırla (Veri sızıntısını önler)
        freqBox.addActionListener(e -> {
            currentFreq = (int) freqBox.getSelectedItem();
            currentTrials.clear();
            log("Sistem -> Frekans değişti (" + currentFreq + " Hz). Yeni test döngüsü başlatıldı.");
        });

        // Kulak değiştiğinde eşik geçmişini sıfırla (Veri sızıntısını önler)
        earSelectBox.addActionListener(e -> {
            currentTrials.clear();
            log("Sistem -> Kulak değiştirildi. Test geçmişi sıfırlandı.");
        });

        sendBtn.addActionListener(e -> sendTone());
        // simulateBtn.addActionListener(e -> onResponse(true));
        // noResponseBtn.addActionListener(e -> onResponse(false)); // Geliştirici testi için kullanılabilir.

        topPanel.add(new JLabel("Freq:")); topPanel.add(freqBox);
        topPanel.add(new JLabel("dB HL:")); topPanel.add(dbField);
        topPanel.add(earSelectBox);
        topPanel.add(sendBtn);
        // topPanel.add(simulateBtn);
        // topPanel.add(noResponseBtn);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(chart.createChartPanel(), BorderLayout.CENTER);
        
        logArea = new JTextArea(6, 40);
        logArea.setEditable(false);
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void sendTone() {
        currentFreq = (int) freqBox.getSelectedItem();

        // Girdi Validasyonu (Sayı formatı ve dB Aralık kontrolü)
        try {
            int parsedDb = Integer.parseInt(dbField.getText());
            if (parsedDb < -10 || parsedDb > 120) {
                log("HATA: Geçersiz dB değeri! (-10 ile 120 arası olmalı)");
                return;
            }
            currentDb = parsedDb;
        } catch (NumberFormatException ex) {
            log("HATA: Lütfen tam sayı giriniz.");
            return;
        }

        String cmd = "FREQ:" + currentFreq + ";DB:" + currentDb;
        
        // Bağlantı durumuna göre gerçekçi bilgilendirme logu
        if (!serial.isOpen()) {
            log("UYARI: Donanım bağlantısı yok. (" + cmd + ") SİMÜLASYON modunda işleniyor.");
        } else {
            serial.send(cmd);
            log("Gönderildi -> Cihaz " + currentFreq + " Hz / " + currentDb + " dB veriyor.");
        }
    }

    private void onResponse(boolean heard) {
        currentTrials.add(new HughsonWestlake.Trial(currentDb, heard));

        if (heard) {
            log("Sistem -> Hasta duydu (" + currentDb + " dB). Hughson-Westlake klinik kuralı kontrol ediliyor...");

            Optional<Integer> clinicalThreshold = algorithm.determineThreshold(currentTrials);
            
            if (clinicalThreshold.isPresent()) {
                int finalDb = clinicalThreshold.get();
                boolean isRight = earSelectBox.getSelectedIndex() == 0;
                
                // Immutable veri modelini güncelle ve grafiğe bas
                this.audiogram = this.audiogram.addResult(currentFreq, finalDb, isRight);
                chart.update(this.audiogram);
                
                log(">>> BAŞARILI: " + currentFreq + " Hz için KLİNİK EŞİK BULUNDU: " + finalDb + " dB <<<");
                currentTrials.clear();
                
                return; // Eşik bulunduğunda algoritmanın o frekans için adım değiştirmesini durdur.
            }
        } else {
            log("Sistem -> Hasta duymadı (" + currentDb + " dB).");
        }

        // Eşik henüz doğrulanmadıysa adım değiştirmeye (down 10, up 5) devam et
        currentDb = algorithm.updateDb(currentDb, heard);
        dbField.setText(String.valueOf(currentDb));
        log("Sistem -> Teste devam. Önerilen yeni seviye: " + currentDb + " dB");
    }

    private void log(String msg) {
        if (logArea != null) {
            logArea.append(msg + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }
    }
}
