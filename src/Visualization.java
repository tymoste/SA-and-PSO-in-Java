import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.function.BiFunction;

public class Visualization {
    private static void createAndShowChart(XYSeriesCollection dataset, String title) {
        JFreeChart chart = ChartFactory.createScatterPlot(
                title,
                "X",
                "Y",
                dataset
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        plot.setRenderer(renderer);

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();

        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        xAxis.setNumberFormatOverride(decimalFormat);
        yAxis.setNumberFormatOverride(decimalFormat);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void printResult(String algorithm, XYSeries series, BiFunction<Double, Double, Double> fun) {
        System.out.println(algorithm + " Result: x = " + series.getX(0) + ", y = " + series.getY(0) +
                ", f(x, y) = " + fun.apply(series.getX(0).doubleValue(), series.getY(0).doubleValue()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            double maxX = 0.0;
            double minX = 0.0;
            System.out.println("Enter bounds");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter minX: ");
            minX = scanner.nextDouble();

            System.out.print("Enter maxX: ");
            maxX = scanner.nextDouble();

            System.out.println("Enter the expression for the function (use 'x' and 'y' as variables):");
            String expression = new Scanner(System.in).nextLine();
            UserFunction userFunction = new UserFunction(expression);

            SAAlgorithm saAlgorithm = new SAAlgorithm();
            XYSeries saSeries = new XYSeries("SA Path");
            Timer userTimer = new Timer(1, null);

            saAlgorithm.plotPath(saSeries, userTimer, userFunction.getBiFunction(), minX, maxX);

            createAndShowChart(new XYSeriesCollection(saSeries), "SA Path");

            PSOAlgorithm psoAlgorithm = new PSOAlgorithm();
            XYSeries psoSeries = new XYSeries("PSO Path");
            Timer psoTimer = new Timer(1, null);

            psoAlgorithm.plotPath(psoSeries, psoTimer, 750, userFunction.getBiFunction(), minX, maxX);

            createAndShowChart(new XYSeriesCollection(psoSeries), "PSO Path");
        });
    }
}
