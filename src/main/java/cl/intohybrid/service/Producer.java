package cl.intohybrid.service;

import com.example.Customer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;


@Service
public class Producer {
	private static final Logger logger = LoggerFactory.getLogger(Producer.class);
	private static final String TOPIC = "topic-marcelo";

	@Autowired
	private KafkaTemplate<String, Customer> kafkaTemplate;


	public void sendMessage(String firstName, String lastName, Integer age, String height, String weight, boolean automatedEmail) {


		// copied from avro examples
		Customer customer = Customer.newBuilder()
				.setAge(age)
				.setAutomatedEmail(automatedEmail)
				.setFirstName(firstName)
				.setLastName(lastName)
				.setHeight(new Float(height))
				.setWeight(new Float(weight))
				.build();


		logger.info("Producing message");
		ListenableFuture<SendResult<String, Customer>> future = this.kafkaTemplate.send(TOPIC, customer.getFirstName() , customer);

		future.addCallback(new ListenableFutureCallback<SendResult<String, Customer>>() {

			@Override
			public void onSuccess(SendResult<String, Customer> message) {
				System.out.println("Sent message=[" + message +
						"] with offset=[" + message.getRecordMetadata().offset() + "]");
			}
			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Unable to send message=["
						+ customer + "] due to : " + ex.getMessage());
			}
		});


	}



}