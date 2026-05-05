package gui;

import chart.AudiogramChart;
import logic.HughsonWestlake;
import model.Audiogram;
import serial.SerialHandler;
import util.Constants;

import javax.swing.*;
import java.awt.*;

public class AudiometerGUI {

    private JFrame frame;
    private JComboBox<Integer> freqBox;
    private JTextField dbField;
    private JTextArea logArea;
    private JCheckBox rightEarBox;

    private SerialHandler serial;
    private HughsonWestlake algorithm;
    private Audiogram audiogram;
    private AudiogramChart chart;

    private int currentDb = 50;
    private int currentFreq = 1000;

    public AudiometerGUI() {
        serial = new SerialHandler();
        algorithm = new HughsonWestlake();
        audiogram = new Audiogram();
        chart = new AudiogramChart();

        serial.connect();

        initUI();
        startListeningThread();
    }

    private void initUI() {
        frame = new JFrame("Audiometer");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();

        freqBox = new JComboBox<>(Constants.FREQUENCIES);
        dbField = new JTextField("50", 5);
        rightEarBox = new JCheckBox("Right Ear", true);

        JButton sendBtn = new JButton("Send Tone");
        JButton simulateBtn = new JButton("Simulate RESPONSE");

        logArea = new JTextArea(5, 40);

        sendBtn.addActionListener(e -> sendTone());
        simulateBtn.addActionListener(e -> onResponse(true));

        topPanel.add(new JLabel("Freq:"));
        topPanel.add(freqBox);
        topPanel.add(new JLabel("dB:"));
        topPanel.add(dbField);
        topPanel.add(rightEarBox);
        topPanel.add(sendBtn);
        topPanel.add(simulateBtn);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(chart.createChartPanel(), BorderLayout.CENTER);
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void sendTone() {
        currentFreq = (int) freqBox.getSelectedItem();
        currentDb = Integer.parseInt(dbField.getText());

        String cmd = "FREQ:" + currentFreq + ";DB:" + currentDb;
        serial.send(cmd);

        log("Sent → " + cmd);
    }

    private void onResponse(boolean heard) {
        currentDb = algorithm.updateDb(currentDb, heard);
        dbField.setText(String.valueOf(currentDb));

        boolean right = rightEarBox.isSelected();
        audiogram.addResult(currentFreq, currentDb, right);

        chart.update(audiogram);

        log("Updated threshold");
    }

    private void startListeningThread() {
        new Thread(() -> {
            while (true) {
                String data = serial.read();
                if (data != null && data.contains("RESPONSE")) {
                    SwingUtilities.invokeLater(() -> onResponse(true));
                }
            }
        }).start();
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
    }
}
