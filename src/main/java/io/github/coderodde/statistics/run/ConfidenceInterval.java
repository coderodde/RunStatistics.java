package io.github.coderodde.statistics.run;

import org.apache.commons.statistics.distribution.TDistribution;

import java.util.List;
import java.util.Objects;

/**
 * This class implements confidence intervals for data with unknown mean and
 * variance.
 */
public final class ConfidenceInterval {
    
    private final double lo;
    private final double hi;
    private final double significanceLevel;
    
    private ConfidenceInterval(double lo, double hi, double confidenceLevel) {
        this.lo = lo;
        this.hi = hi;
        this.significanceLevel = confidenceLevel;
    }
    
    public double lo() {
        return lo;
    }
    
    public double hi() {
        return hi;
    }
    
    public double significanceLevel() {
        return significanceLevel;
    }
    
    /**
     * This method computes the {@code alpha}-confidence interval for the input
     * data {@code data} where mean and variance are not known.
     * 
     * @param data              the input data.
     * @param significanceLevel the significance level.
     * 
     * @return the confidence interval object.
     */
    public static ConfidenceInterval of(List<Double> data, double significanceLevel) {
        validateData(data);
        validateAlpha(significanceLevel);
        
        double ybar = Utils.mean(data);
        double s    = Utils.std(data, ybar);
        int    n    = data.size();
        
        // Get t-distribution (Student's distribution) with degree of freedom
        // n - 1:
        TDistribution tdist = TDistribution.of(n - 1);
        
        // LaTeX: t_{n - 1}(\alpha / 2):
        double tAlpha = tdist.inverseCumulativeProbability(
            1.0 -  significanceLevel / 2.0);
        
        double intervalHalfDistance = tAlpha * s / Math.sqrt(n);
        
        return new ConfidenceInterval(ybar - intervalHalfDistance, 
                                      ybar + intervalHalfDistance,
                                      significanceLevel);
    }
    
    /**
     * Makes sure that the input data is valid.
     * 
     * @param data the input data to validate.
     * @throws IllegalArgumentException if data list has less than 2 elements, 
     *         or one of elements is {@code null} or is not a real number.
     */
    private static void validateData(List<Double> data)
            throws IllegalArgumentException {
        
        Objects.requireNonNull(data, "The input data is null.");
        
        if (data.size() < 2) {
            throw new IllegalArgumentException(
                String.format(
                    "The input data set is too small: %d elements. " + 
                    "Must be at least 2.", 
                        data.size()));
        }
        
        int index = 0;
        
        for (Double y : data) {
            Objects.requireNonNull(
                y,
                String.format("The input datum at index %d is null.", index));
            
            if (y.isNaN()) {
                throw new IllegalArgumentException(
                    String.format("The input datum at index %d is NaN.", 
                                  index));
            }
            
            if (y.isInfinite()) {
                throw new IllegalArgumentException(
                    String.format(
                        "The input datum at index %d is infinite in " + 
                        "absolute value.",
                            index));
            }
            
            ++index;
        }
    }
    
    /**
     * Makes sure the alpha value is within range {@code (0, 1)} and is not NaN.
     * 
     * @param alpha the alpha confidence level to validate. 
     * @throws IllegalArgumentException if the alpha is NaN or outside the range
     *         {@code (0, 1)}.
     */
    private static void validateAlpha(double alpha) {
        if (Double.isNaN(alpha)) {
            throw new IllegalArgumentException("The input alpha is NaN.");
        }
        
        if (alpha <= 0.0 || alpha >= 1.0) {
            throw new IllegalArgumentException(
                String.format(
                    "The input alpha (%f) does not belong " + 
                        "to the interval (0, 1).",
                    alpha));
        }
    }
    
    @Override
    public String toString() {
        return "[%s, %s]".formatted(lo, hi);
    }
}
