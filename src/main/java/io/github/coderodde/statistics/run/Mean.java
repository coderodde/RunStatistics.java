package io.github.coderodde.statistics.run;

import java.util.List;

/**
 * This class holds the mean of input data.
 */
public final class Mean {
    
    private Mean() {}
    
    public static double of(List<Double> data) {
        double sum = 0.0;
        
        for (double y : data) {
            sum += y;
        }
        
        return sum / data.size();
    }
}
