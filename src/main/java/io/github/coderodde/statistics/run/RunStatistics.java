package io.github.coderodde.statistics.run;

/**
 * This record class encapsulates the run statistics.
 */
public final record RunStatistics(double minimumDuration,
                                  double maximumDuration,
                                  double meanDuration,
                                  double medianDuration,
                                  double standardDeviation,
                                  ConfidenceInterval confidenceInterval) {
    
    @Override
    public String toString() {
        return ("min = %.6f seconds, max = %.6f seconds, " +
                "mean = %.6f seconds, median = %.6f seconds, " + 
                "sd = %.6f seconds, significance level = %.3f, " + 
                "confidence interval = [%.6f, %.6f] seconds")
                .formatted(
                        seconds(minimumDuration),
                        seconds(maximumDuration),
                        seconds(meanDuration), 
                        seconds(medianDuration),
                        seconds(standardDeviation),
                        confidenceInterval.significanceLevel().value(),
                        seconds(confidenceInterval.lo()),
                        seconds(confidenceInterval.hi()));
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
