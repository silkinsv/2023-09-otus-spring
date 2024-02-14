package ru.otus.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.domain.Butterfly;
import ru.otus.domain.Cocoon;

import java.util.Collection;

@MessagingGateway
public interface ThirdStep {
    @Gateway(requestChannel = "cocoonChannel", replyChannel = "butterflyChannel")
    Collection<Butterfly> process(Collection<Cocoon> cocoonCollection);
}
