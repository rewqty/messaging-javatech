package com.example.messagingrabbitmq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Receiver receiver;

	public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void run(String... args) throws Exception {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));

		while(true) {
			String message = reader.readLine();
			if (message.isEmpty()){
				continue;
			}
			if(message.equalsIgnoreCase("exit")) {
				break;
			}

			rabbitTemplate.convertAndSend(MessagingRabbitmqApplication.fanoutExchangeName, "", message);
			receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
		}
	}

}
