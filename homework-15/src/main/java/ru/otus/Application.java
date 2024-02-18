package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import ru.otus.services.ButterflyProducer;

@SpringBootApplication
@IntegrationComponentScan
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

		ButterflyProducer producer = ctx.getBean(ButterflyProducer.class);
		producer.start();
	}

}
