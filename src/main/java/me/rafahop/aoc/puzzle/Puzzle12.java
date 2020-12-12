package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle12 implements Puzzle<Long> {
    List<NavigationInstruction> instructions;

    @Override
    public void init(Path file) throws Exception {
        instructions = Files.lines(file)
                .map(s -> new NavigationInstruction(s.charAt(0), Integer.parseInt(s.substring(1))))
                .collect(Collectors.toList());
    }

    @Override
    public Long part1() throws Exception {
        return run(new ShipMovementStrategyPart1());
    }

    @Override
    public Long part2() throws Exception {
        return run(new ShipMovementStrategyPart2());
    }

    private Long run(ShipMovementStrategy strategy) {
        Ship ship = new Ship(0, new Coordinate(0, 0), new Coordinate(10, 1));
        ship.setMovementStrategy(strategy);
        instructions.stream().forEach(ship::move);
        return ship.getPosition().getManhattanDistance();
    }
}

interface ShipMovementStrategy {
    void move(Ship ship, NavigationInstruction inst);
}

class ShipMovementStrategyPart1 implements ShipMovementStrategy {

    @Override
    public void move(Ship ship, NavigationInstruction inst) {
        switch (inst.action) {
        case 'N':
        case 'S':
        case 'E':
        case 'W':
            ship.getPosition().move(Coordinate.getByDirection(inst.action, inst.value));
            break;
        case 'L':
            ship.turnLeft(inst.value);
            break;
        case 'R':
            ship.turnRight(inst.value);
            break;
        case 'F':
            ship.getPosition().move(Coordinate.getByDirection(ship.getDirection(), inst.value));
            break;
        default:
        }
    }
}

class ShipMovementStrategyPart2 implements ShipMovementStrategy {

    @Override
    public void move(Ship ship, NavigationInstruction inst) {
        switch (inst.action) {
        case 'N':
        case 'S':
        case 'E':
        case 'W':
            ship.getWaypoint().move(Coordinate.getByDirection(inst.action, inst.value));
            break;
        case 'L':
            ship.getWaypoint().rotateLeft(inst.value);
            break;
        case 'R':
            ship.getWaypoint().rotateRight(inst.value);
            break;
        case 'F':
            IntStream.range(0, inst.value).forEach(i -> ship.position.move(ship.getWaypoint()));
            break;
        default:
        }
    }
}

class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate getByDirection(char dir, int value) {
        int x = 0;
        int y = 0;
        switch (dir) {
        case 'N':
            y += value;
            break;
        case 'S':
            y -= value;
            break;
        case 'E':
            x += value;
            break;
        case 'W':
            x -= value;
            break;
        }
        return new Coordinate(x, y);
    }

    public void move(Coordinate c) {
        this.x += c.x;
        this.y += c.y;
    }

    private void change(Coordinate c) {
        this.x = c.x;
        this.y = c.y;
    }

    public void rotateLeft(int degrees) {
        while (degrees > 0) {
            change(new Coordinate(-y, x));
            degrees -= 90;
        }
    }

    public void rotateRight(int degrees) {
        int leftDegrees = degrees == 180 ? 180 : (degrees + 180) % 360;
        rotateLeft(leftDegrees);
    }

    public long getManhattanDistance() {
        return (long) Math.abs(x) + (long) Math.abs(y);
    }
}

class Ship {
    int direction;
    Coordinate position;
    Coordinate waypoint;

    ShipMovementStrategy movementStrategy;

    public Ship(int direction, Coordinate position, Coordinate waypoint) {
        this.direction = direction;
        this.position = position;
        this.waypoint = waypoint;
    }

    public void move(NavigationInstruction inst) {
        movementStrategy.move(this, inst);
    }

    public void turnLeft(int degrees) {
        direction = (direction + degrees) % 360;
    }

    public void turnRight(int degrees) {
        direction = (direction - degrees + 360) % 360;
    }

    public Coordinate getPosition() {
        return position;
    }

    public char getDirection() {
        char dir = 'N';
        if (direction == 0) {
            dir = 'E';
        } else if (direction == 90) {
            dir = 'N';
        } else if (direction == 180) {
            dir = 'W';
        } else if (direction == 270) {
            dir = 'S';
        }
        return dir;
    }

    public Coordinate getWaypoint() {
        return waypoint;
    }

    public void setMovementStrategy(ShipMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }
}

class NavigationInstruction {
    char action;
    int value;

    public NavigationInstruction(char action, int value) {
        this.action = action;
        this.value = value;
    }
}