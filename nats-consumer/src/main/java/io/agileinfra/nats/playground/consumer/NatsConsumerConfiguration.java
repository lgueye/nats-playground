package io.agileinfra.nats.playground.consumer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.nats.client.Connection;
import io.nats.client.Nats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.List;

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
public class NatsConsumerConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // use ISODate and not Timestamps
				.modules(new JavaTimeModule()).build();
	}

	@Value("${nats.server.url}")
	private String natsServerUrl;

	@Bean
	public QueueConsumer queueConsumer(final ObjectMapper objectMapper) {
		return new QueueConsumer("bar", "events-queue", objectMapper);
	}
	@Bean
	public TopicConsumer topicConsumer(final ObjectMapper objectMapper) {
		return new TopicConsumer("foo", objectMapper);
	}

	@Bean
	public Connection natsClient(final List<QueueHandler> queueHandlers, final List<TopicHandler> topicHandlers) throws IOException,
			InterruptedException {
		final Connection natsClient = Nats.connect(natsServerUrl);
		queueHandlers.forEach(handler -> natsClient.createDispatcher(handler).subscribe(handler.getSubject(), handler.getQueue()));
		topicHandlers.forEach(handler -> natsClient.createDispatcher(handler).subscribe(handler.getSubject()));
		return natsClient;
	}

}
