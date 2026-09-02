package io.github.coderodde.statistics.run;

/**
 * This record represents significance levels.
 */
public final record SignificanceLevel(double value) {
    
    public SignificanceLevel {
        if (Double.isNaN(value)) {
            throw new IllegalArgumentException(
                    "The input significance level is NaN.");
        }
        
        if (value <= 0.0 || 1.0 <= value) {
            throw new IllegalArgumentException(
                String.format("The input significance level (%f) does not belong " + 
                        "to the interval (0, 1).",
                    value));
        }
    }
    
    @Override
    public String toString() {
        return "significance level = %f".formatted(value);
    }
}
