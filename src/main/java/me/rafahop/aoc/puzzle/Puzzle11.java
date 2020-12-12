package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle11 implements Puzzle<Long> {

    WaitingAreaElement[][] grid;

    @Override
    public void init(Path file) throws Exception {
        grid = Files.lines(file)
                    .map(String::chars)
                    .map(s -> s.mapToObj(e -> WaitingAreaElement.getElement((char) e)))
                    .map(s -> s.toArray(WaitingAreaElement[]::new))
                    .toArray(WaitingAreaElement[][]::new);
    }

    @Override
    public Long part1() {
        return findEquilibrium(new Part1SeatingStrategy());
    }

    @Override
    public Long part2() {
        return findEquilibrium(new Part2SeatingStrategy());
    }
    
    private Long findEquilibrium(SeatingStrategy strategy) {
        WaitingArea waitingArea = new WaitingArea(grid);
        long prev;
        long occupied = waitingArea.countOccupiedSeats();
        do {
            waitingArea.change(strategy);
            prev = occupied;
            occupied = waitingArea.countOccupiedSeats();
        } while (occupied != prev);
        return occupied;
    }
}

class WaitingArea {
    WaitingAreaElement[][] grid;
    
    public WaitingArea(WaitingAreaElement[][] grid) {
        this.grid = grid;
    }

    public WaitingAreaElement getElement(int row, int col) {
        WaitingAreaElement element = null;
        if (row >= 0 && row < grid.length) {
            WaitingAreaElement[] r = grid[row];
            if (col >=0 && col < r.length) {
                element = r[col];
            }
        }
        return element;
    }
    
    public void change(SeatingStrategy strategy) {
        grid = strategy.apply(this);
    }
    
    public long countOccupiedSeats() {
        return Stream.of(grid).flatMap(Stream::of).filter(WaitingAreaElement.OCCUPIED::equals).count();
    }

    void printGrid() {
        Stream.of(grid).map(Arrays::toString).forEach(System.out::println);
    }
}

enum WaitingAreaElement {
    FLOOR('.'), EMPTY('L'), OCCUPIED('#');

    private char value;

    WaitingAreaElement(char value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    static WaitingAreaElement getElement(char c) {
        return Stream.of(WaitingAreaElement.values())
             .filter(e -> e.value == c)
             .findFirst()
             .orElseThrow(() -> new IllegalArgumentException("Invalid WaitingAreaElement " + c));
    }
}


interface SeatingStrategy {
    WaitingAreaElement[][] apply(WaitingArea waitingArea);
}

class Part1SeatingStrategy implements SeatingStrategy {

    @Override
    public WaitingAreaElement[][] apply(WaitingArea waitingArea) {
        WaitingAreaElement[][] grid = waitingArea.grid;
        WaitingAreaElement[][] newGrid = Arrays.stream(grid).map(WaitingAreaElement[]::clone).toArray(WaitingAreaElement[][]::new);
        for (int i = 0; i<grid.length; i++) {
            WaitingAreaElement[] row = grid[i];
            for (int j=0; j<row.length; j++) {
                if (canBeOccupied(waitingArea, i, j)) {
                    newGrid[i][j] = WaitingAreaElement.OCCUPIED;
                } else if (canBeFreed(waitingArea, i,j) ) {
                    newGrid[i][j] = WaitingAreaElement.EMPTY;
                }
            }
        }
        return newGrid;
    }

    private boolean canBeOccupied(WaitingArea waitingArea, int row, int col) {
        if (!WaitingAreaElement.EMPTY.equals(waitingArea.getElement(row, col))) {
            return false;
        }
        return !searchOccupied(waitingArea, row, col, e -> true);
    }
    private boolean canBeFreed(WaitingArea waitingArea,int row, int col) {
        if (!WaitingAreaElement.OCCUPIED.equals(waitingArea.getElement(row, col))) {
            return false;
        }
        List<WaitingAreaElement> occupiedElements = new ArrayList<>();
        return searchOccupied(waitingArea, row, col, e -> { 
                                                        occupiedElements.add(e); 
                                                        return occupiedElements.size()>=4;
                                                    });
    }

