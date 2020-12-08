package me.rafahop.aoc.puzzle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Puzzle03 implements Puzzle<Long> {
    private Element[][] grid;

    @Override
    public void init(Path file) throws IOException {
        grid = Files.lines(file)
                .map(String::chars)
                .map(s -> s.mapToObj(Element::getElement))
                .map(s -> s.toArray(Element[]::new))
                .toArray(Element[][]::new);
    }

    @Override
    public Long part1() throws IOException {
        return countTrees(new Slope(3, 1));
    }

    @Override
    public Long part2() throws IOException {
        return Stream.of(new Slope(1,1), new Slope(3,1), new Slope(5,1), new Slope(7,1), new Slope(1,2))
                    .mapToLong(this::countTrees)
                    .reduce(1, (a,b) -> a*b);
    }

    private long countTrees(Slope slope) {
        long trees = 0;
        int pos = 0;
        for (int i = 0; i < grid.length; i += slope.getDown()) {
            Element[] row = grid[i];
            if (row[pos % row.length].equals(Element.TREE)) {
                trees++;
            }
            pos += slope.getRight();
        }
        return trees;
    }
}

enum Element {
    OPEN('.'), TREE('#');

    private char value;

    Element(char value) {
        this.value = value;
    }

    static Element getElement(int c) {
        for (Element e : Element.values()) {
            if (e.value == c) {
                return e;
            }
        }
        return null;
    }
}

class Slope {
    int right;
    int down;

    public Slope(int right, int down) {
        super();
        this.right = right;
        this.down = down;
    }

    protected int getRight() {
        return right;
    }

    protected void setRight(int right) {
        this.right = right;
    }

    protected int getDown() {
        return down;
    }

    protected void setDown(int down) {
        this.down = down;
    }

}