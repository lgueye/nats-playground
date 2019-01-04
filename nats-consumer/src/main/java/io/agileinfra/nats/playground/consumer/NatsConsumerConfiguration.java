package io.agileinfra.nats.playground.consumer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Splitter;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.Duration;
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

	private Connection natsClient;

	@Bean
	public QueueConsumer queueConsumer(@Value("${server.port}") final int port, final ObjectMapper objectMapper) {
		return new QueueConsumer(String.format("%s:%s", "localhost", String.valueOf(port)), "bar", "events-queue", objectMapper);
	}
	@Bean
	public TopicConsumer topicConsumer(@Value("${server.port}") final int port, final ObjectMapper objectMapper) {
		return new TopicConsumer(String.format("%s:%s", "localhost", String.valueOf(port)), "foo", objectMapper);
	}

	@Bean
	public Connection connection(@Value("${nats.server.urls}") final String natsServerUrls, final List<QueueHandler> queueHandlers,
			final List<TopicHandler> topicHandlers) throws IOException, InterruptedException {
		final Options.Builder builder = new Options.Builder();
		Splitter.on(",").split(natsServerUrls).forEach(builder::server);
		natsClient = Nats.connect(builder.maxReconnects(5000).maxPingsOut(5000).pingInterval(Duration.ofMillis(500)).build());
		queueHandlers.forEach(handler -> natsClient.createDispatcher(handler).subscribe(handler.getSubject(), handler.getQueue()));
		topicHandlers.forEach(handler -> natsClient.createDispatcher(handler).subscribe(handler.getSubject()));
		return natsClient;
	}

}
