package cl.intohybrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cl.intohybrid.service.Producer;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
	private final Producer producer;

	@Autowired
	public KafkaController(Producer producer) {
		this.producer = producer;
	}

	@PostMapping(value = "/publish")
	public void sendMessageToKafkaTopic(@RequestParam("firstName") String firstName,
										@RequestParam("lastName") String lastName,
										@RequestParam("age") Integer age,
										@RequestParam("height") String height,
										@RequestParam("weight") String weight,
										@RequestParam("automatedEmail") boolean automatedEmail) {


		this.producer.sendMessage(firstName, lastName, age, height, weight, automatedEmail);
	}

}