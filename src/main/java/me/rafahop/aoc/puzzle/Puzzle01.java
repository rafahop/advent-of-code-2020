package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Puzzle01 implements Puzzle<Long>{

    Collection<Long> data;
    
    @Override
    public void init(Path file) throws Exception {
        data = Files.lines(file).map(Long::parseLong).collect(Collectors.toList());
    }
    
    @Override
    public Long part1() {
        return getData().filter(d1 -> getData().anyMatch(d2 -> d1 + d2 == 2020))
                        .reduce(1L, (a,b) -> a*b);
    }

    @Override
    public Long part2() {
        return getData().filter(d1 -> getData().anyMatch(d2 -> getData().anyMatch(d3 -> d1 + d2 + d3 == 2020)))
                        .reduce(1L, (a,b) -> a*b);
    }
    
    private Stream<Long> getData() {
        return data.stream().parallel();
    }
}
