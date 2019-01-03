package io.agileinfra.nats.playground.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agileinfra.nats.dto.Event;
import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @author louis.gueye@gmail.com
 */
@RestController
@RequestMapping("queue")
@RequiredArgsConstructor
public class QueueResource {

	private final Connection natsClient;
	private final ObjectMapper objectMapper;

	@PostMapping(value = "events", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void postEvents(@RequestBody final Event event) throws JsonProcessingException {
		natsClient.publish("bar", objectMapper.writeValueAsString(event).getBytes(StandardCharsets.UTF_8));
	}
}
