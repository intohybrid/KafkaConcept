package cl.intohybrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaConceptApplication {

	public static void main(String[] args) {

		//String rootDirectory=System.getProperty("user.dir")+"/src/main/resources/";
		//String jassFile = "jaas-spring-client.conf";
		//System.setProperty("java.security.auth.login.config", rootDirectory+jassFile);

		SpringApplication.run(KafkaConceptApplication.class, args);
	}

}
