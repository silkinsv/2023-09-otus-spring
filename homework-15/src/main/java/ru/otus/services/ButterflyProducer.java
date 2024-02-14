package ru.otus.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.domain.Egg;
import ru.otus.integration.FirstStep;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ButterflyProducer {
    private final FirstStep firstStep;

    public void start() {
        List<Egg> eggs = new ArrayList<>();
        eggs.add(new Egg());
        eggs.add(new Egg());


        firstStep.process(eggs);
    }
}
