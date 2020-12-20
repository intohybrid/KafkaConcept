package cl.intohybrid.service;

import com.example.Customer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private static final String TRUSTSTORE_JKS = "/home/debian/store/truststore/kafka.truststore.jks";
    private static final String SASL_PROTOCOL = "SASL_SSL";
    private static final String SCRAM_SHA_512 = "SCRAM-SHA-512";
    private final String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
    private final String prodJaasCfg = String.format(jaasTemplate, "marcelo", "marcelo123");
    //ssl_sasl
    private static final String bootstrapAddress = "localhost:9094";
    //private static final String bootstrapAddress = "localhost:9092";

    @Bean
    public ProducerFactory<String, Customer> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put("sasl.mechanism", SCRAM_SHA_512);
        configProps.put("sasl.jaas.config", prodJaasCfg);
        configProps.put("security.protocol", SASL_PROTOCOL);
        configProps.put("ssl.truststore.location", TRUSTSTORE_JKS);
        configProps.put("ssl.truststore.password", "kafkakeystore");
        configProps.put("ssl.endpoint.identification.algorithm", "");

        // avro part
        configProps.put("key.serializer", StringSerializer.class.getName());
        configProps.put("value.serializer", KafkaAvroSerializer.class.getName());
        configProps.put("schema.registry.url", "http://127.0.0.1:8081");

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Customer> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}