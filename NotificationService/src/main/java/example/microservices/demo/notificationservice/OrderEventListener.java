package example.microservices.demo.notificationservice;

import example.microservices.demo.notificationservice.model.NotificationUser;
import example.microservices.demo.notificationservice.services.OrderService;
import example.microservices.demo.notificationservice.services.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderEventListener {

    private Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    OrderService orderService;

    @SuppressWarnings("unused")
    public void handleOrderCreated(String orderId) {

        logger.info("Handling order created event for order id '" + orderId + "'");

        Map<String, Object> order = orderService.getOrder(orderId);

        if (order != null) {

            String userId = (String) order.get("userId");

            NotificationUser user = userAccountService.getAccountServiceUser(userId);

            logger.info("Order " + orderId + " was created by user " + user.getId());
            logger.info("Sending email to user " + user.getEmail());
        }

    }
}
