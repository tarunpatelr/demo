package example.microservices.demo.orderservice.events;

public enum OrderEventType {

    CREATED("created"), CANCELLED("cancelled"), UPDATED("updated");

    OrderEventType(String routingKey) {
        this.routingKey = routingKey;
    }

    private final String routingKey;

    public String getRoutingKey() {
        return routingKey;
    }
}
