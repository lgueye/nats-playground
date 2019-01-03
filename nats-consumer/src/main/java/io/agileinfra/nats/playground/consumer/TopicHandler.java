package io.agileinfra.nats.playground.consumer;

import io.nats.client.MessageHandler;

/**
 * @author louis.gueye@gmail.com
 */
public interface TopicHandler extends MessageHandler {
	String getSubject();
}
