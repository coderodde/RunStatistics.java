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
                                        final int iterations) {
        
        Objects.requireNonNull(runnable, "The input runnable is null");
        checkIterations(iterations);
        
        double meanDuration = 0.0;
        double medianDuration;
        double standardDeviation;
        final List<Long> durations = new ArrayList<>(iterations);
        
        for (int iteration = 0;
                 iteration < iterations;
                 iteration++) {
            
            final long duration = measure(runnable);
            meanDuration += duration;
            durations.add(duration);
        }
        
        Collections.sort(durations);
        
        final long minimumDuration = durations.getFirst();
        final long maximumDuration = durations.getLast();
        
        meanDuration /= iterations;
        medianDuration = computeMedianDuration(durations);
        standardDeviation = computeStandardDeviation(durations, meanDuration);
        
        return new RunStatistics(minimumDuration,
                                 maximumDuration,
                                 meanDuration, 
                                 medianDuration,
                                 standardDeviation);
    }
    
    public static RunStatistics measure(final List<Runnable> runnables) {
        Objects.requireNonNull(runnables, "The input runnables is null");
        
        if (runnables.isEmpty()) {
            throw new IllegalArgumentException("Nothing to measure");
        }
        
        double meanDuration = 0.0;
        double medianDuration;
        double standardDeviation;
        final List<Long> durations = new ArrayList<>(runnables.size());
        
        for (final Runnable runnable : runnables) {
            
            final long duration = measure(runnable);
            
            meanDuration += duration;
            durations.add(duration);
        }
        
        Collections.sort(durations);
        
        long minimumDuration = durations.getFirst();
        long maximumDuration = durations.getLast();
        
        meanDuration /= runnables.size();
        medianDuration = computeMedianDuration(durations);
        standardDeviation = computeStandardDeviation(durations, meanDuration);
        
        return new RunStatistics(minimumDuration,
                                 maximumDuration,
                                 meanDuration, 
                                 medianDuration,
                                 standardDeviation);
        
    }
    
    private static long measure(final Runnable runnable) {
        final long ta = System.nanoTime();
        runnable.run();
        final long tb = System.nanoTime();
        final long duration = tb - ta;
        return duration;
    }
    
    private static double computeMedianDuration(final List<Long> durations) {
        if (durations.size() % 2 == 1) {
            return durations.get(durations.size() / 2);
        } else {
            final int index1 = durations.size() / 2;
            final int index2 = index1 - 1;
            
            return (durations.get(index1) + 
                    durations.get(index2)) / 2.0;
        }
    }
    
    private static double computeStandardDeviation(final List<Long> durations,
                                                   final double meanDuration) {
        double sum = 0.0;
        
        for (final Long duration : durations) {
            sum += Math.pow(duration - meanDuration, 2.0);
        }
        
        return Math.sqrt(sum / durations.size());
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
}
