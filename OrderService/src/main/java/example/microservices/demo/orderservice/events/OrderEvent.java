package example.microservices.demo.orderservice.events;

import example.microservices.demo.orderservice.models.Order;
import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {

    private Order order;

    private OrderEventType orderEventType;

    public OrderEvent(Object source, Order order, OrderEventType orderEventType) {
        super(source);
        this.order = order;
        this.orderEventType = orderEventType;
    }

    public Order getOrder() {
        return order;
    }

    public OrderEventType getOrderEventType() {
        return orderEventType;
    }
}
