package me.rafahop.aoc.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Puzzle05Test extends PuzzleTest<Integer> {

    @Override
    int getDay() {
        return 5;
    }

    @Override
    Integer expectedPart1() {
        return 820;
    }

    @Override
    Integer expectedPart2() {
        return 120;
    }
    
    @Test
    void testBoardingCard0() {
        String boardingCard = "FBFBBFFRLR";
        assertEquals(357, Puzzle05.getSeatId(boardingCard));
    }
    
    @Test
    void testBoardingCard1() {
        String boardingCard = "BFFFBBFRRR";
        assertEquals(567, Puzzle05.getSeatId(boardingCard));
    }
    
    @Test
    void testBoardingCard2() {
        String boardingCard = "FFFBBBFRRR";
        assertEquals(119, Puzzle05.getSeatId(boardingCard));
    }
    
    @Test
    void testBoardingCard3() {
        String boardingCard = "BBFFBBFRLL";
        assertEquals(820, Puzzle05.getSeatId(boardingCard));
    }
}
