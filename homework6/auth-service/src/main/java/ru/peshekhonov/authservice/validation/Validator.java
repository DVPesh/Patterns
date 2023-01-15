package ru.peshekhonov.authservice.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Validator<T> {

    Map<Predicate<T>, String> rules = new HashMap<>();

    public void addRule(Predicate<T> rule, String errorMessage) {
        rules.put(rule, errorMessage);
    }

    public String validate(T obj) {
        return rules.keySet().stream()
                .filter(rule -> rule.test(obj))
                .map(rule -> rules.get(rule))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
