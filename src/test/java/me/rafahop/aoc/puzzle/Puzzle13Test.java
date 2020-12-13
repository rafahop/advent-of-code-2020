package me.rafahop.aoc.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class Puzzle13Test extends PuzzleTest<Long> {

    @Override
    int getDay() {
        return 13;
    }

    @Override
    Long expectedPart1() {
        return 295L;
    }

    @Override
    Long expectedPart2() {
        return 1068781L;
    }

    @Test
    void testPart2Example1() throws Exception {
        Puzzle13 puzzle = (Puzzle13) constructPuzzle();
        puzzle.busIds = Arrays.asList("17", "x", "13", "19");
        assertEquals(3417L, puzzle.part2());
    }

    @Test
    void testPart2Example2() throws Exception {
        Puzzle13 puzzle = constructPuzzle();
        puzzle.busIds = Arrays.asList("67", "7", "59", "61");
        assertEquals(754018, puzzle.part2());
    }

    @Test
    void testPart2Example3() throws Exception {
        Puzzle13 puzzle = constructPuzzle();
        puzzle.busIds = Arrays.asList("67", "x", "7", "59", "61");
        assertEquals(779210, puzzle.part2());
    }

    @Test
    void testPart2Example4() throws Exception {
        Puzzle13 puzzle = constructPuzzle();
        puzzle.busIds = Arrays.asList("67", "7", "x", "59", "61");
        assertEquals(1261476, puzzle.part2());
    }

    @Test
    void testPart2Example5() throws Exception {
        Puzzle13 puzzle = constructPuzzle();
        puzzle.busIds = Arrays.asList("1789", "37", "47", "1889");
        assertEquals(1202161486, puzzle.part2());
    }

    @Override
    Puzzle13 constructPuzzle() throws Exception {
        return (Puzzle13) super.constructPuzzle();
    }
}
