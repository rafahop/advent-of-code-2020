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
    
    Puzzle<T> getPuzzle(String filename) throws Exception {
        Puzzle<T> puzzle = (Puzzle<T>) constructPuzzle();
        puzzle.init(getInput(filename));
        return puzzle;
    }
    
    Puzzle<T> constructPuzzle() throws Exception {
        return (Puzzle<T>) Class.forName(getClassName(getDayString())).getDeclaredConstructor().newInstance();
    }
    
    private String getDayString() {
        return String.format("%02d", getDay());
    }
    
    @Test
    void part1() throws Exception {
        assertEquals(expectedPart1(), getPuzzle(getInputPart1()).part1());
    }
    
    @Test
    void part2() throws Exception {
        assertEquals(expectedPart2(), getPuzzle(getInputPart2()).part2());
    }
    
    
    String getInputPart1() {
        return "sample.txt";
    }
    
    String getInputPart2() {
        return "sample.txt";
    }
    
    private static String getClassName(String day) {
        return PUZZLES_PACKAGE + "." + PUZZLE_PREFIX + day;
    }

    private Path getInput(String filename) {
        String fileName = getDayString() + "/" + filename;
        return Paths.get(PuzzleTest.class.getClassLoader().getResource(fileName).getPath());
    }
}
