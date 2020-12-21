package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Puzzle21 implements Puzzle<String> {

    private static final String LINE_REGEX = "([\\w ]*)\\(contains ([\\w ,]*)\\)";
    private static final Pattern PATTERN = Pattern.compile(LINE_REGEX);

    List<Food> foods;

    @Override
    public void init(Path file) throws Exception {
        foods = Files.lines(file)
                     .map(PATTERN::matcher)
                     .filter(Matcher::matches)
                     .map( m -> new Food(Arrays.asList(m.group(1).split(" ")), Arrays.asList(m.group(2).split(", "))))
                     .collect(Collectors.toList());
    }

    @Override
    public String part1() throws Exception {
        Map<String, String> allergens = identifyAllergens();
        long result = foods.stream()
                          .map(Food::getIngredients)
                          .flatMap(List::stream)
                          .filter(a -> !allergens.containsValue(a))
                          .count();
        return String.valueOf(result);
    }

    @Override
    public String part2() throws Exception {
        return identifyAllergens().entrySet()
                                  .stream()
                                  .sorted(Comparator.comparing(Entry::getKey))
                                  .map(Entry::getValue)
                                  .collect(Collectors.joining(","));
    }
    
    private Map<String, List<String>> findPotentialAllergens() {

        Map<String, List<String>> potentialAllergens = new HashMap<>();
        foods.stream()
             .forEach(f -> f.allergens.forEach(allergen -> potentialAllergens.merge(allergen, 
                                                                                    new ArrayList<>(f.ingredients), 
                                                                                    (a, b) -> { 
                                                                                                a.retainAll(b);
                                                                                                return a;
                                                                                            })));
        return potentialAllergens;
    }
    
    private Map<String, String> identifyAllergens() {
        Map<String, List<String>> allergens = findPotentialAllergens();

        Map<String, String> result = new HashMap<>();
        // Search allergens with only one ingredient -> remove the ingredient from other allergens - do until all found
        Optional<Map.Entry<String, List<String>>> singleAllergen = functionFindSingleAllergen().apply(allergens);
        while (singleAllergen.isPresent()) {
            String ingredient = singleAllergen.get().getValue().get(0); // it has only one ingredient
            result.put(singleAllergen.get().getKey(), ingredient);
            // Remove ingredient from other potential allergens
            allergens.values().stream().filter(i -> i.contains(ingredient)).forEach(i -> i.remove(ingredient));
            singleAllergen = functionFindSingleAllergen().apply(allergens);
        }
        return result;
    }
    
    private static Function<Map<String, List<String>>, Optional<Map.Entry<String, List<String>>>> functionFindSingleAllergen() {
        return a -> a.entrySet().stream().filter(e -> e.getValue().size() == 1).findAny();
    }
}

class Food {
    List<String> ingredients;
    List<String> allergens;

    public Food(List<String> ingredients, List<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getAllergens() {
        return allergens;
    }
}