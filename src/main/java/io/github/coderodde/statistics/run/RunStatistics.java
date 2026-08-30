package io.github.coderodde.statistics.run;

import java.text.NumberFormat;

/**
 * This record class encapsulates the run statistics.
 */
public final record RunStatistics(double minimumDuration,
                                  double maximumDuration,
                                  double meanDuration,
                                  double medianDuration,
                                  double standardDeviation,
                                  ConfidenceInterval confidenceInterval) {
    
    private static final NumberFormat NUMBER_FORMAT = 
        NumberFormat.getInstance();
    
    @Override
    public String toString() {
        return ("min = %s seconds, max = %s seconds, " +
                "mean = %s seconds, median = %s seconds, " + 
                "sd = %s seconds, confidence interval " + 
                "with significance level %s = [%s, %s] seconds")
                .formatted(
                        seconds(minimumDuration),
                        seconds(maximumDuration),
                        seconds(meanDuration), 
                        seconds(medianDuration),
                        seconds(standardDeviation),
                        NUMBER_FORMAT.format(
                            confidenceInterval.significanceLevel()),
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
    private static String seconds(double nanoseconds) {
        return NUMBER_FORMAT.format(nanoseconds / 1E9);
    }
}
