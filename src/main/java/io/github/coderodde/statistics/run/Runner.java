package io.github.coderodde.statistics.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class provides methods for obtaining the running time statistics.
 * 
 * @author Rodion "rodde" Efremov
 */
public final class Runner {
    
    private static final int MINIMUM_ITERATIONS = 5;

    public static RunStatistics measure(final Runnable runnable, 
                                        final int iterations,
                                        final double significanceLevel) {
        
        Objects.requireNonNull(runnable, "The input runnable is null");
        checkIterations(iterations);
        checkSignificanceLevel(significanceLevel);
        
        double meanDuration = 0.0;
        double medianDuration;
        double standardDeviation;
        final List<Double> durations = new ArrayList<>(iterations);
        
        for (int iteration = 0;
                 iteration < iterations;
                 iteration++) {
            
            final double duration = measure(runnable);
            meanDuration += duration;
            durations.add(duration);
        }
        
        Collections.sort(durations);
        
        final double minimumDuration = durations.getFirst();
        final double maximumDuration = durations.getLast();
        
        meanDuration /= iterations;
        medianDuration    = Utils.median(durations);
        standardDeviation = Utils.std(durations, meanDuration);
        
        final ConfidenceInterval confidenceInterval = 
            ConfidenceInterval.of(durations, significanceLevel);
        
        return new RunStatistics(minimumDuration,
                                 maximumDuration,
                                 meanDuration, 
                                 medianDuration,
                                 standardDeviation,
                                 confidenceInterval);
    }
    
    public static RunStatistics measure(final List<Runnable> runnables,
                                        final double significanceLevel) {
        
        Objects.requireNonNull(runnables, "The input runnables is null");
        
        if (runnables.isEmpty()) {
            throw new IllegalArgumentException("Nothing to measure");
        }
        
        checkSignificanceLevel(significanceLevel);
        
        double meanDuration = 0.0;
        double medianDuration;
        double standardDeviation;
        final List<Double> durations = new ArrayList<>(runnables.size());
        
        for (final Runnable runnable : runnables) {
            
            final double duration = measure(runnable);
            meanDuration += duration;
            durations.add(duration);
        }
        
        Collections.sort(durations);
        
        final double minimumDuration = durations.getFirst();
        final double maximumDuration = durations.getLast();
        
        meanDuration /= runnables.size();
        medianDuration    = Utils.median(durations);
        standardDeviation = Utils.std(durations, meanDuration);
        
        final ConfidenceInterval confidenceInterval = 
            ConfidenceInterval.of(durations, significanceLevel);
        
        return new RunStatistics(minimumDuration,
                                 maximumDuration,
                                 meanDuration, 
                                 medianDuration,
                                 standardDeviation,
                                 confidenceInterval);
        
    }
    
    private static double measure(final Runnable runnable) {
        final long ta = System.nanoTime();
        runnable.run();
        final long tb = System.nanoTime();
        final long duration = tb - ta;
        return duration;
    }
    
    private static void checkIterations(final int iterations) {
        if (iterations < MINIMUM_ITERATIONS) {
            final String exceptionMessage = 
                    String.format("Number of iterations (%d) is too small. " + 
                                  "Must be at least %d.", 
                                  iterations,
                                  MINIMUM_ITERATIONS);
            
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
    
    private static void checkSignificanceLevel(final double significanceLevel) {
        if (Double.isNaN(significanceLevel)) {
            throw new IllegalArgumentException(
                "The input significance level is NaN.");
        }
        
        if (significanceLevel <= 0.0 || 1.0 <= significanceLevel) {
            throw new IllegalArgumentException(
                String.format(
                    "Bad significance level: %f. " + 
                    "Must be within interval (0, 1):",
                    significanceLevel));
        }
    }
}
