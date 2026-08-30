package io.github.coderodde.statistics.run.demo;

import io.github.coderodde.statistics.run.RunStatistics;
import io.github.coderodde.statistics.run.Runner;
import java.util.Random;

public class Demo {

    private static final Random RANDOM = new Random(13L);
    
    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                Thread.sleep(RANDOM.nextInt(100));
            } catch (InterruptedException ex) {}
        };
        
        final RunStatistics runStatistics = Runner.measure(r, 20, 0.05);
        
        System.out.println(runStatistics);
    }
}
