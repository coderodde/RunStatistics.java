package io.github.coderodde.statistics.run;

import java.util.List;

/**
 * This class contains a method for computing standard deviations.
 */
public final class StandardDeviation {
    
    private StandardDeviation() {
        
    }
    
    public static double of(List<Long> data, double mean) {
        double sum = 0.0;
        int n = data.size();
        
        for (double y : data) {
            sum += Math.pow(y - mean, 2.0);
        }
        
        return Math.sqrt(sum / (n - 1));
    }
}
