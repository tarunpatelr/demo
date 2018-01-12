package example.microservices.demo.useraccountservice;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

    @Value("${user.account.service.exchange.name}")
    public String exchangeName;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}