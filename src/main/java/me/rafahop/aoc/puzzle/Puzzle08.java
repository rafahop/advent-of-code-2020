package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle08 implements Puzzle<Integer> {

    Program program;

    @Override
    public void init(Path file) throws Exception {
        this.program = new Program(Files.lines(file).map(s -> s.split(" "))
                .map(a -> new Instruction(a[0], Integer.parseInt(a[1]))).collect(Collectors.toList()));
    }

    @Override
    public Integer part1() throws Exception {
        try {
            program.run();
            return 0; // Finished correctly?
        } catch (InfinteLoopException ex) {
            return ex.getResult();
        }
    }

    @Override
    public Integer part2() throws Exception {
        for (Instruction inst : program.getInstructions()) {
            String newOperation = changeOperation(inst.getOperation());
            if (newOperation != null) {
                inst.setOperation(newOperation);
                try {
                    return program.run();
                } catch (InfinteLoopException ex) {
                    // infinite loop: try next instruction
                }
                // Change it back
                inst.setOperation(changeOperation(inst.operation));
            }
        }
        return 0;
    }

    private static String changeOperation(String operation) {
        String newOperation = null;
        switch (operation) {
        case "jmp":
            newOperation = "nop";
            break;
        case "nop":
            newOperation = "jmp";
            break;
        }
        return newOperation;
    }
}

class InfinteLoopException extends Exception {
    int result;

    public InfinteLoopException(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}

class Program {
    List<Instruction> instructions;

    public Program(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Instruction getLine(int index) {
        return instructions.get(index);
    }

    public int numLines() {
        return instructions.size();
    }

    public int run() throws InfinteLoopException {
        boolean[] hasRun = new boolean[numLines()];
        int acc = 0;
        for (int line = 0; line < numLines();) {
            if (hasRun[line]) {
                throw new InfinteLoopException(acc);
            }
            hasRun[line] = true;
            Instruction inst = getLine(line);
            switch (inst.operation) {
            case "jmp":
                line += inst.argument;
                break;
            case "acc":
                acc += inst.argument;
            case "nop":
            default:
                line++;
                break;
            }
        }
        return acc;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }
}

class Instruction {
    String operation;
    int argument;

    public Instruction(String operation, int argument) {
        super();
        this.operation = operation;
        this.argument = argument;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getArgument() {
        return argument;
    }
}
