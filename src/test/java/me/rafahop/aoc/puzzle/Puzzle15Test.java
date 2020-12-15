package me.rafahop.aoc.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class Puzzle15Test extends PuzzleTest<Integer> {

    @Override
    int getDay() {
        return 15;
    }

    @Override
    Integer expectedPart1() {
        return 436;
    }

    @Override
    Integer expectedPart2() {
        return 175594;
    }

    @Test
    void testPart1Example1() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(1, 3, 2);
        assertEquals(1, puzzle.part1());
    }

    @Test
    void testPart1Example2() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(2, 1, 3);
        assertEquals(10, puzzle.part1());
    }

    @Test
    void testPart1Example3() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(1, 2, 3);
        assertEquals(27, puzzle.part1());
    }

    @Test
    void testPart1Example4() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(2, 3, 1);
        assertEquals(78, puzzle.part1());
    }

    @Test
    void testPart1Example5() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(3, 2, 1);
        assertEquals(438, puzzle.part1());
    }

    @Test
    void testPart1Example6() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(3, 1, 2);
        assertEquals(1836, puzzle.part1());
    }
    
    @Test
    void testPart2Example1() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(1, 3, 2);
        assertEquals(2578, puzzle.part2());
    }

    @Test
    void testPart2Example2() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(2, 1, 3);
        assertEquals(3544142, puzzle.part2());
    }

    @Test
    void testPart2Example3() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(1, 2, 3);
        assertEquals(261214, puzzle.part2());
    }

    @Test
    void testPart2Example4() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(2, 3, 1);
        assertEquals(6895259, puzzle.part2());
    }

    @Test
    void testPart2Example5() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(3, 2, 1);
        assertEquals(18, puzzle.part2());
    }

    @Test
    void testPart2Example6() throws Exception {
        Puzzle15 puzzle = (Puzzle15) constructPuzzle();
        puzzle.startingNumbers = Arrays.asList(3, 1, 2);
        assertEquals(362, puzzle.part2());
    }
}
