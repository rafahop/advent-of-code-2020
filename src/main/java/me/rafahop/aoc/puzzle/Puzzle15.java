package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Puzzle15 implements Puzzle<Integer> {

    List<Integer> startingNumbers;

    @Override
    public void init(Path file) throws Exception {
        startingNumbers = Arrays.stream(Files.readString(file).split(",")).map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @Override
    public Integer part1() throws Exception {
        return play(2020);
    }

    @Override
    public Integer part2() throws Exception {
        return play(30_000_000);
    }

    private Integer play(int rounds) {
        MemoryGame game = new MemoryGame(startingNumbers);

        while (game.getTurn() <= rounds) {
            game.play();
        }

        return game.getLastNumber();
    }
}

class MemoryGame {
    Map<Integer, Integer> memory = new HashMap<>();
    int turn = 1;
    int lastNumber;

    public MemoryGame(List<Integer> startingNumbers) {
        startingNumbers.forEach(this::speakNumber);
    }

    private void speakNumber(int num) {
        if (turn > 1) {
            memory.put(lastNumber, turn);
        }
        lastNumber = num;
        turn++;
    }

    public int play() {
        int newNumber = 0;
        if (memory.containsKey(lastNumber)) {
            newNumber = turn - memory.get(lastNumber);
        }
        speakNumber(newNumber);
        return newNumber;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public int getTurn() {
        return turn;
    }
}
