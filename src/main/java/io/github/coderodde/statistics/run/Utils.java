package io.github.coderodde.statistics.run;

import java.util.List;
import java.util.Objects;

/**
 * This class contains common utilities.
 */
final class Utils {
    
    private Utils() {}
    
    
    /**
     * Computes the mean value in {@code data}.
     * 
     * @param data the input data.
     * @return the mean value of input data.
     */
    static double mean(List<Double> data) {
        double sum = 0.0;
        
        for (double y : data) {
            sum += y;
        }
        
        return sum / data.size();
    }
    
    /**
     * Computes the standard deviation of {@code data}.
     * 
     * @param data the input data.
     * @param ybar the mean value of the input data.
     * @return the standard deviation of {@code data}.
     */
    static double std(List<Double> data, double ybar) {
        double sum = 0.0;
        int n = data.size();
        
        for (double y : data) {
            sum += Math.pow(y - ybar, 2.0);
        }
        
        return Math.sqrt(sum / (n - 1));
    }
    
    /**
     * This method returns the median of the input data list. 
     * 
     * Warning: this method assumes that the input data is <b>sorted</b>.
     * 
     * @param data the sorted data list from which to return the median.
     * 
     * @return the median of the input data list.
     */
    static double median(List<Double> data) {
        if (Objects.requireNonNull(data).isEmpty()) {
            throw new IllegalArgumentException(
                "Can't compute the median of an empty list.");
        }
        
        if (data.size() % 2 == 0) {
            int index1 = data.size() / 2 - 1;
            int index2 = index1 + 1;
            return (data.get(index1) + data.get(index2)) / 2.0;
        } else {
            return data.get(data.size() / 2);
        }
    }
}
