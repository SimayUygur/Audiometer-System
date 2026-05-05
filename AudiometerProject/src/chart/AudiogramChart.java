package chart;

import model.Audiogram;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class AudiogramChart {

    private XYSeries rightSeries = new XYSeries("Right Ear");
    private XYSeries leftSeries = new XYSeries("Left Ear");

    private XYSeriesCollection dataset = new XYSeriesCollection();

    private JFreeChart chart;

    public AudiogramChart() {
        dataset.addSeries(rightSeries);
        dataset.addSeries(leftSeries);
    }

    public JPanel createChartPanel() {

        chart = ChartFactory.createScatterPlot(
                "Audiogram",
                "Frequency (Hz)",
                "Hearing Level (dB)",
                dataset
        );

        XYPlot plot = chart.getXYPlot();

        //dB ekseni ters
        plot.getRangeAxis().setInverted(true);

        //Custom renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);

        //Sağ kulak → O (circle)
        Shape circle = new Ellipse2D.Double(-5, -5, 10, 10);
        renderer.setSeriesShape(0, circle);
        renderer.setSeriesPaint(0, Color.RED);

        //Sol kulak → X (cross)
        Shape cross = createCrossShape(5);
        renderer.setSeriesShape(1, cross);
        renderer.setSeriesPaint(1, Color.BLUE);

        plot.setRenderer(renderer);

        return new ChartPanel(chart);
    }

    // ✖ X shape oluşturma
    private Shape createCrossShape(int size) {
        int s = size;
        GeneralPath path = new GeneralPath();
        path.moveTo(-s, -s);
        path.lineTo(s, s);
        path.moveTo(-s, s);
        path.lineTo(s, -s);
        return path;
    }

    public void update(Audiogram audiogram) {

        rightSeries.clear();
        leftSeries.clear();

        audiogram.getRightEar().forEach((f, db) -> {
            rightSeries.add(f, db);
        });

        audiogram.getLeftEar().forEach((f, db) -> {
            leftSeries.add(f, db);
        });
    }
}
