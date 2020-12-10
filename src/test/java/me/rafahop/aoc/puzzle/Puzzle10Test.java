package me.rafahop.aoc.puzzle;

public class Puzzle10Test extends PuzzleTest<Long> {

    @Override
    int getDay() {
        return 10;
    }

    @Override
    Long expectedPart1() {
        return (long) 22 * 10;
    }

    @Override
    Long expectedPart2() {
        return 19208L;
    }

}
