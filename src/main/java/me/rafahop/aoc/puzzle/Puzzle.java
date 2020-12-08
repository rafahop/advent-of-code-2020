package me.rafahop.aoc.puzzle;

import java.nio.file.Path;

public interface Puzzle<T> {
    void init(Path file) throws Exception;
    
    T part1() throws Exception;

    T part2() throws Exception;
}
