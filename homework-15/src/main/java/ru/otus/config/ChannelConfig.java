package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.services.TransformationService;

@Configuration
public class ChannelConfig {

    @Bean
    public MessageChannelSpec<?, ?> eggsChannel() {
        return MessageChannels.queue(50);
    }

    @Bean
    public MessageChannelSpec<?, ?> caterpillarChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> cocoonChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public MessageChannelSpec<?, ?> butterflyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow eggsFlow(TransformationService service) {
        return IntegrationFlow.from("eggsChannel")
                .split()
                .handle(service, "transformToCaterpillar")
                .aggregate()
                .channel("caterpillarChannel")
                .get();
    }

    @Bean
    public IntegrationFlow caterpillarFlow(TransformationService service) {
        return IntegrationFlow.from("caterpillarChannel")
                .split()
                .handle(service, "transformToCocoon")
                .aggregate()
                .channel("cocoonChannel")
                .get();
    }

    @Bean
    public IntegrationFlow cocoonFlow(TransformationService service) {
        return IntegrationFlow.from("cocoonChannel")
                .split()
                .handle(service,"transformToButterfly")
                .aggregate()
                .channel("butterflyChannel")
                .get();
    }
}