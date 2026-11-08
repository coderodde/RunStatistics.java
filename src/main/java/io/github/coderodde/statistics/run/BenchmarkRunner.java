package io.github.coderodde.statistics.run;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class provides methods for obtaining the running time statistics.
 */
public final class BenchmarkRunner {
   
    private static final int MINIMUM_ITERATIONS = 5;
    
    /**
     * Stores the actual measurements in nanoseconds.
     */
    private final List<Long> measurements = new ArrayList<>();
    private final SignificanceLevel significanceLevel;
    
    public BenchmarkRunner(SignificanceLevel significanceLevel) {
        this.significanceLevel = 
            Objects.requireNonNull(
                significanceLevel, 
                "The input significance level is null.");
    }
    
    public void measure(Runnable runnable, int iterations) {
        Objects.requireNonNull(runnable, "The input runnable is null.");
        checkIterations(iterations);
        
        for (int i = 0; i < iterations; ++i) {
            measurements.add(measure(runnable));
        }
    }
    
    public void measure(List<Runnable> runnables) {
        Objects.requireNonNull(runnables, "The input runnable list is null.");
        
        checkIterations(runnables.size());
        
        for (Runnable r : runnables) {
            measurements.add(measure(r));
        }
    }
    
    /**
     * Processes and returns a statistical summary of the benchmarking.
     * 
     * @return the record describing all the gathered statistics. 
     */
    public BenchmarkSummary getBenchmarkSummary() {
        double meanDuration = 0.0;
        
        Collections.sort(measurements);
        
        long minimumDuration = measurements.getFirst();
        long maximumDuration = measurements.getLast();
        
        meanDuration /= measurements.size();
        
        long medianDuration      = Median.of(measurements);
        double standardDeviation = StandardDeviation.of(measurements, 
                                                        meanDuration);
        
        ConfidenceInterval confidenceInterval = 
            ConfidenceInterval.of(measurements, 
                                  significanceLevel);
        
        return new BenchmarkSummary(Duration.ofNanos(minimumDuration),
                                    Duration.ofNanos(maximumDuration),
                                    Duration.ofNanos((long) meanDuration), 
                                    Duration.ofNanos(medianDuration),
                                    Duration.ofNanos((long) standardDeviation),
                                    confidenceInterval);
    }
    
    /**
     * The actual method for measuring how much time passes in {@code runnable}.
     * 
     * @param runnable the runnable whose duration to measure.
     * 
     * @return the number of nanoseconds spent in the runnable.
     */
    private static long measure(final Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return System.nanoTime() - start;
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
}
