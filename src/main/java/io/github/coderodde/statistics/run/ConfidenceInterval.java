package io.github.coderodde.statistics.run;

import org.apache.commons.statistics.distribution.TDistribution;

/**
 * This class implements confidence intervals for data with unknown mean and
 * variance.
 */
public final class ConfidenceInterval {
    
    private final double lo;
    private final double hi;
    private final SignificanceLevel significanceLevel;
    
    private ConfidenceInterval(double lo, 
                               double hi,
                               SignificanceLevel significanceLevel) {
        this.lo = lo;
        this.hi = hi;
        this.significanceLevel = significanceLevel;
    }
    
    public double lo() {
        return lo;
    }
    
    public double hi() {
        return hi;
    }
    
    public SignificanceLevel significanceLevel() {
        return significanceLevel;
    }
    
    /**
     * This method computes the {@code alpha}-confidence interval for the input
     * data {@code data} where mean and variance are not known.
     * 
     * @param statisticalSample
     * 
     * @return the confidence interval object.
     */
    public static ConfidenceInterval of(StatisticalSample statisticalSample) {
        
        double mean = statisticalSample.mean();
        double s    = statisticalSample.std();
        int    n    = statisticalSample.size();
        
        // Get t-distribution (Student's distribution) with degree of freedom
        // n - 1:
        TDistribution tdist = TDistribution.of(n - 1);
        
        // LaTeX: t_{n - 1}(\alpha / 2):
        double tAlpha = tdist.inverseCumulativeProbability(
            1.0 -  statisticalSample.getSignificanceLevel().value() / 2.0);
        
        double intervalHalfDistance = tAlpha * s / Math.sqrt(n);
        
        return new ConfidenceInterval(mean - intervalHalfDistance, 
                                      mean + intervalHalfDistance,
                                      statisticalSample.getSignificanceLevel());
    }
    
    @Override
    public String toString() {
        return "[%s, %s]".formatted(lo, hi);
    }
}
