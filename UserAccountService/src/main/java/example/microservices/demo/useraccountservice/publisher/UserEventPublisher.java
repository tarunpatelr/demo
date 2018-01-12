package example.microservices.demo.useraccountservice.publisher;

import example.microservices.demo.useraccountservice.event.UserEvent;
import example.microservices.demo.useraccountservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher implements ApplicationListener<UserEvent> {

    private Logger logger = LoggerFactory.getLogger(UserEventPublisher.class);

    private RabbitTemplate rabbitTemplate;

    @Value("${user.account.service.routingKey.prefix}")
    public String routingKeyPrefix;

    @Value("${user.account.service.exchange.name}")
    public String exchangeName;

    public UserEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void onApplicationEvent(UserEvent userEvent) {

        User user = userEvent.getUser();

        logger.debug("User event occurred for user id :: " + user.getId());

        String routingKey = routingKeyPrefix +
                userEvent.getUserEventType().getRoutingKey();

        this.rabbitTemplate.convertAndSend(exchangeName,
                routingKey, user.getId());

        logger.info("User event published to exchange :: "
                + exchangeName + " with routing key :: " + routingKey);
    }
}
