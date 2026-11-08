package io.github.coderodde.statistics.run;

import java.time.Duration;

/**
 * This record class encapsulates the run statistics.
 */
public final record RunStatistics(Duration minimumDuration,
                                  Duration maximumDuration,
                                  Duration meanDuration,
                                  Duration medianDuration,
                                  Duration standardDeviation,
                                  ConfidenceInterval confidenceInterval) {
    
    @Override
    public String toString() {
        return ("min = %.6f seconds, max = %.6f seconds, " +
                "mean = %.6f seconds, median = %.6f seconds, " + 
                "sd = %.6f seconds, significance level = %.3f, " + 
                "confidence interval = [%.6f, %.6f] seconds")
                .formatted(
                        durationToSeconds(minimumDuration),
                        durationToSeconds(maximumDuration),
                        durationToSeconds(meanDuration), 
                        durationToSeconds(medianDuration),
                        durationToSeconds(standardDeviation),
                        confidenceInterval.significanceLevel().value(),
                        seconds(confidenceInterval.lo()),
                        seconds(confidenceInterval.hi()));
    }
    
    private static double durationToSeconds(Duration d) {
        return d.toNanos() / 1E9;
    }
    
    /**
     * Converts the number of nanoseconds to a string.
     * 
     * @param nanoseconds the number of nanoseconds.
     * 
     * @return the string representation.
     */
    private static double seconds(double nanoseconds) {
        return nanoseconds / 1E9;
    }
}
