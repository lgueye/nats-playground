package io.agileinfra.nats.playground.producer;

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

/**
 * Created by <a href="mailto:louis.gueye@gmail.com">Louis Gueye</a>.
 */
@Configuration
public class NatsProducerConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // use ISODate and not Timestamps
				.modules(new JavaTimeModule()).build();
	}

	@Bean
	public Connection nats(@Value("${nats.server.urls}") final String natsServerUrls) throws IOException, InterruptedException {
		final Options.Builder builder = new Options.Builder();
		Splitter.on(",").split(natsServerUrls).forEach(builder::server);
		return Nats.connect(builder.maxReconnects(5000).maxPingsOut(5000).pingInterval(Duration.ofMillis(500)).build());
	}

}
