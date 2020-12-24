package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle24 implements Puzzle<Integer> {

    LobbyFloor lobby = new LobbyFloor();

    @Override
    public void init(Path file) throws Exception {
        List<HexagonTile> initialFlip = Files.lines(file)
                                             .map(Puzzle24::parseOrientation)
                                             .map(HexagonTile::new)
                                             .collect(Collectors.toList());
        lobby.flip(initialFlip);
    }

    @Override
    public Integer part1() throws Exception {
        return lobby.countBlackTiles();
    }

    @Override
    public Integer part2() throws Exception {
        IntStream.rangeClosed(1, 100).forEach(i -> lobby.dailyChange());
        return lobby.countBlackTiles();
    }

    static List<Orientation> parseOrientation(String line) {
        String code = Character.toString(line.charAt(0));
        Orientation o = Orientation.getOrientation(code);
        if (o == null) {
            // Try 2-char code orientation
            code += Character.toString(line.charAt(1));
            o = Orientation.getOrientation(code);
        }
        List<Orientation> all = new ArrayList<>();
        all.add(o);
        String rest = line.replaceFirst(o.getId(), "");
        if (!rest.isEmpty()) {
            all.addAll(parseOrientation(rest));
        }
        return all;
    }
}

class LobbyFloor {
    Set<HexagonTile> blackTiles = new HashSet<>();
    
    public void flip(Collection<HexagonTile> tiles) {
        Map<HexagonTile, Long> count = tiles.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        count.entrySet()
             .stream()
             .filter(e -> e.getValue() % 2 != 0)
             .map(Map.Entry::getKey)
             .forEach(t -> {
                         if (blackTiles.contains(t)) {
                             blackTiles.remove(t);
                         } else {
                             blackTiles.add(t);
                         }
                     });
    }
    
    public void dailyChange() {
        Set<HexagonTile> flip = Collections.synchronizedSet(new HashSet<>());
        blackTiles.parallelStream()
                  .forEach(t -> {
                                flip.addAll(t.getAdjacentTiles()
                                             .parallelStream()
                                             .filter(a -> !blackTiles.contains(a))
                                             .filter(a -> countBlackAdjacentTiles(a) == 2)
                                             .collect(Collectors.toList()));

                                long blackAdjacent = countBlackAdjacentTiles(t);
                                if (blackAdjacent == 0 || blackAdjacent > 2) {
                                    flip.add(t);
                                }
                          });
        flip(flip);
    }
    
    private long countBlackAdjacentTiles(HexagonTile tile) {
        return tile.getAdjacentTiles().parallelStream().filter(blackTiles::contains).count();
    }

    public int countBlackTiles() {
        return blackTiles.size();
    }

}

class HexagonTile {
    double x = 0;
    int y = 0;

    public HexagonTile(double x, int y) {
        this.x = x;
        this.y = y;
    }

    public HexagonTile(List<Orientation> position) {
        for (Orientation o : position) {
            switch (o) {
            case EAST:
                x++;
                break;
            case NORTHEAST:
                y--;
                x += 0.5;
                break;
            case NORTHWEST:
                y--;
                x -= 0.5;
                break;
            case SOUTHEAST:
                y++;
                x += 0.5;
                break;
            case SOUTHWEST:
                y++;
                x -= 0.5;
                break;
            case WEST:
                x--;
                break;
            default:
                throw new IllegalStateException("Invalid orientation");
            }
        }
    }

    public double getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<HexagonTile> getAdjacentTiles() {
        return List.of(new HexagonTile(x - 1, y), new HexagonTile(x + 1, y), new HexagonTile(x + 0.5, y - 1),
                new HexagonTile(x - 0.5, y - 1), new HexagonTile(x + 0.5, y + 1), new HexagonTile(x - 0.5, y + 1));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HexagonTile other = (HexagonTile) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}

enum Orientation {
    EAST("e"), SOUTHEAST("se"), SOUTHWEST("sw"), WEST("w"), NORTHEAST("ne"), NORTHWEST("nw");

    private String id;

    private Orientation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Orientation getOrientation(String id) {
        return Arrays.stream(Orientation.values()).filter(o -> o.id.equals(id)).findAny().orElse(null);
    }
}