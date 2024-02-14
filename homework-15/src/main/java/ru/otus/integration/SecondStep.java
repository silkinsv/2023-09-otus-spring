package ru.otus.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.domain.Caterpillar;
import ru.otus.domain.Cocoon;

import java.util.Collection;

@MessagingGateway
public interface SecondStep {
    @Gateway(requestChannel = "caterpillarChannel", replyChannel = "cocoonChannel")
    Collection<Cocoon> process(Collection<Caterpillar> caterpillarCollection);
}
