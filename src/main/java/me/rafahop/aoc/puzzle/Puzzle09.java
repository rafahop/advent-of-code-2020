package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Puzzle09 implements Puzzle<Long> {
    private int preambleSize = 25;

    List<Long> data;

    @Override
    public void init(Path file) throws Exception {
        data = Files.lines(file).map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public Long part1() {
        for (int i = preambleSize; i < data.size(); i++) {
            List<Long> preamble = data.subList(i-preambleSize, i);
            Long value = data.get(i);
            if (!isValid(preamble, value) ) {
                return value;
            }
        }
        return null;
    }

    @Override
    public Long part2() {
        Long invalidNumber = part1();
        for (int i=0; i<data.size(); i++) {
            List<Long> subList = findContiguousSumming(data.subList(i, data.size()), invalidNumber);
            if (!subList.isEmpty()) {
                long min = subList.stream().mapToLong(Long::valueOf).min().getAsLong();
                long max = subList.stream().mapToLong(Long::valueOf).max().getAsLong();
                return min + max;
            }
        }
        return null;
    }

    private boolean isValid(List<Long> preamble, Long value) {
        return preamble.stream().anyMatch(n1 -> preamble.stream().anyMatch(n2 -> n1 != n2 && n1+n2 == value));
    }

    private List<Long> findContiguousSumming(List<Long> list, long sum) {
        return list.stream().sequential().reduce((List<Long>) new ArrayList<Long>(), 
                (acc, v) -> {
                                long partialSum = acc.stream().mapToLong(Long::valueOf).sum();
                                if (partialSum == sum) {
                                    return acc;
                                } else if (v!=sum && partialSum + v <= sum) {
                                    acc.add(v);
                                    return acc;
                                } else {
                                    return new ArrayList<>();
                                }
                            }, 
                (a,b) -> Stream.of(a, b).flatMap(List::stream).collect(Collectors.toList()));
    }
    
    public void setPreambleSize(int preambleSize) {
        this.preambleSize = preambleSize;
    }
}
