package io.github.coderodde.statistics.run;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class StatisticalSample {
        
    private final int size;
    private final long min;
    private final long max;
    private final double mean;
    private final double median;
    private final double std;
    private final SignificanceLevel significanceLevel;
    
    private boolean meanComputed = false;
    private boolean stdComputed  = false;

    private static final int MINIMUM_SAMPLE_SIZE = 5;

    StatisticalSample(List<Long> data,
                      SignificanceLevel significanceLevel) {

        validateData(data);
        checkSampleSize(data);

        Collections.sort(data);

        size   = data.size();
        min    = data.getFirst();
        max    = data.getLast();
        mean   = mean(data);
        median = median(data);
        std    = std(data);

        this.significanceLevel = Objects.requireNonNull(significanceLevel);
    }

    int size() {
        return size;
    }

    long min() {
        return min;
    }

    long max() {
        return max;
    }

    double mean() {
        return mean;
    }

    double median() {
        return median;
    }

    double std() {
        return std;
    }
    
    SignificanceLevel getSignificanceLevel() {
        return significanceLevel;
    }

    private double median(List<Long> data) {
        if (data.size() % 2 == 0) {
            int index1 = data.size() / 2 - 1;
            int index2 = index1 + 1;
            return (data.get(index1) + data.get(index2)) / 2;
        } else {
            return data.get(data.size() / 2);
        }
    }

    private double mean(List<Long> data) {
        if (meanComputed) {
            return mean;
        }
        
        double sum = 0.0;

        for (long y : data) {
            sum += y;
        }

        meanComputed = true;
        return sum / data.size();
    }

    private double std(List<Long> data) {
        if (stdComputed) {
            return std;
        }
        
        double sum = 0.0;
        int n = data.size();

        for (double y : data) {
            sum += Math.pow(y - mean(), 2.0);
        }

        stdComputed = true;
        return Math.sqrt(sum / (n - 1));
    }

    private static void checkSampleSize(List<Long> data) {
        if (data.size() < MINIMUM_SAMPLE_SIZE) {
            throw new IllegalArgumentException(
                "Data size (%d) is too small. Must be at least %d."
                    .formatted(data.size(), MINIMUM_SAMPLE_SIZE)
            );
        }
    }
    
    /**
     * Makes sure that the input data is valid.
     * 
     * @param data the input data to validate.
     * @throws IllegalArgumentException if data list has less than 2 elements, 
     *         or one of elements is {@code null} or is not a real number.
     */
    private static void validateData(List<Long> data)
            throws IllegalArgumentException {
        
        Objects.requireNonNull(data, "The input data is null.");
        
        int index = 0;
        
        for (long y : data) {
            if (y < 0L) {
                throw new IllegalArgumentException(
                    "Negative sample (%d) at index %d.".formatted(y, index)
                );
            }
            
            ++index;
        }
    }
}