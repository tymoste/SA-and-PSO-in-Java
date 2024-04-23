import java.util.Random;
import java.util.function.BiFunction;

public class ParticleSwarmOptimization {

    public static final int SWARM_SIZE = 30;

    private static final double MIN_X = -5.0;
    private static final double MAX_X = 5.0;

    static class Particle {
        double x;
        double y;
        double velocityX;
        double velocityY;
        double bestX;
        double bestY;
        double bestFitness;
        BiFunction<Double, Double, Double> userFunction;

        Particle(double x, double y, BiFunction<Double, Double, Double> objectiveFunction) {
            this.x = x;
            this.y = y;
            this.velocityX = new Random().nextDouble();
            this.velocityY = new Random().nextDouble();
            this.bestX = x;
            this.bestY = y;
            this.bestFitness = objectiveFunction.apply(x, y);
            this.userFunction = objectiveFunction;
        }
    }

    static void updateParticle(Particle particle, double[] globalBest, double inertiaWeight, double cognitiveWeight, double socialWeight) {
        Random random = new Random();

        double r1 = random.nextDouble();
        double r2 = random.nextDouble();

        double inertiaX = inertiaWeight * particle.velocityX;
        double cognitiveX = cognitiveWeight * r1 * (particle.bestX - particle.x);
        double socialX = socialWeight * r2 * (globalBest[0] - particle.x);

        particle.velocityX = inertiaX + cognitiveX + socialX;
        particle.x += particle.velocityX;
        particle.x = Math.min(Math.max(particle.x, MIN_X), MAX_X);

        double r3 = random.nextDouble();
        double r4 = random.nextDouble();

        double inertiaY = inertiaWeight * particle.velocityY;
        double cognitiveY = cognitiveWeight * r3 * (particle.bestY - particle.y);
        double socialY = socialWeight * r4 * (globalBest[1] - particle.y);
        particle.velocityY = inertiaY + cognitiveY + socialY;
        particle.y += particle.velocityY;
        particle.y = Math.min(Math.max(particle.y, MIN_X), MAX_X);
        double currentFitness = particle.userFunction.apply(particle.x, particle.y);
        if (currentFitness < particle.bestFitness) {
            particle.bestX = particle.x;
            particle.bestY = particle.y;
            particle.bestFitness = currentFitness;
        }
    }

    static double[] findGlobalBest(Particle[] swarm) {
        double[] globalBest = {Double.MAX_VALUE, Double.MAX_VALUE};
        double globalFitness = Double.MAX_VALUE;

        for (Particle particle : swarm) {
            if (particle.bestFitness < globalFitness) {
                globalBest[0] = particle.bestX;
                globalBest[1] = particle.bestY;
                globalFitness = particle.bestFitness;
            }
        }

        return globalBest;
    }
}
