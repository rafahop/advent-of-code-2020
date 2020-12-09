package me.rafahop.aoc.puzzle;

public class Puzzle09Test extends PuzzleTest<Long> {

    @Override
    int getDay() {
        return 9;
    }
    
    @Override
    Puzzle<Long> getPuzzle() throws Exception {
        Puzzle09 puzzle = (Puzzle09) super.getPuzzle();
        // Override preamble for test data
        puzzle.setPreambleSize(5);
        return puzzle;
    }

    @Override
    Long expectedPart1() {
        return 127L;
    }

    @Override
    Long expectedPart2() {
        return 62L;
    }

}
