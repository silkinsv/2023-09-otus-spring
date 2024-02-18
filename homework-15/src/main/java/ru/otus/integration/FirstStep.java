package ru.otus.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.domain.Caterpillar;
import ru.otus.domain.Egg;

import java.util.Collection;

@MessagingGateway
public interface FirstStep {
    @Gateway(requestChannel = "eggsChannel", replyChannel = "caterpillarChannel")
    Collection<Caterpillar> process(Collection<Egg> eggCollection);
}
