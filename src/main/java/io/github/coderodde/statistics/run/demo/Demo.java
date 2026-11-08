package io.github.coderodde.statistics.run.demo;

import io.github.coderodde.statistics.run.BenchmarkRunner;
import io.github.coderodde.statistics.run.SignificanceLevel;
import java.util.Random;

public class Demo {

    private static final Random RANDOM = new Random(13L);
    
    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                Thread.sleep(RANDOM.nextInt(100));
            } catch (InterruptedException ignored) {}
        };
        
        BenchmarkRunner runner = 
            new BenchmarkRunner(new SignificanceLevel(0.01));
        
        runner.measure(r, 20);
        
        System.out.println(runner.getBenchmarkSummary());
    }
}
