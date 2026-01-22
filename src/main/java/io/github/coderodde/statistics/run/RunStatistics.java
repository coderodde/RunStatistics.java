package io.github.coderodde.statistics.run;

/**
 * This record class encapsulates the run statistics.
 * 
 * @author Rodion "rodde" Efremov
 */
public final record RunStatistics(long minimumDuration,
                                  long maximumDuration,
                                  double meanDuration,
                                  double medianDuration,
                                  double standardDeviation) {
    
    @Override
    public String toString() {
        return "min = %.6f seconds, max = %.6f seconds, " +
               "mean = %.6f seconds, median = %.6f seconds, sd = %.6f seconds"
                .formatted(
                        seconds(minimumDuration),
                        seconds(maximumDuration),
                        seconds(meanDuration), 
                        seconds(medianDuration),
                        seconds(standardDeviation));
    }
    
    private static double seconds(long ns) {
        return ns / 1_000_000_000.0;
    }
    
    private static double seconds(double d) {
        return d / 1_000_000_000.0;
    }
}
