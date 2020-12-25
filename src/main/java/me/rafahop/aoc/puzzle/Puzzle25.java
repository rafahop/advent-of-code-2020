package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle25 implements Puzzle<Long> {
    private static final int SUBJECT_NUMBER = 7;

    List<DeviceCryptoInfo> devices;

    @Override
    public void init(Path file) throws Exception {
        devices = Files.lines(file)
                       .map(Integer::valueOf)
                       .map(DeviceCryptoInfo::new)
                       .peek(d -> d.calculateLoopSize(SUBJECT_NUMBER))
                       .collect(Collectors.toList());
    }

    @Override
    public Long part1() throws Exception {
        DeviceCryptoInfo card = devices.get(0);
        DeviceCryptoInfo door = devices.get(1);
        return card.encrypt(door.getPublicKey());
    }

    @Override
    public Long part2() throws Exception {
        return 0L; // Nothing to do here today
    }
}

class DeviceCryptoInfo {
    private static final int DIVISOR = 20201227;
    final int publicKey;
    int loopSize = 0;

    public DeviceCryptoInfo(int publicKey) {
        this.publicKey = publicKey;
    }

    public int getPublicKey() {
        return publicKey;
    }

    public void calculateLoopSize(int subjectNumber) {
        long value = 1;
        loopSize = 0;
        while (value != publicKey) {
            value = (value * subjectNumber) % DIVISOR;
            loopSize++;
        }
    }

    public Long encrypt(int subjectNumber) {
        long value = 1L;
        for (int i = 0; i < loopSize; i++) {
            value = (value * subjectNumber) % DIVISOR;
        }
        return value;
    }
}
