package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Puzzle10 implements Puzzle<Long> {

    List<Integer> data;

    @Override
    public void init(Path file) throws Exception {
        data = Files.lines(file).map(Integer::parseInt).sorted().collect(Collectors.toList());
    }

    @Override
    public Long part1() {
        List<Integer> joltDiffs = new ArrayList<>();
        int prev = 0; // charger outlet
        for (int v : data) {
            joltDiffs.add(v-prev);
            prev = v;
        }
        Map<Integer, Long> diffCount = joltDiffs.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long devices1JoltDiff = diffCount.get(1);
        long devices3JoltDiff = diffCount.get(3) + 1; //adding device's built-in adapter
        return devices1JoltDiff * devices3JoltDiff;
    }

    @Override
    public Long part2() {
        long[] a = new long[data.size()+1]; // 1 element longer to include the charger outlet
        calc(-1, a); // Start at -1
        return a[0];
    }
    
    private void calc(int pos, long[] partial) {
        if (pos == data.size()-1) {
            partial[pos+1] = 1; // Last element: only one option
            return; 
        }
        int maxAhead = Math.min(pos+3, data.size()-1);
        int currentValue = pos < 0 ? 0 : data.get(pos); // Negative means 0 (charger outlet)
        for (int i = pos + 1; i <= maxAhead; i++) {
            
            if (data.get(i) - currentValue <= 3) {
                if (partial[i+1] == 0) {
                    calc(i, partial);
                }
                partial[pos+1] += partial[i+1];
            }
        }
    }
}
