package io.github.coderodde.statistics.run;

import java.util.List;
import java.util.Objects;

/**
 * This record implements computation of medians.
 */
public final class Median {
    
    private Median() {
    
    }
    
    public static double of(List<Double> data) {
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
