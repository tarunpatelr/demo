package example.microservices.demo.orderservice.publisher;

import example.microservices.demo.orderservice.events.OrderEvent;
import example.microservices.demo.orderservice.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher implements ApplicationListener<OrderEvent> {

    private Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    private RabbitTemplate rabbitTemplate;

    @Value("${order.service.routingKey.prefix}")
    public String routingKeyPrefix;

    @Value("${order.service.exchange.name}")
    public String exchangeName;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {

        Order order = orderEvent.getOrder();

        logger.debug("Order event occurred for order id :: " + order.getId());

        String routingKey = routingKeyPrefix +
                orderEvent.getOrderEventType().getRoutingKey();

        this.rabbitTemplate.convertAndSend(exchangeName,
                routingKey, order.getId());

        logger.info("Order event published to exchange :: "
                + exchangeName + " with routing key :: " + routingKey);
    }
}
