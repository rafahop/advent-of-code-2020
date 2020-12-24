package me.rafahop.aoc.puzzle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class Puzzle24Test extends PuzzleTest<Integer> {

    @Override
    int getDay() {
        return 24;
    }

    @Override
    Integer expectedPart1() {
        return 10;
    }

    @Override
    Integer expectedPart2() {
        return 2208;
    }

    @Test
    void hexagonTileConstructor1() {
        List<Orientation> position = List.of(Orientation.EAST, Orientation.SOUTHEAST, Orientation.WEST);
        HexagonTile tile = new HexagonTile(position);
        assertEquals(0.5, tile.getX());
        assertEquals(1, tile.getY());
    }

    @Test
    void hexagonTileConstructor2() {
        List<Orientation> position = List.of(Orientation.NORTHWEST, Orientation.WEST, Orientation.SOUTHWEST,
                Orientation.EAST, Orientation.EAST);
        HexagonTile tile = new HexagonTile(position);
        assertEquals(0, tile.getX());
        assertEquals(0, tile.getY());
    }
    
    @Test
    void hexagonTileConstructor3() {
        List<Orientation> position = List.of(Orientation.WEST, Orientation.WEST, Orientation.WEST,
                Orientation.NORTHWEST, Orientation.SOUTHEAST, Orientation.NORTHWEST, Orientation.NORTHWEST,
                Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.NORTHEAST,
                Orientation.WEST, Orientation.NORTHEAST, Orientation.WEST, Orientation.SOUTHEAST, Orientation.WEST,
                Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.WEST);

        HexagonTile tile = new HexagonTile(position);
        assertEquals(-9.5, tile.getX());
        assertEquals(-9, tile.getY());
    }

    @Test
    void parseOrientation() {
        List<Orientation> expected = List.of(Orientation.WEST, Orientation.WEST, Orientation.WEST,
                Orientation.NORTHWEST, Orientation.SOUTHEAST, Orientation.NORTHWEST, Orientation.NORTHWEST,
                Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.NORTHEAST,
                Orientation.WEST, Orientation.NORTHEAST, Orientation.WEST, Orientation.SOUTHEAST, Orientation.WEST,
                Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.NORTHWEST, Orientation.WEST);
        assertEquals(expected, Puzzle24.parseOrientation("wwwnwsenwnwnwnwnwnewnewsewnwnwnww"));
    }
}
