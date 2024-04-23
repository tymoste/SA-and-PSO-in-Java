import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiFunction;

public class SAAlgorithm {

    public void plotPath(XYSeries saSeries, Timer timer, BiFunction<Double, Double, Double> objectiveFunction, double minX, double maxX) {
        final double[] currentX = {minX};
        final double[] currentY = {minX};
        final double[] temperature = {1000};
        double coolingRate = 0.995;
        timer.addActionListener(new ActionListener() {
            int stepCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (temperature[0] <= 1) {
                    timer.stop();
                    Visualization.printResult("SA", saSeries, objectiveFunction);
                    return;
                }

                double newX = currentX[0] + (Math.random() - 0.5) * 0.1;
                double newY = currentY[0] + (Math.random() - 0.5) * 0.1;

                newX = Math.min(Math.max(newX, minX), maxX);
                newY = Math.min(Math.max(newY, minX), maxX);

                double newEnergy = objectiveFunction.apply(newX, newY);
                double currentEnergy = objectiveFunction.apply(currentX[0], currentY[0]);
                double energyDiff = newEnergy - currentEnergy;

                if (energyDiff < 0 || Math.random() < Math.exp(-energyDiff / temperature[0])) {
                    currentX[0] = newX;
                    currentY[0] = newY;
                }
                saSeries.clear();
                saSeries.add(currentX[0], currentY[0]);
                temperature[0] *= coolingRate;
                stepCount++;
                if (stepCount % 250 == 0) {
                    saSeries.add(currentX[0], currentY[0]);
                }
            }
        });

        timer.start();
    }
}
