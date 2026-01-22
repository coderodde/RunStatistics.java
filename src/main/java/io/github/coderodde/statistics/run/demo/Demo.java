package io.github.coderodde.statistics.run.demo;

import io.github.coderodde.statistics.run.RunStatistics;
import io.github.coderodde.statistics.run.Runner;
import java.util.Random;

public class Demo {

    private static final Random RANDOM = new Random(13L);
    
    public static void main(String[] args) {
        final RunStatistics runStatistics = Runner.measure(() -> {
            try {
                Thread.sleep(RANDOM.nextInt(100));
            } catch (InterruptedException ex) {}
        }, 20);
        
        System.out.println(runStatistics);
    }
}