    /**
     * Find an adjacent occupied seat matching the given condition predicate
     * @param waitingArea The waiting area
     * @param row The row of the seat we need to find adjacent occupied seats
     * @param col The column of the seat we need to find adjacent occupied seats
     * @param condition An additional condition (other than occupied) the seat has to meet
     * @return True if there is an adjacent occupied seat meeting the condition
     */
    private boolean searchOccupied(WaitingArea waitingArea,int row, int col, Predicate<WaitingAreaElement> condition) {
        return IntStream.rangeClosed(row -1, row+1)
                        .anyMatch(i-> IntStream.rangeClosed(col -1, col+1)
                                            .filter(j -> i!= row || j != col)
                                            .mapToObj(j -> waitingArea.getElement(i,j))
                                            .anyMatch(el -> WaitingAreaElement.OCCUPIED.equals(el) && condition.test(el)));
    }
}

class Part2SeatingStrategy implements SeatingStrategy {

    @Override
    public WaitingAreaElement[][] apply(WaitingArea waitingArea) {
        WaitingAreaElement[][] grid = waitingArea.grid;
        WaitingAreaElement[][] newGrid = Arrays.stream(grid).map(WaitingAreaElement[]::clone).toArray(WaitingAreaElement[][]::new);
        for (int i = 0; i<grid.length; i++) {
            for (int j=0; j<grid[i].length; j++) {
                WaitingAreaElement el = waitingArea.getElement(i, j);

                if (WaitingAreaElement.OCCUPIED.equals(el) && findSeatsAllDirections(waitingArea, i, j)
                        .filter(WaitingAreaElement.OCCUPIED::equals).count() >= 5) {
                    newGrid[i][j] = WaitingAreaElement.EMPTY;
                } else if (WaitingAreaElement.EMPTY.equals(el)
                        && findSeatsAllDirections(waitingArea, i, j).noneMatch(WaitingAreaElement.OCCUPIED::equals)) {
                    newGrid[i][j] = WaitingAreaElement.OCCUPIED;
                }
            }
        }
        return newGrid;
    }
    
    /**
     * Find the first seat (available or occupied) in each of the 8 directions from a given point
     * @param waitingArea The waiting area
     * @param row The row to start searching
     * @param col The column to start searching
     * @return A stream of WaitingAreaElements containing up to 8 seats (occupied or available)
     */
    private Stream<WaitingAreaElement> findSeatsAllDirections(WaitingArea waitingArea, int row, int col) {
        return IntStream.rangeClosed(-1,1).mapToObj(i -> IntStream.rangeClosed(-1, 1)
                                                           .filter(j -> j!=0 || i != 0)
                                                           .mapToObj(j -> findFirstSeat(waitingArea, row, col, i, j))
                                                           .collect(Collectors.toList()))
                                          .flatMap(List::stream);
    }
    
    /**
     * Find the first seat (not a floor element) when looking from a given point to a given direction
     * @param waitingArea The waiting area
     * @param row The row to start searching
     * @param col The column to start searching
     * @param rowInc The row direction (+1,0,-1)
     * @param colInc The column direction  (+1,0,-1)
     * @return The first element found that is not floor or null if there is none
     */
    private WaitingAreaElement findFirstSeat(WaitingArea waitingArea, int row, int col, int rowInc, int colInc) {
        WaitingAreaElement[][] grid = waitingArea.grid;
        for (int i = row + rowInc, j = col + colInc; i >= 0 && i < grid.length && j >= 0
                && j < grid[i].length; i += rowInc, j += colInc) {
            if (i != row || j != col) {
                WaitingAreaElement el = waitingArea.getElement(i, j);
                if (!WaitingAreaElement.FLOOR.equals(el)) {
                    return el;
                }
            }
        }
        return null;
    }

}