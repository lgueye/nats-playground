package io.agileinfra.nats.playground.consumer;

import io.nats.client.MessageHandler;

/**
 * @author louis.gueye@gmail.com
 */
public interface QueueHandler extends MessageHandler {
	String getSubject();
	String getQueue();
}
