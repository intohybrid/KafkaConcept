package cl.intohybrid.service;

import com.example.Customer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
	private final Logger logger = LoggerFactory.getLogger(Consumer.class);

	@KafkaListener(topics = "topic-marcelo")
	public void consume(ConsumerRecord<String, Customer> record) {
		Customer customer = record.value();
		logger.info(String.format("Consumed Message -> %s", customer.toString()));
	}
}