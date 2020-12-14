package me.rafahop.aoc.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class Puzzle14Test extends PuzzleTest<Long> {

    @Override
    int getDay() {
        return 14;
    }

    @Override
    Long expectedPart1() {
        return 165L;
    }

    @Override
    Long expectedPart2() {
        return 208L;
    }
    
    @Override
    String getInputPart2() {
        return "sample2.txt";
    }

    @Test
    void testDecoderChip1ApplyMask1() {
        long num = 11L;
        String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X";
        assertEquals(73L, new DecoderChip1().applyMask(mask, num));
    }

    @Test
    void testDecoderChip1ApplyMask2() {
        long num = 101L;
        String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X";
        assertEquals(101L, new DecoderChip1().applyMask(mask, num));
    }

    @Test
    void testDecoderChip1ApplyMask3() {
        long num = 0L;
        String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X";
        assertEquals(64L, new DecoderChip1().applyMask(mask, num));
    }

    @Test
    void testDecoderChip2ApplyMask1() {
        long num = 42;
        String mask = "000000000000000000000000000000X1001X";
        Collection<Long> result = new DecoderChip2().applyMask(mask, num).collect(Collectors.toList());
        assertEquals(4, result.size());
        assertTrue(result.containsAll(Arrays.asList(26L, 27L, 58L, 59L)));
    }

    @Test
    void testDecoderChip2ApplyMask2() {
        long num = 26;
        String mask = "00000000000000000000000000000000X0XX";
        Collection<Long> result = new DecoderChip2().applyMask(mask, num).collect(Collectors.toList());
        assertEquals(8, result.size());
        assertTrue(result.containsAll(Arrays.asList(16L, 17L, 18L, 19L, 24L, 25L, 26L, 27L)));
    }
}
