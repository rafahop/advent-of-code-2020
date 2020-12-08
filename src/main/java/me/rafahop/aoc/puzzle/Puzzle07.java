package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Puzzle07 implements Puzzle<Integer> {
    private static final Pattern BAG_PATTERN = Pattern.compile("(?:(\\d*) )?([a-z]+ [a-z]+) bags?");

    Collection<Bag> rules;
    @Override
    public void init(Path file) throws Exception {
        this.rules = Files.lines(file)
                .map(s -> s.split(" contain "))
                .map(a -> Arrays.asList(BAG_PATTERN.matcher(a[0]), BAG_PATTERN.matcher(a[1])))
                .peek(l -> l.get(0).find())
                .map(l -> new Bag(l.get(0).group(2), readRules(l.get(1).results())))
                .collect(Collectors.toList());
    }

    @Override
    public Integer part1() throws Exception {
        return findOptions("shiny gold").size();
    }

    private Set<String> findOptions(String bag) {
        return rules.stream().filter(r -> r.accepts(bag))
                      .map(Bag::getColour)
                      .map(b-> { Set<String> options = findOptions(b); options.add(b); return options; })
                      .flatMap(Set::stream).collect(Collectors.toSet());
    }
    
    @Override
    public Integer part2() throws Exception {
        return countBagsInside("shiny gold");
    }
    
    int countBagsInside(String bagColour) {
        Optional<Bag> bag = rules.stream().filter(b -> b.getColour().equals(bagColour)).findFirst();
        if (bag.isPresent() && !bag.get().getRules().isEmpty()) {
            return bag.get().getRules().stream()
                    .mapToInt(r -> r.getQuantity() + (r.getQuantity() * countBagsInside(r.getColour())))
                    .sum();
        } else {
            return 0;
        }
    }
    
    static List<BagRule> readRules(Stream<MatchResult> results) {
        return results.filter(m -> m.group(1)!=null)
                      .map(bag -> new BagRule(bag.group(2), Integer.parseInt(bag.group(1))))
                      .collect(Collectors.toList());
    }
}

class BagRule {
    String colour;
    Integer quantity;
    
    public BagRule(String colour, Integer quantity) {
        super();
        this.colour = colour;
        this.quantity = quantity;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public String getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return "BagRule [colour=" + colour + ", quantity=" + quantity + "]";
    }

}

class Bag {
    String colour;
    Set<BagRule> rules;
    public Bag(String colour, Collection<BagRule> rules) {
        super();
        this.colour = colour;
        this.rules = Set.copyOf(rules);
    }
    
    public boolean accepts(String bagColour) {
        return rules.stream().anyMatch(r -> r.colour.equals(bagColour));
    }
    
    public String getColour() {
        return colour;
    }
    
    public Set<BagRule> getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return "Bag [colour=" + colour + ", rules=" + rules + "]";
    }

}
