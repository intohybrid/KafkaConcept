package cl.intohybrid.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {


    private static final String TRUSTSTORE_JKS = "/home/debian/store/truststore/kafka.truststore.jks";
    private static final String SASL_PROTOCOL = "SASL_SSL";
    private static final String SCRAM_SHA_512 = "SCRAM-SHA-512";
    private final String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
    private final String consJaasCfg = String.format(jaasTemplate, "marcelo", "marcelo123");
    //ssl_sasl
    private static final String bootstrapAddress = "localhost:9094";
    //private static final String bootstrapAddress = "localhost:9092";


    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "marcelo-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("sasl.mechanism", SCRAM_SHA_512);
        props.put("sasl.jaas.config", consJaasCfg);
        props.put("security.protocol", SASL_PROTOCOL);
        props.put("ssl.truststore.location", TRUSTSTORE_JKS);
        props.put("ssl.truststore.password", "kafkakeystore");
        props.put("ssl.endpoint.identification.algorithm", "");



        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
