package io.agileinfra.nats.playground.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@SpringBootApplication
public class NatsConsumer {
	public static void main(String[] args) {
		SpringApplication.run(NatsConsumer.class, args);
	}
}
