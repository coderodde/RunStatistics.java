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
        return "min = %.6f seconds, max = %.6f seconds, " +
               "mean = %.6f seconds, median = %.6f seconds, " + 
               "sd = %.6f seconds, confidence interval " + 
               "with significance level %f = %s"
                .formatted(
                        seconds(minimumDuration),
                        seconds(maximumDuration),
                        seconds(meanDuration), 
                        seconds(medianDuration),
                        seconds(standardDeviation),
                        confidenceInterval.significanceLevel(),
                        confidenceInterval);
    }
    
    private static double seconds(double d) {
        return d / 1E9;
    }
}
