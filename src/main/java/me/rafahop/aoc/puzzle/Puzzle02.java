package me.rafahop.aoc.puzzle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle02 implements Puzzle<Long> {
    private static final String LINE_REGEX = "(\\d+)-(\\d+) ([a-zA-Z]): ([a-zA-Z]*)";
    private static final Pattern PATTERN = Pattern.compile(LINE_REGEX);

    Path file;

    @Override
    public void init(Path file) throws Exception {
        this.file = file;
    }

    @Override
    public Long part1() throws IOException {
        return countValidPasswords(CorporatePolicyStrategy01::new);
    }

    @Override
    public Long part2() throws IOException {
        return countValidPasswords(CorporatePolicyStrategy02::new);
    }

    private long countValidPasswords(Function<CorporatePolicyDefinition, CorporatePolicyStrategy> policyConstructor)
            throws IOException {
        return Files.lines(file)
                    .map(PATTERN::matcher)
                    .filter(Matcher::find)
                    .map(m -> new Password(m.group(4),
                                policyConstructor.apply(new CorporatePolicyDefinition(Integer.valueOf(m.group(1)),
                                        Integer.valueOf(m.group(2)), m.group(3).charAt(0)))))
                    .filter(Password::isValid)
                    .count();
    }
}

class Password {
    CorporatePolicyStrategy policy;
    String password;

    public Password(String password, CorporatePolicyStrategy policy) {
        super();
        this.password = password;
        this.policy = policy;
    }

    public boolean isValid() {
        return policy.isValid(password);
    }
}

class CorporatePolicyDefinition {
    int num1;
    int num2;
    char letter;

    public CorporatePolicyDefinition(int min, int max, char letter) {
        super();
        this.num1 = min;
        this.num2 = max;
        this.letter = letter;
    }
}

interface CorporatePolicyStrategy {
    boolean isValid(String password);
}

class CorporatePolicyStrategy01 implements CorporatePolicyStrategy {
    CorporatePolicyDefinition definition;

    public CorporatePolicyStrategy01(CorporatePolicyDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean isValid(String password) {
        long appearances = password.chars().mapToObj(i -> (char) i).filter(c -> c.equals(definition.letter)).count();
        return appearances >= definition.num1 && appearances <= definition.num2;
    }
}

class CorporatePolicyStrategy02 implements CorporatePolicyStrategy {
    CorporatePolicyDefinition definition;

    public CorporatePolicyStrategy02(CorporatePolicyDefinition definition) {
        this.definition  = definition;
    }

    @Override
    public boolean isValid(String password) {
        int coincidences = 0;
        if (password.charAt(definition.num1 - 1) == definition.letter) {
            coincidences++;
        }
        if (password.charAt(definition.num2 - 1) == definition.letter) {
            coincidences++;
        }
        return coincidences == 1;
    }
}