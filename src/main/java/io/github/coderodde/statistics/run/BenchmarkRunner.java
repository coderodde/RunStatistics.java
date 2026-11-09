package io.github.coderodde.statistics.run;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class provides methods for obtaining the running time statistics.
 */
public final class BenchmarkRunner {
    
    /**
     * Stores the actual measurements in nanoseconds.
     */
    private final List<Long> measurements = new ArrayList<>();
    
    /**
     * Stores the significance level.
     */
    private final SignificanceLevel significanceLevel;
    
    /**
     * Constructs this benchmark runner.
     * 
     * @param significanceLevel the significance level to use for computing the
     *                          the confidence interval.
     */
    public BenchmarkRunner(SignificanceLevel significanceLevel) {
        this.significanceLevel = 
            Objects.requireNonNull(
                significanceLevel, 
                "The input significance level is null.");
    }
    
    /**
     * Measure how much time it takes to run only one {@code runnable}.
     * 
     * @param runnable the runnable to measure.
     */
    public void measure(Runnable runnable) {
        measure(runnable, 1);
    }
    
    /**
     * Measure how much time it takes to run {@code runnable} {@code iterations}
     * times.
     * 
     * @param runnable   the runnable to measure.
     * @param iterations the number of iterations.
     */
    public void measure(Runnable runnable, int iterations) {
        Objects.requireNonNull(runnable, "The input runnable is null.");
        
        for (int i = 0; i < iterations; ++i) {
            measurements.add(measureImpl(runnable));
        }
    }
    
    /**
     * Measure the duration of all the runnables in the list.
     * 
     * @param runnables the list of runnables to measure.
     */
    public void measure(List<Runnable> runnables) {
        Objects.requireNonNull(runnables, "The input runnable list is null.");
        
        for (Runnable r : runnables) {
            measurements.add(measureImpl(r));
        }
    }
    
    /**
     * Processes and returns a statistical summary of the benchmarking.
     * 
     * @return the record describing all the gathered statistics. 
     */
    public BenchmarkSummary getBenchmarkSummary() {
        StatisticalSample sample = 
            new StatisticalSample(measurements, 
                                  significanceLevel);
        
        ConfidenceInterval confidenceInterval = ConfidenceInterval.of(sample);
        
        return new BenchmarkSummary(Duration.ofNanos(sample.min()),
                                    Duration.ofNanos(sample.max()),
                                    Duration.ofNanos((long) sample.mean()), 
                                    Duration.ofNanos((long) sample.median()),
                                    Duration.ofNanos((long) sample.std()),
                                    confidenceInterval);
    }
    
    /**
     * The actual method for measuring how much time passes in {@code runnable}.
     * 
     * @param runnable the runnable whose duration to measure.
     * 
     * @return the number of nanoseconds spent in the runnable.
     */
    private static long measureImpl(final Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        return System.nanoTime() - start;
    }
}
