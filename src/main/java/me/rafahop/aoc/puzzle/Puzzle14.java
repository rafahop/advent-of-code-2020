package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Puzzle14 implements Puzzle<Long> {

    List<DockingProgramOperation> operations;

    @Override
    public void init(Path file) throws Exception {
        operations = Files.lines(file).map(DockingProgramOperation::getOperation).collect(Collectors.toList());
    }

    @Override
    public Long part1() throws Exception {
        DockingProgram program = new DockingProgram(new DecoderChip1());
        program.execute(operations);
        return program.sumAllMemoryValues();
    }

    @Override
    public Long part2() throws Exception {
        DockingProgram program = new DockingProgram(new DecoderChip2());
        program.execute(operations);
        return program.sumAllMemoryValues();
    }
}

class DockingProgram {
    DecoderChip decoder;
    String bitmask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    Map<Long, Long> memory = new HashMap<>();

    public DockingProgram(DecoderChip decoder) {
        this.decoder = decoder;
    }

    void execute(List<DockingProgramOperation> operations) {
        for (DockingProgramOperation op : operations) {
            op.run(this);
        }
    }

    public void changeMask(String mask) {
        this.bitmask = mask;
    }

    public void changeMemoryValue(long pos, long value) {
        memory.put(pos, value);
    }

    public String getBitmask() {
        return bitmask;
    }

    public DecoderChip getDecoder() {
        return decoder;
    }

    public long sumAllMemoryValues() {
        return memory.values().stream().mapToLong(i -> i).sum();
    }
}

abstract class DockingProgramOperation {
    private static final Pattern MASK_PATTERN = Pattern.compile("mask = ([01X]+)");
    private static final Pattern MEM_PATTERN = Pattern.compile("mem\\[(\\d*)\\] = (\\d*)");

    abstract void run(DockingProgram program);

    public static DockingProgramOperation getOperation(String s) {
        Matcher m;
        if ((m = MASK_PATTERN.matcher(s)).matches()) {
            return new MaskOperation(m.group(1));
        } else if ((m = MEM_PATTERN.matcher(s)).matches()) {
            long pos = Long.parseLong(m.group(1));
            long value = Long.parseLong(m.group(2));
            return new MemoryOperation(pos, value);
        } else {
            throw new IllegalArgumentException("Invalid operation " + s);
        }
    }
}

class MaskOperation extends DockingProgramOperation {
    String mask;

    public MaskOperation(String mask) {
        this.mask = mask;
    }

    @Override
    void run(DockingProgram program) {
        program.changeMask(mask);
    }
}

class MemoryOperation extends DockingProgramOperation {
    long position;
    long value;

    public MemoryOperation(long position, long value) {
        this.position = position;
        this.value = value;
    }

    @Override
    void run(DockingProgram program) {
        program.getDecoder().decode(this, program.getBitmask())
                .forEach(op -> program.changeMemoryValue(op.position, op.value));
    }

}

interface DecoderChip {
    Collection<MemoryOperation> decode(MemoryOperation operation, String mask);
}

class DecoderChip1 implements DecoderChip {

    @Override
    public Collection<MemoryOperation> decode(MemoryOperation operation, String mask) {
        return List.of(new MemoryOperation(operation.position, applyMask(mask, operation.value)));
    }

    long applyMask(String mask, long value) {
        long maskOnes = Long.parseLong(mask.replace('X', '0'), 2);
        long maskZeroes = Long.parseLong(mask.replace('X', '1'), 2);
        return (value | maskOnes) & maskZeroes;
    }
}

class DecoderChip2 implements DecoderChip {

    @Override
    public Collection<MemoryOperation> decode(MemoryOperation operation, String mask) {
        return applyMask(mask, operation.position)
                    .map(p -> new MemoryOperation(p, operation.value))
                    .collect(Collectors.toList());
    }
    
    Stream<Long> applyMask(String mask, long value) {
        long maskOnes = Long.parseLong(mask.replace('X', '0'), 2);
        long maskFloating = Long.parseLong(mask.replace('1', '0').replace('X', '1'), 2);

        List<Long> floatingBits = LongStream.range(0, mask.length()) 
                                            .map(i -> 1L << i) // Check mask bit by bit
                                            .filter(bit -> (maskFloating & bit) != 0) // Get only floating bits
                                            .boxed()
                                            .collect(Collectors.toList());

        Stream<Long> addresses = Stream.of(value | maskOnes); // Overwrite 1s
        for (Long bit : floatingBits) {
            addresses = addresses.flatMap(a -> Stream.of(a | bit, a & ~bit));
        }
        
        return addresses;
    }
}