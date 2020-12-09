package me.rafahop.aoc.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

public abstract class PuzzleTest<T> {
    private static final String PUZZLES_PACKAGE = "me.rafahop.aoc.puzzle";
    private static final String PUZZLE_PREFIX = "Puzzle";
    
    abstract int getDay();
    
    abstract T expectedPart1();
    
    abstract T expectedPart2();
    
    Puzzle<T> getPuzzle() throws Exception {
        String dayString = String.format("%02d", getDay());
        Path file = getInput(dayString);
        Puzzle<T> puzzle = (Puzzle<T>) Class.forName(getClassName(dayString)).getDeclaredConstructor().newInstance();
        puzzle.init(file);
        return puzzle;
    }
    
    
    @Test
    void part1() throws Exception {
        assertEquals(expectedPart1(), getPuzzle().part1());
    }
    
    @Test
    void part2() throws Exception {
        assertEquals(expectedPart2(), getPuzzle().part2());
    }
    
    private static String getClassName(String day) {
        return PUZZLES_PACKAGE + "." + PUZZLE_PREFIX + day;
    }

    private static Path getInput(String dayString) {
        String fileName = dayString + "/sample.txt";
        return Paths.get(PuzzleTest.class.getClassLoader().getResource(fileName).getPath());
    }
}
