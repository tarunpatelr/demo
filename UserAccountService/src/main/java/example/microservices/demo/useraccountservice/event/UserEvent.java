package example.microservices.demo.useraccountservice.event;

import example.microservices.demo.useraccountservice.model.User;
import org.springframework.context.ApplicationEvent;

public class UserEvent extends ApplicationEvent {

    private User user;

    private UserEventType userEventType;

    public UserEvent(Object source, User user, UserEventType userEventType) {
        super(source);
        this.user = user;
        this.userEventType = userEventType;
    }

    public User getUser() {
        return user;
    }

    public UserEventType getUserEventType() {
        return userEventType;
    }
}
