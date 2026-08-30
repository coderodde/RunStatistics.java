package io.github.coderodde.statistics.run;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class provides methods for obtaining the running time statistics.
 */
public final class Runner {
    
    private static final int MINIMUM_ITERATIONS = 5;

    /**
     * Measures the duration of {@code runnable} {@code iterations} times with
     * given significance level for the confidence interval.
     * 
     * @param runnable          the runnable to measure.
     * @param iterations        the number of times to iterate the runner.
     * @param significanceLevel the significance level to use.
     * 
     * @return the record describing all the gathered statistics. 
     */
    public static RunStatistics measure(final Runnable runnable, 
                                        final int iterations,
                                        final double significanceLevel) {
        
        Objects.requireNonNull(runnable, "The input runnable is null");
        checkIterations(iterations);
        checkSignificanceLevel(significanceLevel);
        
        double meanDuration = 0.0;
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
        
        final double medianDuration    = Utils.median(durations);
        final double standardDeviation = Utils.std(durations, meanDuration);
        
        final ConfidenceInterval confidenceInterval = 
            ConfidenceInterval.of(durations, significanceLevel);
        
        return new RunStatistics(minimumDuration,
                                 maximumDuration,
                                 meanDuration, 
                                 medianDuration,
                                 standardDeviation,
                                 confidenceInterval);
    }
    
    /**
     * Measures the duration of all runnables in the {@code runnables} with
     * given significance level for the confidence interval.
     * 
     * @param runnables         the list of runnables to measure.
     * @param significanceLevel the significance level to use.
     * 
     * @return the record describing all the gathered statistics. 
     */
    public static RunStatistics measure(final List<Runnable> runnables,
                                        final double significanceLevel) {
        
        Objects.requireNonNull(runnables, "The input runnables is null");
        
        if (runnables.isEmpty()) {
            throw new IllegalArgumentException("Nothing to measure");
        }
        
        checkSignificanceLevel(significanceLevel);
        
        double meanDuration = 0.0;
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
        
        final double medianDuration    = Utils.median(durations);
        final double standardDeviation = Utils.std(durations, meanDuration);
        
        final ConfidenceInterval confidenceInterval = 
            ConfidenceInterval.of(durations, significanceLevel);
        
        return new RunStatistics(minimumDuration,
                                 maximumDuration,
                                 meanDuration, 
                                 medianDuration,
                                 standardDeviation,
                                 confidenceInterval);
        
    }
    
    /**
     * The actual method for measuring how much time passes in {@code runnable}.
     * 
     * @param runnable the runnable whose duration to measure.
     * 
     * @return the number of nanoseconds spent in the runnable.
     */
    private static double measure(final Runnable runnable) {
        final long ta = System.nanoTime();
        runnable.run();
        final long tb = System.nanoTime();
        final long duration = tb - ta;
        return duration;
    }
    
    /**
     * Checks that the number of iterations is sufficient.
     * 
     * @param iterations the number of iterations.
     */
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
    
    /**
     * Checks that the input significance level is reasonable.
     * 
     * @param significanceLevel the significance level to check.
     */
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
