package io.agileinfra.nats.playground.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.agileinfra.nats.dto.Event;
import io.nats.client.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author louis.gueye@gmail.com
 */
@RequiredArgsConstructor
@Slf4j
public class QueueConsumer implements QueueHandler {

	private final String subject;
	private final String queue;
	private final ObjectMapper objectMapper;

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public String getQueue() {
		return queue;
	}

	@Override
	public void onMessage(Message msg) throws InterruptedException {
		try {
			final Event event = objectMapper.readValue(msg.getData(), Event.class);
			log.info("Queue () received event {} from subject {}", event, msg.getSubject());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
