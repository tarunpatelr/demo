package example.microservices.demo.useraccountservice.event;

public enum UserEventType {

    CREATED("created"), UPDATED("updated"), DELETED("deleted");

    UserEventType(String routingKey) {
        this.routingKey = routingKey;
    }

    private final String routingKey;

    public String getRoutingKey() {
        return routingKey;
    }
}
