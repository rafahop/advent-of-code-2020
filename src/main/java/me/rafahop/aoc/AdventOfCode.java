package me.rafahop.aoc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import me.rafahop.aoc.puzzle.Puzzle;

public class AdventOfCode {
    private static final String PUZZLES_PACKAGE = "me.rafahop.aoc.puzzle";
    private static final String PUZZLE_PREFIX = "Puzzle";
    private static final boolean USE_SAMPLE = false;
    
    public static void main(String[] args) throws Exception {
        int day = LocalDateTime.now().getDayOfMonth(); // Change this to run a different puzzle
        Puzzle<?> p = getPuzzle(day);
        
        System.out.println("Part 1: " + p.part1());
        System.out.println("Part 2: " + p.part2());
    }

    public static Puzzle<?> getPuzzle(int day) throws Exception {
        String dayString = String.format("%02d", day);
        Path file = getInput(dayString);
        Puzzle<?> puzzle = (Puzzle<?>) Class.forName(getClassName(dayString)).getDeclaredConstructor().newInstance();
        puzzle.init(file);
        return puzzle;
    }
    
    private static Path getInput(String day) {
        String fileName;
        if (USE_SAMPLE) {
            fileName = day + "/sample.txt";
        } else {
            fileName = day + "/input.txt";
        }
        return Paths.get(AdventOfCode.class.getClassLoader().getResource(fileName).getPath());
    }
    
    private static String getClassName(String day) {
        return PUZZLES_PACKAGE + "." + PUZZLE_PREFIX + day;
    }
}
