package chart;

import model.Audiogram;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class AudiogramChart {

    private XYSeries rightSeries = new XYSeries("Right Ear");
    private XYSeries leftSeries = new XYSeries("Left Ear");
    private XYSeriesCollection dataset = new XYSeriesCollection();
    private JFreeChart chart;

    // Frekansların string etiketleri (Eksende eşit aralıkla göstermek için)
    private final String[] freqLabels = {"250", "500", "1000", "2000", "4000", "8000"};

    public AudiogramChart() {
        dataset.addSeries(rightSeries);
        dataset.addSeries(leftSeries);
    }

    public JPanel createChartPanel() {
        chart = ChartFactory.createScatterPlot(
                "Clinical Audiogram (IEC 60645-1 Standard)",
                "Frequency (Hz)",
                "Hearing Level (dB HL)",
                dataset
        );

        XYPlot plot = chart.getXYPlot();
        
        // dB ekseni ters (Odyogram standardı)
        plot.getRangeAxis().setInverted(true);
        plot.getRangeAxis().setRange(-10, 120);

        // 1. DÜZELTME: Frekans eksenini Lineer'den Eşit Aralık'lı Oktav (SymbolAxis) yapısına çeviriyoruz.
        SymbolAxis xAxis = new SymbolAxis("Frequency (Hz)", freqLabels);
        plot.setDomainAxis(xAxis);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);

        // Sağ Kulak (Seri 0) Ayarları: Kırmızı O
        Shape circle = new Ellipse2D.Double(-5, -5, 10, 10);
        renderer.setSeriesShape(0, circle);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesShapesFilled(0, false); // İçini boşaltır.
        renderer.setSeriesStroke(0, new BasicStroke(2.0f)); // Halkayı belirginleştirmek için kalınlaştırır

        // Sol Kulak (Seri 1) Ayarları: Mavi X
        Shape cross = createCrossShape(5);
        renderer.setSeriesShape(1, cross);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesShapesFilled(1, true); // X sembolü için dolgu fark etmez ama true kalabilir
        renderer.setSeriesStroke(1, new BasicStroke(2.0f)); // X işaretini de aynı kalınlığa getirir

        plot.setRenderer(renderer);
        return new ChartPanel(chart);
    }

    private Shape createCrossShape(int size) {
        GeneralPath path = new GeneralPath();
        path.moveTo(-size, -size); path.lineTo(size, size);
        path.moveTo(-size, size); path.lineTo(size, -size);
        return path;
    }

    // Frekansı eksen indeksine (0, 1, 2...) çeviren yardımcı metot
    private int getFreqIndex(int freq) {
        switch (freq) {
            case 250: return 0; case 500: return 1; case 1000: return 2;
            case 2000: return 3; case 4000: return 4; case 8000: return 5;
            default: return 0;
        }
    }

    public void update(Audiogram audiogram) {
        rightSeries.clear();
        leftSeries.clear();

        // 2. DÜZELTME: Verileri gerçek değerleriyle değil, eksen indeksleriyle grafiğe ekliyoruz.
        audiogram.getRightEar().forEach((f, db) -> rightSeries.add(getFreqIndex(f), db));
        audiogram.getLeftEar().forEach((f, db) -> leftSeries.add(getFreqIndex(f), db));
    }
}
