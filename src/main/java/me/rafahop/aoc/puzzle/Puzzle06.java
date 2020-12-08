package me.rafahop.aoc.puzzle;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle06 implements Puzzle<Long> {
    Collection<DeclarationForm> allForms;
    
    @Override
    public void init(Path file) throws Exception {
        List<String> lines = Files.lines(file).collect(Collectors.toList());
        allForms = new ArrayList<>();
        DeclarationForm form = new DeclarationForm();
        for (String line : lines) {
            if (line.isEmpty()) {
                allForms.add(form);
                form = new DeclarationForm();
            } else {
                form.addAnswers(line);
            }
        }
        allForms.add(form);
    }

    @Override
    public Long part1() throws Exception {
        return allForms.stream().map(DeclarationForm::getQuestions)
                                .map(l -> l.stream().reduce("", (acc, el) -> acc + el))
                                .mapToLong(s -> s.chars().distinct().count())
                                .sum();
    }

    @Override
    public Long part2() throws Exception {
        return allForms.stream().map(DeclarationForm::getQuestions)
                                .map(l -> l.stream().map(s -> s.chars().boxed().collect(Collectors.toSet()))
                                                    .reduce((acc, el) -> { el.retainAll(acc); return el; }))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .mapToLong(Set::size)
                                .sum();
    }
}

class DeclarationForm {
    Collection<String> questions = new ArrayList<>();
    
    public void addAnswers(String answers) {
        questions.add(answers);
    }
    
    public Collection<String> getQuestions() {
        return questions;
    }
}
