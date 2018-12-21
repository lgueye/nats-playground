package io.agileinfra.nats.playground.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@SpringBootApplication
public class NatsProducer {
	public static void main(String[] args) {
		SpringApplication.run(NatsProducer.class, args);
	}
}
