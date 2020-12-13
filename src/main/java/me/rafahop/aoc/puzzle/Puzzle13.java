package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle13 implements Puzzle<Long> {

    int earliestDepartureTime;
    List<String> busIds;

    @Override
    public void init(Path file) throws Exception {
        List<String> lines = Files.readAllLines(file);
        earliestDepartureTime = Integer.parseInt(lines.get(0));
        busIds = Arrays.asList(lines.get(1).split(","));
    }

    private List<BusConstraint> getConstraints(Function<Integer, Integer> delayFunction) {
        return IntStream.range(0, busIds.size()).filter(i -> !busIds.get(i).equals("x"))
                .mapToObj(i -> new BusConstraint(Integer.parseInt(busIds.get(i)), delayFunction.apply(i)))
                .collect(Collectors.toList());
    }

    @Override
    public Long part1() throws Exception {
        List<BusConstraint> constraints = getConstraints(i -> 0); // Do not need delays for this part
        int waitingTime = 0;
        Optional<BusConstraint> bus;
        do {
            int departureTime = earliestDepartureTime + waitingTime;
            bus = constraints.stream().filter(c -> c.accepts(departureTime)).findAny();
            waitingTime++;
        } while (bus.isEmpty());
        return bus.get().getBusId() * --waitingTime;
    }

    @Override
    public Long part2() throws Exception {
        long timestamp = 0;
        long inc = 1;
        for (BusConstraint bus : getConstraints(Function.identity())) {
            while (!bus.accepts(timestamp)) {
                timestamp += inc;
            }
            inc *= bus.getBusId();
        }

        return timestamp;
    }

    class BusConstraint {
        long busId;
        int delay;

        public BusConstraint(long busId, int delay) {
            super();
            this.busId = busId;
            this.delay = delay;
        }

        public long getBusId() {
            return busId;
        }

        public boolean accepts(long timestamp) {
            return (timestamp + delay) % busId == 0;
        }
    }
}