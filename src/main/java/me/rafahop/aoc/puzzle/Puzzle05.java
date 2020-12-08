package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Puzzle05 implements Puzzle<Integer> {

    List<String> boardingPasses;

    @Override
    public void init(Path file) throws Exception {
        boardingPasses = Files.lines(file).collect(Collectors.toList());
    }

    @Override
    public Integer part1() throws Exception {
        return boardingPasses.stream().mapToInt(Puzzle05::getSeatId).max().getAsInt();
    }

    @Override
    public Integer part2() throws Exception {
        int[] orderedPasses = boardingPasses.stream().mapToInt(Puzzle05::getSeatId).sorted().toArray();

        int i = 0;
        int prev;
        do {
            prev = orderedPasses[i++];
        } while (prev + 1 == orderedPasses[i]);
        return prev + 1;
    }

    static int getSeatId(String boardingPass) {
        return findPosition(boardingPass, 0, c -> c == 'R' || c == 'B');
        // A simpler solution:
        // return Integer.parseInt(boardingPass.replaceAll("[BR]", "1").replaceAll("[FL]", "0"), 2);
    }

    private static int findPosition(String input, int value, Predicate<Character> isUpperHalf) {
        if (input == null || input.isEmpty()) {
            return value;
        }
        int nextValue = value;
        if (isUpperHalf.test(input.charAt(0))) {
            nextValue += Math.pow(2, input.length()) / 2;
        }
        return findPosition(input.substring(1), nextValue, isUpperHalf);
    }
    
}
