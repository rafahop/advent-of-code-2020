package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle23 implements Puzzle<String> {

    List<Integer> cups;

    @Override
    public void init(Path file) throws Exception {
        cups = Stream.of(Files.readString(file).split("")).map(Integer::valueOf).collect(Collectors.toList());
    }

    @Override
    public String part1() throws Exception {
        CrabCupGame game = new CrabCupGame(cups);
        IntStream.range(0, 100).forEach(i -> game.move());
        return game.getOrderedCups(1).stream().map(String::valueOf).collect(Collectors.joining()).substring(1);
    }

    @Override
    public String part2() throws Exception {
        List<Integer> moreCups = Stream.concat(cups.stream(), IntStream.rangeClosed(cups.size() + 1, 1_000_000).boxed())
                                       .collect(Collectors.toList());

        CrabCupGame game = new CrabCupGame(moreCups);
        IntStream.range(0, 10_000_000).forEach(i -> game.move());
        int a = game.getNext(1);
        int b = game.getNext(a);
        return String.valueOf((long) a * b);
    }
}

class CrabCupGame {
    int[] next;
    Integer currentCup;

    public CrabCupGame(List<Integer> cups) {
        this.next = new int[cups.size()];
        this.currentCup = cups.get(0);

        int value = cups.get(0);
        for (int i = 1; i < cups.size(); i++) {
            setNext(value, cups.get(i));
            value = cups.get(i);
        }
        setNext(value, cups.get(0));
    }

    public void move() {
        List<Integer> pickup = getPickUp();
        Integer destination = getDestinationCup(pickup);

        int afterPickup = getNext(pickup.get(2));
        setNext(pickup.get(2), getNext(destination));
        setNext(destination, pickup.get(0));
        setNext(currentCup, afterPickup);

        currentCup = getNext(currentCup);
    }

    private Integer getDestinationCup(List<Integer> pickup) {
        int destination = currentCup;
        do {
            destination--;
            if (destination < 1) {
                destination = next.length;
            }
        } while (pickup.contains(destination));
        return destination;
    }

    private List<Integer> getPickUp() {
        return List.of(getNext(currentCup), getNext(getNext(currentCup)), getNext(getNext(getNext(currentCup))));
    }

    public List<Integer> getOrderedCups(int startingNumber) {
        List<Integer> result = new ArrayList<>();
        int n = getNext(startingNumber);
        result.add(startingNumber);
        while (result.size() < next.length) {
            result.add(n);
            n = getNext(n);
        }
        return result;
    }

    int getNext(int cup) {
        return next[cup - 1];
    }

    void setNext(int cup, int value) {
        next[cup - 1] = value;
    }
}