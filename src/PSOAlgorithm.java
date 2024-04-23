import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiFunction;

public class PSOAlgorithm {

    public void plotPath(XYSeries psoSeries, Timer timer, int steps, BiFunction<Double, Double, Double> objectiveFunction, double min_x, double max_x) {
        ParticleSwarmOptimization.Particle[] swarm = new ParticleSwarmOptimization.Particle[ParticleSwarmOptimization.SWARM_SIZE];
        double[] globalBest = {min_x, max_x};

        for (int i = 0; i < ParticleSwarmOptimization.SWARM_SIZE; i++) {
            double initialX = min_x + Math.random() * (max_x - min_x);
            double initialY = min_x + Math.random() * (max_x - min_x);
            swarm[i] = new ParticleSwarmOptimization.Particle(initialX, initialY, objectiveFunction);
        }

        double inertiaWeight = 0.729;
        double c1 = 1.49445;
        double c2 = 1.49445;

        timer.addActionListener(new ActionListener() {
            int stepCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepCount >= steps) {
                    timer.stop();
                    updatePSOPath(psoSeries, swarm, globalBest, objectiveFunction);
                    Visualization.printResult("PSO", psoSeries, objectiveFunction);
                    return;
                }
                double[] currentGlobalBest = ParticleSwarmOptimization.findGlobalBest(swarm);
                if (objectiveFunction.apply(currentGlobalBest[0], currentGlobalBest[1]) < objectiveFunction.apply(globalBest[0],
                        globalBest[1])) {
                    globalBest[0] = currentGlobalBest[0];
                    globalBest[1] = currentGlobalBest[1];
                }
                psoSeries.clear();
                for (ParticleSwarmOptimization.Particle particle : swarm) {
                    psoSeries.add(particle.x, particle.y);
                }
                for (ParticleSwarmOptimization.Particle particle : swarm) {
                    ParticleSwarmOptimization.updateParticle(particle, globalBest, inertiaWeight, c1, c2);
                }
                stepCount++;
                if (stepCount % 10 == 0) {
                    psoSeries.add(globalBest[0], globalBest[1]);
                }
            }
        });

        timer.start();
    }

    private void updatePSOPath(XYSeries psoSeries, ParticleSwarmOptimization.Particle[] swarm, double[] globalBest, BiFunction<Double, Double, Double> objectiveFunction) {
        psoSeries.clear();
        for (ParticleSwarmOptimization.Particle particle : swarm) {
            psoSeries.add(particle.x, particle.y);
        }

        double[] currentGlobalBest = ParticleSwarmOptimization.findGlobalBest(swarm);
        if (objectiveFunction.apply(currentGlobalBest[0], currentGlobalBest[1]) < objectiveFunction.apply(globalBest[0], globalBest[1])) {
            globalBest[0] = currentGlobalBest[0];
            globalBest[1] = currentGlobalBest[1];
        }

        psoSeries.add(globalBest[0], globalBest[1]);
    }
}
